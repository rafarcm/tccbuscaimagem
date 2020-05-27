package br.com.tccbuscaimagem.common;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ImagemCaracteristicaId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.EAGER) 
	@JoinColumn(name="id_imagem")  
	private Imagem imagem;
	
	@ManyToOne(fetch = FetchType.EAGER) 
	@JoinColumn(name="id_caracteristica") 
	private Caracteristica caracteristica;

	public Imagem getImagem() {
		return imagem;
	}

	public void setImagem(Imagem imagem) {
		this.imagem = imagem;
	}

	public Caracteristica getCaracteristica() {
		return caracteristica;
	}

	public void setCaracteristica(Caracteristica caracteristica) {
		this.caracteristica = caracteristica;
	}

}
