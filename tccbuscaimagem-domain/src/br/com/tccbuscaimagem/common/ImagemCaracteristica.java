package br.com.tccbuscaimagem.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="imagem_caracteristica")
public class ImagemCaracteristica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private ImagemCaracteristicaId id;
	
	@Column(name = "valor", nullable = true, length=10)
	private Double valor;

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public ImagemCaracteristicaId getId() {
		return id;
	}

	public void setId(ImagemCaracteristicaId id) {
		this.id = id;
	}
}
