package com.kippen.first_project_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProdutoRequestDTO {
    
    @NotBlank(message = "Este campo não pode ficar vazio!")
    private String nome;
    @Positive(message = "O valor precisa sem positivo!")
    private double valor;
    @Positive(message = "O valor precisa sem positivo!")
    private int codigo;
}
