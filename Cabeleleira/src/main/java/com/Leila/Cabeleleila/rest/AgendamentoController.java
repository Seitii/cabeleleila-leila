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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/api/agendamento")
@RequiredArgsConstructor
public class AgendamentoController {

    private final UsuarioRepository usuarioRepository;
    private final AgendamentoRepository agendamentoRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgendamentoDTO salvar(@RequestBody AgendamentoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getCliente_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario não existe"));

        Agendamento agendamento = new Agendamento();
        agendamento.setDescricao(dto.getDescricao());
        agendamento.setData_agendamento(dto.getData_agendamento()); // Garanta que isso esteja no formato correto
        agendamento.setUsuario(usuario);

        Agendamento novoAgendamento = agendamentoRepository.save(agendamento);

        dto.setNomeUsuario(usuario.getNome()); // Inclui o nome do usuário no DTO
        return dto; // Retorna o DTO incluindo o nome do usuário
    }

    @GetMapping
    public List<AgendamentoDTO> pesquisar(@RequestParam(value = "nome", required = false, defaultValue = "") String nome,
                                          @RequestParam(value = "mes", required = false) Integer mes) {
        List<Agendamento> agendamentos = agendamentoRepository.findAll(); // Pode precisar ajustar para sua lógica específica

        // Filtro adicional em Java para exemplo; idealmente, faça isso via consulta SQL/JPA se possível
        return agendamentos.stream()
                .filter(agendamento -> agendamento.getUsuario().getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(agendamento -> mes == null || LocalDateTime.parse(agendamento.getData_agendamento(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).getMonthValue() == mes)
                .map(agendamento -> {
                    AgendamentoDTO dto = new AgendamentoDTO();
                    dto.setCliente_id(agendamento.getUsuario().getId_usuario());
                    dto.setDescricao(agendamento.getDescricao());
                    dto.setData_agendamento(agendamento.getData_agendamento());
                    dto.setNomeUsuario(agendamento.getUsuario().getNome()); // Assume que o `Usuario` tem um método `getNome()`
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
