package com.isban.scf.sid.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) 
      throws IOException, ServletException {
        // Establece un mensaje de error en la sesión
        request.getSession().setAttribute("error", "Login error");
        
        // Redirige de nuevo a la página de login
        response.sendRedirect("/user/login");
    }
    
}
