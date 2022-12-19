package br.com.adamis;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import br.com.adamis.enuns.TypeEnvio;
import br.com.adamis.filters.DadosEnderecos;
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
	
	
	
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		DadosEnderecos dadosEnderecos = new DadosEnderecos();
        dadosEnderecos.setnCdEmpresa("");
        dadosEnderecos.setsDsSenha("");
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        
        dadosEnderecos.setsDtCalculo(calendar.getTime());
        dadosEnderecos.setnCdServico(TypeEnvio.PAC_Varejo);
        dadosEnderecos.setsCepOrigem(38046622);
        dadosEnderecos.setsCepDestino(38195000);
        dadosEnderecos.setnVlPeso(1);
        dadosEnderecos.setnCdFormato(1);
        dadosEnderecos.setnVlComprimento(20);
        dadosEnderecos.setnVlAltura(5);
        dadosEnderecos.setnVlLargura(15);
        dadosEnderecos.setnVlDiametro(0);
        dadosEnderecos.setsCdMaoPropria("n");
        dadosEnderecos.setnVlValorDeclarado(100);
        dadosEnderecos.setsCdAvisoRecebimento("n");

        NodeList callRequest = callRequest(StaticUtils.URL_PRAZO_DATA, dadosEnderecos);
        
        //PrecoPrazoData precoPrazoData = callPrecoPrazoData(StaticUtils.URL_PRECO_PRAZO_DATA, dadosEnderecos);
        //System.err.println(""+precoPrazoData.getValor());

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
		PrazoMaximo convertToPrazoMaximo = convertToPrazoMaximo(callRequest);
        return convertToPrazoMaximo;
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
		PrecoPrazoData convertToPrecoPrazoData = convertToPrecoPrazoData(callRequest);
        return convertToPrecoPrazoData;
	}
	
	private static NodeList callRequest(String urlApi, DadosEnderecos dadosEnderecos) throws Exception {
		URL url = new URL(urlApi);
        Map<String,Object> params = new LinkedHashMap<>();
        
        ObjectMapper oMapper = new ObjectMapper();
        params = oMapper.convertValue(dadosEnderecos, Map.class);
        System.err.println("---------------------------------");
        System.err.println("PARAMETROS> "+params.toString());
        System.err.println("---------------------------------");
        
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
        		System.err.println(
        				childNodesAtributos.item(i).getNodeName()
        				+ "Valor> "+ childNodesAtributos.item(i).getTextContent()
        		);
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
					} catch (DOMException e) {						
						e.printStackTrace();
					} catch (ParseException e) {						
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
        		
        		System.err.println(
        				childNodesAtributos.item(i).getNodeName()
        				+ "Valor> "+ childNodesAtributos.item(i).getTextContent()
        		);
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
