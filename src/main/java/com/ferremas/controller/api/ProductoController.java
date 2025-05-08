package com.ferremas.controller.api;

import com.ferremas.model.Producto;
import com.ferremas.service.ProductoService;
import com.ferremas.util.Logger;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    private static final AtomicInteger successfulRequests = new AtomicInteger(0);
    private static final int MAX_CONCURRENT_REQUESTS = 50;
    private static final Semaphore semaphore = new Semaphore(MAX_CONCURRENT_REQUESTS);


    // Obtener todos los productos
    @GetMapping
    public ResponseEntity<?> getAllProductos() {
        boolean acquired = false;
        try {



            List<Producto> productos = productoService.listarTodos();
            if (productos.isEmpty()) {

                return ResponseEntity.noContent().build(); // Si no hay productos, respondemos con 204 (No Content)
            }

            heavyTask();
            Logger.logInfo("Get Productos "+successfulRequests.incrementAndGet());
            return ResponseEntity.ok(productos); // Respondemos con 200 OK y los productos
        }  catch (Exception e) {
            // Log de la excepción
            e.printStackTrace();
            // Error de servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private void heavyTask() throws InterruptedException {
        Thread.sleep(TimeUnit.MILLISECONDS.toSeconds(3));
    }


    // Crear un nuevo producto
    @PostMapping
    public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) {
        Producto createdProducto = productoService.guardar(producto);
        return new ResponseEntity<>(createdProducto, HttpStatus.CREATED);
    }

    // Obtener un producto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductoById(@PathVariable("id") Integer id) {
        Optional<Producto> producto = productoService.buscarPorId(id);

        if (producto.isPresent()){
            return new ResponseEntity<>(producto.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Vuelo no encontrado", HttpStatus.NOT_FOUND);
        }

        /*return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Producto no encontrado"));*/
    }


    // Actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProducto(@PathVariable("id") Integer id, @Valid @RequestBody Producto productoDetails) {
        if (productoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado para actualizar");
        }
        productoDetails.setIdProducto(id);
        Producto updatedProducto = productoService.actualizar(productoDetails);
        return ResponseEntity.ok(updatedProducto);
    }

    // Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable("id") Integer id) {
        if (productoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        productoService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    // Filtrar productos por nombre
    @GetMapping("/nombre")
    public ResponseEntity<List<Producto>> getProductosByNombre(@RequestParam("nombre") String nombre) {
        List<Producto> productos = productoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(productos);
    }

}
