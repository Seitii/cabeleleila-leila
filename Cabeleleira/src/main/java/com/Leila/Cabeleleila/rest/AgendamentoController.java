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

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/api/agendamento")
@RequiredArgsConstructor
public class AgendamentoController {

    private final UsuarioRepository usuarioRepository;
    private final AgendamentoRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agendamento salvar(@RequestBody AgendamentoDTO dto) {
        String dataAgendamento = dto.getData_agendamento();
        Integer clienteId = dto.getCliente_id();

        Usuario usuario = usuarioRepository.findById(clienteId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario não existe"));

        Agendamento agendamento = new Agendamento();
        agendamento.setDescricao(dto.getDescricao());
        agendamento.setData_agendamento(dataAgendamento);
        agendamento.setUsuario(usuario);

        return repository.save(agendamento);
    }
    @GetMapping
    public List<Agendamento> pesquisar(@RequestParam(value = "nome", required = false, defaultValue = "") String nome,
                                       @RequestParam(value = "mes", required = false) Integer mes) {
        List<Agendamento> resultados = repository.findByNome("%" + nome + "%");

        if (mes != null) {
            return resultados.stream()
                    .filter(a -> {
                        String[] parts = a.getData_agendamento().split("-");
                        int mesAgendamento = Integer.parseInt(parts[1]);
                        return mesAgendamento == mes;
                    })
                    .collect(Collectors.toList());
        } else {
            return resultados;
        }
    }

}
