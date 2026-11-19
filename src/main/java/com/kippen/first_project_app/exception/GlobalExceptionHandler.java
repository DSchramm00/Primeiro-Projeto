package com.kippen.first_project_app.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import jakarta.validation.ConstraintViolationException;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@RestControllerAdvice 
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarErrosValidacao(MethodArgumentNotValidException ex) {
        List<String> erros = new ArrayList<>();

        for (FieldError erro : ex.getBindingResult().getFieldErrors()){
            erros.add(erro.getDefaultMessage());
        }

        Map<String, Object> resposta = new HashMap<>(); 
        resposta.put("status", 400);
        resposta.put("erros", erros);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    @ExceptionHandler(CodigoDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> tratarCodigoDuplicado(CodigoDuplicadoException ex){
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", 409);
        resposta.put("erro", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> tratarErroParametro(ConstraintViolationException ex){
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", 400);
        resposta.put("erro", ex.getConstraintViolations().iterator().next().getMessage());

        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, Object>> TratarValidacaoInvalida(HandlerMethodValidationException ex){
        List<String> erros = new ArrayList<>();

        for (MessageSourceResolvable  erro : ex.getAllErrors()){
            erros.add(erro.getDefaultMessage());
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", 400);
        resposta.put("erro", erros);

        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> tratarOrdenacaoInvalida(IllegalArgumentException ex){
        Map<String, Object> resposta = new HashMap<>();

        resposta.put("status", 400);
        resposta.put("erro", ex.getMessage());

        return ResponseEntity.badRequest().body(resposta);
    }
}
