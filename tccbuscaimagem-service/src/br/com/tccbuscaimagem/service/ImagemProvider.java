package br.com.tccbuscaimagem.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import br.com.tccbuscaimagem.common.Caracteristica;
import br.com.tccbuscaimagem.common.Imagem;
import br.com.tccbuscaimagem.common.ImagemCaracteristica;
import br.com.tccbuscaimagem.common.ImagemCaracteristicaId;
import br.com.tccbuscaimagem.dao.CaracteristicaDataManager;
import br.com.tccbuscaimagem.dao.ImagemCaracteristicaDataManager;
import br.com.tccbuscaimagem.dao.ImagemDataManager;
import br.com.tccbuscaimagem.extratores.ExtratorCor;
import br.com.tccbuscaimagem.extratores.ExtratorForma;
import br.com.tccbuscaimagem.extratores.ExtratorTextura;
import br.com.tccbuscaimagem.facade.ImagemFacade;
import br.com.tccbuscaimagem.util.Constantes;
import br.com.tccbuscaimagem.util.HibernateUtil;

/**
 * Classe com operções do sistema de busca de imagens
 * @author Rafael Mraes
 */
public class ImagemProvider implements ImagemFacade {
	private static Logger logger = Logger.getLogger(ImagemProvider.class);
	
	/**
	 * Remove o erro das classes de aceleração usadas pelo JAI
	 */
	static { 
		System.setProperty("com.sun.media.jai.disableMediaLib", "true"); 
	}
	
	/**
	 * Realiza o processamento da imagem apenas se a mesma não tiver sido processada anteriormente.
	 * Processa e insere a imagem no banco de dados.
	 * @author Rafael Mraes
	 * @param url - URL da imagem original
	 * @param minUrl - URL da imagem em miniatura do google
	 * @return Imagem
	 */
	public Imagem processarImagem(final String url, final String minUrl){
		Imagem imagemRetorno = null;
		try{
			final List<Imagem> listaImagens = listarImagensPorUrl(url);
		
			if(listaImagens == null || listaImagens.isEmpty()){
				final URL urlInst = new URL(minUrl);
				final PlanarImage imagem = JAI.create("url", urlInst);
				
				final ExtratorCor extratorCor = new ExtratorCor(imagem, 256);
				final ExtratorTextura extratorTextura = new ExtratorTextura(imagem);
				final ExtratorForma extratorForma = new ExtratorForma(imagem);
				
				final Imagem newImagem = new Imagem();
				
				newImagem.setMinUrl(minUrl);
				newImagem.setUrl(url);
				inserirImagem(newImagem);
				
				final Collection<Caracteristica> listCaracteristicas = recuperarCaracteristicas();
				
				final List<ImagemCaracteristica> listaImagenCaracteristicas = new ArrayList<ImagemCaracteristica>();
				if(listCaracteristicas != null && !listCaracteristicas.isEmpty()){
					for (final Caracteristica caracteristica : listCaracteristicas) {
						
						final ImagemCaracteristicaId id = new ImagemCaracteristicaId();
						
						final ImagemCaracteristica imagemCaracteristica = new ImagemCaracteristica();
						
						id.setCaracteristica(caracteristica);
						id.setImagem(newImagem);
						
						imagemCaracteristica.setId(id);
						
						imagemCaracteristica.setValor(
							recuperarValorcaracteristica(
									caracteristica.getIdCaracteristica(), 
									extratorCor, extratorTextura, extratorForma));
	
						inserirImagemCaracteristica(imagemCaracteristica);
						
						listaImagenCaracteristicas.add(imagemCaracteristica);

					}
					newImagem.setImagemCaracteristicas(listaImagenCaracteristicas);
					imagemRetorno = newImagem;
				}
			}else{
				final Session session = HibernateUtil.currentSession();
				
				final ImagemCaracteristicaDataManager imagemCaracteristicaDataManager = new ImagemCaracteristicaDataManager(session);

				for (Imagem imagem : listaImagens) {
					final List<ImagemCaracteristica> listaImagenCaracteristicas = new ArrayList<ImagemCaracteristica>();
					final Collection<Caracteristica> listCaracteristicas = recuperarCaracteristicas();
					
					for (Caracteristica caracteristica : listCaracteristicas) {
						final ImagemCaracteristica imagemCaracteristica = 
							imagemCaracteristicaDataManager.recuperarImagemCaracteristica(
									imagem.getIdImagem(), caracteristica.getIdCaracteristica());
						
						listaImagenCaracteristicas.add(imagemCaracteristica);
						
					}
					imagem.setImagemCaracteristicas(listaImagenCaracteristicas);
					imagemRetorno = imagem;
				}
			}
		} catch (Exception e) {
			StringBuffer msg = new StringBuffer();
			msg.append("[*]Erro ImagemProvider.processarImagem(String,String) \n");
			msg.append("	- url = ");
			msg.append(url);
			msg.append("\n");
			msg.append("	- minUrl = ");
			msg.append(minUrl);
			
			logger.error(msg.toString(), e);
		}
		
		return imagemRetorno;
	}

	public List<Imagem> buscarImagensSimilares(final String path, final Integer numRegistros){

		return null;
	}
	
	/**
	 * Insere imagem no banco de dados
	 * @author Rafael Moraes
	 * @param imagem - Imagem
	 */
	private void inserirImagem(final Imagem imagem){
		try{
			final Session session = HibernateUtil.currentSession();
	
			final ImagemDataManager imagemDataManager = new ImagemDataManager(session);

			imagemDataManager.save(imagem);

		} catch (Exception e) {
			logger.error("[*]Erro ImagemProvider.inserirImagem(Imagem) ", e);
		}
	}
	
	/**
	 * Recupera os valores de características
	 * @author Rafael Moraes
	 * @return Collection<Caracteristica>
	 */
	private Collection<Caracteristica> recuperarCaracteristicas(){
		Collection<Caracteristica> listCaracteristica = null;
		try{
			final Session session = HibernateUtil.currentSession();
	
			final CaracteristicaDataManager caracteristicaDataManager = new CaracteristicaDataManager(session);

			listCaracteristica = caracteristicaDataManager.list();

		} catch (Exception e) {
			logger.error("[*]Erro ImagemProvider.recuperarCaracteristicas() ", e);
		}
		
		return listCaracteristica;
	}
	
	/**
	 * Insere ImagemCaracteristica no banco de dados
	 * @author Rafael Moraes
	 * @param imagemCaracteristica
	 */
	private void inserirImagemCaracteristica(final ImagemCaracteristica imagemCaracteristica){
		try{
			final Session session = HibernateUtil.currentSession();
	
			final ImagemCaracteristicaDataManager imagemDataManager = new ImagemCaracteristicaDataManager(session);

			imagemDataManager.save(imagemCaracteristica);

		} catch (Exception e) {
			logger.error("[*]Erro ImagemProvider.inserirImagemCaracteristica(ImagemCaracteristica) ", e);
		}
	}
	
	/**
	 * Recupera os valores dos extratores
	 * @author Rafael Moraes
	 * @param caracteristica - Caracteristica desejada
	 * @param extratorCor - Extrator de cor
	 * @param extratorTextura - Extrator de textura
	 * @param extratorForma - Extrator de forma
	 * @return valor do extrator
	 */
	private Double recuperarValorcaracteristica(
			final Integer caracteristica,
			final ExtratorCor extratorCor,
			final ExtratorTextura extratorTextura,
			final ExtratorForma extratorForma) {
		
		Double valor = 0.0;
		switch (caracteristica.shortValue()) {
			case Constantes.COR_FRACA:
				valor = extratorCor.coresFracas();
				break;
			case Constantes.COR_MEDIA:
				valor = extratorCor.coresMedias();
				break;
			case Constantes.COR_FORTE:
				valor = extratorCor.coresFortes();
				break;
			case Constantes.AREA_PONTO:
				valor = extratorForma.transformacaoAreaPonto();
				break;
			case Constantes.RAZAO_ALT_LARG:
				valor = extratorForma.razaoAltLarg();
				break;
			case Constantes.ASSINATURAS:
				valor = extratorForma.assinaturas(2);
				break;
			case Constantes.CONTRASTE:
				valor = extratorTextura.getContraste(extratorTextura.getMatrizCoOcorrenciaDefault());
				break;
			case Constantes.INVERSO_CONTRASTE:
				valor = extratorTextura.getInversoContraste(extratorTextura.getMatrizCoOcorrenciaDefault());
				break;
			case Constantes.ENTROPIA:
				valor = extratorTextura.getEntropia(extratorTextura.getMatrizCoOcorrenciaDefault());
				break;
			case Constantes.UNIFORMIDADE:
				valor = extratorTextura.getUniformidade(extratorTextura.getMatrizCoOcorrenciaDefault());
				break;
			case Constantes.PROB_MAXIMA:
				valor = extratorTextura.getProbabilidadeMaxima(extratorTextura.getMatrizCoOcorrenciaDefault());
				break;
			default:
				break;
		}
		
		return valor;
	}

	public double recuperarDistanciaEuclidiana(final Imagem imagem1, final Imagem imagem2, final List<Integer> listExtratores){
		double somaValores = 0.0;
		for (Integer extrator : listExtratores) {

			for (ImagemCaracteristica imagemCaracteristica1 : imagem1.getImagemCaracteristicas()) {
				
				Caracteristica caracteristica1 = imagemCaracteristica1.getId().getCaracteristica();
				
				if(extrator.intValue() == caracteristica1.getIdCaracteristica().intValue()){
				
					for (ImagemCaracteristica imagemCaracteristica2 : imagem2.getImagemCaracteristicas()) {
						
						Caracteristica caracteristica2 = imagemCaracteristica2.getId().getCaracteristica();
						
						if(caracteristica2.getIdCaracteristica().intValue() == caracteristica1.getIdCaracteristica().intValue()){
							double diferenca = imagemCaracteristica1.getValor() - imagemCaracteristica2.getValor();
		
							somaValores += (diferenca * diferenca);
							break;
						}
					}
				}
			}
		}
		return Math.sqrt(somaValores);
	}
	public List<Imagem> recuperarKVizinhos(final List<Imagem> listaImagens, final Integer k){
		final List<Imagem> listRet = new ArrayList<Imagem>();
		Collections.sort (listaImagens, new Comparator<Imagem>() { 
	            public int compare(Imagem o1, Imagem o2) {  
	                Imagem imagem1 = (Imagem) o1;  
	                Imagem imagem2 = (Imagem) o2;  
	                return imagem1.getDistanciaImagemBusca() < imagem2.getDistanciaImagemBusca() ? -1 : 
	                	(imagem1.getDistanciaImagemBusca() > imagem2.getDistanciaImagemBusca() ? +1 : 0);  
	            }  
		});
		
		for (int i = 0; i < k; i++) {
			if(i < listaImagens.size()){
				listRet.add(listaImagens.get(i));
			}else{
				break;
			}
		}
		return listRet;
	}
	
	/**
	 * Recupera as imagens através da URL
	 * @author Rafael Moraes
	 * @param url - URL a ser encontrada (String)
	 * @param Lista de Imagens encontaradas
	 */
	public List<Imagem> listarImagensPorUrl(final String url){
		List<Imagem> retorno = null;
		try {
			final Session session = HibernateUtil.currentSession();
			final ImagemDataManager imagemDataManager = new ImagemDataManager(session);
			
			retorno = imagemDataManager.listarImagensPorUrl(url);
		} catch (Exception e) {
			logger.error("[*]Erro ImagemProvider.listarImagensPorUrl(String) ", e);
		}

		return retorno;
	}

}
