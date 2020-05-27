package br.com.tcc.buscaimagem.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import br.com.tccbuscaimagem.util.HibernateUtil;

public class HibernateSessionFilter implements Filter {
	private static Logger logger = Logger.getLogger(HibernateSessionFilter.class);

	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
		logger.info("======== Entro no HibernateSessionFilter ========");
		logger.info("[*]Abrindo a session");
		HibernateUtil.openSession();
		try{
			logger.info("[*]Recuperando a transacao");
			HibernateUtil.currentSession().beginTransaction();
			fc.doFilter(req, res);
			logger.info("[*]Commit na transacao");
			HibernateUtil.currentSession().getTransaction().commit();
		}catch (Exception e) {
			logger.error("[*]Houve um erro no Filter do HibernateUtil");
			logger.error("[*]Sera feito rollback da transacao corrente");
			HibernateUtil.currentSession().getTransaction().rollback();
			throw new ServletException(e);
		}finally{
			logger.info("[*]Fechando a session");
			HibernateUtil.closeCurrentSession();
		}
		logger.info("======== Saindo do HibernateSessionFilter ========");
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}
