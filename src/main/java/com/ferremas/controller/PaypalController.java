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

            Logger.logInfo("Pagando");


            for (Links links: payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return new RedirectView(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            Logger.logInfo(e.getMessage());
        }
        return new RedirectView("/home/pagoError.xhtml");
    }



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
                var IdPedido=pedidoGuardado.getIdPedido();
                carritoBean.resetCart();
                //FacesContext.getCurrentInstance().getExternalContext().redirect("/home/pagoExitoso.xhtml?idPedido="+IdPedido);
                return new RedirectView("/home/pagoExitoso.xhtml?idPedido="+IdPedido);
            }
        } catch (Exception e) {
            Logger.logError("Error en pago exitoso: " + e.getMessage());
        }

        return new RedirectView("/home/pagoError.xhtml");
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


    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        return "/home/pagoCancelado.xhtml";
    }

    @GetMapping("/payment/error")
    public String paymentError() {
        return "/home/pagoError.xhtml";
    }
}