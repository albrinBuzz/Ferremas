package com.ferremas.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;


@FacesValidator("contrasenaValidator")
public class ContrasenaValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String contrasena = value.toString().trim();

        if (!contrasena.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$")) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contraseña insegura", "La contraseña debe tener al menos 8 caracteres, incluyendo números, letras mayúsculas y caracteres especiales."));
        }
    }
}
