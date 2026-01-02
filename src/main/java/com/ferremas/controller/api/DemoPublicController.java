package com.ferremas.controller.api;

import com.ferremas.model.CustomUserDetails;
import com.ferremas.util.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/demo")
public class DemoPublicController {

    @GetMapping("/publico")
    public ResponseEntity<String> publico() {
        Logger.logInfo("dentro del endpoint publico");
        return ResponseEntity.ok("Este endpoint es público");
    }

    @GetMapping("/testUsuario")
    public ResponseEntity<?> demo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "¡Acceso concedido!");
        response.put("usuario", userDetails.getUsername());
        response.put("rutUsuario", userDetails.getUsuario().getRutUsuario());
        response.put("roles", userDetails.getAuthorities());
        response.put("firstLogin", userDetails.getUsuario().isFirstLogin());

        return ResponseEntity.ok(response);
    }
}
