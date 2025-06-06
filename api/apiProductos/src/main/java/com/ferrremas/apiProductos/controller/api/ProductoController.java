package com.ferrremas.apiProductos.controller.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferrremas.apiProductos.model.Producto;
import com.ferrremas.apiProductos.model.ProductoDTO;
import com.ferrremas.apiProductos.service.CategoriaService;
import com.ferrremas.apiProductos.service.ProductoService;
import com.ferrremas.apiProductos.util.Logger;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {


    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

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



    /*
    curl -X POST http://localhost:8081/api/productos/crear \
  -H "Content-Type: multipart/form-data" \
  -F "producto={\"nombre\":\"Taladro Bosch\",\"precio\":199.99,\"descripcion\":\"Taladro de impacto\",\"stock\":15}" \
  -F "imagen=@/ruta/a/tu/imagen.jpg"

     */
    // Crear un nuevo producto
    @PostMapping(value = "/crear", consumes = "multipart/form-data")
    public ResponseEntity<?> createProducto( @RequestParam("producto") String productoJson,@RequestParam("imagen") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Archivo vacío");
        }

        // Aquí podrías guardar la imagen en disco, base de datos, S3, etc.
        String fileName = file.getOriginalFilename();

        try {

            // Ruta para Angular (Spring static)
            Path rutaStatic = Paths.get("src", "main", "resources", "static", "product");
            Path ruta = Paths.get("uploads", "product"); // Ruta externa
            Files.createDirectories(ruta);

            // Para copiar a la ruta static, primero tenemos que obtener un nuevo InputStream
            // porque el anterior ya se consumió
            InputStream in2 = file.getInputStream();
            upload(fileName, ruta.toString(), in2);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            ObjectMapper mapper=new ObjectMapper();
            ProductoDTO productoDTO=mapper.readValue(productoJson, ProductoDTO.class);
            Producto producto=productoDTO.toEntity();
            producto.setCategoria(categoriaService.findById(productoDTO.getIdCategoria()).get());

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

    public void upload(String fileName, String destination, InputStream in) {
        try {
            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(destination + File.separator + fileName);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            Logger.logInfo("Archivo creeado en: "+destination + File.separator + fileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
