package com.ferremas.views;

import com.ferremas.model.Pedido;
import com.ferremas.service.PedidoService;
import com.ferremas.util.Logger;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

@Named("pagoExitosoBean")
@ViewScoped
public class PagoExitosoBean {

    private Pedido pedido;
    private static String idPedido;

    @Autowired
    private PedidoService pedidoService;
    private StreamedContent file;
    @PostConstruct
    public void init() {


        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        idPedido = String.valueOf(Integer.parseInt(facesContext.getExternalContext().getRequestParameterMap().get("idPedido")));

        Logger.logInfo("Id del pedido: " + idPedido);

        /*if (idPedido == null) {
            // Usamos un FacesContext para la navegación posterior
            FacesContext.getCurrentInstance().getApplication()
                    .getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/home/index.xhtml?faces-redirect=true");
            return;
        }*/
        if (idPedido!=null) {
            try {
                pedido = pedidoService.obtenerPorId(Integer.parseInt(idPedido)); // Asumiendo que devuelve un Pedido por ID
            } catch (NumberFormatException e) {
                Logger.logError("ID de pedido inválido: " + idPedido);
            } catch (Exception e) {
                Logger.logError("Error al obtener el pedido: " + e.getMessage());
            }
        }
    }

    public void exportarBoletaPDF() {
        Logger.logInfo("Exportando Boleta");
        try {
            // Se obtiene el contenido que se quiere exportar, en este caso la boleta

            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();

            String fileName = "dynamic.pdf";
            String contentType = "application/pdf";


            // Preparar el documento PDF
            Document document = new Document();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);

            document.open();
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            // Agregar contenido al PDF
            document.add(new Paragraph("Boleta de Pago", font));
            document.add(new Paragraph("ID de Pedido: " + pedido.getIdPedido(), font));
            document.add(new Paragraph("Fecha: " + pedido.getFecha(), font));
            document.add(new Paragraph("Total: " + pedido.getTotal(), font));

            document.close();

            // Preparar el archivo para la descarga
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();

            file = DefaultStreamedContent.builder()
                    .name(fileName)
                    .contentType(contentType)
                    //.stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/demo/images/boromir.jpg"))
                    .stream(() ->new ByteArrayInputStream(pdfBytes))
                    .build();


            // Configurar la respuesta de la descarga
            /*externalContext.setResponseContentType("application/pdf");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=boleta-pago-" + pedido.getIdPedido() + ".pdf");
            externalContext.getResponseOutputStream().write(pdfBytes);

            context.responseComplete(); // Importante: Evita que JSF continúe con el procesamiento de la página*/

            // Mensaje de éxito
            //context.addMessage(null, new FacesMessage("La boleta se ha exportado a PDF exitosamente"));

        } catch (Exception e) {
            // En caso de error
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Hubo un problema al exportar la boleta"));
        }

    }



    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public StreamedContent getFile() {

        return file;
    }
}
