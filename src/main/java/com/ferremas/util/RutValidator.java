package com.ferremas.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FacesValidator("rutValidator")

public class RutValidator implements Validator<String> {

    @Override
    public void validate(FacesContext facesContext, UIComponent component, String value) throws ValidatorException {
        if (value == null || value.trim().isEmpty()) {
            return; // No valida si está vacío porque el campo ya tiene la validación de "required"
        }

        // Eliminar puntos y guiones
        String rut = value.replaceAll("[^0-9Kk]", "");

        // Verificación de formato: debe tener 8 o 9 caracteres (sin el guion y puntos)
        Pattern rutPattern = Pattern.compile("^\\d{7,8}[0-9Kk]$");
        Matcher matcher = rutPattern.matcher(rut);
        if (!matcher.matches()) {
            FacesMessage msg = new FacesMessage("El RUT no tiene un formato válido. 11.111.111-1 o 11111111-1");
            throw new ValidatorException(msg);
        }

        // Verificación del dígito verificador
        int rutNum = Integer.parseInt(rut.substring(0, rut.length() - 1));
        char dv = rut.charAt(rut.length() - 1);
        char dvCalculado = calcularDigitoVerificador(rutNum);
        if (dv != dvCalculado) {
            FacesMessage msg = new FacesMessage("El dígito verificador del RUT es incorrecto. Rut invalido");
            throw new ValidatorException(msg);
        }
    }

    // Método para calcular el dígito verificador
    private char calcularDigitoVerificador(int rut) {
        int sum = 0;
        int multiplier = 2;

        for (int i = String.valueOf(rut).length() - 1; i >= 0; i--) {
            sum += Integer.parseInt(String.valueOf(rut).charAt(i) + "") * multiplier;
            multiplier = (multiplier == 7) ? 2 : multiplier + 1;
        }

        int remainder = sum % 11;
        int dv = 11 - remainder;
        if (dv == 11) {
            return '0';
        } else if (dv == 10) {
            return 'K';
        } else {
            return (char) (dv + '0');
        }
    }
}