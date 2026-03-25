package com.gestionPeliculas.gestionPeliculas.service;

import com.gestionPeliculas.gestionPeliculas.models.Usuario;
import com.gestionPeliculas.gestionPeliculas.repository.UsuarioRepository;
import com.gestionPeliculas.gestionPeliculas.services.UsuarioDaoImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioDaoImpl usuarioDao;

    @Test
    public void shouldLoadUserByUsername(){

        //GIVEN
        Usuario usuario = new Usuario("Jllopis33","Pepin_30");
        when(usuarioRepository.findByUsername(usuario.getUsername())).thenReturn(Optional.of(usuario));

        //WHEN
        UserDetails userDetails = usuarioDao.loadUserByUsername("Jllopis33");

        //THEN
        assertEquals("Jllopis33",userDetails.getUsername());

        verify(usuarioRepository).findByUsername(usuario.getUsername());
    }

    @Test
    public void shouldThrowExceptionUserNotFound(){

        //GIVEN
        Usuario usuario = new Usuario("Jllopis33","Pepin_30");
        when(usuarioRepository.findByUsername(usuario.getUsername())).thenReturn(Optional.empty());

        //WHEN
        UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class,
                ()->usuarioDao.loadUserByUsername("Jllopis33"));

        //THEN
        assertEquals("Usuario no encontrado: Jllopis33",usernameNotFoundException.getMessage());

        verify(usuarioRepository).findByUsername(usuario.getUsername());
        verifyNoMoreInteractions(usuarioRepository);
    }
}
