package com.Leila.Cabeleleila.rest;

import com.Leila.Cabeleleila.rest.exception.ApiErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // aponta os erros para o usuario
    public ApiErrors handleValidationErrors(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult(); // resultado da validação
        List<String> messages = bindingResult.getAllErrors()
                .stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList()); // faz um array de strings
        return  new ApiErrors(messages); // Intercepta e pega as mensagem de erros e retorna em um objeto personalizado
    }

    @ExceptionHandler(ResponseStatusException.class
    )
    public ResponseEntity<ApiErrors> handleResponseStatusException(ResponseStatusException ex){
        String mensagemError = ex.getReason(); // Preferencialmente use getReason() ao invés de getMessage()
        HttpStatus codigoStatus = ex.getStatus();
        ApiErrors apiErrors = new ApiErrors(mensagemError);
        return new ResponseEntity<>(apiErrors, codigoStatus);
    }// Retorno dinamico
}
