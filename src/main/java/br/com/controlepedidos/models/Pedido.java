package br.com.controlepedidos.models;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.controlepedidos.daos.PedidoDAO;

@Entity
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "id_cliente")
	private int idCliente; // Remover a classe cliente ou chamar por Cliente direto?

	@Column(name = "numero_controle")
	private int numeroControle;

	@Column(name = "nome_produto")
	private String nomeProduto;

	@Column(name = "valor_produto")
	private BigDecimal valorProduto;

	@Column(name = "quantidade_produto")
	private int quantidadeProduto;

	@Column(name = "data_criacao")
	@Basic(optional = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataCriacao;

	@Column(name = "valor_total")
	private BigDecimal valorTotal;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public int getNumeroControle() {
		return numeroControle;
	}

	public void setNumeroControle(int numeroControle) {
		this.numeroControle = numeroControle;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public BigDecimal getValorProduto() {
		return valorProduto;
	}

	public void setValorProduto(BigDecimal valorProduto) {
		this.valorProduto = valorProduto;
	}

	public int getQuantidadeProduto() {
		return quantidadeProduto;
	}

	public void setQuantidadeProduto(int quantidadeProduto) {
		this.quantidadeProduto = quantidadeProduto;
	}

	public Calendar getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Calendar dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	private void gravarPedidoFromJson(JsonObject json) {
		int quantidadeProduto = 1;

		this.setIdCliente(json.get("idCliente").getAsInt());
		this.setNumeroControle(json.get("numeroControle").getAsInt());
		this.setNomeProduto(json.get("nomeProduto").getAsString());
		this.setValorProduto(json.get("valorProduto").getAsBigDecimal());

		json.remove("quantidadeProduto");

		if (json.get("quantidadeProduto") != null) {
			quantidadeProduto = json.get("quantidadeProduto").getAsInt();
			if (quantidadeProduto <= 0) {
				quantidadeProduto = 1;
			}

			this.setQuantidadeProduto(quantidadeProduto);
		}

		if (!json.get("nomeProduto").getAsString().equals("") && json.get("nomeProduto") != null) { // ALTERAR PARA
																									// data_criacao NO
																									// GET
			String data = "15/04/2015 12:55:04";
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			try {
				calendar.setTime(sdf.parse(data));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.setDataCriacao(calendar);
		}

		BigDecimal valorTotal = new BigDecimal(getQuantidadeProduto()).multiply(getValorProduto());
		BigDecimal valorTotalComDesconto = new Desconto(valorTotal).descontoPorQuantidade(getQuantidadeProduto());

		this.setValorTotal(valorTotalComDesconto);
		// new PedidoDAO().gravar(this);
	}

	private boolean validacaoLimiteDezArquivosJson(JsonArray json) { // Criar Exception
		if (json.size() > 10) {
			return false;
		}

		return true;
	}

	private String validacaoNumeroControleCadastradoJson(JsonObject json) { // Criar Exception
		Pedido pedido = new Pedido();
		pedido = new PedidoDAO().getPedido(json.get("numeroControle").getAsInt());

		if (pedido != null) {
			return "Já existe um pedido com numero de controle: " + pedido.getNumeroControle();
		}

		return "";
	}

	private void validacaoCamposObrigatoriosJson(JsonObject json) throws Exception {
		String mensagemErro = "";
		boolean erro = false;

		if (json.get("idCliente") == null) {
			mensagemErro += " O campo idCliente não está no arquivo";
			erro = true;
		}
		if (json.get("numeroControle") == null) {
			mensagemErro += " O campo numeroControle não está no arquivo";
			erro = true;
		}
		if (json.get("nomeProduto") == null) {
			mensagemErro += " O campo nomeProduto não está no arquivo";
			erro = true;
		}
		if (json.get("valorProduto") == null) {
			mensagemErro += " O campo valorProduto não está no arquivo";
			erro = true;
		}

		if (erro) {
			throw new Exception(mensagemErro);
		}
	}

	private void validacaoValoresValidosJson(JsonObject json) throws Exception {
		String mensagemErro = "Os valores no arquivo enviado são inválidos";

		try {
			json.get("idCliente").getAsInt();
			json.get("numeroControle").getAsInt();
			json.get("nomeProduto").getAsString();
			json.get("valorProduto").getAsBigDecimal();

			if (json.get("quantidadeProduto") != null) {
				json.get("quantidadeProduto").getAsInt();
			}
			if (json.get("dataCriacao") != null) {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				df.setLenient(false);
				df.parse(json.get("dataCriacao").getAsString());
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(mensagemErro);
		}

		// } catch (NullPointerException e) {
		// try {
		// erro = true;
		// throw new Exception(mensagemErro, e);
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// e.printStackTrace();
		// } catch (NumberFormatException e) {
		// try {
		// erro = true;
		// throw new Exception(mensagemErro, e);
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// e.printStackTrace();
		// } catch (ParseException e) {
		// try {
		// erro = true;
		// throw new Exception(mensagemErro, e);
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// }
	}

	private void validadorDeCamposJson(JsonObject jsonObject) throws Exception {
		validacaoValoresValidosJson((JsonObject) jsonObject);
		validacaoCamposObrigatoriosJson((JsonObject) jsonObject);
		String validacaoNomeControleCadastrado = validacaoNumeroControleCadastradoJson(jsonObject);
		if (!validacaoNomeControleCadastrado.equals("")) {
			throw new Exception(validacaoNomeControleCadastrado);
		}
	}

	/**
	 * A data do json deve estar com formato "dd/MM/yyyy HH:mm:ss".
	 * 
	 * @param jsonElement
	 * @throws Exception
	 */
	public boolean gravarPedidosFromJson(JsonElement jsonElement) {
		try {

			if (jsonElement instanceof JsonObject) {
				validadorDeCamposJson(((JsonObject) jsonElement));
				gravarPedidoFromJson(jsonElement.getAsJsonObject());
				return true;

			} else if (jsonElement instanceof JsonArray) {
				JsonArray json = jsonElement.getAsJsonArray();
				if (validacaoLimiteDezArquivosJson(json)) {
					for (JsonElement jsonObject : json) {
						validadorDeCamposJson(jsonObject.getAsJsonObject());
					}
					for (JsonElement jsonObject : json) {
						gravarPedidoFromJson(jsonObject.getAsJsonObject());
					}
					return true;
				}
				throw new Exception("Limite de Arquivos excedido. Máximo Permitido: 10");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		try {
			throw new Exception("O parâmetro passado não é uma instancia de JsonObject ou JsonArray.");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public String toJson() {
		return new Gson().toJson(this);
	}

	/**
	 * Incompleto - XML
	 * 
	 * @param xml
	 */
	private void gravarPedidoFromXml(String xml) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			try {
				Document doc = (Document) db.parse("");
				doc.getDocumentElement().normalize();
				NodeList nodeList = doc.getElementsByTagName("Pedido");

				List<Pedido> pedidoList = new ArrayList<Pedido>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					// pedidoList.add();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Incompleto - XML
	 * 
	 * @param node
	 * @return
	 */
	private Pedido getPedidoFromXmlNode(Node node) {
		Pedido pedido = new Pedido();
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
			pedido.setIdCliente(Integer.parseInt(getTagValue("clienteId", element)));
			pedido.setNumeroControle(Integer.parseInt(getTagValue("numeroControle", element)));
			pedido.setNomeProduto(getTagValue("nomeProduto", element));
			pedido.setValorProduto(new BigDecimal(Integer.parseInt(getTagValue("valorProduto", element))));
		}

		return pedido;
	}

	/**
	 * XML
	 * 
	 * @param tag
	 * @param element
	 * @return
	 */
	private String getTagValue(String tag, Element element) {
		NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodeList.item(0);
		return node.getNodeValue();
	}
}
