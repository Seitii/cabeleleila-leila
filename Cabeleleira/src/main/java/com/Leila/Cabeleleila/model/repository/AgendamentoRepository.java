package com.Leila.Cabeleleila.model.repository;

import com.Leila.Cabeleleila.model.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

    @Query(value = "SELECT s FROM Agendamento s JOIN s.usuario u WHERE UPPER(u.nome) LIKE UPPER(:nome) AND EXTRACT(MONTH FROM s.data_agendamento) = :mes", nativeQuery = true)
    List<Agendamento> findByNomeClienteAndMes(@Param("nome") String nome, @Param("mes") Integer mes);
}
