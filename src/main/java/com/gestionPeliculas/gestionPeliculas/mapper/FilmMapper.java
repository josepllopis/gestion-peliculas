package com.gestionPeliculas.gestionPeliculas.mapper;

import com.gestionPeliculas.gestionPeliculas.dto.FilmRequestDTO;
import com.gestionPeliculas.gestionPeliculas.dto.FilmResponseDTO;
import com.gestionPeliculas.gestionPeliculas.models.Film;
import com.gestionPeliculas.gestionPeliculas.models.Usuario;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FilmMapper {



    public Film toEntity(FilmRequestDTO filmRequestDTO, Usuario usuario){
        Film film = new Film();
        film.setNombre(filmRequestDTO.getNombre());
        film.setPais(filmRequestDTO.getPais());
        film.setFecha(filmRequestDTO.getFecha());
        film.setDuracion(filmRequestDTO.getDuracion());
        film.setDirector(filmRequestDTO.getDirector());
        film.setPuntuacion(filmRequestDTO.getPuntuacion());
        film.setCinema(filmRequestDTO.getCinema());
        film.setUsuario(usuario);
        return film;
    }

    public FilmResponseDTO toResponse(Film film){
        FilmResponseDTO filmResponseDTO = new FilmResponseDTO();
        filmResponseDTO.setId(film.getId());
        filmResponseDTO.setNombre(film.getNombre());
        filmResponseDTO.setPais(film.getPais().getNombre());
        filmResponseDTO.setFecha(film.getFecha());
        filmResponseDTO.setDuracion(String.valueOf(film.getDuracion()));
        filmResponseDTO.setDirector(film.getDirector());
        filmResponseDTO.setPuntuacion(film.getPuntuacion());
        filmResponseDTO.setCinema(film.getCinema());
        return filmResponseDTO;
    }
}
