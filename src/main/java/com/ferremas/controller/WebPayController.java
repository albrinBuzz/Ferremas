package com.ferremas.controller;

import com.ferremas.config.webPay.entity.RefundRequest;
import com.ferremas.service.WebPayService;
import com.ferremas.views.CarritoBean;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@RestController
public class WebPayController {

    @Autowired
    private WebPayService webPayService;

    @Inject
    private CarritoBean carritoBean;


    static final String LOCAL_ADDRESS = "http://localhost:8080";
    private static final String TOKEN_KEY = "token_ws";


    @CrossOrigin(origins = LOCAL_ADDRESS)
    @GetMapping("/webpay/commit")
    public RedirectView commitTransaction(
            @RequestParam(value = TOKEN_KEY, required = false) Optional<String> token,
            @RequestParam(value = "TBK_TOKEN", required = false) Optional<String> tokenError,
            @RequestParam(value = "TBK_ORDEN_COMPRA", required = false) Optional<String> ordenCompra,
            @RequestParam(value = "TBK_ID_SESION", required = false) Optional<String> idSesion) {

        // Verificar si el tokenError está presente
        if (tokenError.isPresent()) {
            RefundRequest request = new RefundRequest();
            request.setAmount(carritoBean.getPedido().getTotal());
            var respuesta = webPayService.refundTransaction(tokenError.orElse(""), request); // orElse("") si tokenError está presente
            return new RedirectView("/home/pagoCancelado.xhtml");
        } else if (token.isPresent() && !token.get().trim().isEmpty()) {
            // Si token está presente y no está vacío
            var IdPedido = webPayService.commitTransaction(token.get());

            if (IdPedido > 0) {
                return new RedirectView("/home/pagoExitoso.xhtml?idPedido=" + IdPedido);
            } else {
                return new RedirectView("/home/pagoError.xhtml");
            }
        } else {
            // Si token no está presente o es vacío, redirigir a pagoError
            return new RedirectView("/home/pagoError.xhtml");
        }
    }




    @CrossOrigin(origins = LOCAL_ADDRESS)
    @PostMapping("/webpay/commit")
    public RedirectView commitTransaction(
            @RequestParam("TBK_TOKEN") String token,
            @RequestParam("TBK_ORDEN_COMPRA") String ordenCompra,
            @RequestParam("TBK_ID_SESION") String idSesion) {

        // Aquí se realiza la lógica de commit con los datos recibidos
        // Por ejemplo, llamar a un servicio que procese el pago
        RefundRequest request=new RefundRequest();
        request.setAmount(carritoBean.getPedido().getTotal());
        var respuesta= webPayService.refundTransaction(token,request);
        return new RedirectView("/home/pagoCancelado.xhtml.xhtml");

    }

}
