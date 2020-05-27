package br.com.tccbuscaimagem.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.tccbuscaimagem.common.Imagem;

public class ImagemDataManager extends Dao<Imagem> {

	private static Logger logger = Logger.getLogger(Dao.class);
	
	public ImagemDataManager(Session session) {
		super(session, Imagem.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Imagem> listarImagensPorUrl(String url){
		logger.info("[*]Lendo Imagens com url " + url);
				
		final Criteria criteria = getSession().createCriteria(Imagem.class);    
		
		criteria.add(Restrictions.eq("url", url));
		
		return criteria.list();
	}
}
