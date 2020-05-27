package br.com.tcc.buscaimagem.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.tccbuscaimagem.common.Imagem;
import br.com.tccbuscaimagem.common.ImagemUtil;
import br.com.tccbuscaimagem.facade.ImagemFacade;
import br.com.tccbuscaimagem.service.ImagemProvider;

/**
 * Servlet implementation class BuscaImagemServlet
 */
public class BuscaImagemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * Default constructor.z
	 */
	public BuscaImagemServlet() {
		// TODO Auto-generated constructor stub
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		/*
		final ImagemFacade facade = new ImagemProvider();
		//facade.inserirImagem(pathURL, Integer.valueOf(codigoImagem));
		
		final List<Imagem> imagensSimilares = facade.buscarImagensSimilares(pathURL, Integer.valueOf(codigoImagem));

		final PrintWriter out = response.getWriter();
		// escreve o texto
		out.println("<html>");
		out.println("<body>");
		out.println("IMAGENS SIMILARES ENCONTRADAS<br>");
		out.println("--------------------------------------------------<br>");

	    for (Imagem imagem : imagensSimilares) {
	    	out.println(imagem.getIdImagem() + " - " + imagem.getUrl() + " - " + imagem.getDistanciaImagemBusca());
			out.println("<br>");
		}

		out.println("</body>");
		out.println("</html>");*/
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
