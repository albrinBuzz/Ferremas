package com.ferremas.service;


import cl.transbank.webpay.exception.TransactionCommitException;
import cl.transbank.webpay.webpayplus.WebpayPlus;
import com.ferremas.model.Detallepedido;
import com.ferremas.model.Pedido;
import com.ferremas.model.Transaccion;
import com.ferremas.util.Logger;
import com.ferremas.views.CarritoBean;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;

import com.mercadopago.resources.preference.Preference;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MercadoPagoService {

    @Value("${mercado-pago.access-token}")
    private String accessToken;

    @Autowired
    private WebPayService webPayService;

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


    @PostConstruct
    public void init(){
        MercadoPagoConfig.setAccessToken(accessToken);

    }


    public String crearPago() throws Exception {



        PreferenceItemRequest itemRequest =
                PreferenceItemRequest.builder()
                        .title("Games")
                        .description("PS5")
                        .categoryId("games")
                        .quantity(2)
                        .currencyId("CLP")
                        .unitPrice(new BigDecimal("4000"))
                        .build();

        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);

        PreferenceBackUrlsRequest backUrls =
// ...
                PreferenceBackUrlsRequest.builder()
                        .success("http://localhost:8080/api/pagos/success")
                        .pending("http://localhost:8080/api/pagos/pending")
                        .failure("http://localhost:8080/api/pagos/failure")
                        .build();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items).
                backUrls(backUrls).
                build();


        PreferenceClient client = new PreferenceClient();

        Preference preference = client.create(preferenceRequest);

        Logger.logInfo(preference.getSandboxInitPoint());
        Logger.logInfo(preference.getId());


        return preference.getInitPoint(); // <-- Checkout Pro URL
    }

    public int commitTransaction() {


                Pedido pedidoGuardado = guardarPedidoDesdeCarrito();
                guardarTransaccion(pedidoGuardado);
                carritoBean.resetCart();
                return pedidoGuardado.getIdPedido();

    }

    private Pedido guardarPedidoDesdeCarrito() {
        Pedido pedidoCarrito = carritoBean.getPedido();
        Pedido nuevoPedido = new Pedido();

        nuevoPedido.setRutcliente(pedidoCarrito.getRutcliente());
        nuevoPedido.setFecha(new Date());
        nuevoPedido.setSucursal(pedidoCarrito.getSucursal());
        nuevoPedido.setEstado("espera");

        // Asigna el estado "En espera"
        nuevoPedido.setEstadopedido(
                estadopedidoService.findById(2)
                        .orElseThrow(() -> new IllegalArgumentException("EstadoPedido no encontrado"))
        );

        // Copia los detalles del carrito
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
     * Registra una transacción exitosa en el sistema.
     *
     * @param pedido Pedido asociado a la transacción.
     */
    private void guardarTransaccion(Pedido pedido) {
        Transaccion transaccion = new Transaccion();
        transaccion.setPedido(pedido);
        transaccion.setFecha(new Date());
        transaccion.setMonto(pedido.getTotal());
        transaccion.setMetodopago(metodopagoService.obtenerPorId(2));

        transaccionService.guardar(transaccion);
    }
}
