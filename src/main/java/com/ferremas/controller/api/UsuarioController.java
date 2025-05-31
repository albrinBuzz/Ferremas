package com.ferremas.controller.api;


import com.ferremas.model.Usuario;
import com.ferremas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        System.out.println(usuarioService.findAll());
        return usuarioService.findAll();
    }

    @GetMapping("/{rut}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable String rut) {
        return usuarioService.findByRut(rut)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }

    @PutMapping("/{rut}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable String rut, @RequestBody Usuario usuario) {
        return usuarioService.findByRut(rut).map(u -> {
            usuario.setRutUsuario(rut); // Asegura que el ID sea el mismo
            return ResponseEntity.ok(usuarioService.save(usuario));
        }).orElse(ResponseEntity.notFound().build());
    }

    /*@DeleteMapping("/{rut}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable String rut) {
        return usuarioService.findByRut(rut).map(u -> {
            usuarioService.delete(rut);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }*/
}
