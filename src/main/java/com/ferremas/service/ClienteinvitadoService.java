package com.ferremas.service;

import com.ferremas.model.Clienteinvitado;
import com.ferremas.repository.ClienteinvitadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteinvitadoService {

    private final ClienteinvitadoRepository clienteinvitadoRepository;

    @Autowired
    public ClienteinvitadoService(ClienteinvitadoRepository clienteinvitadoRepository) {
        this.clienteinvitadoRepository = clienteinvitadoRepository;
    }

    // Crear un nuevo Clienteinvitado
    public Clienteinvitado crearClienteinvitado(Clienteinvitado clienteinvitado) {
        return clienteinvitadoRepository.save(clienteinvitado);
    }

    // Leer todos los Clienteinvitados
    public List<Clienteinvitado> obtenerTodosClientes() {
        return clienteinvitadoRepository.findAll();
    }

    // Leer un Clienteinvitado por su RUT
    public Optional<Clienteinvitado> obtenerClientePorRut(String rutcliente) {
        return clienteinvitadoRepository.findById(rutcliente);
    }

    // Actualizar un Clienteinvitado
    public Clienteinvitado actualizarClienteinvitado(String rutcliente, Clienteinvitado clienteinvitadoActualizado) {
        if (clienteinvitadoRepository.existsById(rutcliente)) {
            clienteinvitadoActualizado.setRutcliente(rutcliente); // Asegura que el RUT no cambie
            return clienteinvitadoRepository.save(clienteinvitadoActualizado);
        }
        return null; // Si no existe, no se puede actualizar
    }

    // Eliminar un Clienteinvitado
    public boolean eliminarClienteinvitado(String rutcliente) {
        if (clienteinvitadoRepository.existsById(rutcliente)) {
            clienteinvitadoRepository.deleteById(rutcliente);
            return true;
        }
        return false; // Si no existe, no se puede eliminar
    }
}
