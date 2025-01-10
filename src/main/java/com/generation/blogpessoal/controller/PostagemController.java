package com.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

@RestController 								// Define ao Spring que essa classe é uma Controller
@RequestMapping("/postagens") 				   //Define qual endpoint vai ser tratado por essa classe
@CrossOrigin(origins="*", allowedHeaders="*") // Libera o acesso a qualquer front-end que for designado. Essencial para consumação da APi pelo front/insomnia
public class PostagemController{

	@Autowired // O Spring dá autonomia para a Interface poder invocar os métodos
	private PostagemRepository postagemRepository;
	
	@GetMapping // indica que esse método é chamado em Verbos/Métodos HTTP do tipo GET.
	public ResponseEntity<List<Postagem>> getAll(){
		return ResponseEntity.ok(postagemRepository.findAll());
		

	}
}
