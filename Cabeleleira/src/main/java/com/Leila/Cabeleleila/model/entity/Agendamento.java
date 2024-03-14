package com.Leila.Cabeleleila.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(schema = "agenda", name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_agendamento;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false) // Ajustado para corresponder à coluna de chave estrangeira do banco de dados
    private Usuario usuario; // Recomenda-se usar o nome da entidade em vez do nome da coluna para clareza

    @Column(name = "descricao", nullable = false, length = 255)
    private String descricao;

    @Column(name = "data_agendamento")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") // Ajustado para o padrão de data e hora
    private LocalDateTime data_agendamento; // Tipo alterado para LocalDateTime

}
