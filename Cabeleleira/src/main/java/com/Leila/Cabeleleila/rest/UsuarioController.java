package com.Leila.Cabeleleila.rest;

import com.Leila.Cabeleleila.model.entity.Usuario;
import com.Leila.Cabeleleila.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/usuarios") // A URL base para essa classe controladora é /api/clientes
public class UsuarioController {

    private final UsuarioRepository repository;

    @Autowired
    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody @Valid Usuario usuario) {
        return repository.save(usuario);
    }

    @GetMapping("{id_usuario}")
    public Usuario acharPorId(@PathVariable Integer id_usuario) {
        return repository.findById(id_usuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    @DeleteMapping("{id_usuario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id_usuario) {
        repository.findById(id_usuario).ifPresent(repository::delete);
    }

    @PutMapping("{id_usuario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id_usuario, @RequestBody @Valid Usuario usuarioAtualizado) {
        repository.findById(id_usuario)
                .map(usuario -> {
                    usuario.setEmail(usuarioAtualizado.getEmail());
                    usuario.setNome(usuarioAtualizado.getNome());
                    usuario.setSenha(usuarioAtualizado.getSenha());
                    return repository.save(usuario);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }
}
