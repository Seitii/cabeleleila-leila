package com.Leila.Cabeleleila.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(schema = "agenda", name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome")
    @NotEmpty(message = "{campo.nome.obrigatorio}")
    private String nome;

    @Column(name = "email")
    @NotEmpty(message = "{campo.email.obrigatorio}")
    private String email;

    @Column(name = "senha")
    @NotEmpty(message = "{campo.senha.obrigatorio}")
    private String senha;

    @Column(name = "telefone")
    @NotEmpty(message = "{campo.telefone.obrigatorio}")
    private String telefone;

    @Column(name = "data_cadastro")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataCadastro; // Ajuste no nome da variável para seguir a convenção Java

    @Column(name = "descricao", nullable = false)
    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    private String descricao;

    @Column(name = "sexo", nullable = false)
    @NotEmpty(message = "{campo.sexo.obrigatorio}")
    private String sexo;
}
