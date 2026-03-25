package com.gestionPeliculas.gestionPeliculas.exception;

import com.gestionPeliculas.gestionPeliculas.controllers.ControllerAdvice;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionTest {


    private final ControllerAdvice controllerAdvice = new ControllerAdvice();

    @Test
    public void entityNotFoundException(){

        // GIVEN
        EntityNotFoundException ex = new EntityNotFoundException("Entidad no encontrada");

        // WHEN
        ResponseEntity<String> response = controllerAdvice.entityNotFoundException(ex);

        // THEN
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Entidad no encontrada", response.getBody());
    }

    @Test
    public void illegalArgumentException(){

        //GIVEN
        IllegalArgumentException ilegalArgumentException = new IllegalArgumentException("Argumentos no válidos");

        //WHEN
        ResponseEntity<String> response = controllerAdvice.illegalArgumentException(ilegalArgumentException);

        //THEN
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals("Argumentos no válidos",response.getBody());
    }

    @Test
    public void handleIllegalState(){

        //GIVEN
        IllegalStateException illegalStateException = new IllegalStateException("Estado no válido");

        //WHEN
        ResponseEntity<String> response = controllerAdvice.handleIllegalState(illegalStateException);

        //THEN
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
        assertEquals("Estado no válido",response.getBody());
    }

    @Test
    public void movieAlreadyExistsException(){

        //GIVEN
        MovieAlreadyExistsException movieAlreadyExistsException = new MovieAlreadyExistsException("La película ya existe");

        //WHEN
        ResponseEntity<Map<String,Object>> response = controllerAdvice.movieAlreadyExistsException(movieAlreadyExistsException);

        //THEN
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("La película ya existe", response.getBody().get("errors"));
        assertNotNull(response.getBody().get("timestamp"));

    }

    @Test
    public void shouldReturnBadRequestWhenMethodArgumentNotValid(){

        // GIVEN
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("filmRequestDTO", "nombre", "El nombre no puede estar vacío");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // WHEN
        ResponseEntity<Map<String, Object>> response = controllerAdvice.methodArgumentNotValidException(ex);

        // THEN
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertNotNull(response.getBody().get("timestamp"));

        Map<String, String> errors = (Map<String, String>) response.getBody().get("errors");
        assertEquals("El nombre no puede estar vacío", errors.get("nombre"));
    }

    @Test
    public void handleInvalidJson(){

        //GIVEN
        HttpMessageNotReadableException httpMessageNotReadableException = new HttpMessageNotReadableException("JSON inválido");

        //WHEN
        ResponseEntity<String> response = controllerAdvice.handleInvalidJson(httpMessageNotReadableException);

        //THEN
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals("JSON inválido o campos no permitidos",response.getBody());

    }

    @Test
    public void filmNotFoundException(){

        //GIVEN
        FilmNotFoundException filmNotFoundExc = new FilmNotFoundException("Película no encontrada");

        //WHEN
        ResponseEntity<Map<String,Object>> response = controllerAdvice.filmNotFoundException(filmNotFoundExc);

        //THEN
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals("Película no encontrada",response.getBody().get("errors"));
        assertNotNull(response.getBody().get("timestamp"));
    }
}
