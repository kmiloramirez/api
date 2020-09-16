package com.finantory.api.seguridad.controlador;


import com.finantory.api.seguridad.persistencia.entidad.Usuario;
import com.finantory.api.seguridad.servicio.seguridad.SeguridadServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@RequestMapping("/login")
public class Loggeo {

    @Autowired
    private SeguridadServicio seguridadServicio;


    @PostMapping(value = "/web")
    public void registrar(@RequestBody Usuario usuario) {

    }


}
