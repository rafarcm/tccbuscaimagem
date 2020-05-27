package br.com.tccbuscaimagem.extratores;

import java.util.ArrayList;
import java.util.List;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;

/**
 * Classe que implementa um extrator de características de forma.
 * @author Rafael Moraes
 */
public class ExtratorForma extends Extrator {
	
	private PlanarImage imagemOriginal;
	private PlanarImage imagemTransformada;
	private PlanarImage imagemRetangular;
	private PlanarImage[] rgbBandsBin;
	private List<int[][]> cantosRetangulo;
	private int[][] retanguloImagem;
	private double[][] centroImagem;
	
	private int width;
	private int height;
	
	int numPixelPreto = 0;
	int numPixelBranco = 0;
	
	public ExtratorForma(final PlanarImage imagem){
		//Recupera a imagem
		this.imagemOriginal = imagem;
		
		this.width = this.imagemOriginal.getWidth();
		this.height = this.imagemOriginal.getHeight();
		
		this.imagemTransformada = converterCorParaCinza(this.imagemOriginal);
		this.imagemTransformada = detectarBordasImagem(this.imagemOriginal);
		this.imagemTransformada = JAI.create("invert", this.imagemTransformada);
		this.rgbBandsBin = binarizarImagem(this.imagemTransformada);
		
		this.cantosRetangulo = recuperarCantosRetangulo(this.rgbBandsBin[0]);
		this.retanguloImagem = recuperarRetanguloImagem(this.rgbBandsBin[0], cantosRetangulo);
		this.imagemRetangular = criarImagem(width, height, retanguloImagem);
		
		this.centroImagem = recuperarCentroImagem(this.imagemRetangular, cantosRetangulo);
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}

	public PlanarImage getImagemOriginal() {
		return imagemOriginal;
	}

	public double razaoAltLarg(){

		double razaoAltLarg = 0.0;

		final PlanarImage imagemAlt = detectarBordasImagem(rgbBandsBin[0]);

		double normalizaAltura = (double)((double)recuperarAlturaObjeto(imagemAlt) / (double)height);
		double normalizaLargura = (double)((double)recuperarLarguraObjeto(imagemAlt) / (double)width);
		
		if(normalizaLargura > normalizaAltura){
			razaoAltLarg = normalizaAltura/normalizaLargura;
		}else{
			razaoAltLarg = normalizaLargura/normalizaAltura;
		}
		return razaoAltLarg;
	}
	
	public double transformacaoAreaPonto(){
		
		final PlanarImage imagemAlt = dilatar(this.rgbBandsBin[0]);
		final int[][] newImag = removerBordaDaImagem(this.width, this.height, imagemAlt);
		
		int count = 0;

		for(int h = 0; h < this.height; h = h+2){
			for(int w = 0; w < this.width; w = w+2){
				count = 0;
				int difH = height - h;
				if(difH >= 3){
					difH = 3;
				}
				int difW = width - w;
				if(difW >= 3){
					difW = 3;
				}
				for(int i = h; i < (h+difH); i++){
					for(int j = w; j < (w+difW); j++){
						if(newImag[j][i] == 0){
							count++;
						}
					}
				}
				
				if(count > 2){
					for(int i = h; i < (h+difH); i++){
						for(int j = w; j < (w+difW); j++){
							if(((i+1) == (h+difH)) && ((j+1) == (w+difW))){
								newImag[j][i] = 0;
							}else{
								newImag[j][i] = 1;
							}
						}
					}
				}
			}
		}
		int countTotalPontos = 0;
		//Conta o número de pontos
		for(int h = 0; h < this.height; h++){
			for(int w = 0; w < this.width; w++){
				
				if(newImag[w][h] == 0){
					countTotalPontos++;
				}
			}
		}
		
		double retorno = (double)(((double)countTotalPontos)/(double)256);
		return retorno;
	}
	
	public double assinaturas(final double angulo){
		
		boolean continueLaco = true;
		double anguloRadiano = Math.toRadians(0);
		double valorColuna = centroImagem[0][0];
		double valorLinha = centroImagem[0][1];
		
		int[] centro = {(int)centroImagem[0][0],(int)centroImagem[0][1]};
		int[] borda = new int[2];

		final RandomIter iterator = RandomIterFactory.create(this.imagemRetangular,null);
		
		int[] pixel = new int[this.imagemRetangular.getNumBands()];

		final List<Double> distancias = new ArrayList<Double>();
		
		int linha = 0;
		int coluna = 0;
		
		int angulAux = 0;
		
		double senoAngulo = Math.sin(anguloRadiano);
		double cosenoAnugulo = Math.cos(anguloRadiano);

		while(angulAux < 360){

			do {
				
				if(angulAux <= 90){
					valorColuna = valorColuna + cosenoAnugulo;
					valorLinha = valorLinha - senoAngulo;
				}else if (angulAux > 90 && angulAux <= 180){
					valorColuna = valorColuna - cosenoAnugulo;
					valorLinha = valorLinha - senoAngulo;
				}else if (angulAux > 180 && angulAux <= 270){
					valorColuna = valorColuna - cosenoAnugulo;
					valorLinha = valorLinha + senoAngulo;
				}else if (angulAux > 270 && angulAux <= 360){
					valorColuna = valorColuna + cosenoAnugulo;
					valorLinha = valorLinha + senoAngulo;
				}else{
					//Erro
				}
				
				linha = (int) valorLinha;
				coluna = (int) valorColuna;
				
				if((coluna >= 0 && linha >= 0) && (coluna < width && linha < height)){
					iterator.getPixel(coluna,linha,pixel);
					
					if(pixel[0] == 1){

						if(coluna >= 0 && linha >= 0){
							iterator.getPixel(coluna-1,linha-1,pixel);
							if(pixel[0] == 0){
								continueLaco = false;
								linha = linha-1;
								coluna = coluna-1;
							}
						}

						if(continueLaco && linha >= 0){
							iterator.getPixel(coluna,linha-1,pixel);
							if(pixel[0] == 0){
								continueLaco = false;
								linha = linha-1;
							}
						}
						
						if(continueLaco && coluna < width && linha >= 0){
							iterator.getPixel(coluna+1,linha-1,pixel);
							if(pixel[0] == 0){
								continueLaco = false;
								linha = linha-1;
								coluna = coluna+1;
							}
						}

						if(continueLaco && coluna < width){
							iterator.getPixel(coluna+1,linha,pixel);
							if(pixel[0] == 0){
								continueLaco = false;
								coluna = coluna+1;
							}
						}

						if(continueLaco && coluna < width && linha < height){
							iterator.getPixel(coluna+1,linha+1,pixel);
							if(pixel[0] == 0){
								continueLaco = false;
								linha = linha+1;
								coluna = coluna+1;
							}
						}
						
						if(continueLaco && coluna < width && linha < height){
							iterator.getPixel(coluna,linha+1,pixel);
							if(pixel[0] == 0){
								continueLaco = false;
								linha = linha+1;
							}
						}

						if(continueLaco && coluna < width && linha >= 0){
							iterator.getPixel(coluna+1,linha-1,pixel);
							if(pixel[0] == 0){
								continueLaco = false;
								linha = linha-1;
								coluna = coluna+1;
							}
						}

						if(continueLaco && coluna < width){
							iterator.getPixel(coluna-1,linha,pixel);
							if(pixel[0] == 0){
								continueLaco = false;
								coluna = coluna-1;
							}
						}
					}else{
						continueLaco = false;	
					}
				}else{
					if(coluna < 0){
						coluna += coluna;
					}
					if(linha < 0){
						linha += linha;
					} 
					if(coluna >= width){
						coluna -= (coluna - width - 1);
					} 
					if(linha >= height){
						linha -= (linha - height - 1);
					}
					continueLaco = false;
				}
	
			} while (continueLaco);
			
			borda[0] = coluna;
			borda[1] = linha;
			distancias.add(calcularDistanciaPontos(centro, borda));
			
			angulAux += angulo;
			
			anguloRadiano = Math.toRadians(angulAux);
			senoAngulo = Math.sin(anguloRadiano) > 0 ? Math.sin(anguloRadiano): Math.sin(anguloRadiano) * -1;
			cosenoAnugulo = Math.cos(anguloRadiano) > 0 ? Math.cos(anguloRadiano): Math.cos(anguloRadiano) * -1;
			
			linha = 0;
			coluna = 0;
			
			valorColuna = centroImagem[0][0];
			valorLinha = centroImagem[0][1];
			continueLaco = true;
		}
		
		return calcularDesvioPadrao(distancias);
	}
	
	private double calcularDistanciaPontos(final int[] pontoA, final int[] pontoB){
		
		int x = pontoA[0] - pontoB[0];
		int y = pontoA[1] - pontoB[1];
		
		x = x * x;
		y = y * y;
		
		int xy = x + y;
		
		return Math.sqrt(xy);
	}
	
	private double calcularDesvioPadrao(List<Double> distancias){
		
		double mediaValores = 0;
		double desvioPadrao = 0;
		
		final List<Double> distanciasNormalizadas = 
			normalizarDistancias(distancias);
		
		for (Double double1 : distanciasNormalizadas) {
			mediaValores += double1;
		}
		
		mediaValores /= distancias.size();
		
		for (Double double1 : distanciasNormalizadas) {
			desvioPadrao += ((double1 - mediaValores) * (double1 - mediaValores));
		}
		
		desvioPadrao /= (distancias.size() - 1);
		return Math.sqrt(desvioPadrao);
	}
	
	private List<Double> normalizarDistancias(List<Double> distancias){
		double maiorValor = 0;
		final List<Double> distanciasNormalizadas = new ArrayList<Double>();
		for (Double double1 : distancias) {
			if(maiorValor < double1){
				maiorValor = double1;
			}
		}
		
		for (Double double1 : distancias) {
			distanciasNormalizadas.add(double1 / maiorValor);
		}
		return distanciasNormalizadas;
	}
	

	private int[][] recuperarRetanguloImagem(final PlanarImage imagem, final List<int[][]> cantosRetangulo){
		final RandomIter iterator = RandomIterFactory.create(imagem,null);
		
		int[] pixel = new int[imagem.getNumBands()];
		int[][] newImag = new int[imagem.getWidth()][imagem.getHeight()];
		
		for(int h = 0; h < imagem.getHeight(); h++){
			for(int w = 0; w < imagem.getWidth(); w++){
				iterator.getPixel(w,h,pixel);

				newImag[w][h] = 1;
			}
		}

		int[][] canto1 = cantosRetangulo.get(0);
		int[][] canto2 = cantosRetangulo.get(1);
		int[][] canto3 = cantosRetangulo.get(2);
		int[][] canto4 = cantosRetangulo.get(3);

		for (int i = canto1[0][0]; i < canto2[0][0]; i++) {
			newImag[i][canto1[0][1]] = 0;
		}
		
		for (int i = canto1[0][1]; i < canto3[0][1]; i++) {
			newImag[canto1[0][0]][i] = 0;
		}
		
		for (int i = canto4[0][1]; i >= canto2[0][1]; i--) {
			newImag[canto4[0][0]][i] = 0;
		}
		
		for (int i = canto4[0][0]; i >= canto3[0][0]; i--) {
			newImag[i][canto4[0][1]] = 0;
		}

		return newImag;
	}
	
	private int recuperarAlturaObjeto(PlanarImage image){
		
		int topH = -1;
		int bottomH = -1;
		
		final RandomIter iterator = RandomIterFactory.create(image,null);
		
		int[] pixel = new int[image.getNumBands()];
		for(int h = 0; h < this.height; h++){
			for(int w = 0; w < this.width; w++){
				iterator.getPixel(w,h,pixel);

				if(pixel[0] != 0){
					topH = h;
					break;
				}
			}
			
			if(topH != -1){
				break;
			}
		}
		
		for(int h = (this.height - 1); h > 0; h--){
			for(int w = 0; w < this.width; w++){
				iterator.getPixel(w,h,pixel);

				if(pixel[0] != 0){
					bottomH = h;
					break;
				}
			}
			
			if(bottomH != -1){
				break;
			}
		}
		
		return (this.height - (topH + (this.height - bottomH)));
	}
	
	private int recuperarLarguraObjeto(PlanarImage image){
		
		int leftW = -1;
		int rightW = -1;
		
		final RandomIter iterator = RandomIterFactory.create(image,null);
		
		int[] pixel = new int[image.getNumBands()];
		for(int w = 0; w < this.width; w++){
			for(int h = 0; h < this.height; h++){
				iterator.getPixel(w,h,pixel);

				if(pixel[0] != 0){
					leftW = w;
					break;
				}
			}
			
			if(leftW != -1){
				break;
			}
		}
		
		for(int w = (this.width - 1); w > 0; w--){
			for(int h = 0; h < this.height; h++){
				iterator.getPixel(w,h,pixel);

				if(pixel[0] != 0){
					rightW = w;
					break;
				}
			}
			
			if(rightW != -1){
				break;
			}
		}
		
		return (this.width - (leftW + (this.width - rightW)));
	}
	
	private List<int[][]> recuperarCantosRetangulo(PlanarImage image){

		int xEsquerda = -1;
		int xDireita = -1;
		
		int yCima = -1;
		int yBaixo = -1;
		
		final RandomIter iterator = RandomIterFactory.create(image,null);
		
		int[] pixel = new int[image.getNumBands()];
		
		for(int w = 0; w < this.width; w++){
			for(int h = 0; h < this.height; h++){
				iterator.getPixel(w,h,pixel);

				if(pixel[0] == 0){
					xEsquerda = w;
					break;
				}
			}
			
			if(xEsquerda != -1){
				break;
			}
		}
		
		for(int w = (this.width - 1); w > 0; w--){
			for(int h = 0; h < this.height; h++){
				iterator.getPixel(w,h,pixel);

				if(pixel[0] == 0){
					xDireita = w;
					break;
				}
			}
			
			if(xDireita != -1){
				break;
			}
		}
		
		for(int h = 0; h < this.height; h++){
			for(int w = 0; w < this.width; w++){
				iterator.getPixel(w,h,pixel);

				if(pixel[0] == 0){
					yCima = h;
					break;
				}
			}
			
			if(yCima != -1){
				break;
			}
		}
		
		for(int h = (this.height - 1); h > 0; h--){
			for(int w = 0; w < this.width; w++){
				iterator.getPixel(w,h,pixel);

				if(pixel[0] == 0){
					yBaixo = h;
					break;
				}
			}
			
			if(yBaixo != -1){
				break;
			}
		}
		
		List<int[][]> cantosRetangulo = new ArrayList<int[][]>();

		int[][] canto = null;
		
		//Primeiro Canto
		canto = new int[1][2];
		canto[0][0] = xEsquerda; 
		canto[0][1] = yCima; 
		cantosRetangulo.add(canto);
		
		//Segundo Canto
		canto = new int[1][2];
		canto[0][0] = xDireita; 
		canto[0][1] = yCima; 
		cantosRetangulo.add(canto);
		
		//Terceiro Canto
		canto = new int[1][2];
		canto[0][0] = xEsquerda; 
		canto[0][1] = yBaixo; 
		cantosRetangulo.add(canto);
		
		//Quarto Canto
		canto = new int[1][2];
		canto[0][0] = xDireita; 
		canto[0][1] = yBaixo; 
		cantosRetangulo.add(canto);	

		return cantosRetangulo;
	}
	
	private double[][] recuperarCentroImagem(PlanarImage image, List<int[][]> cantosRetangulo){
				
		double[][] newPontoA = calcularPontoMedio(cantosRetangulo.get(0), cantosRetangulo.get(2));
		double[][] newPontoB = calcularPontoMedio(cantosRetangulo.get(1), cantosRetangulo.get(3));
		
		double[][] centroImagem = calcularPontoMedio(newPontoA, newPontoB);
		
		return centroImagem;
	}
	
	private double[][] calcularPontoMedio(final int[][] pontoA, final int[][] pontoB){
		
		double[][] pontoMedio = new double[1][2];
		
		double x = (pontoA[0][0] + pontoB[0][0])/2;
		double y = (pontoA[0][1] + pontoB[0][1])/2;

		pontoMedio[0][0] = x;
		pontoMedio[0][1] = y;            

		return pontoMedio;
	}
	
	private double[][] calcularPontoMedio(final double[][] pontoA, final double[][] pontoB){
		
		double[][] pontoMedio = new double[1][2];
		
		double x = (pontoA[0][0] + pontoB[0][0])/2;
		double y = (pontoA[0][1] + pontoB[0][1])/2;

		pontoMedio[0][0] = x;
		pontoMedio[0][1] = y;            

		return pontoMedio;
	}	
}
