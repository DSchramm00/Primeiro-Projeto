package com.kippen.first_project_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kippen.first_project_app.ApiProduto.Produto;

@Repository 
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByCodigo(int codigo);

    void deleteByCodigo(int codigo);

    boolean existsByCodigo(int codigo);
}
