package com.ferremas.views;


import com.ferremas.model.Cliente;
import com.ferremas.model.Usuario;
import com.ferremas.service.RolService;
import com.ferremas.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Named("userBean")
@RequestScoped
public class UserBean implements Serializable {

    private String name;
    private boolean loggedIn = false;
    private boolean isAuthenticated = false;
    private boolean isAdmin = false;
    private String rol;
    private Usuario usuario;
    private Cliente cliente;
    private Usuario usuarioRegistro;

    private String contrasenaInput;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RolService rolService;
    // Constructor
    public UserBean(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            this.loggedIn = true;
            this.isAuthenticated = true;
            
            try {
            	this.usuario = (Usuario) session.getAttribute("usuario");
            	this.name = usuario.getNombreusuario();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            // Comprobamos si el usuario tiene el rol de administrador
            rol = authentication.getAuthorities().toArray()[0].toString();
            this.isAdmin = authentication.getAuthorities().stream()
                                         .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));


        }

    }

    @PostConstruct
    public void init() {
        usuarioRegistro = new Usuario();
        cliente = new Cliente();
        contrasenaInput = null;
    }

    public void guardarCliente() {
        if (contrasenaInput != null && !contrasenaInput.isEmpty()) {
            usuarioRegistro.setContrasena(contrasenaInput);
        }

        var rol=rolService.findById(2).get();
        cliente.setRutUsuario(usuarioRegistro.getRutUsuario());
        cliente.setUsuario(usuarioRegistro);
        usuarioRegistro.setCliente(cliente);
        var roles=new HashSet<>(Collections.singleton(rol));
        usuarioRegistro.setRoles(roles);
        usuarioService.save(usuarioRegistro);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cliente registrado correctamente."));
        //limpiarFormulario();
    }
    public void limpiarFormulario() {
        usuarioRegistro = new Usuario();
        cliente = new Cliente();
        contrasenaInput = null;
    }


    public boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }



    // Getter y setter para 'name' y 'loggedIn'
    public String getName() {
        return name;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setUsuarioRegistro(Usuario usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public void setContrasenaInput(String contrasenaInput) {
        this.contrasenaInput = contrasenaInput;
    }

    public Usuario getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public String getContrasenaInput() {
        return contrasenaInput;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    // Método para cerrar sesión
    public String logout() {
        SecurityContextHolder.clearContext();  // Limpiar el contexto de seguridad
        this.loggedIn = false;
        this.isAuthenticated = false;
        return "index?faces-redirect=true";  // Redirigir a la página de inicio
    }
}
