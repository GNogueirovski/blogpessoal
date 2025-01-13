package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import org.springframework.web.server.ResponseStatusException;

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

	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getByID(@PathVariable Long id) {
		return postagemRepository.findById(id)
				.map(postagem -> ResponseEntity.status(HttpStatus.OK).body(postagem))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado o registro"));
	}

	@GetMapping("/titulo/{titulo}")
	public ResponseEntity <List<Postagem>> getByTitulo(@PathVariable String titulo) {
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
	}


	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) {
		return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem));
	}

	@PutMapping
	public ResponseEntity<Postagem> update(@Valid @RequestBody Postagem postagem) {
		return postagemRepository.findById(postagem.getId())
				.map(resposta -> ResponseEntity.status(HttpStatus.OK)
						.body(postagemRepository.save(postagem)))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado o registro"));

	}
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping ("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Postagem> postagem = postagemRepository.findById(id);
		if (postagem.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado o registro");
		}
		postagemRepository.deleteById(id);
	}
}
