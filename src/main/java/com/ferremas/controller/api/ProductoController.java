package com.ferremas.controller.api;

import com.ferremas.model.Producto;
import com.ferremas.service.ProductoService;
import com.ferremas.util.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {


    @Autowired
    private ProductoService productoService;

    // Obtener todos los productos
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getAllProductos() {
        try {
            List<Producto> productos = productoService.listarTodos();
            if (productos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponse("No se encontraron productos disponibles.", HttpStatus.NO_CONTENT.value()));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse("Productos obtenidos con éxito.", HttpStatus.OK.value(), productos));
        } catch (Exception e) {
            Logger.logInfo("Error: "+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error en el servidor al obtener los productos.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    // Crear un nuevo producto
    @PostMapping
    public ResponseEntity<?> createProducto(@Valid @RequestBody Producto producto) {
        try {
            Producto createdProducto = productoService.guardar(producto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Producto creado con éxito.", HttpStatus.CREATED.value(), createdProducto));
        } catch (Exception e) {
            Logger.logInfo("Error: "+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error al crear el producto.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    // Obtener un producto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductoById(@PathVariable("id") Integer id) {
        Logger.logInfo("Id producto: "+id);
        try {
            Optional<Producto> producto = productoService.buscarPorId(id);

            if (producto.isPresent()) {
                Logger.logInfo(producto.get().toString());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiResponse("Producto encontrado con éxito.", HttpStatus.OK.value(), producto.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Producto no encontrado.", HttpStatus.NOT_FOUND.value()));
            }
        } catch (Exception e) {
            Logger.logInfo("Error: "+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error en el servidor al obtener el producto.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    // Actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProducto(@PathVariable("id") Integer id, @Valid @RequestBody Producto productoDetails) {
        try {
            if (productoService.buscarPorId(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Producto no encontrado para actualizar.", HttpStatus.NOT_FOUND.value()));
            }
            productoDetails.setIdProducto(id);
            Producto updatedProducto = productoService.actualizar(productoDetails);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse("Producto actualizado con éxito.", HttpStatus.OK.value(), updatedProducto));
        } catch (Exception e) {
            Logger.logInfo("Error: "+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error al actualizar el producto.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    // Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable("id") Integer id) {
        try {
            if (productoService.buscarPorId(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Producto no encontrado para eliminar.", HttpStatus.NOT_FOUND.value()));
            }
            productoService.eliminarPorId(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse("Producto eliminado con éxito.", HttpStatus.OK.value()));
        } catch (Exception e) {
            Logger.logInfo("Error: "+e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error al eliminar el producto.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    // Filtrar productos por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> getProductosByNombre(@PathVariable("nombre") String nombre) {
        try {
            List<Producto> productos = productoService.buscarPorNombre(nombre);
            if (productos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiResponse("No se encontraron productos con ese nombre.", HttpStatus.NO_CONTENT.value()));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse("Productos encontrados con éxito.", HttpStatus.OK.value(), productos));
        } catch (Exception e) {
            Logger.logInfo("Error: "+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error en el servidor al buscar productos por nombre.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    // Respuesta estándar de la API
    public static class ApiResponse {
        private String message;
        private int status;
        private Object data;

        // Constructor para respuestas generales
        public ApiResponse(String message, int status) {
            this.message = message;
            this.status = status;
        }

        // Constructor para respuestas con datos
        public ApiResponse(String message, int status, Object data) {
            this.message = message;
            this.status = status;
            this.data = data;
        }

        // Getters y setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

}
