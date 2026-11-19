package com.kippen.first_project_app.ApiProduto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Getter 
@Setter 
@NoArgsConstructor 
@Entity
public class Produto {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é orbigatório!")
    private String nome;

    @Positive(message = "O valor deve ser maior que 0!")
    private double valor;

    @Positive(message = "O valor deve ser maior que 0!")
    @Column(unique = true)
    private int codigo;
    

    public Produto(String nome, double valor, int codigo){
        this.nome = nome;
        this.valor = valor;
        this.codigo = codigo;
    }
}
