package com.gestionPeliculas.gestionPeliculas.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestionPeliculas.gestionPeliculas.controllers.FilmController;
import com.gestionPeliculas.gestionPeliculas.dto.FilmRequestDTO;
import com.gestionPeliculas.gestionPeliculas.dto.FilmResponseDTO;
import com.gestionPeliculas.gestionPeliculas.dto.RankingResponseDTO;
import com.gestionPeliculas.gestionPeliculas.enums.Pais;
import com.gestionPeliculas.gestionPeliculas.models.Usuario;
import com.gestionPeliculas.gestionPeliculas.services.FilmDao;
import com.gestionPeliculas.gestionPeliculas.utils.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.MockMvcExtensionsKt.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(FilmController.class)
public class PeliculaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FilmDao filmDao;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;


    @Test
    @WithMockUser(username = "Jllopis33", roles = "USER")
    public void shouldReadAllFilms() throws Exception {

        // GIVEN
        FilmResponseDTO filmResponseDTO = new FilmResponseDTO();
        FilmResponseDTO filmResponseDTO2 = new FilmResponseDTO();
        List<FilmResponseDTO> listFilms = List.of(filmResponseDTO, filmResponseDTO2);
        when(filmDao.readAll(any(UserDetails.class))).thenReturn(listFilms);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(username="Jllopis33",roles = "USER")
    public void shouldReadAllFilmsByOtherUser() throws Exception {

        //GIVEN
        FilmResponseDTO filmResponseDTO = new FilmResponseDTO();
        FilmResponseDTO filmResponseDTO2 = new FilmResponseDTO();
        List<FilmResponseDTO> listFilms = List.of(filmResponseDTO, filmResponseDTO2);
        when(filmDao.readAllByOtherUsuario(any(UserDetails.class),any(String.class))).thenReturn(listFilms);

        //WHEN && THEN
        mockMvc.perform(get("/api/v1/films/user/jllopis33"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

    }

    @Test
    @WithMockUser(username="Jllopis33")
    public void shouldGetFilm() throws Exception {

        //GIVEN
        FilmResponseDTO responseEsperado = new FilmResponseDTO(1L,"Terminator", new Date(),"300","James Cameron","Estados Unidos",6,"Prime Video");
        when(filmDao.read(any(Long.class),any(UserDetails.class))).thenReturn(Optional.of(responseEsperado));

        //WHEN && THEN
        mockMvc.perform(get("/api/v1/films/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Terminator"))
                .andExpect(jsonPath("$.director").value("James Cameron"));


    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldReturnNotFoundWhenFilmNotExists() throws Exception {

        //GIVEN
        when(filmDao.read(any(Long.class),any(UserDetails.class))).thenReturn(Optional.empty());

        //WHEN & THEN
        mockMvc.perform(get("/api/v1/films/1"))
                .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldCreateFilm() throws Exception {

        // GIVEN
        FilmRequestDTO filmRequestDTO = new FilmRequestDTO("Terminator 2", new Date(), 300, "James Cameron", Pais.ESTADOS_UNIDOS, 6, "Prime Video");
        FilmResponseDTO filmResponseDTO = new FilmResponseDTO(1L, filmRequestDTO.getNombre(), filmRequestDTO.getFecha(), String.valueOf(filmRequestDTO.getDuracion()), filmRequestDTO.getDirector(), filmRequestDTO.getPais().toString(), filmRequestDTO.getPuntuacion(), filmRequestDTO.getCinema());
        when(filmDao.create(any(FilmRequestDTO.class), any(UserDetails.class))).thenReturn(filmResponseDTO);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/films")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Terminator 2"))
                .andExpect(jsonPath("$.director").value("James Cameron"));
    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldDeleteFilm() throws Exception {

        //GIVEN
        when(filmDao.read(any(Long.class),any(UserDetails.class))).thenReturn(Optional.empty());

        //WHRN & THEN
        mockMvc.perform(delete("/api/v1/films/1")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(filmDao, never()).delete(any(Long.class), any(UserDetails.class));
    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldReturnNotContentDeleteFilm() throws Exception {

        //GIVEN
        FilmResponseDTO responseEsperado = new FilmResponseDTO(1L,"Terminator", new Date(),"300","James Cameron","Estados Unidos",6,"Prime Video");
        when(filmDao.read(any(Long.class),any(UserDetails.class))).thenReturn(Optional.of(responseEsperado));

        mockMvc.perform(delete("/api/v1/films/1")
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(filmDao).delete(any(Long.class), any(UserDetails.class));

    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldUpdateFilm() throws Exception {

        //GIVEN
        FilmRequestDTO filmRequestDTO = new FilmRequestDTO("Terminator 2", new Date(), 300, "James Cameron", Pais.ESTADOS_UNIDOS, 6, "Prime Video");
        FilmResponseDTO filmResponseDTO = new FilmResponseDTO(1L, filmRequestDTO.getNombre(), filmRequestDTO.getFecha(), String.valueOf(filmRequestDTO.getDuracion()), filmRequestDTO.getDirector(), filmRequestDTO.getPais().toString(), filmRequestDTO.getPuntuacion(), filmRequestDTO.getCinema());
        when(filmDao.update(any(Long.class),any(FilmRequestDTO.class),any(UserDetails.class))).thenReturn(Optional.of(filmResponseDTO));

        //WHEN & THEN
        mockMvc.perform(put("/api/v1/films/1")
                .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filmRequestDTO)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Terminator 2"))
                .andExpect(jsonPath("$.director").value("James Cameron"));;


        verify(filmDao).update(any(Long.class), any(FilmRequestDTO.class), any(UserDetails.class));
    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldReturnNotFoundWhenFilmNotExistsUpdate() throws Exception {

        //GIVEN
        FilmRequestDTO filmRequestDTO = new FilmRequestDTO("Terminator 2", new Date(), 300, "James Cameron", Pais.ESTADOS_UNIDOS, 6, "Prime Video");
        when(filmDao.update(any(Long.class),any(FilmRequestDTO.class),any(UserDetails.class))).thenReturn(Optional.empty());


        //WHEN & THEN
        mockMvc.perform(put("/api/v1/films/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmRequestDTO)))

                .andExpect(status().isNotFound());

        verify(filmDao).update(any(Long.class),any(FilmRequestDTO.class),any(UserDetails.class));

    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldReturnSortedListFilms() throws Exception {

        //GIVEN
        FilmResponseDTO responseEsperado = new FilmResponseDTO(1L,"Terminator", new Date(),"300","James Cameron","Estados Unidos",6,"Prime Video");
        FilmResponseDTO responseEsperado2 = new FilmResponseDTO(2L,"Torrente Presidente", new Date(),"200","Santiago Segura","España",7,"Cine Axión");
        List<FilmResponseDTO> filmResponseDTOS = List.of(responseEsperado,responseEsperado2);
        when(filmDao.getAllSortedByPuntuacion(any(Sort.class),any(UserDetails.class))).thenReturn(filmResponseDTOS);

        //WHEN & THEN
        mockMvc.perform(get("/api/v1/films/sorted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Terminator"))
                .andExpect(jsonPath("$[1].nombre").value("Torrente Presidente"));
    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldReturnNotContentWhenNotFilmsExistsGetAllSorted() throws Exception {

        //GIVEN
        List<FilmResponseDTO> filmResponseDTOS = new ArrayList<>();
        when(filmDao.getAllSortedByPuntuacion(any(Sort.class),any(UserDetails.class))).thenReturn(filmResponseDTOS);

        //WHEN & THEN
        mockMvc.perform(get("/api/v1/films/sorted"))
                .andExpect(status().isNoContent());



        verify(filmDao).getAllSortedByPuntuacion(any(Sort.class),any(UserDetails.class));
    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldGetAllSortedOtherUsuario() throws Exception {

        //GIVEN
        FilmResponseDTO responseEsperado = new FilmResponseDTO(1L,"Terminator", new Date(),"300","James Cameron","Estados Unidos",6,"Prime Video");
        FilmResponseDTO responseEsperado2 = new FilmResponseDTO(2L,"Torrente Presidente", new Date(),"200","Santiago Segura","España",7,"Cine Axión");
        List<FilmResponseDTO> filmResponseDTOS = List.of(responseEsperado,responseEsperado2);
        when(filmDao.getAllSortedByPuntuacionOtherUsuario(any(String.class),any(Sort.class),any(UserDetails.class))).thenReturn(filmResponseDTOS);

        mockMvc.perform(get("/api/v1/films/user/jllopis33/sorted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Terminator"))
                .andExpect(jsonPath("$[1].nombre").value("Torrente Presidente"));

        verify(filmDao).getAllSortedByPuntuacionOtherUsuario(any(String.class),any(Sort.class),any(UserDetails.class));
    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldGetAllSortedOtherUsuarioCustomParams() throws Exception {

        //GIVEN
        FilmResponseDTO responseEsperado = new FilmResponseDTO(1L,"Terminator", new Date(),"300","James Cameron","Estados Unidos",6,"Prime Video");
        FilmResponseDTO responseEsperado2 = new FilmResponseDTO(2L,"Torrente Presidente", new Date(),"200","Santiago Segura","España",7,"Cine Axión");
        List<FilmResponseDTO> filmResponseDTOS = List.of(responseEsperado,responseEsperado2);

        when(filmDao.getAllSortedByPuntuacionOtherUsuario(any(String.class),any(Sort.class),any(UserDetails.class))).thenReturn(filmResponseDTOS);

        mockMvc.perform(get("/api/v1/films/user/jllopis33/sorted")
                        .param("sortBy","director")
                        .param("direction","ASC"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Terminator"))
                .andExpect(jsonPath("$[1].nombre").value("Torrente Presidente"));

        verify(filmDao).getAllSortedByPuntuacionOtherUsuario(any(String.class),any(Sort.class),any(UserDetails.class));
    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldReturnNotContentAllSortedOtherUsuario() throws Exception {

        //GIVEN
        List<FilmResponseDTO> filmResponseDTOS = List.of();
        when(filmDao.getAllSortedByPuntuacionOtherUsuario(any(String.class),any(Sort.class),any(UserDetails.class))).thenReturn(filmResponseDTOS);

        mockMvc.perform(get("/api/v1/films/user/jllopis33/sorted"))
                .andExpect(status().isNoContent());


        verify(filmDao).getAllSortedByPuntuacionOtherUsuario(any(String.class),any(Sort.class),any(UserDetails.class));
    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldExportDataToPDF() throws Exception {

        //GIVEN
        byte [] pdf = new byte[256];
        when(filmDao.generarPdfDeFilms(eq("puntuacion"),eq("DESC"),any(UserDetails.class))).thenReturn(pdf);

        //WHEN & THEN
        mockMvc.perform(get("/api/v1/films/pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", containsString("peliculas.pdf")));



        verify(filmDao).generarPdfDeFilms(eq("puntuacion"),eq("DESC"),any(UserDetails.class));
    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldExportPdfWithCustomParams() throws Exception {

        // GIVEN
        byte[] pdf = new byte[256];
        when(filmDao.generarPdfDeFilms(eq("nombre"), eq("ASC"), any(UserDetails.class)))
                .thenReturn(pdf);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/films/pdf")
                        .param("sortBy", "nombre")
                        .param("direction", "ASC"))
                .andExpect(status().isOk());

        verify(filmDao).generarPdfDeFilms(eq("nombre"), eq("ASC"), any(UserDetails.class));
    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldReturnNoContentWhenNoPdf() throws Exception {

        //GIVEN
        when(filmDao.generarPdfDeFilms(any(String.class),any(String.class),any(UserDetails.class))).thenReturn(null);

        //WHEN & THEN
        mockMvc.perform(get("/api/v1/films/pdf"))
                .andExpect(status().isNoContent());

        verify(filmDao).generarPdfDeFilms(any(String.class),any(String.class),any(UserDetails.class));
    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldReturnInternalServerErrorWhenException() throws Exception {

        //GIVEN
        when(filmDao.generarPdfDeFilms(any(String.class),any(String.class),any(UserDetails.class))).thenThrow(new RuntimeException("Error"));

        //WHEN & THEN
        mockMvc.perform(get("/api/v1/films/pdf"))
                .andExpect(status().isInternalServerError());

        verify(filmDao).generarPdfDeFilms(any(String.class),any(String.class),any(UserDetails.class));

    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldDevolverRanking(){

    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldReturnDevolverRanking() throws Exception {
        //GIVEN
        RankingResponseDTO ranking1 = new RankingResponseDTO("Jllopis33",15L);
        RankingResponseDTO ranking2 = new RankingResponseDTO("Brother",10L);
        List<RankingResponseDTO> rankingsResponseDTO = List.of(ranking1,ranking2);
        when(filmDao.getRanking(any(UserDetails.class))).thenReturn(rankingsResponseDTO);

        mockMvc.perform(get("/api/v1/ranking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("$[0].username").value("Jllopis33"))
                .andExpect(jsonPath("$[1].username").value("Brother"));

        verify(filmDao).getRanking(any(UserDetails.class));
    }

    @Test
    @WithMockUser(username = "Jllopis33")
    public void shouldReturnNoContentWhenRankingIsEmpty() throws Exception {

        // GIVEN
        when(filmDao.getRanking(any(UserDetails.class))).thenReturn(List.of());

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/ranking"))
                .andExpect(status().isNoContent());

        verify(filmDao).getRanking(any(UserDetails.class));
    }

}
