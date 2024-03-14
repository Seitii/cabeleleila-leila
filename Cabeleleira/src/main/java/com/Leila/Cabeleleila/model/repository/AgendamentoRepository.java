package com.Leila.Cabeleleila.model.repository;

import com.Leila.Cabeleleila.model.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

    @Query("select s from Agendamento s join s.usuario u " +
            "where upper(u.nome) like upper(:nome) and MONTH(s.data_agendamento) =:mes")
    List<Agendamento> findByNomeClienteAndMes(@Param("nome") String nome, @Param("mes") Integer mes);
}
