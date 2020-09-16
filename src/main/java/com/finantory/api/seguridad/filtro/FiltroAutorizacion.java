package com.finantory.api.seguridad.filtro;

import com.finantory.api.seguridad.servicio.jwt.IJwtServicio;
import com.finantory.api.seguridad.servicio.jwt.JwtServicio;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FiltroAutorizacion extends BasicAuthenticationFilter {

    private IJwtServicio jwtServicio;

    public FiltroAutorizacion(AuthenticationManager authenticationManager, IJwtServicio jwtServicio) {
        super(authenticationManager);
        this.jwtServicio = jwtServicio;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(JwtServicio.AUTHORIZATION);
        if (!requiereAutenticacion(header)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = null;
        if (jwtServicio.validate(header)) {
            authentication = new UsernamePasswordAuthenticationToken(jwtServicio.getUsername(header), null,
                    jwtServicio.getRoles(header));
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    protected boolean requiereAutenticacion(String header) {

        if (header != null) {
            return header.startsWith(JwtServicio.BEARER);
        }
        return false;
    }

}
