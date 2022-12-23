package br.com.adamis;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import br.com.adamis.enuns.TypeEnvio;
import br.com.adamis.filters.DadosEnderecos;
import br.com.adamis.orm.daos.EnderecosDAO;
import br.com.adamis.orm.datasources.MySQLBuilder;
import br.com.adamis.orm.entity.Enderecos;
import br.com.adamis.responses.CepResponse;
import br.com.adamis.responses.PrazoMaximo;
import br.com.adamis.responses.PrecoPrazoData;
import br.com.adamis.utils.StaticUtils;

/**
 * 
 */

/**
 * @author Adami
 *
 */
public class Main {
		
	private static final String USER_AGENT = "Mozilla/5.0";
	
	public static void main(String[] args) throws Exception {
		
		
		MySQLBuilder builder = new MySQLBuilder();
		ConnectionSource connectionSource = builder.getConnectionSource();		
		
		EnderecosDAO enderecosDAO = new EnderecosDAO(connectionSource);
		//enderecosDAO.queryBuilder().where().eq("", enderecosDAO);
		
		List<Enderecos> queryForAll = enderecosDAO.queryForAll();
		
		Double total = 0D;
		
		for (int i = 0; i < queryForAll.size(); i++) {
					
			DadosEnderecos dadosEnderecos = new DadosEnderecos();
	        dadosEnderecos.setnCdEmpresa("");
	        dadosEnderecos.setsDsSenha("");
	        
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(new Date());
	        calendar.add(Calendar.DAY_OF_MONTH, 4);
	        
	        dadosEnderecos.setsDtCalculo(calendar.getTime());
	        dadosEnderecos.setnCdServico(TypeEnvio.PAC_Varejo);
	        dadosEnderecos.setsCepOrigem(Integer.valueOf(queryForAll.get(i).getCepOrigem()));
	        dadosEnderecos.setsCepDestino(Integer.valueOf(queryForAll.get(i).getCep()));
	        dadosEnderecos.setnVlPeso(1);
	        dadosEnderecos.setnCdFormato(1);
	        dadosEnderecos.setnVlComprimento(20);
	        dadosEnderecos.setnVlAltura(5);
	        dadosEnderecos.setnVlLargura(15);
	        dadosEnderecos.setnVlDiametro(0);
	        dadosEnderecos.setsCdMaoPropria("n");
	        dadosEnderecos.setnVlValorDeclarado(100);
	        dadosEnderecos.setsCdAvisoRecebimento("n");
	
	        PrazoMaximo prazoMaximo = callPrazoMaximo(StaticUtils.URL_PRAZO_DATA, dadosEnderecos);	        
	        PrecoPrazoData precoPrazoData = callPrecoPrazoData(StaticUtils.URL_PRECO_PRAZO_DATA, dadosEnderecos);
	        //System.err.println("cep>"+listCep.get(i)+" Data>"+prazoMaximo.getDataMaxEntrega()+" valor>"+precoPrazoData.getValor());
	        CepResponse callRequestCEP = callRequestCEP(queryForAll.get(i).getCep());
	        
	        Enderecos enderecos = queryForAll.get(i);	        
	        enderecos.setBairro(callRequestCEP.getBairro());
	        enderecos.setCidade(callRequestCEP.getLocalidade());
	        enderecos.setEstado(callRequestCEP.getUf());
	        enderecos.setLogradouro(callRequestCEP.getLogradouro());
	        enderecos.setDataEnvio(dadosEnderecos.getsDtCalculo());
	        enderecos.setDataRecebimento(prazoMaximo.getDataMaxEntrega());
	        enderecos.setValorEnvio(""+precoPrazoData.getValor());
	        
	        enderecosDAO.update(enderecos);
	        
//
//	        total += precoPrazoData.getValor();        

	        
	        
		}
		
		System.err.println("TOTAL>"+total);
	}

	/**
	 * 
	 * @param urlApi
	 * @param dadosEnderecos
	 * @return
	 * @throws Exception
	 */
	public static PrazoMaximo callPrazoMaximo(String urlApi, DadosEnderecos dadosEnderecos) throws Exception {
		NodeList callRequest = callRequest(urlApi, dadosEnderecos);
		return convertToPrazoMaximo(callRequest);        
	}
	
	/**
	 * 
	 * @param urlApi
	 * @param dadosEnderecos
	 * @return
	 * @throws Exception
	 */
	public static PrecoPrazoData callPrecoPrazoData(String urlApi, DadosEnderecos dadosEnderecos) throws Exception {
		NodeList callRequest = callRequest(urlApi, dadosEnderecos);
		return convertToPrecoPrazoData(callRequest);        
	}
	
	private static CepResponse callRequestCEP(String cep) {		
		URL url;
		CepResponse cepResponse = null;
		
		try {		
			
			url = new URL("https://viacep.com.br/ws/"+cep+"/json/");	
			
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			int responseCode = con.getResponseCode();
			//System.out.println("GET Response Code :: " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				byte[] germanBytes = response.toString().getBytes();
				String asciiEncodedString = new String(germanBytes, StandardCharsets.UTF_8);				
								
				ObjectMapper mapper = new ObjectMapper();
				cepResponse = mapper.readValue(asciiEncodedString, CepResponse.class);				
				
			} else {
				System.out.println("GET request did not work.");
			}
						
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		return cepResponse;
		
	}
	
	private static NodeList callRequest(String urlApi, DadosEnderecos dadosEnderecos) throws Exception {
		URL url = new URL(urlApi);
        Map<String,Object> params = new LinkedHashMap<>();
        
        ObjectMapper oMapper = new ObjectMapper();
        params = oMapper.convertValue(dadosEnderecos, Map.class);
                
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        String temp = "";
        
        for (int c; (c = in.read()) >= 0;) {
            //System.out.print((char)c);
            temp += (char)c;
        }
        System.out.println("---------------------------------");
        System.err.println("PARAMETROS> "+params.toString());        
        System.out.println("RESULT>> "+temp);
        System.out.println("---------------------------------");
        
        //Use method to convert XML string content to XML Document object
        Document doc = convertStringToXMLDocument( temp );
        
        Node firstChildCResultado = doc.getFirstChild();
        NodeList childNodes = firstChildCResultado.getChildNodes();
        Node firstChildServicos = null;
        Node firstChildcServico = null;
        
        //BUSCA SERVICOS
        for (int i = 0; i < childNodes.getLength(); i++) {
        	if(!childNodes.item(i).getNodeName().contains("#")) {
        		firstChildServicos = childNodes.item(i);
        	}			
		}        
        
        NodeList childNodesServicos = firstChildServicos.getChildNodes();
        
        //BUSCA CSERVICOS
        for (int i = 0; i < childNodesServicos.getLength(); i++) {
        	if(!childNodesServicos.item(i).getNodeName().contains("#")) {
        		firstChildcServico = childNodesServicos.item(i);
        	}			
		}   
        
        //TODA A LISTA DE ATRIBUTOS
        NodeList childNodesAtributos = firstChildcServico.getChildNodes();
        
        return childNodesAtributos;
        
	}
	
	private static PrazoMaximo convertToPrazoMaximo(NodeList childNodesAtributos) {
		PrazoMaximo prazoMaximo = new PrazoMaximo();
		
		for (int i = 0; i < childNodesAtributos.getLength(); i++) {
        	if(!childNodesAtributos.item(i).getNodeName().contains("#")) {
        		
        		if("Codigo".equals(childNodesAtributos.item(i).getNodeName())) {
        			prazoMaximo.setCodigo(Integer.valueOf(childNodesAtributos.item(i).getTextContent()));
        		}
        		if("PrazoEntrega".equals(childNodesAtributos.item(i).getNodeName())) {
        			prazoMaximo.setPrazoEntrega(Integer.valueOf(childNodesAtributos.item(i).getTextContent()));
        		}        		
        		if("EntregaDomiciliar".equals(childNodesAtributos.item(i).getNodeName())) {
        			prazoMaximo.setEntregaDomiciliar(childNodesAtributos.item(i).getTextContent());
        		}
        		if("EntregaSabado".equals(childNodesAtributos.item(i).getNodeName())) {
        			prazoMaximo.setEntregaSabado(childNodesAtributos.item(i).getTextContent());
        		}        		
        		if("Erro".equals(childNodesAtributos.item(i).getNodeName())) {
        			prazoMaximo.setErro(childNodesAtributos.item(i).getTextContent());
        		}
        		if("MsgErro".equals(childNodesAtributos.item(i).getNodeName())) {
        			prazoMaximo.setMsgErro(childNodesAtributos.item(i).getTextContent());
        		}
        		if("obsFim".equals(childNodesAtributos.item(i).getNodeName())) {
        			prazoMaximo.setObsFim(childNodesAtributos.item(i).getTextContent());
        		}		
        		if("DataMaxEntrega".equals(childNodesAtributos.item(i).getNodeName())) {
        			prazoMaximo.setDataMaxEntrega(childNodesAtributos.item(i).getTextContent());
        		}
//        		System.err.println(
//        				childNodesAtributos.item(i).getNodeName()
//        				+ "Valor> "+ childNodesAtributos.item(i).getTextContent()
//        		);
        	}
        }
		
		return prazoMaximo;
	}
	
	private static PrecoPrazoData convertToPrecoPrazoData(NodeList childNodesAtributos) {
		PrecoPrazoData precoPrazoData = new PrecoPrazoData();
		
		for (int i = 0; i < childNodesAtributos.getLength(); i++) {
        	if(!childNodesAtributos.item(i).getNodeName().contains("#")) {  

        		if("Codigo".equals(childNodesAtributos.item(i).getNodeName())) {
        			precoPrazoData.setCodigo(Integer.valueOf(childNodesAtributos.item(i).getTextContent()));
        		}
        		if("Valor".equals(childNodesAtributos.item(i).getNodeName())) {
        			NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        		    Number number;
        		    Double d = null;
					try {
						number = format.parse(childNodesAtributos.item(i).getTextContent());
						d = number.doubleValue();
					} catch (DOMException | ParseException e) {						
						e.printStackTrace();
					}	    
        			
        			precoPrazoData.setValor(d);
        		}
        		if("PrazoEntrega".equals(childNodesAtributos.item(i).getNodeName())) {
        			precoPrazoData.setPrazoEntrega(Integer.valueOf(childNodesAtributos.item(i).getTextContent()));
        		}        		
        		if("EntregaDomiciliar".equals(childNodesAtributos.item(i).getNodeName())) {
        			precoPrazoData.setEntregaDomiciliar(childNodesAtributos.item(i).getTextContent());
        		}
        		if("EntregaSabado".equals(childNodesAtributos.item(i).getNodeName())) {
        			precoPrazoData.setEntregaSabado(childNodesAtributos.item(i).getTextContent());
        		}        		
        		if("Erro".equals(childNodesAtributos.item(i).getNodeName())) {
        			precoPrazoData.setErro(Integer.valueOf(childNodesAtributos.item(i).getTextContent()));
        		}
        		if("MsgErro".equals(childNodesAtributos.item(i).getNodeName())) {
        			precoPrazoData.setMsgErro(childNodesAtributos.item(i).getTextContent());
        		}
        		if("obsFim".equals(childNodesAtributos.item(i).getNodeName())) {
        			precoPrazoData.setObsFim(childNodesAtributos.item(i).getTextContent());
        		}        		
        		
//        		System.err.println(
//        				childNodesAtributos.item(i).getNodeName()
//        				+ "Valor> "+ childNodesAtributos.item(i).getTextContent()
//        		);
        	}
        }
		
		return precoPrazoData;
	}
	
	private static Document convertStringToXMLDocument(String xmlString) 
	  {
	    //Parser that produces DOM object trees from XML content
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	     
	    //API to obtain DOM Document instance
	    DocumentBuilder builder = null;
	    try
	    {
	      //Create DocumentBuilder with default configuration
	      builder = factory.newDocumentBuilder();
	       
	      //Parse the content to Document object
	      Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
	      return doc;
	    } 
	    catch (Exception e) 
	    {
	      e.printStackTrace();
	    }
	    return null;
	  }
	
	
}
