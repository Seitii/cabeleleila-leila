package com.Leila.Cabeleleila.model.repository;

import com.Leila.Cabeleleila.model.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
    // Método para buscar por nome (sem considerar o mês)
    @Query("SELECT a FROM Agendamento a JOIN a.usuario u WHERE UPPER(u.nome) LIKE UPPER(:nome)")
    List<Agendamento> findByNome(@Param("nome") String nome);
}
