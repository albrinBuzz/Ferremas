package com.ferrremas.apiProductos.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.ferrremas.apiProductos.model.Producto;
import com.ferrremas.apiProductos.model.ProductoDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.UUID;

public class Main {

    public static void main(String[] args) throws Exception {


        ProductoApiClient apiClient=new ProductoApiClient();

        ProductoDTO producto = new ProductoDTO();
        producto.setNombre("Taladro Bosch");
        producto.setPrecio(19999);
        producto.setDescripcion("Taladro de impacto");
        producto.setIdCategoria(1);
        producto.setMarca("boch");
        producto.setImagen("Cinta_Métrica_Stanley_30-615.jpeg"); // solo nombre, se usa en filename

        Path imagePath = Path.of("/home/cris/java/web/Ferremas/src/main/webapp/resources/images/product/Cinta_Métrica_Stanley_30-615.jpeg");

        //Producto creado = apiClient.crearProductoConImagen(producto, imagePath);
        //System.out.println("Producto creado: " + creado);
        crearProducto();
        //var  list= apiClient.getAllProductos();
        //list.forEach(producto1 -> System.out.println(producto1.toString()));
    }

    public static void  crearProducto() throws IOException, InterruptedException {
        String boundary = "----WebKitFormBoundary" + UUID.randomUUID();
        String imagePath = "/home/cris/Downloads/play5.jpeg"; // Reemplaza con la ruta real
        Path filePath = Path.of(imagePath);


        ProductoDTO producto=new ProductoDTO();

        producto.setNombre("play5");
        producto.setPrecio(19999);
        producto.setImagen(filePath.toFile().getName());
        producto.setDescripcion("fjdkfdf");
        producto.setIdCategoria(1);
        producto.setMarca("boch");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(producto);



        // Construcción manual del cuerpo multipart
        String bodyStart = "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"producto\"\r\n"
                + "Content-Type: application/json\r\n\r\n"
                + json + "\r\n"
                + "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"imagen\"; filename=\""+filePath.toFile().getName()+"\"\r\n"
                + "Content-Type: image/jpeg\r\n\r\n";

        String bodyEnd = "\r\n--" + boundary + "--\r\n";

        byte[] fileBytes = java.nio.file.Files.readAllBytes(filePath);
        byte[] startBytes = bodyStart.getBytes();
        byte[] endBytes = bodyEnd.getBytes();

        byte[] requestBody = new byte[startBytes.length + fileBytes.length + endBytes.length];
        System.arraycopy(startBytes, 0, requestBody, 0, startBytes.length);
        System.arraycopy(fileBytes, 0, requestBody, startBytes.length, fileBytes.length);
        System.arraycopy(endBytes, 0, requestBody, startBytes.length + fileBytes.length, endBytes.length);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/productos/crear"))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            JsonNode root = mapper.readTree(response.body());
            JsonNode dataNode = root.get("data"); // accedes a todo el contenido de "data"
            Producto productoDTO = mapper.treeToValue(dataNode, Producto.class);
            System.out.println("Producto: " + productoDTO.toString());
        }catch (Exception e){

        }

    }
}
