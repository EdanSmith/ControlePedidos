package br.com.controlepedidos.teste;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Test;

import com.google.gson.JsonParser;

import br.com.controlepedidos.models.Pedido;
//import junit.framework.Assert;

public class PedidoTeste {

	@Test
	public void ausenciaDeCampoObrigatorioJson() {
		Pedido pedido = new Pedido();

		// pedido.setIdCliente(5);
		pedido.setNumeroControle(5);
		pedido.setNomeProduto("produto5");
		pedido.setValorProduto(new BigDecimal(50.0));
		pedido.setQuantidadeProduto(20);
		pedido.setDataCriacao(Calendar.getInstance());

		boolean sucesso = pedido.gravarPedidosFromJson(new JsonParser().parse(pedido.toJson()));
		
		assertEquals(false, sucesso);
	}
	
	
	public void todosCamposObrigatoriosPreenchidosCorretamenteJson() {
		Pedido pedido = new Pedido();

		pedido.setIdCliente(5);
		pedido.setNumeroControle(8);
		pedido.setNomeProduto("produto5");
		pedido.setValorProduto(new BigDecimal(50.0));
		pedido.setQuantidadeProduto(20);

		boolean sucesso = pedido.gravarPedidosFromJson(new JsonParser().parse(pedido.toJson()));
		
		assertEquals(true, sucesso);
	}
}
