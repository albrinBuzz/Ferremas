package com.ferremas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapear ruta externa (en webapp) a una URL pública
        registry.addResourceHandler("/imagenes/**")
                .addResourceLocations("classpath:/webapp/resources/images/product/");
    }
}
