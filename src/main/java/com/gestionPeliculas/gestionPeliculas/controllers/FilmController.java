package com.gestionPeliculas.gestionPeliculas.controllers;

import com.gestionPeliculas.gestionPeliculas.dto.FilmRequestDTO;
import com.gestionPeliculas.gestionPeliculas.dto.FilmResponseDTO;

import com.gestionPeliculas.gestionPeliculas.services.FilmDao;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class FilmController {

    final private FilmDao filmDao;



    @Operation(summary = "Lista de películas", description = "Trae la lista de todas las películas que se encuentran en la bbdd")
    @GetMapping("/films")
    public ResponseEntity<List<FilmResponseDTO>> allFilms(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(filmDao.readAll(userDetails));
    }

    @Operation(summary = "Devolver película", description = "Devuelve la película de la bbdd que coincida con el ID que se le pasa por URL")
    @GetMapping("/films/{id}")
    public ResponseEntity<FilmResponseDTO> getFilm(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){

        Optional<FilmResponseDTO> filmOptional = filmDao.read(id,userDetails);

        return filmOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Añadir película",description = "Añade una nueva película a la bbdd")
    @PostMapping("/films")
    public ResponseEntity<FilmResponseDTO> createFilm(@Valid @RequestBody FilmRequestDTO filmRequestDTO, @AuthenticationPrincipal UserDetails userDetails){
        FilmResponseDTO createdFilm = filmDao.create(filmRequestDTO,userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFilm);
    }

    @Operation(summary = "Borrar película",description = "Borra de la bbdd la película que coincida con el ID que se le pasa por URL")
    @DeleteMapping("/films/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        Optional<FilmResponseDTO> film = filmDao.read(id,userDetails);
        if (film.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }

        filmDao.delete(id,userDetails);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar película" , description = "Actualiza de la bbdd la película que coincida con el ID que se le pasa por URL")
    @PutMapping("films/{id}")
    public ResponseEntity<FilmResponseDTO> update(@PathVariable Long id, @RequestBody FilmRequestDTO filmRequestDTO, @AuthenticationPrincipal UserDetails userDetails){
        return filmDao.update(id,filmRequestDTO,userDetails).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Lista de películas ordenadas", description = "Devuelve una lista de películas ordenadas por las 2 variable que se pasan por URL")
    @GetMapping("films/sorted")
    public ResponseEntity<List<FilmResponseDTO>> getAllSortedPuntuacion(@RequestParam(defaultValue = "puntuacion") String sortBy, @RequestParam(defaultValue = "DESC") String direction, @AuthenticationPrincipal UserDetails userDetails){

        Sort sort = Sort.by(Sort.Direction.fromString(direction),sortBy);

        List<FilmResponseDTO> listaFilms = filmDao.getAllSortedByPuntuacion(sort,userDetails);

        if(listaFilms.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listaFilms);
    }

    @Operation(summary = "Descargar PDF", description = "Descarga un PDF con una tabla con los datos de todas las películas de la bbdd")
    @GetMapping("films/pdf")
    public ResponseEntity<byte[]> exportDataToPdf(@RequestParam(defaultValue = "puntuacion") String sortBy,
                                                  @RequestParam(defaultValue = "DESC") String direction,@AuthenticationPrincipal UserDetails userDetails){

        try {
            byte[] pdfBytes = filmDao.generarPdfDeFilms(sortBy, direction,userDetails);

            if (pdfBytes == null || pdfBytes.length == 0) {
                return ResponseEntity.noContent().build(); // 204 No Content
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "peliculas.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }

    }


}
