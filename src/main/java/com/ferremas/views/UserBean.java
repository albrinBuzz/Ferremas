package com.ferremas.views;


import com.ferremas.model.Cliente;
import com.ferremas.model.Usuario;
import com.ferremas.service.RolService;
import com.ferremas.service.UsuarioService;
import com.ferremas.util.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
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
    private String contrasenaConfirm;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RolService rolService;


    private boolean mostrarFormularioCliente = false;
    private Cliente nuevoCliente = new Cliente();

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

        if (usuarioService.findByRut(usuarioRegistro.getRutUsuario()).isPresent()){

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Rut en uso"));
            return;
        }

        if (usuarioService.findByCorreo(usuarioRegistro.getCorreo())!=null){

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Correo en uso"));
            return;
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

    public void changePassword() throws IOException {

        if (usuario==null){
            FacesContext.getCurrentInstance().getExternalContext().redirect("/home/index.xhtml");
        }

        if (contrasenaInput == null || !contrasenaInput.equals(contrasenaConfirm)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Las contraseñas nuevas no coinciden", null));
            return;
        }

        if (!contrasenaInput.isEmpty()) {
            usuario.setContrasena(contrasenaInput);
        }
        if (usuario.getCorreo().equals("admin@ferremas.cl")){
           usuario.setFirstLogin(false);
        }
        usuarioService.update(usuario);


        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Contraseña cambiada con éxito", null));

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


    public boolean isMostrarFormularioCliente() {
        return mostrarFormularioCliente;
    }

    public void setMostrarFormularioCliente(boolean mostrarFormularioCliente) {
        this.mostrarFormularioCliente = mostrarFormularioCliente;
    }

    public Cliente getNuevoCliente() {
        return nuevoCliente;
    }

    public void setNuevoCliente(Cliente nuevoCliente) {
        this.nuevoCliente = nuevoCliente;
    }

    public void mostrarFormularioCrearCliente() {
        this.mostrarFormularioCliente = true;
    }

    public void cancelarFormularioCliente() {
        this.mostrarFormularioCliente = false;
        this.nuevoCliente = new Cliente(); // Limpia el formulario
    }

    public void setContrasenaConfirm(String contrasenaConfirm) {
        this.contrasenaConfirm = contrasenaConfirm;
    }

    public String getContrasenaConfirm() {
        return contrasenaConfirm;
    }

    public void crearClienteParaUsuario() {
        try {

            var rol=rolService.findById(2).get();
            usuario.getRoles().add(rol);
            nuevoCliente.setUsuario(usuario);
            nuevoCliente.setRutUsuario(usuario.getRutUsuario());
            //clienteService.guardar(nuevoCliente);

            usuario.setCliente(nuevoCliente); // Actualiza en memoria
            usuarioService.save(usuario);

            this.mostrarFormularioCliente = false;
            this.nuevoCliente = new Cliente();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Cliente creado correctamente."));
        } catch (Exception e) {
            Logger.logInfo(e.getMessage());

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo crear el cliente."));
        }
    }

    // Método para cerrar sesión
    public String logout() {
        SecurityContextHolder.clearContext();  // Limpiar el contexto de seguridad
        this.loggedIn = false;
        this.isAuthenticated = false;
        return "index?faces-redirect=true";  // Redirigir a la página de inicio
    }
}
