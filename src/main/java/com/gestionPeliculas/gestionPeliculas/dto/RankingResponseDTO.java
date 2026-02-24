package com.gestionPeliculas.gestionPeliculas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RankingResponseDTO {

    private String username;
    private Long totalPeliculas;
}
