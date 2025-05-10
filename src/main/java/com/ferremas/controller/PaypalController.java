package com.ferremas.controller;

import com.ferremas.model.Clienteinvitado;
import com.ferremas.model.Detallepedido;
import com.ferremas.model.Pedido;
import com.ferremas.model.Transaccion;
import com.ferremas.service.*;
import com.ferremas.util.Logger;
import com.ferremas.views.CarritoBean;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import jakarta.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controlador REST que maneja el flujo de pagos con PayPal.
 * Permite iniciar pagos, ejecutar pagos aprobados y manejar cancelaciones o errores.
 */
@RestController
@RequestMapping("/paypal")
public class PaypalController {

    @Inject
    private CarritoBean carritoBean;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private TransaccionService transaccionService;

    @Autowired
    private MetodopagoService metodopagoService;

    @Autowired
    private EstadopedidoService estadopedidoService;

    private final PaypalService paypalService;

    public PaypalController(PaypalService paypalService) {
        this.paypalService = paypalService;
    }

    /**
     * Inicia el proceso de pago en PayPal.
     * Crea un objeto de pago y redirige al usuario a la URL de aprobación.
     *
     * @param method Método de pago (ej. "paypal")
     * @param amount Monto del pago
     * @param currency Moneda del pago (ej. "USD")
     * @param description Descripción del pago
     * @return RedirectView a la URL de aprobación o a la página de error.
     */
    @PostMapping("/payment/create")
    public RedirectView createPayment(
            @RequestParam("method") String method,
            @RequestParam("amount") String amount,
            @RequestParam("currency") String currency,
            @RequestParam("description") String description
    ) {
        try {
            String cancelUrl = "http://localhost:8080/home/pagoCancelado.xhtml";
            String successUrl = "http://localhost:8080/paypal/payment/success";

            Payment payment = paypalService.createPayment(
                    Double.valueOf(amount),
                    currency,
                    method,
                    "sale",
                    description,
                    cancelUrl,
                    successUrl
            );

            Logger.logInfo("Pago creado correctamente, redirigiendo a PayPal");

            // Busca la URL de aprobación del pago
            for (Links links : payment.getLinks()) {
                if ("approval_url".equals(links.getRel())) {
                    return new RedirectView(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            Logger.logError("Error al crear el pago: " + e.getMessage());
        }

        return new RedirectView("/home/pagoError.xhtml");
    }

    /**
     * Se ejecuta cuando PayPal aprueba el pago.
     * Verifica el estado del pago, guarda el pedido y la transacción.
     *
     * @param paymentId ID del pago de PayPal
     * @param payerId ID del pagador proporcionado por PayPal
     * @return RedirectView a la página de éxito o error
     */
    @GetMapping("/payment/success")
    @Transactional
    public RedirectView paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);

            if ("approved".equals(payment.getState())) {
                Pedido pedidoGuardado = guardarPedidoDesdeCarrito();
                guardarTransaccion(pedidoGuardado);

                carritoBean.resetCart();

                return new RedirectView("/home/pagoExitoso.xhtml?idPedido=" + pedidoGuardado.getIdPedido());
            }
        } catch (Exception e) {
            Logger.logError("Error en pago exitoso: " + e.getMessage());
        }

        return new RedirectView("/home/pagoError.xhtml");
    }

    /**
     * Guarda un pedido basado en el contenido del carrito actual.
     *
     * @return Pedido guardado en la base de datos.
     */
    private Pedido guardarPedidoDesdeCarrito() {
        Pedido pedidoCarrito = carritoBean.getPedido();
        Pedido nuevoPedido = new Pedido();

        nuevoPedido.setRutcliente(pedidoCarrito.getRutcliente());
        nuevoPedido.setFecha(new Date());
        nuevoPedido.setSucursal(pedidoCarrito.getSucursal());
        nuevoPedido.setEstado("espera");

        // Asigna estado inicial del pedido (ej. "pagado", id = 2)
        nuevoPedido.setEstadopedido(
                estadopedidoService.findById(2)
                        .orElseThrow(() -> new IllegalArgumentException("EstadoPedido no encontrado"))
        );

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

    /**
     * Registra una transacción asociada al pedido.
     *
     * @param pedido Pedido con el que se relaciona la transacción.
     */
    private void guardarTransaccion(Pedido pedido) {
        Transaccion transaccion = new Transaccion();
        transaccion.setPedido(pedido);
        transaccion.setFecha(new Date());
        transaccion.setMonto(pedido.getTotal());

        // Se asume que el ID 2 corresponde a "PayPal"
        transaccion.setMetodopago(metodopagoService.obtenerPorId(2));
        transaccionService.guardar(transaccion);
    }

    /**
     * Endpoint para manejar cancelación del pago por parte del usuario.
     *
     * @return Redirección a la vista de cancelación.
     */
    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        return "/home/pagoCancelado.xhtml";
    }

    /**
     * Endpoint para manejar errores durante el proceso de pago.
     *
     * @return Redirección a la vista de error.
     */
    @GetMapping("/payment/error")
    public String paymentError() {
        return "/home/pagoError.xhtml";
    }
}
