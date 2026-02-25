package com.gestionPeliculas.gestionPeliculas.services;

import com.gestionPeliculas.gestionPeliculas.dto.FilmRequestDTO;
import com.gestionPeliculas.gestionPeliculas.dto.FilmResponseDTO;
import com.gestionPeliculas.gestionPeliculas.dto.RankingResponseDTO;
import com.gestionPeliculas.gestionPeliculas.exception.FilmNotFoundException;
import com.gestionPeliculas.gestionPeliculas.exception.MovieAlreadyExistsException;
import com.gestionPeliculas.gestionPeliculas.mapper.FilmMapper;
import com.gestionPeliculas.gestionPeliculas.models.Film;
import com.gestionPeliculas.gestionPeliculas.models.Usuario;
import com.gestionPeliculas.gestionPeliculas.repository.FilmRepository;
import com.gestionPeliculas.gestionPeliculas.repository.UsuarioRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao{

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;
    private final UsuarioRepository usuarioRepository;



    @Override
    public FilmResponseDTO create(FilmRequestDTO filmRequestDTO, UserDetails userDetails) {

        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if(filmRepository.existsByNombreAndDirectorAndUsuario(filmRequestDTO.getNombre(),filmRequestDTO.getDirector(),usuario)){
            throw new MovieAlreadyExistsException("Ya existe la película con el nombre: "+filmRequestDTO.getNombre()+" del director: "+filmRequestDTO.getDirector());
        }
        return filmMapper.toResponse(filmRepository.save(filmMapper.toEntity(filmRequestDTO,usuario)));

    }

    @Override
    public Optional<FilmResponseDTO> read(long id,UserDetails userDetails) {
        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Film pelicula = filmRepository.findById(id)
                .orElseThrow(() -> new FilmNotFoundException("Película no encontrada"));


        if(!pelicula.getUsuario().getUsername().equals(userDetails.getUsername())){
            throw new FilmNotFoundException("Película no encontrada");
        }


        return filmRepository.findById(id).map(filmMapper::toResponse);
    }

    @Override
    public List<FilmResponseDTO> readAll(UserDetails userDetails) {

        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return filmRepository.findByUsuario(usuario).stream().map(filmMapper::toResponse).toList();
        //return filmRepository.findAll().stream().map(filmMapper::toResponse).toList();
    }

    @Override
    public List<FilmResponseDTO> readAllByOtherUsuario(UserDetails userDetails,String username) {
        Usuario usuarioActual = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no loggeado"));

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        return filmRepository.findByUsuario(usuario).stream().map(filmMapper::toResponse).toList();
    }

    @Override
    public Optional<FilmResponseDTO> update(long id, FilmRequestDTO filmRequestDTO, UserDetails userDetails) {
        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Film pelicula = filmRepository.findById(id)
                .orElseThrow(() -> new FilmNotFoundException("Película no encontrada"));


        if(!pelicula.getUsuario().getUsername().equals(userDetails.getUsername())){
            throw new FilmNotFoundException("Película no encontrada");
        }


        return filmRepository.findById(id).map(film->{
            film.setPais(filmRequestDTO.getPais());
            film.setFecha(filmRequestDTO.getFecha());
            film.setDuracion(filmRequestDTO.getDuracion());
            film.setPuntuacion(filmRequestDTO.getPuntuacion());
            film.setNombre(filmRequestDTO.getNombre());
            film.setDirector(filmRequestDTO.getDirector());
            film.setCinema(filmRequestDTO.getCinema());
            filmRepository.save(film);
            return filmMapper.toResponse(film);
        });
    }

    @Override
    public void delete(long id, UserDetails userDetails) {

        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Film pelicula = filmRepository.findById(id)
                .orElseThrow(() -> new FilmNotFoundException("Película no encontrada"));


        if(!pelicula.getUsuario().getUsername().equals(userDetails.getUsername())){
           throw new FilmNotFoundException("Película no encontrada");
        }

        Optional<Film> filmEliminar = filmRepository.findById(id);
        filmEliminar.ifPresent(filmRepository::delete);
    }

    @Override
    public List<FilmResponseDTO> getAllSortedByPuntuacion(Sort sort, UserDetails userDetails) {

        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return filmRepository.findByUsuario(usuario,sort).stream().map(filmMapper::toResponse).toList();
    }

    @Override
    public List<FilmResponseDTO> getAllSortedByPuntuacionOtherUsuario(String username, Sort sort, UserDetails userDetails) {
        Usuario usuarioActual = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        return filmRepository.findByUsuario(usuario,sort).stream().map(filmMapper::toResponse).toList();
    }

    @Override
    public List<RankingResponseDTO> getRanking() {
        return filmRepository.getRanking();
    }

    @Override
    public byte[] generarPdfDeFilms(String sortBy, String direction, UserDetails userDetails) throws IOException {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        List<FilmResponseDTO> datosFilms = getAllSortedByPuntuacion(sort,userDetails);

        if(datosFilms.isEmpty()){
            return null;
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Listado de Películas("+userDetails.getUsername()+")"+"\n\n", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(15f);
            document.add(titulo);

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            Font fontCabecera = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font fontNormal = new Font(Font.HELVETICA, 10, Font.NORMAL);

            String[] headersCell = {"Nombre", "País", "Cine", "Fecha", "Duración", "Puntuación", "Director"};
            for (String h : headersCell) {
                PdfPCell cell = new PdfPCell(new Phrase(h, fontCabecera));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(Color.LIGHT_GRAY);
                cell.setPaddingTop(8f);
                cell.setPaddingBottom(8f);
                table.addCell(cell);
            }

            for (FilmResponseDTO film : datosFilms) {
                PdfPCell[] celdas = {
                        new PdfPCell(new Phrase(film.getNombre(), fontNormal)),
                        new PdfPCell(new Phrase(film.getPais(), fontNormal)),
                        new PdfPCell(new Phrase(film.getCinema(), fontNormal)),
                        new PdfPCell(new Phrase(film.getFecha().toString(), fontNormal)),
                        new PdfPCell(new Phrase(String.valueOf(film.getDuracion()), fontNormal)),
                        new PdfPCell(new Phrase(String.valueOf(film.getPuntuacion()), fontNormal)),
                        new PdfPCell(new Phrase(film.getDirector(), fontNormal))
                };
                for (PdfPCell c : celdas) {
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setPaddingTop(8f);
                    c.setPaddingBottom(8f);
                    table.addCell(c);
                }
            }

            document.add(table);
            document.close();

            return baos.toByteArray();
        }
    }
}
