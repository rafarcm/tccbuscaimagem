package br.com.tccbuscaimagem.facade;

import java.util.List;

import br.com.tccbuscaimagem.common.Imagem;

public interface ImagemFacade {

	Imagem processarImagem(final String url, final String minUrl);

	List<Imagem> buscarImagensSimilares(final String url, final Integer numRegistros);
	
	List<Imagem> listarImagensPorUrl(final String url);
	
	double recuperarDistanciaEuclidiana(final Imagem imagem1, final Imagem imagem2, final List<Integer> listExtratores);
	
	List<Imagem> recuperarKVizinhos(final List<Imagem> listaImagens, final Integer k);
}
