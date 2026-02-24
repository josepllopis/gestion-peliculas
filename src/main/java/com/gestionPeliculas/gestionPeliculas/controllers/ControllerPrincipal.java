package com.gestionPeliculas.gestionPeliculas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerPrincipal {

    @GetMapping("/peliculas")
    public String peliculas(){
        return "peliculas";
    }
}
