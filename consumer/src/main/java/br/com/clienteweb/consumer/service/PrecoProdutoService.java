package br.com.clienteweb.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.clienteweb.consumer.model.ProdutoComPreco;
import reactor.core.publisher.Mono;

@Service
public class PrecoProdutoService {

	
	@Autowired
	private WebClient webClientProdutos;
	
	@Autowired 
	private WebClient webClientPrecos;
	
	
	//utilizando o metodo obterPorCodigoParalelo recebemos o retorno das duas chamadas juntas,
	//por causa (Mono.zip) que encapsula o retorno da resposta
	public ProdutoComPreco obterPorCodigoParalelo(Long codigoProduto) {
		
		//O webClientProduto ira trazer o nome do produto e descrição
		Mono<ProdutoComPreco> monoProduto = this.webClientProdutos
				.method(HttpMethod.GET)
				.uri("/produtos/{codigo}", codigoProduto)
				.retrieve()
				.bodyToMono(ProdutoComPreco.class);
		
		//O webClientPreco ira retornar o preço do produto
		Mono<ProdutoComPreco> monoPreco = this.webClientPrecos
				.method(HttpMethod.GET)
				.uri("/precos/{codigo}", codigoProduto)
				.retrieve()
				.bodyToMono(ProdutoComPreco.class);
		
		//Abaixo é criado o Mono.zip que encapsula a resposta das duas chamadas o tuple é o retorno das respostas
		ProdutoComPreco produtoComPreco = Mono.zip(monoProduto, monoPreco).map(tuple ->
		{tuple.getT1().setPreco(tuple.getT2().getPreco());
		
		return tuple.getT1();}).block();
		
		return produtoComPreco;
	}
	
	
	public ProdutoComPreco obterPorCodigoSincrono(Long codigoProduto) {

		Mono<ProdutoComPreco> monoProduto = this.webClientProdutos
			.method(HttpMethod.GET)
			.uri("/produtos/{codigo}", codigoProduto)
			.retrieve()
			.bodyToMono(ProdutoComPreco.class);
	
		Mono<ProdutoComPreco> monoPreco = this.webClientPrecos
				.method(HttpMethod.GET)
				.uri("/precos/{codigo}", codigoProduto)
				.retrieve()
				.bodyToMono(ProdutoComPreco.class);
		
		//O .block serve para bloquear o monoPreco e o monoProduto enquanto não tiver o retorno/resposta da chamada
		ProdutoComPreco produto = monoProduto.block();
		ProdutoComPreco preco = monoPreco.block();

		produto.setPreco(preco.getPreco());		
	

		return produto;
	}
	

	
	//Criar produto através do metodo post
	public ProdutoComPreco criar(ProdutoComPreco produtoComPreco) {
		
		Mono<ProdutoComPreco> monoProduto = this.webClientProdutos
				.post()
				.uri("/produtos")
				.body(BodyInserters.fromValue(produtoComPreco))
				.retrieve()
				.bodyToMono(ProdutoComPreco.class);
		
		return monoProduto.block();
	}
	
	/*public ProdutoComPreco obterPorCodigo(Long codigoProduto) {
		
		Mono<ProdutoComPreco> monoProduto = this.webClientProduto
		.method(HttpMethod.GET)
		.uri("/produtos/{codigo}", codigoProduto)
		.retrieve()
		.bodyToMono(ProdutoComPreco.class); 
		//O Mono permite tratar o retorno sem bloquear o metodo 
		
		
		// O subscribe pega a resposta do Mono e fica escutando, ou seja espera o metodo responder
		// e assim que retornar o subscribe sera executado imediatamente 
		
		monoProduto.subscribe(p -> {
			System.out.println("Aqui sim, finalizou de verdade ");
		});
		//O p-> da expressao lambda equivale ao ProdutoComPreco pq ele esta dentro do generics do Mono
		//  (Mono<ProdutoComPreco>)
		 
		
		System.out.println("Finalizou.");
		return null;
		
	} */
}
