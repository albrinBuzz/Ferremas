package com.ferremas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Value("${app.upload.dir}")
    private String uploadDir;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapear ruta externa (en webapp) a una URL pública

        /*registry.addResourceHandler("/imagenes/**")
                .addResourceLocations("classpath:/webapp/resources/images/product/");*/
        registry.addResourceHandler("/imagenes/**")
                .addResourceLocations("file:" + Paths.get(uploadDir).toAbsolutePath() + "/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Afecta a todos los endpoints
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
