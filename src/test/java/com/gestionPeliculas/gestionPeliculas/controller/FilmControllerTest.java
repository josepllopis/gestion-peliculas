package com.gestionPeliculas.gestionPeliculas.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;

import com.gestionPeliculas.gestionPeliculas.controllers.FilmController;
import com.gestionPeliculas.gestionPeliculas.dto.FilmRequestDTO;
import com.gestionPeliculas.gestionPeliculas.enums.Pais;
import com.gestionPeliculas.gestionPeliculas.models.Film;
import com.gestionPeliculas.gestionPeliculas.services.FilmDaoImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

@WebMvcTest
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FilmDaoImpl filmDao;

    @InjectMocks
    private FilmController filmController;

    @Test
    void testGuardarFilm(){
        //given
        Film film1 = new Film();
        film1.setPais(Pais.ESPANA);
        film1.setPuntuacion(7.6);
        film1.setNombre("American X History");
        film1.setDirector("Almodovar");
        film1.setFecha(new Date());
        film1.setCinema("Bonaire");
        film1.setDuracion(120);

       // given(filmDao.create(any(FilmRequestDTO.class)))
        //        .willAnswer(invocationOnMock -> invocationOnMock.getArguments(0));

        //when

        //ResultActions response = mockMvc.perform(post("api/"))
        //then
    }
    @Test
    void testBuscarFilmPorId(){

    }
    @Test
    void testDevolverListaFilm(){

    }
    @Test
    void testActualizarFilm(){

    }
    @Test
    void testEliminarFilm(){

    }
}
