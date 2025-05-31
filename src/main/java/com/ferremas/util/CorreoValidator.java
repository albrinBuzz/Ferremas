package com.ferremas.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator("correoValidator")
public class CorreoValidator implements Validator<String> {

    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        String correo = value.trim();

        if (!correo.matches("^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}$")) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Correo inválido", "El correo ingresado no es válido."));
        }
    }
}
