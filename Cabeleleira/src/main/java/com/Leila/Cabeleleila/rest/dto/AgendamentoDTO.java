package com.Leila.Cabeleleila.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AgendamentoDTO {
    private Integer cliente_id;

    private String descricao;

    private String data_agendamento;

    private String nomeUsuario;

    private Integer id_agendamento;
}
