package br.com.controlepedidos.models;

import java.math.BigDecimal;

public class Desconto {

	private BigDecimal valor;

	public Desconto(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal descontoPorQuantidade(int quantidade) {
		if (quantidade > 5 && quantidade <= 10) {
			return descontoMaisDeCincoItens();
		} else if (quantidade > 10) {
			return descontoMaisDeDezItens();
		}

		return new BigDecimal(0);
	}

	private BigDecimal descontoMaisDeCincoItens() {
		BigDecimal divisor = new BigDecimal(100);
		BigDecimal multiplicador = new BigDecimal(5);
		BigDecimal valorDesconto = valor.subtract((valor.divide(divisor)).multiply(multiplicador));

		return valorDesconto;
	}

	private BigDecimal descontoMaisDeDezItens() {
		BigDecimal divisor = new BigDecimal(100);
		BigDecimal multiplicador = new BigDecimal(10);
		BigDecimal valorDesconto = valor.subtract((valor.divide(divisor)).multiply(multiplicador));

		return valorDesconto;
	}

}
