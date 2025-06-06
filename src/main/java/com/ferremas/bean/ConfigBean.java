package com.ferremas.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Value;

@Named("configBean")
@ApplicationScoped
public class ConfigBean {

    @Value("${app.url.imagenes}")
    private String urlImagenes;

    public String getUrlImagenes() {
        return urlImagenes;
    }
}
