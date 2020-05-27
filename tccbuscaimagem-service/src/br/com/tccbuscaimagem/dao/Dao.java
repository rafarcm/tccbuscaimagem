package br.com.tccbuscaimagem.dao;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.hibernate.Session;

public class Dao<T> {
	
	private static Logger logger = Logger.getLogger(Dao.class);
	private Class<T> persistentClass;
	private Session session;
	
	public Dao(Session session, Class<T> persistentClass){
		this.session = session;
		this.persistentClass = persistentClass;
	}
	
	@SuppressWarnings("unchecked")
	public T load(Integer id){
		logger.info(
			"[*]Lendo " + persistentClass + " com id " + id);
		return (T) session.load(persistentClass, id);
	}
	
	public void save(T t){
		logger.info("[*]Salvando " + t);
		session.save(t);
	}
	
	public void delete(T t){
		logger.info("[*]Removendo " + t);
		session.delete(t);
	}
	
	public void update(T t){
		logger.info("[*]Alterando " + t);
		session.update(t);
	}
	
	public void merge(T t){
		logger.info("[*]Salvando ou atualizando " + t);
		session.merge(t);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<T> list(){
		logger.info("[*]Listando todos os registros");
		return session.createCriteria(persistentClass).list();
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
}
