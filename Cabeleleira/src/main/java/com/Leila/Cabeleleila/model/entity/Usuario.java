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
    private Integer id_usuario;

    @Column(name = "email")
    @NotEmpty(message = "{campo.email.obrigatorio}")
    private String email;

    @Column(name = "senha")
    @NotEmpty
    private String senha;

}
