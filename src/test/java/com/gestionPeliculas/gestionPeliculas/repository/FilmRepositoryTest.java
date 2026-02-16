package com.gestionPeliculas.gestionPeliculas.repository;

import com.gestionPeliculas.gestionPeliculas.models.Film;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class FilmRepositoryTest {

    @Autowired
    private FilmRepository filmRepository;

    private Film film1;
    private Film film2;
    private Film film3;

    @BeforeEach
    void setUpPeliculas(){
        film1 = new Film();
        film1.setPais("España");
        film1.setPuntuacion(7.6);
        film1.setNombre("American X History");
        film1.setDirector("Almodovar");
        film1.setFecha(new Date());
        film1.setCinema("Bonaire");
        film1.setDuracion(120);

        film2 = new Film();
        film2.setPais("Estados Unidos");
        film2.setPuntuacion(8.6);
        film2.setNombre("El Padrino I");
        film2.setDirector("Peter Lambaras");
        film2.setFecha(new Date());
        film2.setCinema("Prime");
        film2.setDuracion(187);

        film3 = new Film();
        film3.setPais("Francia");
        film3.setPuntuacion(5.6);
        film3.setNombre("Lo que el viento se llevó");
        film3.setDirector("Francua Aunda");
        film3.setFecha(new Date());
        film3.setCinema("Plaza MAyor");
        film3.setDuracion(151);
    }


    @DisplayName("Test para guardar un empleado")
    @Test
    void testGuardarFilm(){

        //GIVEN


        //WHEN
        Film guardada = filmRepository.save(film1);

        //THEN
        Assertions.assertThat(guardada).isNotNull();
        Assertions.assertThat(guardada.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Test para listar todas las películas")
    void testListarPeliculas(){
        //GIVEN
        filmRepository.save(film1);
        filmRepository.save(film2);
        filmRepository.save(film3);

        //WHEN
        List<Film> listaPeliculas = filmRepository.findAll();

        //THEN
        Assertions.assertThat(listaPeliculas).isNotEmpty();
        Assertions.assertThat(listaPeliculas.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Test para obtener película por ID")
    void testObetenerPeliculaID(){

        //GIVEN
        Film f1 = filmRepository.save(film1);
        Film f2 = filmRepository.save(film2);
        Film f3 = filmRepository.save(film3);

        //WHEN
        Optional<Film> film1T = filmRepository.findById(f1.getId());
        Optional<Film> film2T = filmRepository.findById(f2.getId());
        Optional<Film> film3T = filmRepository.findById(f3.getId());

        //THEN
        Assertions.assertThat(film1T).isPresent();
        Assertions.assertThat(film2T).isPresent();
        Assertions.assertThat(film3T).isPresent();
    }

    @Test
    @DisplayName("Test para eliminar una película")
    void testEliminarPelicula(){
        //GIVEN
        filmRepository.save(film1);
        //WHEN
        filmRepository.deleteById(film1.getId());
        Optional<Film> peliculaEliminada = filmRepository.findById(1L);
        //THEN
        Assertions.assertThat(peliculaEliminada).isEmpty();
    }

    @Test
    @DisplayName("Test para actualizar una película")
    void testActualizarPelicula(){
        //GIVEN
        filmRepository.save(film1);
        //WHEN
        Optional<Film> Oguardada = filmRepository.findById(1L);

        if(Oguardada.isPresent()){
            Film guardada = Oguardada.get();
            guardada.setCinema("Mi casa");
            filmRepository.save(guardada);
        }



        //THEN
        Assertions.assertThat(filmRepository.findById(1L).get().getCinema()).isEqualTo("Mi casa");
    }
}
