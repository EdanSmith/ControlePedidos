package br.com.controlepedidos.resources;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonElement;

import br.com.controlepedidos.daos.PedidoDAO;
import br.com.controlepedidos.models.Pedido;

@Consumes({ MediaType.APPLICATION_JSON })
@Path("pedido")
public class PedidoResource {

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String busca(@PathParam("id") int id) {
		Pedido pedido = new PedidoDAO().getPedido(id);
		return pedido.toJson();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response adiciona(JsonElement json) {
		boolean sucesso = new Pedido().gravarPedidosFromJson(json);
		String urlResponse = "";
		if (sucesso) {
			urlResponse = "sucesso";
		} else {
			urlResponse = "falha";
		}
		URI uri = URI.create("/pedido/" + urlResponse);
		return Response.created(uri).build();
	}
}
