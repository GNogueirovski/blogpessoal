package com.generation.blogpessoal.controller;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeAll
    void start(){
        usuarioRepository.deleteAll();

        usuarioService.cadastrarUsuario(new Usuario(0L, "Root", "root@root.com", "rootroot", ""));
    }

    @Test
    @DisplayName("Cadastrar um Usuário")
    public void deveCriarUmUsuario(){

        // Monta o corpo da requisição - tal qual o insomnia
        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, "Paulo Antunes", "paulo_antunes@email.com.br", "12345678", "-"));

        // Executa a requisição e guarda a resposta
        ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

        // verifica se o retorno da requisição era o que esperavamos.
        assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());

    }

    @Test
    @DisplayName("Não deve permitir duplicação do usuário")
    public void naoDeveDuplicarUsuario(){

        usuarioService.cadastrarUsuario(new Usuario(0L, "Maria Marta", "mariamariazinha@maria.com.br", "12345678", "-"));

        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, "Maria Marta", "mariamariazinha@maria.com.br", "12345678", "-"));

        ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());

    }

    @Test
    @DisplayName("Atualizar um Usuário")
    public void deveAtualizarUmUsuario(){

        Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, "João Joana", "jojoa@joao.com.br", "12345678", "-"));

        Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(), "João Joana", "jojoa@joao.com.br", "12345678", "-");

        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);

        ResponseEntity<Usuario> corpoResposta = testRestTemplate.withBasicAuth("root@root.com", "rootroot").exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());

    }

    @Test
    @DisplayName("Listar todos os Usuários")
    public void deveMostrarTodosUsuarios(){

        usuarioService.cadastrarUsuario(new Usuario(0L,
        		"Sabrina Sanches", "sabrina_sanches@email.com.br", "12345678", "-"));
        
        usuarioService.cadastrarUsuario(new Usuario(0L,
        		"Ricardo Marques", "ricardo_marques@email.com.br", "12345678", "-"));
        
        ResponseEntity<String> corpoResposta = testRestTemplate
        		.withBasicAuth("root@root.com", "rootroot")
        		.exchange("/usuarios/all", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());

    }



}
