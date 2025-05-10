package com.ferremas.service;

import com.ferremas.util.Logger;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Servicio que maneja la integración con la API de PayPal.
 * Permite crear y ejecutar pagos a través de la plataforma PayPal.
 */
@Service
public class PaypalService {

    // Contexto de la API de PayPal que contiene las credenciales y configuración.
    private final APIContext apiContext;

    public PaypalService(APIContext apiContext) {
        this.apiContext = apiContext;
    }

    /**
     * Crea un objeto de pago de PayPal con los datos proporcionados.
     *
     * @param total Monto total del pago.
     * @param currency Código de la moneda (ej. "USD", "EUR").
     * @param method Método de pago (ej. "paypal").
     * @param intent Intención del pago (ej. "sale").
     * @param description Descripción del pago.
     * @param cancelUrl URL a redirigir si el usuario cancela el pago.
     * @param successUrl URL a redirigir si el pago se aprueba.
     * @return Objeto Payment con los detalles del pago creado.
     * @throws PayPalRESTException Si ocurre un error al crear el pago.
     */
    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl
    ) throws PayPalRESTException {
        // Configura el monto del pago con su moneda.
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.US, "%.2f", total)); // Formato de monto (ej. 9.99)

        // Crea una transacción con la descripción y el monto.
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        // Agrega la transacción a la lista de transacciones.
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // Configura el pagador con el método proporcionado.
        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        // Crea el objeto de pago principal con intent, pagador y transacciones.
        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        // Define las URLs de redirección en caso de éxito o cancelación.
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        // Crea el pago a través de la API de PayPal.
        return payment.create(apiContext);
    }

    /**
     * Crea un pago de PayPal y retorna la URL de aprobación si tiene éxito.
     *
     * @param method Método de pago.
     * @param amount Monto total como cadena.
     * @param currency Moneda del pago.
     * @param description Descripción del pago.
     * @return URL de aprobación de PayPal o página de error.
     */
    public String crearPago(String method, String amount, String currency, String description) {
        try {
            // URLs de redirección para cancelación y éxito del pago.
            String cancelUrl = "http://localhost:8080/home/pagoCancelado.xhtml";
            String successUrl = "http://localhost:8080/paypal/payment/success";

            // Llama a la función para crear el pago.
            Payment payment = createPayment(
                    Double.valueOf(amount),
                    currency,
                    method,
                    "sale", // venta directa
                    description,
                    cancelUrl,
                    successUrl
            );

            // Busca y retorna la URL de aprobación de PayPal.
            for (Links links : payment.getLinks()) {
                if ("approval_url".equals(links.getRel())) {
                    return links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            // En caso de error, lo registra y redirige a una página de error.
            Logger.logInfo(e.getMessage());
        }

        return "/home/pagoError.xhtml";
    }

    /**
     * Ejecuta un pago previamente aprobado por el usuario.
     *
     * @param paymentId ID del pago proporcionado por PayPal.
     * @param payerId ID del pagador proporcionado por PayPal.
     * @return Objeto Payment ejecutado.
     * @throws PayPalRESTException Si ocurre un error al ejecutar el pago.
     */
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        // Crea un objeto de ejecución del pago con el ID del pagador.
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        // Ejecuta el pago usando la API de PayPal.
        return payment.execute(apiContext, paymentExecution);
    }
}
