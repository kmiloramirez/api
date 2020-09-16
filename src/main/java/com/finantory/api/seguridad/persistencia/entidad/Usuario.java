package com.finantory.api.seguridad.persistencia.entidad;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity(name = "Usuario")
@SequenceGenerator(name = "Usuario_ID", initialValue = 1, allocationSize = 1)
public class Usuario {

    @Column(unique = true, nullable = false)
    protected String nombreDeUsuario;
    @Column(length = 60)
    protected String contrasena;
    @Column
    @ColumnDefault(value = "true")
    protected boolean activo;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Usuario_ID")
    @Column(nullable = false)
    private Long id;

    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }


}
