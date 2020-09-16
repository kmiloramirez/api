package com.finantory.api.seguridad.servicio;

import com.finantory.api.seguridad.persistencia.entidad.Usuario;
import com.finantory.api.seguridad.servicio.seguridad.SeguridadServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuariosDetalleServicio implements UserDetailsService {

    public static final boolean ACTIVO = true;


    @Autowired
    SeguridadServicio seguridadServicio;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {

        Usuario usuario = seguridadServicio.buscarUsuarioPorNombre(username);
        if (usuario != null) {
            return crearUser(username, usuario.getContrasena());
        }
        throw new UsernameNotFoundException("usuario no encontrado");
    }

    protected User crearUser(String usuario, String credencial) {
        SimpleGrantedAuthority rol =new SimpleGrantedAuthority("usuario");
        List<GrantedAuthority> roles = new ArrayList();
        roles.add(rol);
        return new User(usuario, credencial, ACTIVO, true, true, true, roles);

    }

}
