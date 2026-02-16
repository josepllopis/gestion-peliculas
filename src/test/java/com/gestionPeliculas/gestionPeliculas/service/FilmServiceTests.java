package com.gestionPeliculas.gestionPeliculas.service;

import com.gestionPeliculas.gestionPeliculas.dto.FilmRequestDTO;
import com.gestionPeliculas.gestionPeliculas.dto.FilmResponseDTO;
import com.gestionPeliculas.gestionPeliculas.mapper.FilmMapper;
import com.gestionPeliculas.gestionPeliculas.models.Film;
import com.gestionPeliculas.gestionPeliculas.repository.FilmRepository;
import com.gestionPeliculas.gestionPeliculas.services.FilmDao;
import com.gestionPeliculas.gestionPeliculas.services.FilmDaoImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FilmServiceTests {

    @Mock
    private FilmRepository filmRepository;
    @Mock
    private FilmMapper filmMapper;

    @InjectMocks
    private FilmDaoImpl filmDao;

    private Film film1;
    private Film film2;
    private Film film3;

    private FilmRequestDTO filmRequestDTO1;
    private FilmRequestDTO filmRequestDTO2;
    private FilmRequestDTO filmRequestDTO3;

    @BeforeEach
    void setUpPeliculas(){
        film1 = new Film();
        film1.setId(1L);
        film1.setPais("España");
        film1.setPuntuacion(7.6);
        film1.setNombre("American X History");
        film1.setDirector("Almodovar");
        film1.setFecha(new Date());
        film1.setCinema("Bonaire");
        film1.setDuracion(120);

        film2 = new Film();
        film2.setId(2L);
        film2.setPais("Estados Unidos");
        film2.setPuntuacion(8.6);
        film2.setNombre("El Padrino I");
        film2.setDirector("Peter Lambaras");
        film2.setFecha(new Date());
        film2.setCinema("Prime");
        film2.setDuracion(187);

        film3 = new Film();
        film3.setId(3L);
        film3.setPais("Francia");
        film3.setPuntuacion(5.6);
        film3.setNombre("Lo que el viento se llevó");
        film3.setDirector("Francua Aunda");
        film3.setFecha(new Date());
        film3.setCinema("Plaza MAyor");
        film3.setDuracion(151);
    }

    @BeforeEach
    void setUpPeliculasRequest(){
        filmRequestDTO1 = new FilmRequestDTO();
        filmRequestDTO1.setNombre(film1.getNombre());
        filmRequestDTO1.setCinema(film1.getCinema());
        filmRequestDTO1.setDirector(film1.getDirector());
        filmRequestDTO1.setPais(film1.getPais());
        filmRequestDTO1.setPuntuacion(film1.getPuntuacion());
        filmRequestDTO1.setDuracion(film1.getDuracion());

        filmRequestDTO2 = new FilmRequestDTO();
        filmRequestDTO2.setNombre(film2.getNombre());
        filmRequestDTO2.setCinema(film2.getCinema());
        filmRequestDTO2.setDirector(film2.getDirector());
        filmRequestDTO2.setPais(film2.getPais());
        filmRequestDTO2.setPuntuacion(film2.getPuntuacion());
        filmRequestDTO2.setDuracion(film2.getDuracion());

        filmRequestDTO3 = new FilmRequestDTO();
        filmRequestDTO3.setNombre(film3.getNombre());
        filmRequestDTO3.setCinema(film3.getCinema());
        filmRequestDTO3.setDirector(film3.getDirector());
        filmRequestDTO3.setPais(film3.getPais());
        filmRequestDTO3.setPuntuacion(film3.getPuntuacion());
        filmRequestDTO3.setDuracion(film3.getDuracion());
    }


    @Test
    @DisplayName("Debe guardar película correctamente")
    void testGuardarFilm() {

        // GIVEN
        FilmResponseDTO responseDTO = new FilmResponseDTO();
        responseDTO.setNombre(film1.getNombre());

        given(filmMapper.toEntity(filmRequestDTO1)).willReturn(film1);
        given(filmRepository.save(film1)).willReturn(film1);
        given(filmMapper.toResponse(film1)).willReturn(responseDTO);

        // WHEN
        FilmResponseDTO resultado = filmDao.create(filmRequestDTO1);

        // THEN
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("American X History");

        verify(filmMapper).toEntity(filmRequestDTO1);
        verify(filmRepository).save(film1);
        verify(filmMapper).toResponse(film1);
    }

    @Test
    @DisplayName("Debe lanzar un excepcion")
    void testGuardarFilmThrow() {

        // GIVEN
        FilmResponseDTO responseDTO = new FilmResponseDTO();
        responseDTO.setNombre(film1.getNombre());

        given(filmMapper.toEntity(filmRequestDTO1)).willReturn(film1);
        given(filmRepository.save(film1)).willReturn(film1);
        given(filmMapper.toResponse(film1)).willReturn(responseDTO);

        // WHEN
        FilmResponseDTO resultado = filmDao.create(filmRequestDTO1);

        // THEN
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("American X History");

        verify(filmMapper).toEntity(filmRequestDTO1);
        verify(filmRepository).save(film1);
        verify(filmMapper).toResponse(film1);
    }


    @Test
    @DisplayName("Test para listar todas las películas")
    void testListarPeliculas(){
        FilmResponseDTO film1ResponseDTO = new FilmResponseDTO();
        film1ResponseDTO.setNombre("El lobo de Wall Street");
        FilmResponseDTO film2ResponseDTO = new FilmResponseDTO();
        film2ResponseDTO.setNombre("Terminator");


        //GIVEN
        List<Film> listFilms = List.of(film1,film2);

        given(filmRepository.findAll()).willReturn(listFilms);
        given(filmMapper.toResponse(film1)).willReturn(film1ResponseDTO);
        given(filmMapper.toResponse(film2)).willReturn(film2ResponseDTO);

        //WHEN
        List<FilmResponseDTO> resultado = filmDao.readAll();

        //THEN
        assertThat(resultado.size()).isEqualTo(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("El lobo de Wall Street");

        verify(filmRepository).findAll();
    }

    @Test
    @DisplayName("Test para obtener película por ID")
    void testObetenerPeliculaID(){

        FilmResponseDTO filmResponseDTO = new FilmResponseDTO();
        filmResponseDTO.setNombre("Tortuga Ninja");
        //GIVEN
        given(filmRepository.findById(film1.getId())).willReturn(Optional.of(film1));
        given(filmMapper.toResponse(film1)).willReturn(filmResponseDTO);
        //WHEN

        FilmResponseDTO filmLeida = filmDao.read(film1.getId()).get();
        //THEN

        assertThat(filmLeida.getNombre()).isEqualTo("Tortuga Ninja");

        verify(filmRepository).findById(1L);
        verify(filmMapper).toResponse(film1);


    }

    @Test
    @DisplayName("Test para eliminar una película")
    void testEliminarPelicula(){


        //GIVEN
        given(filmRepository.findById(film1.getId())).willReturn(Optional.of(film1));

        //WHEN
        filmDao.delete(film1.getId());
        //THEN
        verify(filmRepository).delete(film1);
    }

    @Test
    @DisplayName("Test para actualizar una película")
    void testActualizarPelicula() {
        // GIVEN
        given(filmRepository.findById(film1.getId())).willReturn(Optional.of(film1));

        Film filmActualizado = new Film();
        filmActualizado.setId(film1.getId());
        filmActualizado.setNombre("Cristiano Ronaldo");

        // Matcher genérico para save
        given(filmRepository.save(any(Film.class))).willReturn(filmActualizado);

        FilmResponseDTO responseDTO = new FilmResponseDTO();
        responseDTO.setNombre("Cristiano Ronaldo");

        // Matcher genérico para toResponse
        given(filmMapper.toResponse(any(Film.class))).willReturn(responseDTO);

        // WHEN
        FilmResponseDTO actualizada = filmDao.update(film1.getId(), filmRequestDTO1).get();

        // THEN
        assertThat(actualizada.getNombre()).isEqualTo("Cristiano Ronaldo");

        verify(filmRepository).save(any(Film.class));
        verify(filmRepository).findById(film1.getId());
        verify(filmMapper).toResponse(any(Film.class));
    }




}

