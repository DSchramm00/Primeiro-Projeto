package com.kippen.first_project_app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ScrollPosition.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kippen.first_project_app.ApiProduto.Produto;
import com.kippen.first_project_app.dto.ProdutoRequestDTO;
import com.kippen.first_project_app.dto.ProdutoResponseDTO;
import com.kippen.first_project_app.service.ProdutoService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    @PostMapping("/produtos")
    public ResponseEntity<?> criarProduto(@Valid @RequestBody ProdutoRequestDTO dados){
        
        Produto produto = new Produto(
                    dados.getNome(),
                    dados.getValor(),
                    dados.getCodigo()   
                );

        Produto resposta = produtoService.criarProduto(produto);

        ProdutoResponseDTO produtoResponse = new ProdutoResponseDTO(
            resposta.getNome(),
            resposta.getValor(),
            resposta.getCodigo()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(produtoResponse);
    }

    @PostMapping("/produtos/lote")
    public List<ProdutoResponseDTO> criarProdutoLote(@Valid @RequestBody List<ProdutoRequestDTO> loteDados) {
        List<Produto> produtosSalvos = produtoService.criarProdutoLote(loteDados);

        return produtosSalvos.stream()
        .map(p ->
            new ProdutoResponseDTO(
                p.getNome(),
                p.getValor(),
                p.getCodigo())
        )
        .toList();
    }

    @GetMapping("/produtos")
    public Page<ProdutoResponseDTO> listarProdutos(
        @PositiveOrZero(message = "Coloque um valor válido!")
        @RequestParam(defaultValue = "0") int page,
        @Positive(message = "Coloque um valor válido!")
        @Max(value = 20 , message = "A quatidade máxima é de 20 produtos por página!")
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "asc") String sort
        ){  
        
        Page<Produto> produto = produtoService.listarProdutos(page, size, sort) ;
        return produto.map(produtoPage -> 
            new ProdutoResponseDTO(
            produtoPage.getNome(),
            produtoPage.getValor(),
            produtoPage.getCodigo()
            ));
    }

    @GetMapping("/produtos/{codigo}")
    public ResponseEntity<ProdutoResponseDTO> buscarProduto(@PathVariable int codigo){
        Optional<Produto> resposta = produtoService.buscarProduto(codigo);
        if (resposta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Produto produto = resposta.get();

        ProdutoResponseDTO dto = new ProdutoResponseDTO(
            produto.getNome(),
            produto.getValor(),
            produto.getCodigo()
        );
        
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/produtos/{codigo}")
    public ResponseEntity<Void> deletarProduto(@PathVariable int codigo){
        boolean removido = produtoService.deletarProduto(codigo);

        if (!removido) {
            return ResponseEntity.notFound().build();
        } return ResponseEntity.noContent().build();
    }

    @PutMapping("/produtos/{codigo}")
    public ResponseEntity<?> atualizarProduto(@PathVariable int codigo,@Valid @RequestBody ProdutoRequestDTO dadosAtualizados){

        Produto produtoAtualizado = new Produto(
            dadosAtualizados.getNome(),
            dadosAtualizados.getValor(),
            dadosAtualizados.getCodigo()
        );

        Optional<Produto> resposta = produtoService.atualizarProduto(produtoAtualizado, codigo);

        if (resposta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        ProdutoResponseDTO responseAtualizado = new ProdutoResponseDTO(
            resposta.get().getNome(),
            resposta.get().getValor(),
            resposta.get().getCodigo()    
        );
        
        return ResponseEntity.ok(responseAtualizado);
    }
}
