package br.com.tccbuscaimagem.util;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {
	private static Logger logger = Logger.getLogger(HibernateUtil.class);
	private static SessionFactory sessionFactory;
	private static ThreadLocal<Session> sessions = new ThreadLocal<Session>();
	
	static {
		sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
	}
	
	public static Session openSession(){
		logger.info("======== Entrou no HibernateUtil.openSession() ========");
		
		if(sessions.get() != null){
			logger.error("[*]Existem sessions que não foram fechadas");
			logger.error("[*]Sera feito rollback nessas sessions");
			final Transaction transaction = sessions.get().beginTransaction();  
			
			transaction.rollback();
			sessions.get().close();
			sessions.set(null);
		}
		
		logger.error("[*]Recuperando uma nova session");
		sessions.set(sessionFactory.openSession());
		
		logger.info("======== Saiu do HibernateUtil.openSession() ========");
		return sessions.get();
	}
	
	public static void closeCurrentSession(){
		sessions.get().close();
		sessions.set(null);
	}
	
	public static Session currentSession(){
		return sessions.get();
	}
}
