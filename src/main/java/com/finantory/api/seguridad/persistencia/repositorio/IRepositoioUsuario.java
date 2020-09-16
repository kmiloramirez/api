package com.finantory.api.seguridad.persistencia.repositorio;

import com.finantory.api.seguridad.persistencia.entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRepositoioUsuario extends JpaRepository<Usuario, Long> {

    public Usuario findByNombreDeUsuarioAndActivo(String nombreDeUsuario, boolean activo);

}
