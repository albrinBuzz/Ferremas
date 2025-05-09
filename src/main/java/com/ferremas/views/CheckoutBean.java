package com.ferremas.views;

import com.ferremas.config.webPay.entity.WebPayTransactionRequest;
import com.ferremas.config.webPay.entity.WebPayTransactionResponse;
import com.ferremas.controller.PaypalController;
import com.ferremas.model.*;
import com.ferremas.service.*;
import com.ferremas.util.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.io.IOException;
import java.util.*;

import java.util.List;
import java.util.stream.Collectors;

@Named("checkoutBean")
@ViewScoped
public class CheckoutBean {


    @Autowired
    private InventarioService inventarioService;
    @Autowired
    private SucursalService sucursalService;
    private List<Inventario> inventarios;
    private List<Sucursal>sucursals;
    private List<Detallepedido>detallepedidos;
    @Inject
    private CarritoBean carritoBean;
    @Inject
    private UserBean userBean;
    private Clienteinvitado clienteInvitado;  // Aquí almacenamos los datos del invitado
    private String metodoPago;
    private Sucursal sucursal;
    private Pedido pedido;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private TransaccionService transaccionService;
    @Autowired
    private MetodopagoService metodopagoService;

    @Autowired
    private EstadopedidoService estadopedidoService;

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private WebPayService webPayService;

    @PostConstruct
    public void init(){

        clienteInvitado=new Clienteinvitado();

        pedido=carritoBean.getPedido();
        detallepedidos=pedido.getDetallepedidos();
        initSucursales();




    }

    private void initSucursales() {

            Set<Integer> idsRequeridos = detallepedidos.stream()
                    .map(dp -> dp.getProducto().getIdProducto())
                    .collect(Collectors.toSet());

        List<Producto> productos = detallepedidos.stream()
                .map(Detallepedido::getProducto)
                .toList();

         inventarios = inventarioService.buscarPorProductoIn(productos);

            Map<Sucursal, List<Inventario>> inventariosPorSucursal = inventarios.stream()
                    .filter(inv -> inv.getStock() > 0)
                    .collect(Collectors.groupingBy(Inventario::getSucursal));

        this.sucursals = inventariosPorSucursal.entrySet().stream()
                .filter(entry -> {
                    Set<Integer> productosEnSucursal = entry.getValue().stream()
                            .map(inv -> inv.getProducto().getIdProducto())
                            .collect(Collectors.toSet());
                    return productosEnSucursal.containsAll(idsRequeridos);
                })
                .map(Map.Entry::getKey)
                .toList();


    }


    public void confirmarPedido() {


        var usurio=userBean.getUsuario();
        if (usurio!=null){
            pedido.setRutcliente(usurio.getRutUsuario());
        }else {
            pedido.setRutcliente(clienteInvitado.getRutcliente());

        }


        pedido.setSucursal(sucursal);

        if (sucursal != null) {

            if (this.metodoPago.equals("paypal")) {
                Logger.logInfo("Pago por PayPal");

                try {
                    // Crear el cuerpo de la solicitud
                    /*Map<String, String> body = new HashMap<>();
                    body.put("method", "paypal");
                    body.put("amount", "00.01");
                    body.put("currency", "USD");
                    body.put("description", "Compra en Ferremas");

                    // Crear headers
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                    // Convertir body a formato URL-encoded
                    StringBuilder formData = new StringBuilder();
                    for (Map.Entry<String, String> entry : body.entrySet()) {
                        formData.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                    }

                    HttpEntity<String> entity = new HttpEntity<>(formData.toString(), headers);

                    // Consumir el endpoint
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> response = restTemplate.postForEntity(
                            "http://localhost:8080/paypal/payment/create",
                            entity,
                            String.class
                    );*/

                    String method="paypal";
                    String amount="00.01";
                    String currency="USD";
                    String description="Compra en Ferremas";

                   String reponse= paypalService.crearPago(method,amount,currency,description);

                    // Redirigir al usuario
                    //FacesContext.getCurrentInstance().getExternalContext().redirect(response.getHeaders().getLocation().toString());
                    FacesContext.getCurrentInstance().getExternalContext().redirect(reponse);

                } catch (Exception e) {
                    Logger.logInfo("Error al redirigir a PayPal: " + e.getMessage());
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo procesar el pago. " + e.getMessage()));
                }

                return;
            } else if (metodoPago.equals("transferencia")) {

                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/home/transferencia.xhtml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

            else if (metodoPago.equals("webPay")) {

                WebPayTransactionRequest request = new WebPayTransactionRequest(
                        "ordenCompra"+new Random().nextInt(1, 99999),
                        "sesion"+new Random().nextInt(1, 99999),
                        carritoBean.getPedido().getTotal(),
                        "http://localhost:8080/webpay/commit"
                );

                try {
                    WebPayTransactionResponse response = webPayService.createTransaction(request);
                    Logger.logInfo(response.toString());
                    FacesContext.getCurrentInstance().getExternalContext().redirect(response.getUrl()+"?token_ws="+response.getToken());

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }


            else if (metodoPago.equals("tarjeta")) {
                Pedido pedidoGuardado = guardarPedidoDesdeCarrito();
                guardarTransaccion(pedidoGuardado);
                carritoBean.resetCart();

                try {

                    FacesContext.getCurrentInstance().getExternalContext().redirect("/home/pagoExitoso.xhtml?idPedido="+pedidoGuardado.getIdPedido());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

            // Pago en efectivo, tarjeta o transferencia
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Pedido confirmado", "Gracias por tu compra."));
        }
        else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Sin sucursal", "Debe seleccionar una sucursal de retiro"));
        }
    }


    private Pedido guardarPedidoDesdeCarrito() {
        Pedido pedidoCarrito = carritoBean.getPedido();
        Pedido nuevoPedido = new Pedido();

        nuevoPedido.setRutcliente(pedidoCarrito.getRutcliente());
        nuevoPedido.setFecha(new Date());
        nuevoPedido.setSucursal(pedidoCarrito.getSucursal());
        nuevoPedido.setEstadopedido(estadopedidoService.findById(2)
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

    private void guardarTransaccion(Pedido pedido) {
        Transaccion transaccion = new Transaccion();
        transaccion.setPedido(pedido);
        transaccion.setFecha(new Date());
        transaccion.setMonto(pedido.getTotal());
        transaccion.setMetodopago(metodopagoService.obtenerPorId(2));

        transaccionService.guardar(transaccion);
    }



    public List<Sucursal> getSucursals() {
        return sucursals;
    }

    public void setSucursals(List<Sucursal> sucursals) {
        this.sucursals = sucursals;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        Logger.logInfo(sucursal.toString());
        this.sucursal = sucursal;
    }

    public Clienteinvitado getClienteInvitado() {
        return clienteInvitado;
    }

    public void setClienteInvitado(Clienteinvitado clienteInvitado) {
        this.clienteInvitado = clienteInvitado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}



