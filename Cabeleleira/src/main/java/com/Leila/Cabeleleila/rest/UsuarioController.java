package com.Leila.Cabeleleila.rest;

// api de usuarios

import com.Leila.Cabeleleila.model.entity.Usuario;
import com.Leila.Cabeleleila.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class UsuarioController {

    private final UsuarioRepository repository;

    @Autowired
    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // retorna o status no servidor CREATION
    public Usuario salvar(@RequestBody @Valid Usuario usuario) { // valida a requisição
        return repository.save(usuario);
    }

    @GetMapping("{id_usuario}") // procura por map, recebe no post. é flexivel.
    public Usuario acharPorId(@PathVariable Integer id_usuario) {
        return repository.findById(id_usuario).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado")); // procura o id pelo id, se caso nao achar faz outra coisa.
    }

    // deleta um usuario
    @DeleteMapping("{id_usuario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id_usuario) {
//        repository.deleteById(id);   // DELETA POR ID de forma rapida
        repository.findById(id_usuario).map(
                usuario -> {
                    repository.delete(usuario);
                    return Void.TYPE;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado"));
    }

    // Atualização do usuario
    @PutMapping("{id}") // Atualiza o recurso
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id_usuario, @RequestBody @Valid Usuario usuarioAtualizado) {
        repository.findById(id_usuario).map(
                usuario -> {
                    usuario.setEmail(usuarioAtualizado.getEmail());
                    usuario.setSenha(usuarioAtualizado.getSenha());
                    return repository.save(usuario);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado"));
    }
}

