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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {


    private static AtomicLong contSolictues=new AtomicLong(1);

    @Autowired
    private ProductoService productoService;

    // Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos(
            @RequestParam(value = "nombre", required = false) String nombre) {
        try {
            if (nombre!=null){
                List<Producto> productos = productoService.buscarPorNombre(nombre);
                return ResponseEntity.ok(productos);
            }

            long startTime = System.currentTimeMillis();
            simulateHeavyTask();
            long endTime = System.currentTimeMillis();

            // Simulación de carga costosa que toma tiempo
            Logger.logInfo("Tarea costosa completada en " + (endTime - startTime) + " ms");

            List<Producto> productos = productoService.listarTodos();
            if (productos.isEmpty()) {
                return ResponseEntity.noContent().build(); // Si no hay productos, respondemos con 204 (No Content)
            }


            contSolictues.incrementAndGet();

            Logger.logInfo("Obteniendo los aviones");

            Logger.logInfo("Cantidadd Veces "+contSolictues.get());
            return ResponseEntity.ok(productos); // Respondemos con 200 OK y los productos
        }  catch (Exception e) {
            // Log de la excepción
            e.printStackTrace();
            // Error de servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private void simulateHeavyTask() {
        try {
            // Simulación de una operación intensiva
            long sum = 0;
            for (long i = 0; i < 1_000_000_000L; i++) {
                sum += i;
            }
            System.out.println("Resultado de la suma costosa: " + sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
