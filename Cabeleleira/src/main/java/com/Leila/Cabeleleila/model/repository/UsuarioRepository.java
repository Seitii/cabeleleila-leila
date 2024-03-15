package com.Leila.Cabeleleila.model.repository;

import com.Leila.Cabeleleila.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // jpa == escanea os repositorios e injeta no componente
    Optional<Usuario> findByEmail(String email);
}
