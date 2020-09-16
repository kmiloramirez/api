package com.finantory.api.seguridad.controlador;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class Rest {

    @GetMapping("/")
    public String home() {
        return "Finantory";
    }
}
