package com.gestionPeliculas.gestionPeliculas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FilmRequestDTO {

    private String nombre;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date fecha;
    private Integer duracion;
    private String director;
    private String pais;

    @Min(1)
    @Max(10)
    private double puntuacion;
    private String cinema;
}
