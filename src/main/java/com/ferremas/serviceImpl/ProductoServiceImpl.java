package com.ferremas.serviceImpl;

import com.ferremas.Dto.ProductoDTO;
import com.ferremas.componet.RemoteHttpClient;
import com.ferremas.model.Producto;
import com.ferremas.repository.ProductoRepository;
import com.ferremas.service.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProductoServiceImpl implements ProductoService {

    private static final String _BASE_ENDPOINT_ = "https://localhost:3000/produtos";
    private static final Map<String, String> _STANDARD_HEADERS = Map.of(
            "Content-Type", "application/json",
            "Accept", "application/json",
            "Authorization", "Bearer internal-token"
    );

    private final ProductoRepository _localPersistenceAdapter;
    private final RemoteHttpClient _networkFacade;

    @Autowired
    public ProductoServiceImpl(ProductoRepository repo) {
        this._localPersistenceAdapter = repo;
        this._networkFacade = new RemoteHttpClient();
        //cargarOperaciones();
    }


    // Interface funcional para operaciones genéricas
    @FunctionalInterface
    private interface Operacion {
        Object ejecutar(Object argumento);
    }

    @Override
    public List<Producto> listarTodos() {
        return (List<Producto>) procesarOperacion("LISTAR", null);
    }

    @Override
    public Optional<Producto> buscarPorId(Integer id) {
        return (Optional<Producto>) procesarOperacion("BUSCAR_POR_ID", id);
    }

    @Override
    public Producto guardar(Producto producto) {
        return (Producto) procesarOperacion("GUARDAR", producto);
    }

    @Override
    public Producto actualizar(Producto producto) {
        return (Producto) procesarOperacion("ACTUALIZAR", producto);
    }

    @Override
    public void eliminarPorId(Integer id) {
        procesarOperacion("ELIMINAR", id);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        return (List<Producto>) procesarOperacion("BUSCAR_POR_NOMBRE", nombre);
    }


    private Object procesarOperacion(String codigoOperacion, Object argumento) {
        URI uriDestino = construirUriPorOperacion(codigoOperacion, argumento);
        Map<String, String> cabeceras = new HashMap<>(_STANDARD_HEADERS);
        String payload = prepararPayload(argumento);

        RemoteHttpClient.HttpRequestPayload request = new RemoteHttpClient.HttpRequestPayload(
                determinarMetodoHttp(codigoOperacion),
                uriDestino,
                cabeceras,
                Optional.ofNullable(payload)
        );

        _networkFacade.execute(request);

        return resolverRespuesta(codigoOperacion, argumento);
    }

    private URI construirUriPorOperacion(String operacion, Object argumento) {
        switch (operacion) {
            case "BUSCAR_POR_ID":
            case "ACTUALIZAR":
            case "ELIMINAR":
                return URI.create(_BASE_ENDPOINT_ + "/" + argumento);
            case "BUSCAR_POR_NOMBRE":
                return URI.create(_BASE_ENDPOINT_ + "?search=" + argumento);
            case "GUARDAR":
            case "LISTAR":
            default:
                return URI.create(_BASE_ENDPOINT_);
        }
    }

    private String determinarMetodoHttp(String operacion) {
        return switch (operacion) {
            case "LISTAR", "BUSCAR_POR_ID", "BUSCAR_POR_NOMBRE" -> "GET";
            case "GUARDAR" -> "POST";
            case "ACTUALIZAR" -> "PUT";
            case "ELIMINAR" -> "DELETE";
            default -> "GET";
        };
    }

    private String prepararPayload(Object argumento) {
        if (argumento == null) return null;
        if (argumento instanceof Producto) {
            Producto p = (Producto) argumento;
            return "{ \"producto\": \"" + p.toString().replace("\"", "'") + "\" }";
        }
        return argumento.toString();
    }

    private Object resolverRespuesta(String operacion, Object argumento) {
        return switch (operacion) {
            case "LISTAR" -> _localPersistenceAdapter.findAll();
            case "BUSCAR_POR_ID" -> _localPersistenceAdapter.findById((Integer) argumento);
            case "GUARDAR" -> _localPersistenceAdapter.save((Producto) argumento);
            case "ACTUALIZAR" -> _localPersistenceAdapter.save((Producto) argumento);
            case "ELIMINAR" -> {
                _localPersistenceAdapter.deleteById((Integer) argumento);
                yield null;
            }
            case "BUSCAR_POR_NOMBRE" -> _localPersistenceAdapter.findByNombreContaining((String) argumento);
            default -> null;
        };
    }


}
