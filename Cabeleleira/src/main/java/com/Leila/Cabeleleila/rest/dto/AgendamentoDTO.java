package com.Leila.Cabeleleila.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AgendamentoDTO {
    private Integer cliente_id; // Confirme se essa deve ser a referência ao ID do usuário

    private String descricao;

    private String data_agendamento;  // Mantido como String para receber a data em formato de texto

}
