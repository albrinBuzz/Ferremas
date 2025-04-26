package com.ferremas.config.security;


import com.ferremas.model.Pedido;
import com.ferremas.model.Usuario;
import com.ferremas.service.UsuarioService;
import com.ferremas.util.Logger;
import com.ferremas.views.CarritoBean;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthCustom implements AuthenticationSuccessHandler {
	
	@Autowired
	private UsuarioService usService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Obtener el usuario autenticado

        Logger.logInfo("Login existoso");
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {

            Logger.logInfo("Roles");
            userDetails.getAuthorities().forEach(arg0 -> Logger.logInfo(arg0.getAuthority()));;
            Usuario usuario=usService.findByCorreo(userDetails.getUsername());



            request.getSession().setAttribute("usuario", usuario);

        }

    }


}
