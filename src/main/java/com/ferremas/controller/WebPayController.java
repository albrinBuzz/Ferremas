package com.ferremas.controller;

import com.ferremas.service.WebPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class WebPayController {

    @Autowired
    private WebPayService webPayService;

    static final String LOCAL_ADDRESS = "http://localhost:8080";
    private static final String TOKEN_KEY = "token_ws";

    @CrossOrigin(origins = LOCAL_ADDRESS)
    @GetMapping("/webpay/commit")
    public RedirectView commitTransaction(
            @RequestParam(TOKEN_KEY) String token) {
        // Delegar la lógica de negocio al servicio
        boolean response = webPayService.commitTransaction(token);

        if (response) {
            return new RedirectView("/home/pagoExitoso.xhtml");
        } else {
            return new RedirectView("/home/pagoError.xhtml");
        }

    }
}
