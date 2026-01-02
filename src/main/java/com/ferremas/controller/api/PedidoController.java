package com.ferremas.controller.api;

import com.ferremas.model.CustomUserDetails;
import com.ferremas.model.Pedido;
import com.ferremas.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<?> getPedidos(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String rutUsuario = userDetails.getUsuario().getRutUsuario();

        List<Pedido> pedidos = pedidoService.obtenerPorCliente(rutUsuario);
        return ResponseEntity.ok(pedidos);
    }
}
