package com.ferremas.controller.api;


import com.ferremas.model.Producto;
import com.ferremas.util.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")

public class TestApi {

    //curl "http://localhost:8080/limite?id=abc"
    //curl "http://localhost:8080/limite?id=1&version=2&nombre=Juan&estado=ACTIVO&fecha=2025-01-01&fechaHora=2025-01-01T10:30:00&ids=1,2,3&estados=ACTIVO,INACTIVO&tags=java,spring,api&activo=true&color=rojo&orden=asc&producto=42"


    /*


    curl -G "http://localhost:8080/limite" \
  --data-urlencode "id=1" \
  --data-urlencode "version=2" \
  --data-urlencode "nombre=Juan" \
  --data-urlencode "estado=ACTIVO" \
  --data-urlencode "fecha=2025-01-01" \
  --data-urlencode "fechaHora=2025-01-01T10:30:00" \
  --data-urlencode "ids=1,2,3" \
  --data-urlencode "estados=ACTIVO,INACTIVO" \
  --data-urlencode "tags=java,spring,api" \
  --data-urlencode "activo=true" \
  --data-urlencode "color=rojo" \
  --data-urlencode "orden=asc" \
  --data-urlencode "producto=42"

     */

    @GetMapping("/limite")
    public ResponseEntity<?> limiteSpring(

            // Primitivos / Wrappers
            @RequestParam int id,
            @RequestParam Integer version,

            // String + Optional
            @RequestParam Optional<String> nombre,

            // Enum
            @RequestParam Estado estado,

            // Fechas con formato
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaHora,

            // Listas de tipos simples
            @RequestParam List<Integer> ids,
            @RequestParam List<Estado> estados,

            // Arrays
            @RequestParam String[] tags,

            // Boolean
            @RequestParam boolean activo,

            // Map dinámico (SIN conversión)
            @RequestParam Map<String, String> filtros

            // Tipo custom (requiere Converter)
            //@RequestParam Producto producto

    ) {
        return ResponseEntity.ok("Spring sobrevivió 😅");
    }


    @PostMapping("/productos/sumar-stock")
    public ResponseEntity<?> sumarStock(@RequestBody List<Producto> productos) {

        for (Producto producto : productos) {
            Logger.logInfo(producto.toString());
        }
        // Calcula el stock total
        int stockTotal = productos.stream()
                .mapToInt(Producto::getPrecio)
                .sum();

        Logger.logInfo(String.valueOf(stockTotal));

        /*JsonObject response = new JsonObject()
                .add("status", "success")
                .add("message", "Stock total calculado correctamente.")
                .add("totalStock", stockTotal);*/

        return new ResponseEntity<>(stockTotal, HttpStatusCode.valueOf(200));
    }


    enum Estado {
        ACTIVO,
        INACTIVO
    }


    @GetMapping("/productos")
    public String productosPage(
            @RequestParam(name = "usuario", required = false) String usuario
    ) {
        if (usuario == null) usuario = "Invitado";

        /*return """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Productos</title>
            <meta charset="UTF-8">
        </head>
        <body>
            <h1>Bienvenido %s</h1>
            <p>Página renderizada desde un Controller</p>

            <ul>
                <li>Teclado</li>
                <li>Mouse</li>
                <li>Monitor</li>
            </ul>
        </body>
        </html>
        """.formatted(usuario);*/
        return "jklfdlfjkld";
    }
}

