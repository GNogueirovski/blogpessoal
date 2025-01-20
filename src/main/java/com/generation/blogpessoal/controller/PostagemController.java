package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController 								// Define ao Spring que essa classe é uma Controller
@RequestMapping("/postagens") 				   //Define qual endpoint vai ser tratado por essa classe
@CrossOrigin(origins="*", allowedHeaders="*") // Libera o acesso a qualquer front-end que for designado. Essencial para consumação da APi pelo front/insomnia
public class PostagemController{

	@Autowired // O Spring dá autonomia para a Interface poder invocar os métodos
	private PostagemRepository postagemRepository;
	
	@Autowired
	private TemaRepository temaRepository;
	
	@GetMapping // indica que esse método é chamado em Verbos/Métodos HTTP do tipo GET.
	public ResponseEntity<List<Postagem>> getAll(){
		return ResponseEntity.ok(postagemRepository.findAll());
	}

	@GetMapping("/{id}") // Indica que esse método recebe um ID que é variavel
	public ResponseEntity<Postagem> getByID(@PathVariable Long id){ // Deixa explicito através da anotação que o ID é o Long id
		return postagemRepository.findById(id) //Optional retornado pelo JPA que usamos o .MAP se tiver o id para dar a resposta ok e o orelsethrow para lançar excessão
				.map(postagem -> ResponseEntity.status(HttpStatus.OK).body(postagem))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado o registro"));
	}

	@GetMapping("/titulo/{titulo}") // indica que o método recebera um titulo
	public ResponseEntity <List<Postagem>> getByTitulo(@PathVariable String titulo) { // indica o caminho do titulo que sera recebido
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo)); //Retorna um response entity com o método personalizado de procura.
	}


	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) {//Checa preenchimento dos campos do bd e só ira passar o objeto do tipo postagem
		if (temaRepository.existsById(postagem.getTema().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));// salva a postagem através do método do jpa repository
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe", null);
	}

	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) { // Checa preenchimento e so passa objeto do tipo postagem
		if (postagemRepository.existsById(postagem.getId())) {
			
			if(temaRepository.existsById(postagem.getTema().getId()))
				return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe", null);
		} return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

	}
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping ("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Postagem> postagem = postagemRepository.findById(id); // Mesma função dos optionals retornados acima só que dessa fez explicitando e utilizando if
		if (postagem.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado o registro");
		}
		postagemRepository.deleteById(id);
	}
}
