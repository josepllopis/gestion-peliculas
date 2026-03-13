package com.gestionPeliculas.gestionPeliculas.controller.service;

import com.gestionPeliculas.gestionPeliculas.dto.FilmRequestDTO;
import com.gestionPeliculas.gestionPeliculas.dto.FilmResponseDTO;
import com.gestionPeliculas.gestionPeliculas.enums.Pais;
import com.gestionPeliculas.gestionPeliculas.exception.FilmNotFoundException;
import com.gestionPeliculas.gestionPeliculas.exception.MovieAlreadyExistsException;
import com.gestionPeliculas.gestionPeliculas.mapper.FilmMapper;
import com.gestionPeliculas.gestionPeliculas.models.Film;
import com.gestionPeliculas.gestionPeliculas.models.Usuario;
import com.gestionPeliculas.gestionPeliculas.repository.FilmRepository;
import com.gestionPeliculas.gestionPeliculas.repository.UsuarioRepository;
import com.gestionPeliculas.gestionPeliculas.services.FilmDaoImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PeliculaServiceTest {

    @Mock
    private FilmRepository filmRepository;
    @Mock
    private FilmMapper filmMapper;
    @Mock
    private UsuarioRepository usuarioRepository;


    @InjectMocks
    private FilmDaoImpl filmDao;

    @Test
    public void shouldCreateFilmWhenFilmDoesNotExist(){


        //GIVEN
        Usuario usuario = new Usuario("Jllopis33","Pepin30");
        FilmRequestDTO filmRequestDTO = new FilmRequestDTO("Terminator 2",new Date(),300,"James Cameron", Pais.ESTADOS_UNIDOS,6,"Prime Video");
        Film film = new Film(1L,"Terminator 2",filmRequestDTO.getFecha(),300,"James Cameron",Pais.ESTADOS_UNIDOS,6,"Prime Video",usuario);
        FilmResponseDTO responseEsperado = new FilmResponseDTO(1L,"Terminator 2", filmRequestDTO.getFecha(),"300","James Cameron","Estados Unidos",6,"Prime Video");

        when(usuarioRepository.findByUsername(usuario.getUsername())).thenReturn(Optional.of(usuario));
        when(filmRepository.existsByNombreAndDirectorAndUsuario(filmRequestDTO.getNombre(),filmRequestDTO.getDirector(),usuario)).thenReturn(false);
        when(filmMapper.toEntity(filmRequestDTO, usuario)).thenReturn(film);
        when(filmMapper.toResponse(film)).thenReturn(responseEsperado);
        when(filmRepository.save(any(Film.class))).thenReturn(film);

        //WHEN

        FilmResponseDTO filmResponseDTO = filmDao.create(filmRequestDTO,usuario);
        //THEN

        assertEquals(responseEsperado.getNombre(),filmResponseDTO.getNombre());
        assertEquals(responseEsperado.getDirector(),filmResponseDTO.getDirector());
        assertEquals(responseEsperado.getFecha(),filmResponseDTO.getFecha());
        assertEquals(responseEsperado.getPais(),filmResponseDTO.getPais());
        assertEquals(responseEsperado.getPuntuacion(),filmResponseDTO.getPuntuacion());
        assertEquals(responseEsperado.getCinema(),filmResponseDTO.getCinema());
        assertEquals(responseEsperado.getDuracion(),filmResponseDTO.getDuracion());

        verify(filmRepository).save(film);
        verify(filmMapper).toEntity(filmRequestDTO, usuario);
        verify(filmMapper).toResponse(film);
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound(){

        //GIVEN
        Usuario usuario = new Usuario("Jllopis33","Pepin30");
        FilmRequestDTO filmRequestDTO = new FilmRequestDTO("Terminator 2",new Date(),300,"James Cameron", Pais.ESTADOS_UNIDOS,6,"Prime Video");
        when(usuarioRepository.findByUsername(usuario.getUsername())).thenReturn(Optional.empty());


        //THEN

        RuntimeException runtimeException = assertThrows(RuntimeException.class, ()->filmDao.create(filmRequestDTO,usuario));

        assertEquals("Usuario no encontrado", runtimeException.getMessage());
        verify(usuarioRepository).findByUsername(usuario.getUsername());
    }

    @Test
    public void shouldThrowExceptionWhenMovieExists(){

        //GIVEN
        Usuario usuario = new Usuario("Jllopis33","Pepin30");
        FilmRequestDTO filmRequestDTO = new FilmRequestDTO("Terminator 2",new Date(),300,"James Cameron", Pais.ESTADOS_UNIDOS,6,"Prime Video");
        when(usuarioRepository.findByUsername(usuario.getUsername())).thenReturn(Optional.of(usuario));
        when(filmRepository.existsByNombreAndDirectorAndUsuario(filmRequestDTO.getNombre(),filmRequestDTO.getDirector(),usuario)).thenReturn(true);

        //WHEN
        MovieAlreadyExistsException movieAlreadyExistsException = assertThrows(MovieAlreadyExistsException.class, ()->filmDao.create(filmRequestDTO,usuario));

        assertEquals("Ya existe la película con el nombre: "+filmRequestDTO.getNombre()+ " del director: "+filmRequestDTO.getDirector(), movieAlreadyExistsException.getMessage());

    }

    @Test
    public void shouldReadFilmWhenExists(){

        //GIVEN
        Usuario usuario = new Usuario("Jllopis33","Pepin");
        FilmRequestDTO filmRequestDTO = new FilmRequestDTO("Terminator 2",new Date(),300,"James Cameron", Pais.ESTADOS_UNIDOS,6,"Prime Video");
        Film film = new Film(1L,"Terminator 2",filmRequestDTO.getFecha(),300,"James Cameron",Pais.ESTADOS_UNIDOS,6,"Prime Video",usuario);
        FilmResponseDTO responseEsperado = new FilmResponseDTO(1L,"Terminator 2", filmRequestDTO.getFecha(),"300","James Cameron","Estados Unidos",6,"Prime Video");
        when(filmMapper.toResponse(film)).thenReturn(responseEsperado);
        when(filmRepository.findById(any(Long.class))).thenReturn(Optional.of(film));
        when(usuarioRepository.findByUsername(usuario.getUsername())).thenReturn(Optional.of(usuario));


        //WHEN
        Optional<FilmResponseDTO> response = filmDao.read(1,usuario);

        //THEN
        assertEquals(responseEsperado.getNombre(),response.get().getNombre());
        assertEquals(responseEsperado.getDirector(),response.get().getDirector());
        assertEquals(responseEsperado.getPuntuacion(),response.get().getPuntuacion());
        assertEquals(responseEsperado.getDuracion(),response.get().getDuracion());
        assertEquals(responseEsperado.getFecha(),response.get().getFecha());
        assertEquals(responseEsperado.getCinema(),response.get().getCinema());
        assertEquals(responseEsperado.getPais(),response.get().getPais());
    }


    @Test
    public void shouldThrowExceptionWhenUserNotFoundRead(){

        Usuario usuario = new Usuario("Jllopis33","Pepin");
        when(usuarioRepository.findByUsername(usuario.getUsername())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, ()->filmDao.read(1,usuario));

        assertEquals("Usuario no encontrado",exception.getMessage());

        verify(usuarioRepository).findByUsername(usuario.getUsername());
        verifyNoMoreInteractions(filmRepository, filmMapper);
    }

    @Test
    public void shouldThrowExceptionWhenFilmNotExists(){

        //GIVEN
        Usuario usuario = new Usuario("Jllopis33","Pepin");
        when(usuarioRepository.findByUsername(usuario.getUsername())).thenReturn(Optional.of(usuario));
        when(filmRepository.findById(1L)).thenReturn(Optional.empty());

        //WHEN & THEN
        FilmNotFoundException exception = assertThrows(FilmNotFoundException.class,
        ()->filmDao.read(1L,usuario));

        assertEquals("Película no encontrada",exception.getMessage());

        verify(usuarioRepository).findByUsername(usuario.getUsername());
        verify(filmRepository).findById(1L);

        verifyNoMoreInteractions(filmMapper);
    }

    @Test
    public void shouldReadAllFilms(){
        Usuario usuario = new Usuario("Jllopis33","Pepin");
        Film film = new Film(1L,"Terminator 2",new Date(),300,"James Cameron",Pais.ESTADOS_UNIDOS,6,"Prime Video",usuario);
        Film film2 = new Film(2L,"Guerra Mundial Z",new Date(),250,"Moris Forst",Pais.ESTADOS_UNIDOS,6,"Prime Video",usuario);
        FilmResponseDTO response1 = new FilmResponseDTO(1L,"Terminator 2", film.getFecha(),"300","James Cameron","Estados Unidos",6,"Prime Video");
        FilmResponseDTO response2 = new FilmResponseDTO(2L,"Guerra Mundial Z", film2.getFecha(),"250","Moris Forst","Estados Unidos",6,"Prime Video");

        List<Film> films = List.of(film,film2);



        when(usuarioRepository.findByUsername(usuario.getUsername())).thenReturn(Optional.of(usuario));
        when(filmMapper.toResponse(films.get(0))).thenReturn(response1);
        when(filmMapper.toResponse(films.get(1))).thenReturn(response2);
        when(filmRepository.findByUsuario(usuario)).thenReturn(films);

        //WHEN
        List<FilmResponseDTO> response = filmDao.readAll(usuario);

        //THEN
        assertNotNull(response);
        assertEquals(2,response.size());
        assertEquals("Terminator 2", response.get(0).getNombre());
        assertEquals("Guerra Mundial Z", response.get(1).getNombre());

        verify(usuarioRepository).findByUsername(usuario.getUsername());
        verify(filmMapper).toResponse(films.get(0));
        verify(filmMapper).toResponse(films.get(1));


    }

    @Test
    public void shouldThrowExceptionUserNotFoundReadAllFilms(){
        //GIVEN
        Usuario usuario = new Usuario("Jllopis33","Pepin");
        when(usuarioRepository.findByUsername(usuario.getUsername())).thenReturn(Optional.empty());

        //WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class,
                ()->filmDao.readAll(usuario));

        assertEquals("Usuario no encontrado",exception.getMessage());
        verify(usuarioRepository).findByUsername(usuario.getUsername());

        verifyNoMoreInteractions(filmRepository,filmMapper);
    }

}
