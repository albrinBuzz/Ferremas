package com.ferremas.config.webPay;

import cl.transbank.webpay.webpayplus.WebpayPlus;
import cl.transbank.webpay.webpayplus.WebpayPlus.Transaction;
import cl.transbank.webpay.common.WebpayOptions;
import cl.transbank.common.IntegrationType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebpayConfiguration {

    private WebpayOptions webpayOptions;


    public WebpayConfiguration() {
        String commerceCode = "597055555532"; // Obtén este dato en el portal de Transbank
        String apiKey = "579B532A7440BB0C9079DED94D31EA1615BACEB56610332264630D42D0A36B1C"; // Obtén este dato en el portal de Transbank
        IntegrationType integrationType = IntegrationType.TEST; // Usa TEST para pruebas, LIVE para producción
        this.webpayOptions = new WebpayOptions(commerceCode, apiKey, integrationType);
    }

    @Bean
    public WebpayOptions getWebpayOptions() {
        return webpayOptions;
    }

}
