package com.Leila.Cabeleleila;

import com.Leila.Cabeleleila.model.entity.Usuario;
import com.Leila.Cabeleleila.model.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class CabeleleilaApplication{

    @Bean
    public CommandLineRunner run(@Autowired UsuarioRepository usuarioRepository) {
        return args -> {
            Usuario usuario = new Usuario();
            usuario.setEmail("email@teste11.com"); // Exemplo de e-mail preenchido
            usuario.setSenha("1234567");
            usuarioRepository.save(usuario); // Salvando o usuário no banco de dados
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(CabeleleilaApplication.class, args);
    }
}
