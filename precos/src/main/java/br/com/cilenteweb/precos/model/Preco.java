package br.com.cilenteweb.precos.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Preco {
	
	private Long codigoProduto;
	private BigDecimal preco;
	
	public Preco (Long cp, BigDecimal p) {
		this.codigoProduto = cp;
		this.preco = p;
	}

}
