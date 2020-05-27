package br.com.tccbuscaimagem.common;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="imagem")
public class Imagem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_imagem", nullable = true, length=10)
	private Integer idImagem;
	
	@Column(name = "url", nullable = true, length=200)
	private String url;
	
	@Column(name = "min_url", nullable = true, length=200)
	private String minUrl;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="imagem_caracteristica", schema="anotacoes",
			joinColumns={@JoinColumn(name="id_imagem")},
			inverseJoinColumns={@JoinColumn(name="id_caracteristica")})
	private Collection<Caracteristica> caracteristicas;

	@Transient
	private Double distanciaImagemBusca;
	
	@Transient
	private Collection<ImagemCaracteristica> imagemCaracteristicas;

	public Double getDistanciaImagemBusca() {
		return distanciaImagemBusca;
	}
	
	public void setDistanciaImagemBusca(Double distanciaImagemBusca) {
		this.distanciaImagemBusca = distanciaImagemBusca;
	}
	
	public Integer getIdImagem() {
		return idImagem;
	}
	
	public void setIdImagem(Integer idImagem) {
		this.idImagem = idImagem;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getMinUrl() {
		return minUrl;
	}
	
	public void setMinUrl(String minUrl) {
		this.minUrl = minUrl;
	}

	public Collection<Caracteristica> getCaracteristicas() {
		return caracteristicas;
	}

	public void setCaracteristicas(Collection<Caracteristica> caracteristicas) {
		this.caracteristicas = caracteristicas;
	}

	public Collection<ImagemCaracteristica> getImagemCaracteristicas() {
		return imagemCaracteristicas;
	}

	public void setImagemCaracteristicas(
			Collection<ImagemCaracteristica> imagemCaracteristicas) {
		this.imagemCaracteristicas = imagemCaracteristicas;
	}

}
