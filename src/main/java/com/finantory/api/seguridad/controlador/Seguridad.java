package com.finantory.api.seguridad.controlador;

import com.finantory.api.seguridad.dto.RegistrarUsuario;
import com.finantory.api.seguridad.persistencia.entidad.Usuario;
import com.finantory.api.seguridad.servicio.seguridad.SeguridadServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/seguridad")
public class Seguridad {

    @Autowired
    private SeguridadServicio seguridadServicio;

    @GetMapping("/")
    public String principal() {
        return "seguridad";
    }

    @PostMapping(value = "/registrar")
    public Usuario registrar(@RequestBody RegistrarUsuario registrarUsuario) {
        return seguridadServicio.registrarUsuario(registrarUsuario.getUsuario());
    }



}
