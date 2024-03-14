package com.Leila.Cabeleleila.rest;

// api de usuarios

import com.Leila.Cabeleleila.model.entity.Usuario;
import com.Leila.Cabeleleila.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController // tudo que retonar no metodo, é o corpo da resposta.
@RequestMapping("/api/teste")  // Mapea a url basica
public class UsuarioController {

    private final UsuarioRepository repository;

    @Autowired // dependencia obrigatoria. pois é construtor
    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // retorna o status no servidor CREATION
    public Usuario save(@RequestBody @Valid Usuario usuario) { // valida a requisição
        return repository.save(usuario);
    }

    @GetMapping("{id}") // procura por map, recebe no post. é flexivel.
    public Usuario acharPorId(@PathVariable Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado")); // procura o id pelo id, se caso nao achar faz outra coisa.
    }

    // deleta um usuario
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
//        repository.deleteById(id);   // DELETA POR ID de forma rapida
        repository.findById(id).map(
                usuario -> {
                    repository.delete(usuario);
                    return Void.TYPE;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado"));
    }

    // Atualização do usuario
    @PutMapping("{id}") // Atualiza o recurso
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @RequestBody @Valid Usuario usuarioAtualizado) {
        repository.findById(id).map(
                usuario -> {
                    usuario.setNome(usuarioAtualizado.getNome());
                    usuario.setDescricao(usuarioAtualizado.getDescricao());
                    usuario.setEmail(usuarioAtualizado.getEmail());
                    return repository.save(usuario);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado"));
    }
}

