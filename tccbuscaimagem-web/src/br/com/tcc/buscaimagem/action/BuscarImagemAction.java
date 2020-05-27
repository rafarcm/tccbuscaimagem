package br.com.tcc.buscaimagem.action;

import java.util.ArrayList;
import java.util.List;

import br.com.tcc.buscaimagem.form.PesquisaForm;
import br.com.tccbuscaimagem.common.Imagem;
import br.com.tccbuscaimagem.facade.ImagemFacade;
import br.com.tccbuscaimagem.service.ImagemProvider;

import com.opensymphony.xwork2.ActionSupport;

public class BuscarImagemAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;

	private PesquisaForm urlsForm;
	
	private PesquisaForm resultados;

	public String execute() throws Exception {
		
		final ImagemFacade facade = new ImagemProvider();
		resultados = new PesquisaForm();
		List<Imagem> listImagens = new ArrayList<Imagem>();
		Imagem imagemBusca = null;
		
		for (int i = 0; urlsForm.getUrls() != null && i < urlsForm.getUrls().length; i++) {

			final String url = urlsForm.getUrls()[i];
			final String minUrl = urlsForm.getMinUrls()[i];
			
			Imagem imagemRetorno = facade.processarImagem(url, minUrl);

			listImagens.add(imagemRetorno);
			if(urlsForm.getImagemPesquisa().equals(minUrl)){
				imagemBusca = imagemRetorno;
			}
		}
		
		List<Integer> listExtratores = new ArrayList<Integer>();
		
		for (int i = 0; i < urlsForm.getExtratores().length; i++) {
			listExtratores.add(Integer.valueOf(urlsForm.getExtratores()[i]));
		}

		for (Imagem imagem : listImagens) {
			imagem.setDistanciaImagemBusca(facade.recuperarDistanciaEuclidiana(imagemBusca, imagem, listExtratores));
		}
		
		resultados.setImagemPesquisa(urlsForm.getImagemPesquisa());
		
		List<Imagem> listImagensSimilares = facade.recuperarKVizinhos(listImagens, Integer.valueOf(urlsForm.getNumeroResultados()));
		
		resultados.setImagensSimilares(new ArrayList<String>());
		
		for (Imagem imagem : listImagensSimilares) {
			resultados.getImagensSimilares().add(imagem.getMinUrl());
		}
		return SUCCESS;
	}

	public PesquisaForm getUrlsForm() {
		return urlsForm;
	}

	public void setUrlsForm(PesquisaForm urlsForm) {
		this.urlsForm = urlsForm;
	}

	public PesquisaForm getResultados() {
		return resultados;
	}

	public void setResultados(PesquisaForm resultados) {
		this.resultados = resultados;
	}
	
	

}
