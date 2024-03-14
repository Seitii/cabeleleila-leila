package com.Leila.Cabeleleila;

import com.Leila.Cabeleleila.model.entity.Usuario;
import com.Leila.Cabeleleila.model.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@SpringBootApplication
public class CabeleleilaApplication {

    @Bean
    public CommandLineRunner run(@Autowired UsuarioRepository usuarioRepository) {
        return args -> {
            Usuario usuario = new Usuario();
            usuario.setNome("Hugo");
            usuario.setEmail("usuario@gmail.com"); // Exemplo de e-mail preenchido
            usuario.setSenha("1234567");
            usuario.setTelefone("61986389157");
            usuario.setDescricao("Cortar cabelo");
            usuario.setDataCadastro(LocalDate.now()); // Ajuste para camelCase
            usuario.setSexo("M"); // ou "F", ou outro valor apropriado
            usuarioRepository.save(usuario); // Salvando o usuário no banco de dados
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(CabeleleilaApplication.class, args);
    }
}
