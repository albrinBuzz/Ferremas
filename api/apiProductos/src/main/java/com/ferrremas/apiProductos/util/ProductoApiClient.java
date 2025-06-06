package com.ferrremas.apiProductos.util;

import java.net.URI;
import java.net.http.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferrremas.apiProductos.model.Producto;
import com.ferrremas.apiProductos.model.ProductoDTO;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class ProductoApiClient {
    private static final String BASE_URL = "http://localhost:8081/api/productos";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Producto> getAllProductos() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // La respuesta es un ApiResponse con lista en data
            ApiResponse<List<Producto>> apiResponse = mapper.readValue(
                    response.body(),
                    new TypeReference<ApiResponse<List<Producto>>>() {}
            );
            return apiResponse.getData();
        } else if (response.statusCode() == 204) {
            // No content
            return List.of();
        } else {
            throw new RuntimeException("Error al obtener productos: " + response.body());
        }
    }

    public Producto getProductoById(int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ApiResponse<Producto> apiResponse = mapper.readValue(
                    response.body(),
                    new TypeReference<ApiResponse<Producto>>() {}
            );
            return apiResponse.getData();
        } else {
            throw new RuntimeException("Producto no encontrado o error: " + response.body());
        }
    }

    public Producto crearProducto(ProductoDTO productoDTO) throws Exception {
        String jsonProducto = mapper.writeValueAsString(productoDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/crear"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonProducto))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            ApiResponse<Producto> apiResponse = mapper.readValue(
                    response.body(),
                    new TypeReference<ApiResponse<Producto>>() {}
            );
            return apiResponse.getData();
        } else {
            throw new RuntimeException("Error al crear producto: " + response.body());
        }
    }

    public  Producto crearProductoConImagen(
            ProductoDTO productoDTO,
            Path imagePath
    ) throws Exception {
        String boundary = "----WebKitFormBoundary" + UUID.randomUUID();

        // Serializar productoDTO a JSON
        String jsonProducto = mapper.writeValueAsString(productoDTO);

        // Construir cuerpo multipart manualmente
        String bodyStart = "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"producto\"\r\n"
                + "Content-Type: application/json\r\n\r\n"
                + jsonProducto + "\r\n"
                + "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"imagen\"; filename=\"" + imagePath.getFileName() + "\"\r\n"
                + "Content-Type: image/jpeg\r\n\r\n";

        String bodyEnd = "\r\n--" + boundary + "--\r\n";

        byte[] fileBytes = Files.readAllBytes(imagePath);
        byte[] startBytes = bodyStart.getBytes();
        byte[] endBytes = bodyEnd.getBytes();

        byte[] requestBody = new byte[startBytes.length + fileBytes.length + endBytes.length];
        System.arraycopy(startBytes, 0, requestBody, 0, startBytes.length);
        System.arraycopy(fileBytes, 0, requestBody, startBytes.length, fileBytes.length);
        System.arraycopy(endBytes, 0, requestBody, startBytes.length + fileBytes.length, endBytes.length);

        // Construir request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestBody))
                .build();

        // Enviar request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());



        // Parsear la respuesta JSON
        JsonNode root = mapper.readTree(response.body());
        JsonNode dataNode = root.get("data");
        return mapper.treeToValue(dataNode, Producto.class);
    }


    public Producto updateProducto(int id, Producto producto) throws Exception {
        String jsonProducto = mapper.writeValueAsString(producto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonProducto))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ApiResponse<Producto> apiResponse = mapper.readValue(
                    response.body(),
                    new TypeReference<ApiResponse<Producto>>() {}
            );
            return apiResponse.getData();
        } else {
            throw new RuntimeException("Error al actualizar producto: " + response.body());
        }
    }

    public boolean deleteProducto(int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return true;
        } else {
            throw new RuntimeException("Error al eliminar producto: " + response.body());
        }
    }

    // Clase interna para mapear respuesta general
    public static class ApiResponse<T> {
        private String message;
        private int status;
        private T data;

        // getters y setters
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        public T getData() { return data; }
        public void setData(T data) { this.data = data; }
    }

    // Debes tener la clase Producto igual que la de tu modelo o DTO
    /*public static class Producto {
        private Integer idProducto;
        private String descripcion;
        private String marca;
        private String nombre;
        private Integer precio;
        private String imagen;
        private Categoria categoria;
        // getters y setters...

        public static class Categoria {
            private Integer idCategoria;
            private String descripcion;
            private String nombre;
            // getters y setters...
        }
    }*/
}
