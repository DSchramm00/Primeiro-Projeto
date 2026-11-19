package com.kippen.first_project_app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ScrollPosition.Direction;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kippen.first_project_app.ApiProduto.Produto;
import com.kippen.first_project_app.dto.ProdutoRequestDTO;
import com.kippen.first_project_app.exception.CodigoDuplicadoException;
import com.kippen.first_project_app.repository.ProdutoRepository;

import jakarta.transaction.Transactional;

@Service 
public class ProdutoService {
    
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }
    
    public Produto criarProduto(Produto produto) {
        if (produtoRepository.existsByCodigo(produto.getCodigo())) {
            throw new CodigoDuplicadoException("Código já cadastrado!");
        }
        return produtoRepository.save(produto);
    }

    public List<Produto> criarProdutoLote(List<ProdutoRequestDTO> produtos) {
        List<Produto> lote = new ArrayList<>();
        Set<Integer> codigos = new HashSet<>();
        for (ProdutoRequestDTO produto : produtos) {
            Produto produtoLote = new Produto(
                produto.getNome(),
                produto.getValor(),
                produto.getCodigo()
            );
            if (produtoRepository.existsByCodigo(produto.getCodigo())) {
                 throw new CodigoDuplicadoException("O código inserido ja existe!");
            }

            if (!codigos.contains(produto.getCodigo())){
                codigos.add(produto.getCodigo());
            } else {
                throw new CodigoDuplicadoException("O código inserido ja existe!");
            }

            lote.add(produtoLote);
        }
        return produtoRepository.saveAll(lote);
    }

    public Page<Produto> listarProdutos(int page, int size, String sort) {
        
        Sort.Direction direcao;

        if (sort.equals("asc")){
            direcao = Sort.Direction.ASC;
        } else if (sort.equals("desc")) {
            direcao = Sort.Direction.DESC;
        } else {
            throw new IllegalArgumentException("Ordenação inválida. Utilize 'asc' ou 'desc'.");
        }

        Pageable paginacao = PageRequest.of(page, size, Sort.by(direcao, "valor"));

        return produtoRepository.findAll(paginacao);
    }

    public Optional<Produto> buscarProduto(int codigo) {
        return produtoRepository.findByCodigo(codigo);
    }

    @Transactional
    public Boolean deletarProduto(int codigo) {
        if (!produtoRepository.existsByCodigo(codigo)) {
            return false;
        }

        produtoRepository.deleteByCodigo(codigo);
        return true;
    }

    public Optional<Produto> atualizarProduto(Produto produtoAtualizado, int codigo) {
        return produtoRepository.findByCodigo(codigo)
    
        .map(produto -> {
            Optional<Produto> produtoExistente = produtoRepository.findByCodigo(produtoAtualizado.getCodigo());
            if (produtoExistente.isPresent() && !produtoExistente.get().getId().equals(produto.getId())){
                throw new CodigoDuplicadoException("O código inserido ja está cadastrado!");
            }
            produto.setNome(produtoAtualizado.getNome());
            produto.setValor(produtoAtualizado.getValor());
            produto.setCodigo(produtoAtualizado.getCodigo());

            return produtoRepository.save(produto);
        });
    }

}
