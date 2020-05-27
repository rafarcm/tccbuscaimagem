package br.com.tccbuscaimagem.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.tccbuscaimagem.common.ImagemCaracteristica;

public class ImagemCaracteristicaDataManager extends Dao<ImagemCaracteristica> {
	
	private static Logger logger = Logger.getLogger(Dao.class);
	
	public ImagemCaracteristicaDataManager(Session session) {
		super(session, ImagemCaracteristica.class);
	}
	
	@SuppressWarnings("unchecked")
	public ImagemCaracteristica recuperarImagemCaracteristica(Integer idImagem, Integer idCaracteristica){
		logger.info("[*]ImagemCaracteristica com idImagem " + idImagem + " e idCaracteristica " + idCaracteristica);
				
		final Criteria criteria = getSession().createCriteria(ImagemCaracteristica.class);    
		
		criteria.add(Restrictions.eq("id.imagem.idImagem", idImagem));
		criteria.add(Restrictions.eq("id.caracteristica.idCaracteristica", idCaracteristica));
		
		return (ImagemCaracteristica)criteria.uniqueResult();
	}
}
