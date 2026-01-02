package com.ferremas.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.transbank.common.IntegrationApiKeys;
import cl.transbank.common.IntegrationCommerceCodes;
import cl.transbank.common.IntegrationType;
import cl.transbank.webpay.common.WebpayOptions;
import cl.transbank.webpay.exception.TransactionCommitException;
import cl.transbank.webpay.exception.TransactionRefundException;
import cl.transbank.webpay.webpayplus.WebpayPlus.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferremas.config.webPay.entity.WebPayTransactionRequest;
import com.ferremas.config.webPay.entity.WebPayTransactionResponse;
import com.ferremas.config.webPay.entity.RefundRequest;
import com.ferremas.model.Detallepedido;
import com.ferremas.model.Pedido;
import com.ferremas.model.Transaccion;
import com.ferremas.util.Logger;
import com.ferremas.views.CarritoBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Servicio que encapsula la lógica de integración con WebPay Plus
 * para crear, confirmar y anular transacciones.
 */
@Service
public class WebPayService {

    private static final String TOKEN_KEY = "token_ws";
    private static final String API_URL = "https://webpay3gint.transbank.cl/rswebpaytransaction/api/webpay/v1.2/transactions";
    //@Value("${webpay.commerceCode}")
    private static  String API_COMERCIO="597055555532";
    //@Value("${webpay.apiKey}")
    private static  String API_KEY ="579B532A7440BB0C9079DED94D31EA1615BACEB56610332264630D42D0A36B1C";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();
    private WebpayOptions webpayOptions;


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

    /**
     * Inicializa las opciones de conexión con WebPay, usando modo TEST.
     */
    @PostConstruct
    public void init() {
        this.webpayOptions = new WebpayOptions(API_COMERCIO, API_KEY, IntegrationType.TEST);
        //IntegrationCommerceCodes.WEBPAY_PLUS_MALL, IntegrationApiKeys.WEBPAY;
    }

    /**
     * Crea una transacción en WebPay Plus.
     *
     * @param request Objeto con los datos requeridos para iniciar la transacción.
     * @return WebPayTransactionResponse con los datos para redirigir al usuario.
     * @throws Exception si ocurre un error de red o serialización.
     */
    public WebPayTransactionResponse createTransaction(WebPayTransactionRequest request) throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Tbk-Api-Key-Id", API_COMERCIO)
                .header("Tbk-Api-Key-Secret", API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), WebPayTransactionResponse.class);
    }

    /**
     * Confirma una transacción previamente iniciada, usando el token entregado por WebPay.
     * Si la transacción es exitosa, guarda el pedido y la transacción.
     *
     * @param token Token de la transacción entregado por WebPay.
     * @return ID del pedido guardado, o 0 si ocurre un error.
     */
    public int commitTransaction(String token) {
        try {
            Transaction transaction = new Transaction(webpayOptions);
            var respuesta = transaction.commit(token);

            Logger.logInfo("Respuesta WebPay: " + respuesta);

            if ("AUTHORIZED".equals(respuesta.getStatus())) {
                Pedido pedidoGuardado = guardarPedidoDesdeCarrito();
                guardarTransaccion(pedidoGuardado);
                carritoBean.resetCart();
                return pedidoGuardado.getIdPedido();
            }
        } catch (TransactionCommitException | IOException e) {
            Logger.logError("Error al confirmar transacción: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Realiza un rollback o anula la trasaccion, cancela la trasaccion  previamente confirmada.
     *
     * @param token   Token de la transacción a reversar.
     * @param request Objeto que contiene el monto a devolver.
     * @return Cadena con el resultado del reembolso.
     */
    public String refundTransaction(String token, RefundRequest request) {
        try {
            Transaction transaction = new Transaction(webpayOptions);
            var respuesta = transaction.refund(token, request.getAmount());

            return respuesta.toString();
        } catch (TransactionRefundException | IOException e) {

            return "Error al anual la transacción: " + e.getMessage();
        }
    }

    /**
     * Guarda un nuevo pedido en base a los datos del carrito.
     *
     * @return Pedido persistido en la base de datos.
     */
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
