package com.gestionPeliculas.gestionPeliculas.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FilmNotFoundException extends RuntimeException {


    public FilmNotFoundException(String message) {
        super(message);
    }
}
