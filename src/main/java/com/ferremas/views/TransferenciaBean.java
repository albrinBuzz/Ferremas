package com.ferremas.views;

import com.ferremas.model.*;
import com.ferremas.service.*;
import com.ferremas.util.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.annotation.ViewMap;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named("transferenciaBean")
@ViewScoped
public class TransferenciaBean implements Serializable {

    private Transferencia transferencia = new Transferencia();
    private List<Transferencia> transferencias;
    private UploadedFile file;
    private boolean isTransferenciaGuardada = false;  // Flag para evitar enviar nuevamente

    @Autowired
    private TransferenciaService transferenciaService;

    @Autowired
    private EstadotransferenciaService estadotransferenciaService;
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private EstadopedidoService estadopedidoService;
    @Autowired
    private PedidoService pedidoService;
    @Inject
    private CarritoBean carritoBean;
    @Autowired
    private HttpSession session;

    @PostConstruct
    public void init(){
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            var empleado = empleadoService.findById(usuario.getRutUsuario()).orElse(null);

            assert empleado != null;
            transferencias=transferenciaService.findBySucursal(empleado.getSucursal().getIdSucursal());
        }

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);


    }

    public String guardarTransferencia() {
        try {
            // Set datos adicionales
            Pedido pedidoGuardado = guardarPedidoDesdeCarrito();

            var estadoTransa=estadotransferenciaService.findById(1).get();
            transferencia.setFecha(new Date());
            transferencia.setEstadotransferencia(estadoTransa);
            transferencia.setPedido(pedidoGuardado);

            if (file != null) {
                transferencia.setComprobante(file.getFileName());

                try {
                    InputStream in = file.getInputStream();
                    String nombre = file.getFileName();
                    Path rutaBase = Paths.get("src", "main", "webapp", "resources", "images", "transa");
                    Files.createDirectories(rutaBase);
                    upload(nombre, rutaBase.toString(), in);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }

            transferenciaService.save(transferencia); // guarda en DB

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Transferencia registrada exitosamente, Recibira un notificacion al correo de confirmacion"));

            transferencia = new Transferencia(); // Reset para nuevo ingreso
            carritoBean.resetCart();
            FacesContext.getCurrentInstance().getExternalContext().redirect("/home/pagoExitoso.xhtml?idPedido="+pedidoGuardado.getIdPedido());
        } catch (Exception e) {
            Logger.logInfo(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar la transferencia"));
        }


        return null;
    }

    public String guardarTransferencia(Pedido pedido) {
        try {

           /* if (isTransferenciaGuardada) {
                // Si ya se guardó, no hacemos nada
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "La transferencia ya ha sido registrada."));
                return null;
            }*/

            // Set datos adicionales
            var pedidoFind= pedidoService.obtenerPorId(pedido.getIdPedido());

            var estadoTransa=estadotransferenciaService.findById(1).get();
            transferencia.setFecha(new Date());
            transferencia.setEstadotransferencia(estadoTransa);
            transferencia.setPedido(pedidoFind);
            Logger.logInfo(transferencia.toString());

            if (file != null) {
                transferencia.setComprobante(file.getFileName());

                try {
                    InputStream in = file.getInputStream();
                    String nombre = file.getFileName();
                    Path rutaBase = Paths.get("src", "main", "webapp", "resources", "images", "transa");
                    Files.createDirectories(rutaBase);
                    upload(nombre, rutaBase.toString(), in);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }

            transferenciaService.save(transferencia); // guarda en DB
            isTransferenciaGuardada = true;
            pedido.addTransferencia(transferencia); // Añadir la transferencia al pedido

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Transferencia registrada exitosamente, se debe esperar la confirmacion de transferncia, por correo"));

            transferencia = new Transferencia(); // Reset para nuevo ingreso
        } catch (Exception e) {
            Logger.logInfo(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar la transferencia"));
        }


        return null;
    }


    private Pedido guardarPedidoDesdeCarrito() {
        Pedido pedidoCarrito = carritoBean.getPedido();
        Pedido nuevoPedido = new Pedido();

        nuevoPedido.setRutcliente(pedidoCarrito.getRutcliente());
        nuevoPedido.setFecha(new Date());
        nuevoPedido.setSucursal(pedidoCarrito.getSucursal());
        nuevoPedido.setEstadopedido(estadopedidoService.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("EstadoPedido no encontrado")));
        nuevoPedido.setEstado("espera");

        List<Detallepedido> nuevosDetalles = new ArrayList<>();
        for (Detallepedido detalle : pedidoCarrito.getDetallepedidos()) {
            Detallepedido nuevoDetalle = new Detallepedido();
            nuevoDetalle.setCantidad(detalle.getCantidad());
            nuevoDetalle.setProducto(detalle.getProducto());
            nuevoDetalle.setPedido(nuevoPedido);
            nuevosDetalles.add(nuevoDetalle);
        }

        nuevoPedido.setDetallepedidos(nuevosDetalles);
        nuevoPedido.setTotal(pedidoCarrito.getTotal());

        return pedidoService.guardar(nuevoPedido);
    }





    public void upload(String fileName, String destination, InputStream in) {
        try {
            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(destination + File.separator + fileName);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            Logger.logInfo("Archivo creeado en: "+destination + File.separator + fileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    // Getters y Setters

    public Transferencia getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(Transferencia transferencia) {
        this.transferencia = transferencia;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public UploadedFile getFile() {
        return file;
    }

    public List<Transferencia> getTransferencias() {
        return transferencias;
    }

    public void setTransferencias(List<Transferencia> transferencias) {
        this.transferencias = transferencias;
    }
}
