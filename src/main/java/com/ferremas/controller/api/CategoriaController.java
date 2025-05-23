package com.ferremas.controller.api;


import com.ferremas.model.Categoria;
import com.ferremas.service.CategoriaService;
import com.ferremas.controller.api.ProductoController.ApiResponse;
import com.ferremas.util.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<?> getAllCategorias() {
        try {
            List<Categoria> categorias = categoriaService.findAll();
            if (categorias.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponse("No se encontraron categorías.", HttpStatus.NO_CONTENT.value()));
            }
            return ResponseEntity.ok(new ApiResponse("Categorías obtenidas con éxito.", HttpStatus.OK.value(), categorias));
        } catch (Exception e) {
            Logger.logInfo("Error: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error al obtener las categorías.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}
