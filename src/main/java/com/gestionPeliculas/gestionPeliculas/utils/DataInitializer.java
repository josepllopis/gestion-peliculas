package com.gestionPeliculas.gestionPeliculas.utils;

import com.gestionPeliculas.gestionPeliculas.models.Usuario;
import com.gestionPeliculas.gestionPeliculas.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if(usuarioRepository.count() == 0) {
                usuarioRepository.save(new Usuario("Josep", passwordEncoder.encode("1234")));
                usuarioRepository.save(new Usuario("hermano", passwordEncoder.encode("brother4321")));
                usuarioRepository.save(new Usuario("madre", passwordEncoder.encode("mother1234")));
                usuarioRepository.save(new Usuario("padre", passwordEncoder.encode("matalarata13")));
            }
        };
    }
}
