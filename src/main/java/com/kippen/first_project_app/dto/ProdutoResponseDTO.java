package com.kippen.first_project_app.dto;

import lombok.Getter;

@Getter
public class ProdutoResponseDTO {

    private String nome;
    private double valor;
    private int codigo;

    public ProdutoResponseDTO(String nome, double valor, int codigo) {
        this.nome = nome;
        this.valor = valor;
        this.codigo = codigo;
    } 
}