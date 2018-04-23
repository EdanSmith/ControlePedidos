package br.com.controlepedidos.beans;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.faces.bean.ManagedBean;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import br.com.controlepedidos.models.Pedido;

@ManagedBean
public class PedidoBean {
	String arquivoTesteJson;
	int numero;

	public String getArquivoTesteJson() {
		//System.out.println("Recuperando valor arquivo: " + arquivoTesteJson);
		return arquivoTesteJson;
	}

	public void setArquivoTesteJson(String arquivoTesteJson) {
		//System.out.println("Setando valor arquivo: " + arquivoTesteJson);
		this.arquivoTesteJson = arquivoTesteJson;
	}

	public int getNumero() {
		//System.out.println("Recuperando valor numero: " + numero);
		return numero;
	}

	public void setNumero(int numero) {
		//System.out.println("Setando valor numero: " + numero);
		this.numero = numero;
	}

	public void salvarPedidosTeste() {
		//System.out.println("Entrou no metodo" + this.arquivoTesteJson);
		//System.out.println("Numero: " + this.numero);
		
		Pedido pedido1 = new Pedido();
		Pedido pedido2 = new Pedido();

		pedido1.setIdCliente(5);
		pedido1.setNumeroControle(5);
		pedido1.setNomeProduto("produto5");
		pedido1.setValorProduto(new BigDecimal(50.0));
		pedido1.setQuantidadeProduto(20);
		pedido1.setDataCriacao(Calendar.getInstance());
		try {
			pedido1.gravarPedidosFromJson(new JsonParser().parse(pedido1.toJson()));
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// JsonParser jp = new JsonParser();
		// JsonElement root = jp.parse(arquivoTesteJson);
		//
		// if (root instanceof JsonObject) {
		// JsonObject rootObj = root.getAsJsonObject();
		// arquivoTesteJson = rootObj.toString();
		// } else if (root instanceof JsonArray) {
		// JsonArray rootArray = root.getAsJsonArray();
		// arquivoTesteJson = rootArray.get(0).toString();
		// } else {
		// arquivoTesteJson = "Erro pegando o ajax";
		// }

		// System.out.println(arquivoTesteJson);
	}

	// public void salvarPedidos() {
	// String sURL = "http://localhost:8080/PersonalTest1/resources/loja/pedido"; //
	// just a string
	//
	// // Connect to the URL using java's native library
	// URL url;
	// try {
	// url = new URL(sURL);
	//
	// HttpURLConnection request = (HttpURLConnection) url.openConnection();
	// request.connect();
	//
	// // Convert to a JSON object to print data
	// JsonParser jp = new JsonParser(); // from gson
	// JsonElement root = jp.parse(new InputStreamReader((InputStream)
	// request.getContent())); // Convert the input
	// // stream to a json
	// // element
	// if (root instanceof JsonObject) {
	// JsonObject rootObj = root.getAsJsonObject(); // May be an array, may be an
	// object.
	// arquivoTesteJson = rootObj.toString();
	// } else if (root instanceof JsonArray) {
	// JsonArray rootArray = root.getAsJsonArray();
	// arquivoTesteJson = rootArray.get(0).toString(); // Add um for depois pra
	// fazer o que precisa
	// } else {
	// arquivoTesteJson = "Erro pegando o ajax";
	// }
	//
	// // testeAjax = rootobj.get("zip_code").getAsString(); //just grab the zipcode
	// } catch (MalformedURLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
}
