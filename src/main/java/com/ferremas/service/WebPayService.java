package com.ferremas.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import cl.transbank.common.IntegrationType;
import cl.transbank.webpay.common.WebpayOptions;
import cl.transbank.webpay.exception.TransactionCaptureException;
import cl.transbank.webpay.exception.TransactionCommitException;
import cl.transbank.webpay.exception.TransactionRefundException;
import cl.transbank.webpay.exception.TransactionStatusException;
import cl.transbank.webpay.webpayplus.WebpayPlus.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferremas.config.webPay.entity.*;

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


@Service
public class WebPayService {

    private static final String TOKEN_KEY = "token_ws";
    private WebpayOptions webpayOptions;


    private  final ObjectMapper objectMapper = new ObjectMapper(); // Para convertir objetos a JSON
    private  final String API_URL = "https://webpay3gint.transbank.cl/rswebpaytransaction/api/webpay/v1.2/transactions"; // Base URL de la API
    private  final String API_KEY_ID = "597055555532"; // Tu API Key
    private  final String API_KEY_SECRET = "579B532A7440BB0C9079DED94D31EA1615BACEB56610332264630D42D0A36B1C"; // Tu API Secret
    private  final HttpClient client = HttpClient.newHttpClient(); // Instancia de HttpClient

    @Value("${webpay.commerceCode}")
    private String commerceCode;

    @Value("${webpay.apiKey}")
    private String apiKey;

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

    // Inicializa las opciones de WebPay con las credenciales
    public WebPayService() {

    }

    @PostConstruct
    public void init(){
        IntegrationType integrationType = IntegrationType.TEST; // Usa TEST para pruebas, LIVE para producción
        this.webpayOptions = new WebpayOptions(commerceCode, apiKey, integrationType);
    }


    // Crear una transacción
    public WebPayTransactionResponse createTransaction(WebPayTransactionRequest request) throws Exception {
        // Convertir el objeto a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        // Crear la solicitud HTTP
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Tbk-Api-Key-Id", API_KEY_ID)
                .header("Tbk-Api-Key-Secret", API_KEY_SECRET)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        // Enviar la solicitud
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Deserializar la respuesta en un objeto WebPayTransactionResponse
        return objectMapper.readValue(response.body(), WebPayTransactionResponse.class);
    }


        // Confirmar una transacción
    public boolean commitTransaction(String token) {
        try {
            // Creación de la transacción
            Transaction transaction = new Transaction(webpayOptions);

            // Commit de la transacción enviando el token como parámetro
            var respuesta = transaction.commit(token);
            Logger.logInfo(respuesta.toString());

            if (respuesta.getStatus().equals("AUTHORIZED")){
                Pedido pedidoGuardado = guardarPedidoDesdeCarrito();
                guardarTransaccion(pedidoGuardado);
                carritoBean.resetCart();
                return  true;
            }

        } catch (TransactionCommitException e) {
            e.printStackTrace();
            return  false;
        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        }
        return  false;
    }



    // Buscar una transacción
    public String searchTransaction(String token) {
        try {
            // Creación de la transacción
            Transaction transaction = new Transaction(webpayOptions);

            // Búsqueda de la transacción enviando el token como parámetro
            var respuesta = transaction.status(token);
            return respuesta.toString(); // Devuelve el estado de la transacción
        } catch (TransactionStatusException e) {
            e.printStackTrace();
            return "Error al buscar el estado de la transacción: " + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error de comunicación con la API: " + e.getMessage();
        }
    }

    // Reversa una transacción
    public String refundTransaction(String token, RefundRequest request) {
        try {
            // Creación de la transacción
            Transaction transaction = new Transaction(webpayOptions);
            double amount = request.getAmount();

            // Reversa de la transacción enviando el token y monto como parámetro
            var respuesta = transaction.refund(token, amount);
            return respuesta.toString(); // Devuelve el resultado del reembolso
        } catch (TransactionRefundException e) {
            e.printStackTrace();
            return "Error al reversar la transacción: " + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error de comunicación con la API: " + e.getMessage();
        }
    }

    // Capturar una transacción
    public String captureTransaction(String token, CaptureRequest request) {
        try {
            // Creación de la transacción
            Transaction transaction = new Transaction(webpayOptions);
            String buyOrder = request.getBuyOrder();
            String authorizationCode = request.getAuthorizationCode();
            double amount = request.getAmount();

            // Captura de la transacción
            var respuesta = transaction.capture(token, buyOrder, authorizationCode, amount);
            return respuesta.toString(); // Devuelve el resultado de la captura
        } catch (TransactionCaptureException e) {
            e.printStackTrace();
            return "Error al capturar la transacción: " + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error de comunicación con la API: " + e.getMessage();
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


}
