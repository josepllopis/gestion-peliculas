package com.gestionPeliculas.gestionPeliculas.services;

import com.gestionPeliculas.gestionPeliculas.dto.FilmRequestDTO;
import com.gestionPeliculas.gestionPeliculas.dto.FilmResponseDTO;
import com.gestionPeliculas.gestionPeliculas.models.Film;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FilmDao {

    //CRUD

    FilmResponseDTO create(FilmRequestDTO filmRequestDTO);
    Optional<FilmResponseDTO> read(long id);
    List<FilmResponseDTO> readAll();
    Optional<FilmResponseDTO> update(long id,FilmRequestDTO filmRequestDTO);
    void delete(long id);

    List<FilmResponseDTO> getAllSortedByPuntuacion(Sort sort);
    public byte[] generarPdfDeFilms(String sortBy, String direction) throws IOException;
}
