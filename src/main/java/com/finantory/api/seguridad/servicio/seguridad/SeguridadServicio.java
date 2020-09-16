package com.finantory.api.seguridad.servicio.seguridad;

import com.finantory.api.seguridad.persistencia.entidad.Usuario;
import com.finantory.api.seguridad.persistencia.repositorio.IRepositoioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

;

@Service
@EnableAutoConfiguration
public class SeguridadServicio {

    private static final boolean ACTIVO = true;
    @Autowired
    private IRepositoioUsuario repositorioUsuario;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setActivo(true);
        if (usuario.getContrasena() != null) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }
        Usuario usuarioGuardado = repositorioUsuario.save(usuario);
        return usuarioGuardado;

    }


    public Usuario buscarUsuarioPorNombre(String nombre) {
        return repositorioUsuario.findByNombreDeUsuarioAndActivo(nombre, ACTIVO);
    }



}
