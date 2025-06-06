package com.ferrremas.apiProductos;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Esto expone la ruta externa /uploads/product como /imagenes/product/
        registry.addResourceHandler("/imagenes/product/**")
                .addResourceLocations("file:uploads/product/");
    }
}
