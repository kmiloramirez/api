package com.finantory.api.seguridad.filtro;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finantory.api.seguridad.persistencia.entidad.Usuario;
import com.finantory.api.seguridad.servicio.jwt.IJwtServicio;
import com.finantory.api.seguridad.servicio.jwt.JwtServicio;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class FiltroAutenticacionWeb extends UsernamePasswordAuthenticationFilter {


    private AuthenticationManager authenticationManager;
    private IJwtServicio jwtServicio;

    public FiltroAutenticacionWeb(AuthenticationManager authenticationManager, IJwtServicio jwtServicio) {
        this.authenticationManager = authenticationManager;
        this.jwtServicio = jwtServicio;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login/web", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        Usuario user;
        String username = "";
        String password = "";
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
            username = user.getNombreDeUsuario();
            password = user.getContrasena();
        } catch (JsonParseException | JsonMappingException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }
        username = username.trim();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String token = jwtServicio.create(authResult);
        String email = authResult.getName();
        Map<String, Object> body = new HashMap<>();
        //response.addHeader("Access-Control-Allow-Headers", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Headers", "Authorization,Content-Type, Accept, X-Custom-header");
        response.addHeader("Access-Control-Expose-Headers", "Authorization,Content-Type, Accept, X-Custom-header");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.addHeader(JwtServicio.AUTHORIZATION, JwtServicio.BEARER + token);
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);


        response.setContentType("application/json");

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        Map<String, Object> body = new HashMap<>();
        body.put("message", "usuario o contraseña incorrecto");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType("application/json");
    }


}
