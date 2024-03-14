package com.Leila.Cabeleleila.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    private Usuario usuario;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "data_agendamento")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataAgendamento;

    @PrePersist
    public void prePersist() {
        this.dataAgendamento = LocalDateTime.now();
    }
}
