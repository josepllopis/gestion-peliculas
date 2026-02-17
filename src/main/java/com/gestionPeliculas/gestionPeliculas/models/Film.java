package com.gestionPeliculas.gestionPeliculas.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gestionPeliculas.gestionPeliculas.enums.Pais;
import com.gestionPeliculas.gestionPeliculas.mapper.PaisConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table
@Entity
@NoArgsConstructor
@Data
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no puede superar los 200 caracteres")
    private String nombre;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "La fecha es obligatoria")
    private Date fecha;
    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración debe ser al menos 1 minuto")
    @Max(value = 873, message = "La duración no puede superar 873 minutos")
    private Integer duracion;
    @NotBlank(message = "El director es obligatorio")
    @Size(max = 100, message = "El nombre del director no puede superar los 100 caracteres")
    private String director;

    @NotNull(message = "El país es obligatorio")
    @Convert(converter = PaisConverter.class)
    private Pais pais;
    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 0, message = "La puntuación mínima es 0")
    @Max(value = 10, message = "La puntuación máxima es 10")
    private double puntuacion;
    @NotBlank(message = "El cine es obligatorio")
    @Size(max = 100, message = "El nombre del cine no puede superar los 100 caracteres")
    private String cinema;
}
