package com.gestionPeliculas.gestionPeliculas.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MovieAlreadyExistsException extends RuntimeException{


   public MovieAlreadyExistsException(String message){
        super(message);
   }

}
