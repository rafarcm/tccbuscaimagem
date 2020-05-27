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

@Entity
@Table(name="caracteristica")
public class Caracteristica implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_caracteristica", nullable = true, length=10)
	private Integer idCaracteristica;
	
	@Column(name = "nome", nullable = true, length=200)
	private String nome;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="imagem_caracteristica", schema="anotacoes",
			joinColumns=@JoinColumn(name="id_caracteristica"),
			inverseJoinColumns=@JoinColumn(name="id_imagem"))
	private Collection<Imagem> imagens;

	public Integer getIdCaracteristica() {
		return idCaracteristica;
	}

	public void setIdCaracteristica(Integer idCaracteristica) {
		this.idCaracteristica = idCaracteristica;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Collection<Imagem> getImagens() {
		return imagens;
	}

	public void setImagens(Collection<Imagem> imagens) {
		this.imagens = imagens;
	}

}
