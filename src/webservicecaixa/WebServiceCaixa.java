/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservicecaixa;

import br.gov.caixa.sibar.HEADERBARRAMENTOTYPE;
import br.gov.caixa.sibar.manutencao_cobranca_bancaria.boleto.externo.ObjectFactory;
import br.gov.caixa.sibar.manutencao_cobranca_bancaria.boleto.externo.DadosEntradaType;
import br.gov.caixa.sibar.manutencao_cobranca_bancaria.boleto.externo.EnderecoType;
import br.gov.caixa.sibar.manutencao_cobranca_bancaria.boleto.externo.FichaCompensacaoType;
import br.gov.caixa.sibar.manutencao_cobranca_bancaria.boleto.externo.IncluiBoletoEntradaType;
import br.gov.caixa.sibar.manutencao_cobranca_bancaria.boleto.externo.JurosMoraType;
import br.gov.caixa.sibar.manutencao_cobranca_bancaria.boleto.externo.ManutencaoCobrancaBancaria_Service;
import br.gov.caixa.sibar.manutencao_cobranca_bancaria.boleto.externo.MensagensFichaCompensacaoType;
import br.gov.caixa.sibar.manutencao_cobranca_bancaria.boleto.externo.MensagensReciboPagadorType;
import br.gov.caixa.sibar.manutencao_cobranca_bancaria.boleto.externo.PagadorType;
import br.gov.caixa.sibar.manutencao_cobranca_bancaria.boleto.externo.PagamentoType;
import br.gov.caixa.sibar.manutencao_cobranca_bancaria.boleto.externo.PosVencimentoType;
import br.gov.caixa.sibar.manutencao_cobranca_bancaria.boleto.externo.ReciboPagadorType;
import br.gov.caixa.sibar.manutencao_cobranca_bancaria.boleto.externo.ServicoEntradaNegocialType;
import br.gov.caixa.sibar.manutencao_cobranca_bancaria.boleto.externo.ServicoSaidaNegocialType;
import br.gov.caixa.sibar.manutencao_cobranca_bancaria.boleto.externo.TituloEntradaType;
import java.io.BufferedReader;
import java.net.MalformedURLException;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.w3c.dom.Document;
import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.httpclient.HttpClient;
import sun.misc.BASE64Encoder;

/**
 *
 * @author kalcarvalho
 */
public class WebServiceCaixa {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ManutencaoCobrancaBancaria_Service service = new ManutencaoCobrancaBancaria_Service();
        ServicoEntradaNegocialType servicoEntrada;
        
        try {
            
            servicoEntrada = preparaTagServicoEntrada();
            ServicoSaidaNegocialType servicoSaida = service.getManutencaoCobrancaBancariaSOAP().incluiBOLETO(servicoEntrada);
            
            int i = 0;
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(WebServiceCaixa.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(WebServiceCaixa.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(WebServiceCaixa.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(WebServiceCaixa.class.getName()).log(Level.SEVERE, null, ex);
        }

//        QName qname = new QName("http://tempuri.org/", "IadWSMediaParcialservice");
//        Service ws = Service.create(url, qname);
//        QName qnamePort = new QName("http://tempuri.org/", "IadWSMediaParcialPort");
//        DLL calc = ws.getPort(qnamePort, DLL.class);
//        String xml = loadXMLtoString("xml//Teste.xml");
//        
//        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
//        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
//        String url = "https://des.barramento.caixa.gov.br/sibar/ManutencaoCobrancaBancaria/Boleto/Externo?wsdl";//url do webservice nao e a url do wsdl do webservice, repare que isto foi copia da parte vermelha da figura 1
//        MimeHeaders headers = new MimeHeaders();
//        headers.addHeader("Content-Type", "text/xml");
// 
//        MessageFactory messageFactory = MessageFactory.newInstance();
// 
//        SOAPMessage msg = messageFactory.createMessage(headers, (new ByteArrayInputStream(xml.getBytes())));
// 
//        SOAPMessage soapResponse = soapConnection.call(msg, url);
//        Document xmlRespostaARequisicao=soapResponse.getSOAPBody().getOwnerDocument();
//        System.out.println(passarXMLParaString(xmlRespostaARequisicao,4));//imprime na tela o xml de retorno.
    }

    public static ServicoEntradaNegocialType preparaTagServicoEntrada() throws NoSuchAlgorithmException, UnsupportedEncodingException, ParseException, DatatypeConfigurationException {
        ServicoEntradaNegocialType entrada = new ObjectFactory().createServicoEntradaNegocialType();
        HEADERBARRAMENTOTYPE header;
        DadosEntradaType dados;
        

        Map<String, String> map = carregaDadosBoleto();

        // Tag <sib:HEADER>
        header = preparaTagHeader(map);
        entrada.setHEADER(header);
        
        //Tag <DADOS>
        dados = preparaTagDados((HashMap) map);
        entrada.setDADOS(dados);

        return entrada;

    }

    public static HEADERBARRAMENTOTYPE preparaTagHeader(Map<String, String> map) {
        HEADERBARRAMENTOTYPE header = new HEADERBARRAMENTOTYPE();
        
        header.setVERSAO(map.get("versao"));
        header.setAUTENTICACAO(map.get("autenticacao"));
        header.setUSUARIOSERVICO(map.get("usuarioServico"));
        header.setOPERACAO(map.get("operacao"));
        header.setSISTEMAORIGEM(map.get("sistemaOrigem"));
        header.setUNIDADE(map.get("unidade"));
        header.setDATAHORA(map.get("dataHora"));
        

        return header;
    }
    
    public static DadosEntradaType preparaTagDados(Map<String, String> map) throws ParseException, DatatypeConfigurationException {
        DadosEntradaType dados = new ObjectFactory().createDadosEntradaType();
        IncluiBoletoEntradaType incluiBoleto;
        
        //Tag <INCLUI_BOLETO>
        incluiBoleto = preparaTagIncluiBoleto(map);
        
        dados.setINCLUIBOLETO(incluiBoleto);
        
        return dados;
    }
    
    public static IncluiBoletoEntradaType preparaTagIncluiBoleto(Map<String, String> map) throws ParseException, DatatypeConfigurationException {
        IncluiBoletoEntradaType incluiBoleto = new ObjectFactory().createIncluiBoletoEntradaType();
        TituloEntradaType titulo;
        
        incluiBoleto.setCODIGOBENEFICIARIO(Integer.valueOf(map.get("codigoBeneficiario")));
        
        //Prepara Tag <TITULO>
        titulo = preparaTagTitulo(map);
        
        incluiBoleto.setTITULO(titulo);
        
        
        
        return incluiBoleto;
    }

    public static TituloEntradaType preparaTagTitulo(Map<String, String> map) throws ParseException, DatatypeConfigurationException {
        TituloEntradaType titulo = new ObjectFactory().createTituloEntradaType();
        JurosMoraType juros;
        PosVencimentoType posVencimento;
        PagadorType pagador;
        FichaCompensacaoType ficha;
        ReciboPagadorType recibo;
        PagamentoType pagamento;

        Date dataVencimento = DateUtils.parseDate(map.get("dataVencimento"), "yyyy-MM-dd");
        Double valorTitulo = Double.parseDouble(map.get("valorTitulo"));
        Double valorAbatimento = Double.parseDouble(map.get("valorAbatimento"));
        short tipoEspecie = Short.valueOf(map.get("tipoEspecie"));
        short codigoMoeda = Short.valueOf(map.get("codigoMoeda"));
        Date dataEmissao = DateUtils.parseDate(map.get("dataEmissao"), "yyyy-MM-dd");

        titulo.setNOSSONUMERO(Long.parseLong(map.get("nossoNumero")));
        titulo.setNUMERODOCUMENTO(map.get("numeroDocumento"));
        titulo.setDATAVENCIMENTO(DateUtils.parseDateToXMLGregorianCalendar(dataVencimento));
        titulo.setVALOR(BigDecimal.valueOf(valorTitulo));
        titulo.setTIPOESPECIE(tipoEspecie);
        titulo.setFLAGACEITE(map.get("flagAceite"));
        titulo.setDATAEMISSAO(DateUtils.parseDateToXMLGregorianCalendar(dataEmissao));

        //Carrega tag <JUROS_MORA>
        juros = preparaTagJurosMora(map);

        titulo.setJUROSMORA(juros);
        titulo.setVALORABATIMENTO(BigDecimal.valueOf(valorAbatimento));

        //Carrega tag <POS_VENCIMENTO>
        posVencimento = preparaTagPosVencimento(map);

        titulo.setPOSVENCIMENTO(posVencimento);
        titulo.setCODIGOMOEDA(codigoMoeda);

        //Carrega tag <PAGADOR>
        pagador = preparaTagPagador(map);

        titulo.setPAGADOR(pagador);

        //Carrega tag <FICHA_COMPENSACAO>
        ficha = preparaTagFichaCompensacao(map);

        //titulo.setFICHACOMPENSACAO(ficha);

        //Carrega tag <RECIBO_PAGADOR>
        recibo = preparaTagReciboPagador(map);

        //titulo.setRECIBOPAGADOR(recibo);

        //Carrega tag <PAGAMENTO>
        pagamento = preparaTagPagamento(map);

        titulo.setPAGAMENTO(pagamento);

        return titulo;
    }

    public static JurosMoraType preparaTagJurosMora(Map<String, String> map) {
        JurosMoraType juros = new ObjectFactory().createJurosMoraType();
        Double valor = Double.parseDouble(map.get("valorJurosMora"));

        juros.setTIPO(map.get("tipoJurosMora"));
        juros.setVALOR(BigDecimal.valueOf(valor));

        return juros;
    }

    public static PosVencimentoType preparaTagPosVencimento(Map<String, String> map) {
        PosVencimentoType posVencimento = new ObjectFactory().createPosVencimentoType();
        short numeroDias = Short.valueOf(map.get("numeroDias"));

        posVencimento.setACAO(map.get("acaoPosVencimento"));
        posVencimento.setNUMERODIAS(numeroDias);

        return posVencimento;

    }

    public static PagadorType preparaTagPagador(Map<String, String> map) {
        PagadorType pagador = new ObjectFactory().createPagadorType();
        EnderecoType endereco;

        pagador.setCPF(Long.parseLong(map.get("cpf")));
        pagador.setNOME(map.get("nome"));

        //Carrega tag <ENDERECO>
        endereco = preparaTagEndereco(map);

        pagador.setENDERECO(endereco);

        return pagador;
    }

    public static EnderecoType preparaTagEndereco(Map<String, String> map) {
        EnderecoType endereco = new ObjectFactory().createEnderecoType();

        endereco.setLOGRADOURO(map.get("logradouro"));
        endereco.setBAIRRO(map.get("bairro"));
        endereco.setCIDADE(map.get("cidade"));
        endereco.setUF(map.get("uf"));
        endereco.setCEP(Integer.valueOf(map.get("cep")));

        return endereco;
    }

    public static FichaCompensacaoType preparaTagFichaCompensacao(Map<String, String> map) {
        FichaCompensacaoType ficha = new ObjectFactory().createFichaCompensacaoType();
        MensagensFichaCompensacaoType mensagem = new ObjectFactory().createMensagensFichaCompensacaoType();

        //Não existe set para colocar texto nas mensagens. Enviando como NULL pois não é obrigatório.
        ficha.setMENSAGENS(mensagem);

        return ficha;
    }

    public static ReciboPagadorType preparaTagReciboPagador(Map<String, String> map) {
        ReciboPagadorType recibo = new ObjectFactory().createReciboPagadorType();
        MensagensReciboPagadorType mensagem = new ObjectFactory().createMensagensReciboPagadorType();

        //Não existe set para colocar texto nas mensagens. Enviando como NULL pois não é obrigatório.
        recibo.setMENSAGENS(mensagem);

        return recibo;
    }

    public static PagamentoType preparaTagPagamento(Map<String, String> map) {
        PagamentoType pagamento = new ObjectFactory().createPagamentoType();
        short quantidadePermitida = Short.valueOf(map.get("quantidadePermitida"));
        Double minimo = Double.parseDouble(map.get("valorMinimo"));
        Double maximo = Double.parseDouble(map.get("valorMaximo"));

        pagamento.setQUANTIDADEPERMITIDA(quantidadePermitida);
        pagamento.setTIPO(map.get("tipoPagamento"));
        pagamento.setVALORMINIMO(BigDecimal.valueOf(minimo));
        pagamento.setVALORMAXIMO(BigDecimal.valueOf(maximo));

        return pagamento;
    }

    public static HashMap carregaDadosBoleto() throws NoSuchAlgorithmException, UnsupportedEncodingException, ParseException {
        Map<String, String> map = new HashMap<>();

        String codigoBeneficiario = "9999999";
        String nossoNumero = "99999999999999999";
        String vencimento = "ddmmyyyy";
        String valor = "000000000085000";
        String documento = "999999999999999"; // cnpj

        String dataEmissao = DateUtils.getCurrentTime("yyyy-MM-dd");
        String dataVencimento = DateUtils.parseDate("30/04/2018", "dd/MM/yyyy", "yyyy-MM-dd");

        String autenticacao = doHash64(codigoBeneficiario + nossoNumero + vencimento + valor + documento);

        //Serviço Entrada - Header
        map.put("versao", "1.0");
        map.put("autenticacao", autenticacao);
        map.put("usuarioServico", "XXXXXXXX");
        map.put("operacao", "INCLUI_BOLETO");
        map.put("sistemaOrigem", "SIGCB");
        map.put("unidade", "0004");
        map.put("dataHora", DateUtils.getTime());

        // Serviço Entrada - Dados - Inclui Boleto
        map.put("codigoBeneficiario", codigoBeneficiario);

        // Serviço Entrada - Dados - Inclui Boleto - Título
        map.put("nossoNumero", nossoNumero);
        map.put("numeroDocumento", "00000000000");
        map.put("dataVencimento", dataVencimento);
        map.put("valorTitulo", "0.00");
        map.put("tipoEspecie", "99");
        map.put("flagAceite", "S");
        map.put("dataEmissao", dataEmissao);
        map.put("valorAbatimento", "0");
        map.put("codigoMoeda", "09");

        // Serviço Entrada - Dados - Inclui Boleto - Título - Juros Mora
        map.put("tipoJurosMora", "ISENTO");
        map.put("valorJurosMora", "0.00");

        // Serviço Entrada - Dados - Inclui Boleto - Título - Pos Vencimento
        map.put("acaoPosVencimento", "DEVOLVER");
        map.put("numeroDias", "0");

        // Serviço Entrada - Dados - Inclui Boleto - Título - Pagador
        map.put("cpf", "00000000000");
        map.put("nome", "TESTE PAGADOR");

        // Serviço Entrada - Dados - Inclui Boleto - Título - Pagador - Endereço
        map.put("logradouro", "LOGRADOURO_PAGADOR");
        map.put("bairro", "BAIRRO_PAGADOR");
        map.put("cidade", "CIDADE_PAGADOR");
        map.put("uf", "UF");
        map.put("cep", "99999999");

        // Serviço Entrada - Dados - Inclui Boleto - Título - Ficha Compensação - Mensagens
        map.put("fichaMensagem1", "TESTE DE INCLUSAO WEBSERVICE 1");
        map.put("fichaMensagem2", "TESTE DE INCLUSAO WEBSERVICE 2");

        // Serviço Entrada - Dados - Inclui Boleto - Título - Recibo Pagador - Mensagens
        map.put("reciboMensagem1", "TESTE DE INCLUSAO WS MSG PAG 1");
        map.put("reciboMensagem2", "TESTE DE INCLUSAO WS MSG PAG 2");
        map.put("reciboMensagem3", "TESTE DE INCLUSAO WS MSG PAG 3");
        map.put("reciboMensagem4", "TESTE DE INCLUSAO WS MSG PAG 4");

        // Serviço Entrada - Dados - Inclui Boleto - Título - Pagamento
        map.put("quantidadePermitida", "01");
        map.put("tipoPagamento", "NAO_ACEITA_VALOR_DIVERGENTE");
        map.put("valorMinimo", "0.00");
        map.put("valorMaximo", "0.00");

        return (HashMap) map;

    }

    public static String loadXMLtoString(String path) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File(path)));
        String line;
        StringBuilder sb = new StringBuilder();

        while ((line = br.readLine()) != null) {
            sb.append(line.trim());
        }

        //
        return sb.toString();
    }

    public static String passarXMLParaString(Document xml, int espacosIdentacao) {
        try {
            //set up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            transfac.setAttribute("indent-number", espacosIdentacao);
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(xml);
            trans.transform(source, result);
            String xmlString = sw.toString();
            return xmlString;
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    public static String doHash64(String dados) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        byte[] hash;

        md = MessageDigest.getInstance("SHA-256");
        hash = md.digest(dados.getBytes("ISO8859-1"));
        BASE64Encoder enc = new BASE64Encoder();
        return enc.encode(hash);

    }

}
