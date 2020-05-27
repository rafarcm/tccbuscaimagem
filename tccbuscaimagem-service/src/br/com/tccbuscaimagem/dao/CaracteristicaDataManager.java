package br.com.tccbuscaimagem.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import br.com.tccbuscaimagem.common.Caracteristica;

public class CaracteristicaDataManager extends Dao<Caracteristica> {
	
	private static Logger logger = Logger.getLogger(Dao.class);
	
	public CaracteristicaDataManager(Session session) {
		super(session, Caracteristica.class);
	}
}
