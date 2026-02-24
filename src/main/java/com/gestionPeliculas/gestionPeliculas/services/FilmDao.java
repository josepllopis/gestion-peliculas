package com.gestionPeliculas.gestionPeliculas.services;

import com.gestionPeliculas.gestionPeliculas.dto.FilmRequestDTO;
import com.gestionPeliculas.gestionPeliculas.dto.FilmResponseDTO;
import com.gestionPeliculas.gestionPeliculas.models.Film;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FilmDao {

    //CRUD

    FilmResponseDTO create(FilmRequestDTO filmRequestDTO, UserDetails userDetails);
    Optional<FilmResponseDTO> read(long id, UserDetails userDetails);
    List<FilmResponseDTO> readAll(UserDetails userDetails);
    Optional<FilmResponseDTO> update(long id,FilmRequestDTO filmRequestDTO, UserDetails userDetails);
    void delete(long id, UserDetails userDetails);

    List<FilmResponseDTO> getAllSortedByPuntuacion(Sort sort, UserDetails userDetails);
    public byte[] generarPdfDeFilms(String sortBy, String direction, UserDetails userDetails) throws IOException;
}
