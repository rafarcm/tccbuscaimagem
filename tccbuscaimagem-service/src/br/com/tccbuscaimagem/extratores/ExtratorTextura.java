package br.com.tccbuscaimagem.extratores;

import javax.media.jai.PlanarImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;

/**
 * Classe que implementa um extrator de características de textura.
 * @author Rafael Moraes
 */
public class ExtratorTextura extends Extrator {
	
	private PlanarImage imagemOriginal;
	private PlanarImage imagemTransformada;
	private double[][] matrizCoOcorrenciaDefault;

	/** Altura (Linha) **/
	private int height;
	
	/** Largura (Coluna) **/
	private int width;

	/**
	 * Construtor para a clase ExtratorTextura.
	 * Este construtor converte a imagem para cinza e depois cria a matriz de co-ocorrência.
	 * @author Rafael Moraes
	 * @param imagem - PlanarImage
	 */
	public ExtratorTextura(final PlanarImage imagem){
		//Recupera a imagem
		this.imagemOriginal = imagem;
		
		this.width = this.imagemOriginal.getWidth();
		this.height = this.imagemOriginal.getHeight();
		
		this.imagemTransformada = converterCorParaCinza(this.imagemOriginal);
		
		this.matrizCoOcorrenciaDefault = getMatrizCoOcorrencia(0);
	}
	
	/**
	 * Recupera a matriz de co-ocorrência a partir da imagem cinza, o angulo informado e distância 1.
	 * @author Rafael Moraes
	 * @param angulo - Ângulo que será percorrida a imagem. 
	 * Só podem ser os angulos 0, 45, 90 ou 135.
	 * @return double[][] (Matriz de co-ocorrência)
	 * Se for informado um ângulo diferente de 0, 45, 90 ou 135 o método retorna null.
	 */
	public double[][] getMatrizCoOcorrencia(final int angulo) {
		double[][] matriz = null;
		
		switch (angulo) {
		case 0:
			matriz = getMatrizCoOcorrenciaAngulo0();
			break;
		case 45:
			matriz = getMatrizCoOcorrenciaAngulo45();
			break;
		case 90:
			matriz = getMatrizCoOcorrenciaAngulo90();
			break;
		case 135:
			matriz = getMatrizCoOcorrenciaAngulo135();
			break;
		default:
			break;
		}
		
		int soma = 0;
		//Encontra a soma de todos os valores da matriz
		for (int linha = 0; linha < 256; linha++){
			for (int coluna = 0; coluna < 256; coluna++){
				soma += matriz[linha][coluna];
			}
		}
		
		//Encontra a probabilidade dos pares
		for (int linha = 0; linha < 256; linha++){
			for (int coluna = 0; coluna < 256; coluna++){
				matriz[linha][coluna] = (matriz[linha][coluna]/soma);
			}
		}
		
		return matriz;
	}
	
	/**
	 * Recupera a matriz de co-ocorrência a partir da imagem cinza, com angulo 0 e distância 1.
	 * @author Rafael Moraes
	 * @return double[][] (Matriz de co-ocorrência)
	 */
	private double[][] getMatrizCoOcorrenciaAngulo0() {
		final RandomIter iterator = RandomIterFactory.create(imagemTransformada,null);
		
		int[] pixel = new int[imagemTransformada.getNumBands()];
		double[][] matriz = new double[256][256];
		
		//Inicializa a matriz
		for(int linha = 0; linha < 256; linha++){
			for(int coluna = 0; coluna < 256; coluna++){
				matriz[linha][coluna] = 0;
			}
		}
		
		for(int linha = 0; linha < this.height; linha++){
			for(int coluna = 0; coluna < this.width; coluna++){
				iterator.getPixel(coluna,linha,pixel);

				int[] pixelAux = new int[imagemTransformada.getNumBands()];
								
				if(coluna == 0){ //Canto esquerdo da imagem
					iterator.getPixel((coluna + 1),linha,pixelAux);
					matriz[pixel[0]][pixelAux[0]] += 1;
				}else if(coluna == (this.width - 1)){ // Canto direito da imagem
					iterator.getPixel((coluna - 1),linha,pixelAux);
					matriz[pixel[0]][pixelAux[0]] += 1;
				}else{
					iterator.getPixel((coluna + 1),linha,pixelAux);
					matriz[pixel[0]][pixelAux[0]] += 1;
					
					iterator.getPixel((coluna - 1),linha,pixelAux);
					matriz[pixel[0]][pixelAux[0]] += 1;
				}
			}
		}		
		return matriz;
	}
	
	/**
	 * Recupera a matriz de co-ocorrência a partir da imagem cinza, com angulo 90 e distância 1.
	 * @author Rafael Moraes
	 * @return double[][] (Matriz de co-ocorrência)
	 */
	private double[][] getMatrizCoOcorrenciaAngulo90() {
		final RandomIter iterator = RandomIterFactory.create(imagemTransformada,null);
		
		int[] pixel = new int[imagemTransformada.getNumBands()];
		double[][] matriz = new double[256][256];
		
		//Inicializa a matriz
		for(int linha = 0; linha < 256; linha++){
			for(int coluna = 0; coluna < 256; coluna++){
				matriz[linha][coluna] = 0;
			}
		}
		
		for(int linha = 0; linha < this.height; linha++){
			for(int coluna = 0; coluna < this.width; coluna++){
				iterator.getPixel(coluna,linha,pixel);

				int[] pixelAux = new int[imagemTransformada.getNumBands()];
								
				if(linha == 0){ //Canto superior da imagem
					iterator.getPixel(coluna,(linha + 1), pixelAux);
					matriz[pixel[0]][pixelAux[0]] += 1;
				}else if(linha == (this.height - 1)){ // Canto inferior da imagem
					iterator.getPixel(coluna, (linha - 1), pixelAux);
					matriz[pixel[0]][pixelAux[0]] += 1;
				}else{
					iterator.getPixel(coluna,(linha + 1), pixelAux);
					matriz[pixel[0]][pixelAux[0]] += 1;
					
					iterator.getPixel(coluna,(linha - 1), pixelAux);
					matriz[pixel[0]][pixelAux[0]] += 1;
				}
			}
		}		
		return matriz;
	}
	
	/**
	 * Recupera a matriz de co-ocorrência a partir da imagem cinza, com angulo 45 e distância 1.
	 * @author Rafael Moraes
	 * @return double[][] (Matriz de co-ocorrência)
	 */
	private double[][] getMatrizCoOcorrenciaAngulo45() {
		final RandomIter iterator = RandomIterFactory.create(imagemTransformada,null);
		
		int[] pixel = new int[imagemTransformada.getNumBands()];
		double[][] matriz = new double[256][256];
		
		//Inicializa a matriz
		for(int linha = 0; linha < 256; linha++){
			for(int coluna = 0; coluna < 256; coluna++){
				matriz[linha][coluna] = 0;
			}
		}
		
		for(int linha = 0; linha < this.height; linha++){
			for(int coluna = 0; coluna < this.width; coluna++){
				iterator.getPixel(coluna,linha,pixel);

				int[] pixelAux = new int[imagemTransformada.getNumBands()];
								
				if(linha == 0 && coluna != (this.width - 1)){ //Canto superior da imagem
					iterator.getPixel((coluna + 1),(linha + 1), pixelAux);
					matriz[pixel[0]][pixelAux[0]] += 1;
				}else if(linha == (this.height - 1) && coluna != (this.width - 1)){ // Canto inferior da imagem
					iterator.getPixel((coluna + 1),(linha - 1), pixelAux);
					matriz[pixel[0]][pixelAux[0]] += 1;
				}else if(coluna != (this.width - 1)){
					iterator.getPixel((coluna + 1),(linha + 1), pixelAux);
					matriz[pixel[0]][pixelAux[0]] += 1;
					
					iterator.getPixel((coluna + 1),(linha - 1), pixelAux);
					matriz[pixel[0]][pixelAux[0]] += 1;
				}
			}
		}

		return matriz;
	}
	
	/**
	 * Recupera a matriz de co-ocorrência a partir da imagem cinza, com angulo 135 e distância 1.
	 * @author Rafael Moraes
	 * @return double[][] (Matriz de co-ocorrência)
	 */
	private double[][] getMatrizCoOcorrenciaAngulo135() {
		final RandomIter iterator = RandomIterFactory.create(imagemTransformada,null);
		
		int[] pixel = new int[imagemTransformada.getNumBands()];
		double[][] matriz = new double[256][256];
		
		//Inicializa a matriz
		for(int linha = 0; linha < 256; linha++){
			for(int coluna = 0; coluna < 256; coluna++){
				matriz[linha][coluna] = 0;
			}
		}
		
		for(int linha = 0; linha < this.height; linha++){
			for(int coluna = 0; coluna < this.width; coluna++){
				iterator.getPixel(coluna,linha,pixel);

				int[] pixelAux = new int[imagemTransformada.getNumBands()];
								
				if(linha == 0 && coluna != 0){ //Canto superior da imagem
					iterator.getPixel((coluna - 1),(linha + 1), pixelAux);
					matriz[pixel[0]][pixelAux[0]] += 1;
				}else if(linha == (this.height - 1) && coluna != 0){ // Canto inferior da imagem
					iterator.getPixel((coluna - 1),(linha - 1), pixelAux);
					matriz[pixel[0]][pixelAux[0]] += 1;
				}else if(coluna != 0){
					iterator.getPixel((coluna - 1),(linha + 1), pixelAux);
					matriz[pixel[0]][pixelAux[0]] += 1;
					
					iterator.getPixel((coluna - 1),(linha - 1), pixelAux);
					matriz[pixel[0]][pixelAux[0]] += 1;
				}
			}
		}		

		return matriz;
	}
	
	/**
	 * Recupera a probabilidade máxima da matriz de co-ocorrência.
	 * @author Rafael Moraes
	 * @param matrizCoOcorrencia - Matriz de co-ocorrência
	 * @return double (Probabilidade máxima)
	 */
	public double getProbabilidadeMaxima(final double[][] matrizCoOcorrencia){
		
		double max = 0;
		
		for (int i = 0; i < matrizCoOcorrencia.length; i++) {
			for (int j = 0; j < matrizCoOcorrencia.length; j++) {
				double valor = matrizCoOcorrencia[i][j];
				if(valor > max){
					max = valor;
				}
			}
		}
		
		return max;
	}
	
	/**
	 * Recupera o contraste.
	 * @author Rafael Moraes
	 * @param matrizCoOcorrencia - Matriz de co-ocorrência
	 * @return double (Contraste)
	 */
	public double getContraste(final double[][] matrizCoOcorrencia){
		
		double momento = 0;
		
		for (int i = 0; i < matrizCoOcorrencia.length; i++) {
			for (int j = 0; j < matrizCoOcorrencia.length; j++) {

				double valor = matrizCoOcorrencia[i][j];
				
				momento += ((i - j)*(i - j)) * valor;
			}
		}
		
		return momento;
	}
	
	/**
	 * Recupera o inverso do contraste.
	 * @author Rafael Moraes
	 * @param matrizCoOcorrencia - Matriz de co-ocorrência
	 * @return double (Inverso do contraste)
	 */
	public double getInversoContraste(final double[][] matrizCoOcorrencia){
		
		double momento = 0;
		
		for (int i = 0; i < matrizCoOcorrencia.length; i++) {
			for (int j = 0; j < matrizCoOcorrencia.length; j++) {
				
				if(i != j){
					double valor = matrizCoOcorrencia[i][j];
					double valorDiv = (i - j)*(i - j);
					
					if(valorDiv != 0){
						momento += valor/valorDiv;
					}
				}
			}
		}
		
		return momento;
	}
	
	/**
	 * Recupera a entropia da matriz de co-ocorrência.
	 * @author Rafael Moraes
	 * @param matrizCoOcorrencia - Matriz de co-ocorrência
	 * @return double (Entropia)
	 */
	public double getEntropia(final double[][] matrizCoOcorrencia){
		
		double entropia = 0;
		
		for (int i = 0; i < matrizCoOcorrencia.length; i++) {
			for (int j = 0; j < matrizCoOcorrencia.length; j++) {

				double valor = matrizCoOcorrencia[i][j];
				if(valor != 0){
					entropia += valor * (Math.log(valor)/Math.log(2));
				}
			}
		}
		
		return 0 - entropia;
	}
	
	/**
	 * Recupera a uniformidade da matriz de co-ocorrência.
	 * @author Rafael Moraes
	 * @param matrizCoOcorrencia - Matriz de co-ocorrência
	 * @return double (Uniformidade)
	 */
	public double getUniformidade(final double[][] matrizCoOcorrencia){
		double uniformidade = 0;
		
		for (int i = 0; i < matrizCoOcorrencia.length; i++) {
			for (int j = 0; j < matrizCoOcorrencia.length; j++) {

				double valor = matrizCoOcorrencia[i][j];
				
				uniformidade += (valor*valor);
			}
		}
		
		return uniformidade;
	}
	
	/**
	 * Recupera a imagem original
	 * @author Rafael Moraes
	 * @return PlanarImage (Imagem original)
	 */
	public PlanarImage getImagemOriginal() {
		return imagemOriginal;
	}
	
	/**
	 * Recupera a matriz de co-ocorrência default que é calculada
	 * com um ângulo de 0 graus.
	 * @return double[][] (Matriz de co_ocorr~encia)
	 */
	public double[][] getMatrizCoOcorrenciaDefault() {
		return matrizCoOcorrenciaDefault;
	}

}