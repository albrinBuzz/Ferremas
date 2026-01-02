package com.ferremas.controller.api;


import com.ferremas.model.Usuario;
import com.ferremas.service.UsuarioService;
import com.ferremas.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    //curl -X GET http://localhost:8080/api/usuarios
    //curl -X GET "http://localhost:8080/api/usuarios?hola=HolaMundo"
    @GetMapping
    public List<Usuario> getAllUsuarios(String hola) {
        //Logger.logInfo(hola);

        System.out.println(usuarioService.findAll());
        return usuarioService.findAll();
    }

    /*@GetMapping("/{rut}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable String rut) {
        return usuarioService.findByRut(rut)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }*/

    //curl -X GET http://localhost:8080/api/usuarios/12345678-9
    @GetMapping("/{rut}")
    public Usuario getUsuarioById(@PathVariable String rut) {
        return usuarioService.findByRut(rut).get();

    }
/*
curl -X POST http://localhost:8080/api/usuarios \
     -H "Content-Type: application/json" \
     -d '{
           "rutUsuario": "12345678-9",
           "nombre": "Juan Perez",
           "email": "juan@example.com"
         }'

 */
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
