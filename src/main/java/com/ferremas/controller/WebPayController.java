package com.ferremas.controller;

import com.ferremas.config.webPay.entity.RefundRequest;
import com.ferremas.service.WebPayService;
import com.ferremas.views.CarritoBean;
import jakarta.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

/**
 * Controlador REST para manejar callbacks y flujos de confirmación
 * o cancelación de pagos con WebPay Plus.
 */
@RestController
public class WebPayController {

    @Autowired
    private WebPayService webPayService;

    @Inject
    private CarritoBean carritoBean;

    private static final String LOCAL_ADDRESS = "http://localhost:8080";
    private static final String TOKEN_KEY = "token_ws";

    /**
     * Endpoint que maneja el retorno desde WebPay (GET).
     * WebPay puede devolver:
     * - token_ws: transacción exitosa.
     * - TBK_TOKEN + otros datos: transacción cancelada por el usuario.
     *
     * @param token       Token exitoso de WebPay (si la transacción fue completada).
     * @param tokenError  Token de error si el usuario canceló la transacción.
     * @param ordenCompra ID de orden (opcional).
     * @param idSesion    ID de sesión (opcional).
     * @return Redirección al frontend según resultado.
     */
    @CrossOrigin(origins = LOCAL_ADDRESS)
    @GetMapping("/webpay/commit")
    public RedirectView commitTransaction(
            @RequestParam(value = TOKEN_KEY, required = false) Optional<String> token,
            @RequestParam(value = "TBK_TOKEN", required = false) Optional<String> tokenError,
            @RequestParam(value = "TBK_ORDEN_COMPRA", required = false) Optional<String> ordenCompra,
            @RequestParam(value = "TBK_ID_SESION", required = false) Optional<String> idSesion) {

        if (tokenError.isPresent()) {
            // El usuario canceló el pago
            RefundRequest request = new RefundRequest();
            request.setAmount(carritoBean.getPedido().getTotal());

            webPayService.refundTransaction(tokenError.get(), request);
            return new RedirectView("/home/pagoCancelado.xhtml");
        }

        if (token.isPresent() && !token.get().trim().isEmpty()) {
            // La transacción fue autorizada, proceder a confirmar y guardar el pedido
            int idPedido = webPayService.commitTransaction(token.get());

            if (idPedido > 0) {
                return new RedirectView("/home/pagoExitoso.xhtml?idPedido=" + idPedido);
            } else {
                return new RedirectView("/home/pagoError.xhtml");
            }
        }

        // No se recibió token válido
        return new RedirectView("/home/pagoError.xhtml");
    }


}
