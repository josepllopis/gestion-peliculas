package com.gestionPeliculas.gestionPeliculas.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
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
    @Column (nullable = false)
    private String nombre;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @Column (nullable = false, length = 90)
    private Date fecha;
    @Column (nullable = false)
    private Integer duracion;
    @Column (nullable = false,length = 90)
    private String director;
    @Column (nullable = false)
    private String pais;
    @Column (nullable = false)
    private double puntuacion;
    @Column (nullable = false, length = 90)
    private String cinema;
}
