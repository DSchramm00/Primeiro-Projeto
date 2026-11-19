package com.kippen.first_project_app.exception;

public class CodigoDuplicadoException extends RuntimeException{
    public CodigoDuplicadoException(String mensagem){
        super(mensagem);
    }
}
