package com.ferremas.views;

import com.ferremas.model.Usuario;
import com.ferremas.util.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;

@Named("loginBean")
@RequestScoped
public class LoginBean {

    private String username;
    private String password;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private  AuthenticationSuccessHandler auth;

    @Autowired
    private FilterChain chain;
    
    @Autowired
    private  SecurityContextRepository securityContextRepository;

    
    @Autowired
    HttpServletRequest request;
    
    @Autowired
    HttpServletResponse response;



    @Autowired
    CarritoBean carritoBean;

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @PostConstruct
    public void init() throws IOException {
        // Verificar si ya existe una sesión activa
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Si ya está autenticado, redirigir a la página de inicio
                //redigir();
        }
    }
    
    // Método de login
    public void login() {
        try {
            // Crear un token de autenticación con las credenciales del usuario
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);

            // Autenticar el token con el AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            if (authentication.isAuthenticated()) {

                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(authentication);
                securityContextRepository.saveContext(context, request, response);

                Logger.logInfo("Auntenticacion Existosa");


                auth.onAuthenticationSuccess(request, response, chain, authentication);

                redigir();
            }

        } catch (BadCredentialsException e) {
            // Error específico para credenciales incorrectas
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales incorrectas", "Nombre de usuario o contraseña incorrectos"));
        } catch (Exception e) {
            // Manejo general de errores
            System.out.println(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de autenticación", "Hubo un problema con el login"));
        }
    }

    private void redigir() throws IOException {

        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        Usuario usuario= (Usuario) request.getSession().getAttribute("usuario");

        String redirectUrl;
        if (usuario.getCorreo().equals("admin@ferremas.cl")){
            if (usuario.isFirstLogin()){
                FacesContext.getCurrentInstance().getExternalContext().redirect("/home/cambioPass.xhtml");
            }
        }

        if (savedRequest != null) {
            redirectUrl = savedRequest.getRedirectUrl();
        } else {
            redirectUrl = request.getContextPath() + "/";
        }
        System.out.println("Redirigiendo a URL original: " + redirectUrl);

        FacesContext.getCurrentInstance().getExternalContext().redirect(redirectUrl);

    }

    public String logout() {
        SecurityContextHolder.clearContext();  // Limpiar el contexto de seguridad
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        request.getSession().removeAttribute("usuario");
        return "login.xhtml";  // Redirige al login
    }
}
