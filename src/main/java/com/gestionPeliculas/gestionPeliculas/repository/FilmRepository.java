package com.gestionPeliculas.gestionPeliculas.repository;

import com.gestionPeliculas.gestionPeliculas.models.Film;
import com.gestionPeliculas.gestionPeliculas.models.Usuario;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film,Long> {

    List<Film> findByUsuario(Usuario usuario);
    List<Film> findByUsuario(Usuario usuario, Sort sort);
    boolean existsByNombreAndDirectorAndUsuario(String nombre, String Director, Usuario usuario);
}
