package com.Leila.Cabeleleila.rest;

import com.Leila.Cabeleleila.model.entity.Agendamento;
import com.Leila.Cabeleleila.model.entity.Usuario;
import com.Leila.Cabeleleila.model.repository.AgendamentoRepository;
import com.Leila.Cabeleleila.model.repository.UsuarioRepository;
import com.Leila.Cabeleleila.rest.dto.AgendamentoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        agendamento.setData_agendamento(dto.getData_agendamento());
        agendamento.setUsuario(usuario);

        Agendamento novoAgendamento = agendamentoRepository.save(agendamento);

        dto.setId_agendamento(novoAgendamento.getId_agendamento());
        dto.setNomeUsuario(usuario.getNome());

        return dto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoDTO> buscarPorId(@PathVariable Integer id) {
        return agendamentoRepository.findById(id)
                .map(agendamento -> {
                    AgendamentoDTO dto = new AgendamentoDTO();
                    dto.setId_agendamento(agendamento.getId_agendamento());
                    dto.setCliente_id(agendamento.getUsuario().getId_usuario());
                    dto.setDescricao(agendamento.getDescricao());
                    dto.setData_agendamento(agendamento.getData_agendamento().toString());
                    dto.setNomeUsuario(agendamento.getUsuario().getNome());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/todos")
    public ResponseEntity<List<AgendamentoDTO>> listarTodos() {
        List<Agendamento> agendamentos = agendamentoRepository.findAll();

        if(agendamentos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<AgendamentoDTO> agendamentoDTOS = agendamentos.stream().map(agendamento -> {
            AgendamentoDTO dto = new AgendamentoDTO();
            dto.setId_agendamento(agendamento.getId_agendamento());
            dto.setCliente_id(agendamento.getUsuario().getId_usuario());
            dto.setDescricao(agendamento.getDescricao());
            dto.setData_agendamento(agendamento.getData_agendamento());
            dto.setNomeUsuario(agendamento.getUsuario().getNome());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(agendamentoDTOS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoDTO> atualizarAgendamento(@PathVariable Integer id, @RequestBody AgendamentoDTO agendamentoDTO) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));

        agendamento.setDescricao(agendamentoDTO.getDescricao());
        agendamento.setData_agendamento(agendamentoDTO.getData_agendamento());

        agendamentoRepository.save(agendamento);

        AgendamentoDTO respostaDTO = new AgendamentoDTO();
        respostaDTO.setId_agendamento(agendamento.getId_agendamento());
        respostaDTO.setCliente_id(agendamento.getUsuario().getId_usuario());
        respostaDTO.setDescricao(agendamento.getDescricao());
        respostaDTO.setData_agendamento(agendamento.getData_agendamento());
        respostaDTO.setNomeUsuario(agendamento.getUsuario().getNome());

        return ResponseEntity.ok(respostaDTO);
    }
}
