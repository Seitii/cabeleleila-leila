package com.Leila.Cabeleleila.rest;


import com.Leila.Cabeleleila.model.entity.Agendamento;
import com.Leila.Cabeleleila.model.entity.Usuario;
import com.Leila.Cabeleleila.model.repository.AgendamentoRepository;
import com.Leila.Cabeleleila.model.repository.UsuarioRepository;
import com.Leila.Cabeleleila.rest.dto.AgendamentoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/agendamento")
@RequiredArgsConstructor
public class AgendamentoController {

    private final UsuarioRepository usuarioRepository;
    private final AgendamentoRepository repository;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agendamento salvar(@RequestBody AgendamentoDTO dto){
        LocalDateTime data = LocalDateTime.parse(dto.getData_agendamento(), DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss"));
        Integer cliente_id = dto.getCliente_id();

        Usuario usuario = usuarioRepository.findById(cliente_id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Usuario não existe"));

        Agendamento agendamento = new Agendamento();
        agendamento.setDescricao(dto.getDescricao());
        agendamento.setData_agendamento(data);
        agendamento.setUsuario(usuario);
        return repository.save(agendamento);
    }
    @GetMapping
    public List<Agendamento> pesquisar(
            @RequestParam(value = "nome", required = false, defaultValue = "") String nome,
            @RequestParam(value = "mes", required = false) Integer mes

    ){
        return repository.findByNomeClienteAndMes("%" + nome + "%", mes);
    }
}
