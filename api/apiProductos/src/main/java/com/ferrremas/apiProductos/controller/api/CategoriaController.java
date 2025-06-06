package com.ferrremas.apiProductos.controller.api;


import com.ferrremas.apiProductos.controller.api.ProductoController.ApiResponse;
import com.ferrremas.apiProductos.model.Categoria;
import com.ferrremas.apiProductos.service.CategoriaService;
import com.ferrremas.apiProductos.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                        .body(new ProductoController.ApiResponse("No se encontraron categorías.", HttpStatus.NO_CONTENT.value()));
            }
            return ResponseEntity.ok(new ApiResponse("Categorías obtenidas con éxito.", HttpStatus.OK.value(), categorias));
        } catch (Exception e) {
            Logger.logInfo("Error: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error al obtener las categorías.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}
