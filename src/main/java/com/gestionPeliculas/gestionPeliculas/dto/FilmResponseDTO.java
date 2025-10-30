package com.gestionPeliculas.gestionPeliculas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FilmResponseDTO {

    private Long id;
    private String nombre;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date fecha;
    private String duracion;
    private String director;
    private String pais;
    private double puntuacion;
    private String cinema;
}
