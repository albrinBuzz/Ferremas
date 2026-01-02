package com.ferremas.controller.api;

import com.ferremas.Dto.ProductoDTO;
import com.ferremas.model.Categoria;
import com.ferremas.model.CustomUserDetails;
import com.ferremas.model.Producto;
import com.ferremas.service.CategoriaService;
import com.ferremas.service.ProductoService;
import com.ferremas.util.Logger;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.juli.logging.Log;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
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
    public ResponseEntity<?> getAllProductos(HttpServletRequest request) {
        // Obtener el método HTTP de la solicitud
        String method = request.getMethod();
        Logger.logInfo("Método HTTP: " + method);

        // Obtener la URL completa de la solicitud
        String url = request.getRequestURL().toString();
        Logger.logInfo("URL completa solicitada: " + url);

        // Obtener la URI de la solicitud (sin parámetros de consulta)
        String uri = request.getRequestURI();
        Logger.logInfo("URI solicitada: " + uri);

        // Obtener los parámetros de consulta (si existen)
        String queryString = request.getQueryString();
        Logger.logInfo("Parámetros de consulta: " + (queryString != null ? queryString : "No hay parámetros de consulta"));

        // Obtener la dirección IP del cliente
        String clientIp = request.getRemoteAddr();
        Logger.logInfo("Dirección IP del cliente: " + clientIp);

        // Obtener el tipo de contenido (Content-Type)
        String contentType = request.getContentType();
        Logger.logInfo("Tipo de contenido: " + contentType);

        // Obtener el valor de un encabezado específico (por ejemplo, User-Agent)
        String userAgent = request.getHeader("User-Agent");
        Logger.logInfo("User-Agent: " + userAgent);

        // Obtener todos los encabezados
        Logger.logInfo("Encabezados HTTP: ");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            Logger.logInfo(headerName + ": " + headerValue);
        }

        // Obtener cookies si existen
        if (request.getCookies() != null) {
            Logger.logInfo("Cookies: ");
            for (Cookie cookie : request.getCookies()) {
                Logger.logInfo(cookie.getName() + ": " + cookie.getValue());
            }
        }

        // Leer el cuerpo de la solicitud (si existe, por ejemplo en POST, PUT)
        String body = null;
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            body = sb.toString();
            Logger.logInfo("Cuerpo de la solicitud: " + body);
        } catch (IOException e) {
            Logger.logInfo("No se encontró cuerpo en la solicitud (por ejemplo, en un GET)");
        }


        Logger.logInfo("Obteniendo los productos");
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

    @RequestMapping(value = "/categorias", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCategorias(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Logger.logInfo("Obteniendo las categorais");
        try {
            List<Categoria>categorias=categoriaService.findAll();
            if (categorias.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponse("No se encontraron categorias disponibles.", HttpStatus.NO_CONTENT.value()));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse("categorias obtenidas con éxito.", HttpStatus.OK.value(), categorias));
        } catch (Exception e) {
            Logger.logInfo("Error: "+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error en el servidor al obtener las categorias.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    // Crear un nuevo producto
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProducto(
            @RequestPart("producto") ProductoDTO productoDTO,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        String method = request.getMethod();
        Logger.logInfo("Método HTTP: " + method);

        // Obtener la URL completa de la solicitud
        String url = request.getRequestURL().toString();
        Logger.logInfo("URL completa solicitada: " + url);

        // Obtener la URI de la solicitud (sin parámetros de consulta)
        String uri = request.getRequestURI();
        Logger.logInfo("URI solicitada: " + uri);

        // Obtener los parámetros de consulta (si existen)
        String queryString = request.getQueryString();
        Logger.logInfo("Parámetros de consulta: " + (queryString != null ? queryString : "No hay parámetros de consulta"));

        // Obtener la dirección IP del cliente
        String clientIp = request.getRemoteAddr();
        Logger.logInfo("Dirección IP del cliente: " + clientIp);

        // Obtener el tipo de contenido (Content-Type)
        String contentType = request.getContentType();
        Logger.logInfo("Tipo de contenido: " + contentType);

        // Obtener el valor de un encabezado específico (por ejemplo, User-Agent)
        String userAgent = request.getHeader("User-Agent");
        Logger.logInfo("User-Agent: " + userAgent);

        // Obtener todos los encabezados
        Logger.logInfo("Encabezados HTTP: ");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            Logger.logInfo(headerName + ": " + headerValue);
        }

        // Obtener cookies si existen
        if (request.getCookies() != null) {
            Logger.logInfo("Cookies: ");
            for (Cookie cookie : request.getCookies()) {
                Logger.logInfo(cookie.getName() + ": " + cookie.getValue());
            }
        }

        // Leer el cuerpo de la solicitud (si existe, por ejemplo en POST, PUT)
        /*String body = null;
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                Logger.logInfo(line);
                sb.append(line);
            }
            body = sb.toString();
            Logger.logInfo("Cuerpo de la solicitud: " + body);
        } catch (IOException e) {
            Logger.logInfo("No se encontró cuerpo en la solicitud (por ejemplo, en un GET)");
        }*/


        try {
            // Crear el Producto real
            Producto producto = new Producto();
            producto.setNombre(productoDTO.getNombre());
            producto.setMarca(productoDTO.getMarca());
            producto.setPrecio(productoDTO.getPrecio());
            producto.setDescripcion("Categoria");

            // Buscar la categoria por id
            if (productoDTO.getIdCategoria() != null) {
                Optional<Categoria> categoria = categoriaService.findById(productoDTO.getIdCategoria());
                if (categoria.isPresent()) {
                    producto.setCategoria(categoria.get());
                } else {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse("Categoría no encontrada", 400));
                }
            }

            // Guardar imagen si viene
            if (imagen != null && !imagen.isEmpty()) {
                String nombreArchivo = productoService.guardarImagen(imagen);
                producto.setImagen(nombreArchivo);
            }

            Producto createdProducto = productoService.guardar(producto);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Producto creado con éxito.", HttpStatus.CREATED.value(), createdProducto));

        } catch (Exception e) {
            Logger.logInfo("Error: " + e);
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
