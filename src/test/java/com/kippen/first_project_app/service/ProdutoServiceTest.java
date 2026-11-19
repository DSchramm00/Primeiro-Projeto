package com.kippen.first_project_app.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;

import com.kippen.first_project_app.ApiProduto.Produto;
import com.kippen.first_project_app.exception.CodigoDuplicadoException;
import com.kippen.first_project_app.repository.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;  
    
    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void deveAparecerExceptionCodigoDuplicado(){
        Produto produto = new Produto("Refrigerante", 50, 123);

        when(produtoRepository.existsByCodigo(123))
            .thenReturn(true);

        assertThrows(
            CodigoDuplicadoException.class,
            () -> produtoService.criarProduto(produto)
        );
        verify(produtoRepository,never()).save(produto);
    }

    @Test
    void deveCadastrarProdutoQuandoCodigoNaoExiste(){
        Produto produto = new Produto("Chocolate", 22, 13);

        when(produtoRepository.existsByCodigo(13))
            .thenReturn(false);

        when(produtoRepository.save(produto))
            .thenReturn(produto);

        Produto resposta = produtoService.criarProduto(produto);

        verify(produtoRepository).save(produto);

        assertEquals(produto, resposta);
    }

    @Test
    void deveRetornarExceptionSeOBancoFalhar(){
        Produto produto = new Produto("Feijão", 41, 22);

        when(produtoRepository.existsByCodigo(22))
            .thenReturn(false);

        when(produtoRepository.save(produto))
            .thenThrow(new DataAccessResourceFailureException("falha no banco!"));

        DataAccessResourceFailureException resposta = assertThrows(
            DataAccessResourceFailureException.class,
            () -> produtoService.criarProduto(produto)
        );

        String resultado = resposta.getMessage();

        assertEquals("falha no banco!", resultado);

        verify(produtoRepository).save(produto);
    }

    @Test 
    void QuandoProdutoForEncotrado(){
        Produto produto = new Produto("Parafal", 780, 157);
        
        when(produtoRepository.findByCodigo(157))
            .thenReturn(Optional.of(produto));

        Optional<Produto> resultado = produtoService.buscarProduto(157); 

        Produto produtoEncontrado = resultado.get();
        
        assertEquals(produto, produtoEncontrado);

        verify(produtoRepository, times(1)).findByCodigo(157);
    }

    @Test
    void QuandoProdutoNaoForEncontrado(){

        when(produtoRepository.findByCodigo(762))
            .thenReturn(Optional.empty());

        Optional<Produto> resultado = produtoService.buscarProduto(762);
        
        assertFalse(resultado.isPresent());

        verify(produtoRepository).findByCodigo(762);
    }

    @Test
    void DeveGerarExcecaoSeDerErroNaBusca(){

        when(produtoRepository.findByCodigo(01))
            .thenThrow(new DataAccessResourceFailureException("falha na busca!"));

        assertThrows(
            DataAccessResourceFailureException.class,
            () -> produtoService.buscarProduto(01)
        );
    }

    @Test
    void TestarRetornoRealDoService(){
        Produto produto = new Produto("Pizza", 25, 99);

        Produto produtoSalvo = new Produto("Pizza", 25, 99);

        when(produtoRepository.existsByCodigo(99)).thenReturn(false);

        when(produtoRepository.save(produto)).thenReturn(produtoSalvo);

        Produto resposta = produtoService.criarProduto(produto);

        assertSame(produtoSalvo, resposta);
    }

    @Test
    void VerificarRetornoInternoCriarProduto(){
        Produto produto = new Produto("Fan 160", 21000, 157);

        when(produtoRepository.existsByCodigo(157))
            .thenReturn(false);

        produtoService.criarProduto(produto);            

        ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);

        verify(produtoRepository).save(captor.capture());

        Produto capturado = captor.getValue();

        assertAll(
            () -> assertEquals("Fan 160", capturado.getNome()),
            () -> assertEquals(21000, capturado.getValor()),
            () -> assertEquals(157, capturado.getCodigo())
        );
    }

    @Test 
    void ProdutoNaoEncontradoDelete(){
        when(produtoRepository.existsByCodigo(47))
            .thenReturn(false);

        Boolean resposta = produtoService.deletarProduto(47);  

        assertFalse(resposta);
            
        verify(produtoRepository, never()).deleteByCodigo(47);

        verify(produtoRepository, times(1)).existsByCodigo(47);
    }

    @Test
    void VerificarSeProdutoFoiDeletado(){
        when(produtoRepository.existsByCodigo(157))
            .thenReturn(true);

        Boolean resposta = produtoService.deletarProduto(157);

        assertTrue(resposta);

        verify(produtoRepository, times(1)).deleteByCodigo(157);
        verify(produtoRepository, times(1)).existsByCodigo(157);
    }

    @Test
    void FalhaNoBancoDuranteDelete(){
        when(produtoRepository.existsByCodigo(99))
            .thenThrow(new DataAccessResourceFailureException("Falha no banco!"));

        assertThrows(
            DataAccessResourceFailureException.class,
            () -> produtoService.deletarProduto(99));
            
        verify(produtoRepository, never()).deleteByCodigo(99);    
        verify(produtoRepository, times(1)).existsByCodigo(99);    
    }
}
