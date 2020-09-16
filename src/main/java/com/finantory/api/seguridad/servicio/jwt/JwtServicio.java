package com.finantory.api.seguridad.servicio.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finantory.api.seguridad.mix.SimpleGrantedAuthorityMixin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

@Service
public class JwtServicio implements IJwtServicio {

    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    private static final String AUTHORITIES = "authorities";
    private static final String SECRETO = "{fiantory.000000000.0000000.000000.00000000000000000000}";
    private static final SecretKey LLAVE = Keys.hmacShaKeyFor(SECRETO.getBytes(StandardCharsets.UTF_8));

    @Override
    public String create(Authentication auth) throws IOException {
        String username = auth.getName();
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
        Claims claims = Jwts.claims();
        claims.put(AUTHORITIES, new ObjectMapper().writeValueAsString(roles));
        LocalDate expiracion = LocalDate.now().plusDays(10) ;
        return Jwts.builder().setClaims(claims).setSubject(username).signWith(LLAVE).compact();
    }

    @Override
    public boolean validate(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(LLAVE).parseClaimsJws(resolve(token)).getBody();
    }

    @Override
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
        Object roles = getClaims(token).get(AUTHORITIES);
        return Arrays
                .asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
                        .readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
    }

    @Override
    public String resolve(String token) {
        return token.replace(BEARER, "");
    }

}
