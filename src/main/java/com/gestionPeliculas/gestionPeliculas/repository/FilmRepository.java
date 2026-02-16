package com.gestionPeliculas.gestionPeliculas.repository;

import com.gestionPeliculas.gestionPeliculas.models.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends JpaRepository<Film,Long> {

    boolean existsByNombreAndDirector(String nombre, String Director);
}
