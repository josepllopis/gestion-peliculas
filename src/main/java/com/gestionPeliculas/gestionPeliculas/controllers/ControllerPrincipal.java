package com.gestionPeliculas.gestionPeliculas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ControllerPrincipal {

    @GetMapping("/peliculas")
    public String peliculas(){
        return "peliculas";
    }

    @GetMapping("/ranking")
    public String ranking(){
        return "ranking";
    }

    @GetMapping("/peliculas-ranking/{user}")
    public String rankingIndividual(@PathVariable String user, Model model){
        model.addAttribute("username", user);
        return "peliculasUsuarioRanking";
    }
}
