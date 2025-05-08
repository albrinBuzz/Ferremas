package com.ferremas.controller;

import com.ferremas.config.webPay.entity.RefundRequest;
import com.ferremas.service.WebPayService;
import com.ferremas.views.CarritoBean;
import jakarta.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

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
            @RequestParam(TOKEN_KEY) String token) {
        // Delegar la lógica de negocio al servicio
        boolean response = webPayService.commitTransaction(token);

        if (response) {
            return new RedirectView("/home/pagoExitoso.xhtml");
        } else {
            return new RedirectView("/home/pagoError.xhtml");
        }
    }

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
        return new RedirectView("/home/pagoError.xhtml");

    }
}
