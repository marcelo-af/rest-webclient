package br.com.cilenteweb.produtos.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Produto {

	private Long codigo;
	private String nome;
	private String descricao;
	
	public Produto(Long c, String n, String d) {
		this.codigo = c;
		this.nome = n;
		this.descricao = d;
	} 
}
