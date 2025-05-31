package com.ferremas.controller;


import com.ferremas.model.PagoResponse;
import com.ferremas.service.MercadoPagoService;
import com.ferremas.util.Logger;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/pagos")
public class PagoController {

    @Value("${mercado-pago.access-token}")
    private String accessToken;

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @PostConstruct
    public void init(){
        MercadoPagoConfig.setAccessToken(accessToken);

    }

    // POST: api/pagos/crear
    @GetMapping("/crear")
    public ResponseEntity<Object> crearPago() {
        try {



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



            // Devolver el ID y el punto de inicio del pago
            return ResponseEntity.ok(new PagoResponseDTO(preference.getId(), preference.getInitPoint()));

        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ErrorResponse("Error interno, vuelve a intentar más tarde", ex.getMessage()));
        }
    }

    // Endpoints para manejar el resultado de los pagos

    @GetMapping("/Success")
    public RedirectView success(PagoResponse pagoResponse) {
        if (pagoResponse == null || pagoResponse.getPaymentId() == null){
            return new RedirectView("/home/pagoError.xhtml");
        }

        int idPedido = mercadoPagoService.commitTransaction();

        if (idPedido > 0) {
            return new RedirectView("/home/pagoExitoso.xhtml?idPedido=" + idPedido);
        } else {
            return new RedirectView("/home/pagoError.xhtml");
        }
    }

    @GetMapping("/webhook")
    public ResponseEntity<String>webHok(){

        return ResponseEntity.ok("webhook");
    }
    @GetMapping("/Failure")
    public RedirectView failure(PagoResponse pagoResponse) {
        return new RedirectView("/home/pagoError.xhtml");
    }

    @GetMapping("/Pending")
    public ResponseEntity<Object> pending(PagoResponse pagoResponse) {
        if (pagoResponse == null || pagoResponse.getPaymentId() == null)
            return ResponseEntity.badRequest().body("Los datos de pago no fueron proporcionados correctamente.");

        return ResponseEntity.ok(new PagoResponseDTO(pagoResponse));
    }
}

class PagoResponseDTO {
    private String collectionId;
    private String collectionStatus;
    private String paymentId;
    private String status;
    private String externalReference;
    private String paymentType;
    private String merchantOrderId;
    private String preferenceId;
    private String siteId;
    private String processingMode;
    private String merchantAccountId;

    public PagoResponseDTO(PagoResponse pagoResponse) {
        this.collectionId = pagoResponse.getCollectionId();
        this.collectionStatus = pagoResponse.getCollectionStatus();
        this.paymentId = pagoResponse.getPaymentId();
        this.status = pagoResponse.getStatus();
        this.externalReference = pagoResponse.getExternalReference();
        this.paymentType = pagoResponse.getPaymentType();
        this.merchantOrderId = pagoResponse.getMerchantOrderId();
        this.preferenceId = pagoResponse.getPreferenceId();
        this.siteId = pagoResponse.getSiteId();
        this.processingMode = pagoResponse.getProcessingMode();
        this.merchantAccountId = pagoResponse.getMerchantAccountId();
    }

    public PagoResponseDTO(String id, String initPoint) {
        this.paymentId = id;
        this.status = "Pending";
        this.collectionStatus = "pending";
        this.siteId = initPoint;
    }

    // Getters y Setters
}

class ErrorResponse {
    private String mensaje;
    private String detalle;

    public ErrorResponse(String mensaje, String detalle) {
        this.mensaje = mensaje;
        this.detalle = detalle;
    }

    // Getters y Setters
}
