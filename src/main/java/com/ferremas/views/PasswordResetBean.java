package com.ferremas.views;


import com.ferremas.model.PasswordResetToken;
import com.ferremas.model.Usuario;
import com.ferremas.repository.PasswordResetTokenRepository;
import com.ferremas.service.PasswordResetService;
import com.ferremas.util.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Optional;

@Named
@ViewScoped
public class PasswordResetBean {

    @Autowired
    private PasswordResetService passwordResetService;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private PasswordResetToken resetToken;
    private Usuario usuario;
    private String email;
    private String token;
    private String newPassword;
    private String confirmPassword;
    private Boolean esCambio;
    private boolean mailSent = false;

    @PostConstruct
    public void init(){

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        // Obtener el vuelo seleccionado desde la sesión
        //producto= (Producto) externalContext.getSessionMap().get("productoSeleccionado");
        Optional<String>token= Optional.ofNullable( facesContext.getExternalContext().getRequestParameterMap().get("token"));

        token.ifPresent(string -> {
            var tokenOptional =passwordResetTokenRepository.findByToken(string);

            if (tokenOptional.isPresent()){
                resetToken=tokenOptional.get();
                Logger.logInfo(resetToken.toString());
                usuario=resetToken.getUser();
                this.token=token.get();
                esCambio=true;
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Token invalido"));
            }


        });


    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    public boolean isMailSent() { return mailSent; }

    // Método para enviar el correo de recuperación de contraseña
    public void sendResetPasswordEmail() {
        Logger.logInfo("Enviado correo a: " + email);
        try {

             passwordResetService.sendResetPasswordEmail(email);

            this.mailSent = true; // Marcamos como enviado
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Instrucciones enviadas a " + email);
        } catch (Exception e) {
            this.mailSent = false;
            Logger.logError(e.getMessage());
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo enviar el correo");
        }
    }

    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }


    // Método para restablecer la contraseña
    public void resetPassword() {
        if (newPassword.equals(confirmPassword)) {
            try {
                passwordResetService.resetPassword(token, newPassword);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Contraseña actualizada correctamente"));

            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Las contraseñas no coinciden"));
        }
    }

    public void mostrarFormularioReset() {
        this.token = ""; // Inicializamos el token para que se muestre el resetForm
        // Al setear el token, el rendered="#{not empty token}" del resetForm se activará
    }
}
