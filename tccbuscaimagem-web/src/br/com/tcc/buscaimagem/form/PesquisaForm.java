package br.com.tcc.buscaimagem.form;

import java.util.List;

public class PesquisaForm {
	
	private String[] urls;
	private String[] minUrls;
	
	private String[] extratores;

	private String imagemPesquisa;
	
	private String numeroResultados;
	
	private List<String> imagensSimilares;
	
	public String[] getExtratores() {
		return extratores;
	}
	public void setExtratores(String[] extratores) {
		this.extratores = extratores;
	}
	
	public String[] getUrls() {
		return urls;
	}
	public void setUrls(String[] urls) {
		this.urls = urls;
	}
	public String[] getMinUrls() {
		return minUrls;
	}
	public void setMinUrls(String[] minUrls) {
		this.minUrls = minUrls;
	}
	public String getImagemPesquisa() {
		return imagemPesquisa;
	}
	public void setImagemPesquisa(String imagemPesquisa) {
		this.imagemPesquisa = imagemPesquisa;
	}
	public List<String> getImagensSimilares() {
		return imagensSimilares;
	}
	public void setImagensSimilares(List<String> imagensSimilares) {
		this.imagensSimilares = imagensSimilares;
	}
	public String getNumeroResultados() {
		return numeroResultados;
	}
	public void setNumeroResultados(String numeroResultados) {
		this.numeroResultados = numeroResultados;
	}
}
