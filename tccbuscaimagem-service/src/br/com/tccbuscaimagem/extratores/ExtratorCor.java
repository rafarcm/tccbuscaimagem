package br.com.tccbuscaimagem.extratores;

import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.Histogram;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

/**
 * Classe que implementa um extrator de características de cor.
 * @author Rafael Moraes
 */
public class ExtratorCor extends Extrator {
	
	private PlanarImage imagemOriginal;
	private PlanarImage imagemTransformada;
	
	/** Altura (Linha) **/
	private int height;
	
	/** Largura (Coluna) **/
	private int width;
	
	private Histogram histograma;

	/**
	 * Construtor da classe. 
	 * Este construtor recebe a imagem, converte as cores da imagem para cinza, 
	 * aplica o filro e cria o histograma de cor.
	 * @author Rafael Moraes
	 * @param imagem - PlanarImage
	 * @param bins - Número de bins do histograma
	 * @param valorCoerencia - Valor limiar para coerência
	 */
	public ExtratorCor(final PlanarImage imagem, final Integer bins){
		//Recupera a imagem
		this.imagemOriginal = imagem;

		this.width = this.imagemOriginal.getWidth();
		this.height = this.imagemOriginal.getHeight();
		
		this.imagemTransformada = converterCorParaCinza(imagem);
		this.imagemTransformada = this.aplicarFiltro(this.imagemTransformada);
		this.imagemTransformada = filtroMedia(this.imagemTransformada, 5);
		this.recuperarHistogramaCor(bins);
	}
	
	/**
	 * Aplica um fitro na imagem afim de se remover regiões com ruídos.
	 * @author Rafael Moraes
	 * @param imagem - PlanarImage
	 * @return PlanarImage
	 */
	private PlanarImage aplicarFiltro(final PlanarImage imagem){
		PlanarImage retorno = dilatar(imagem);
		retorno = erodir(retorno);
		retorno = erodir(retorno);
		retorno = dilatar(retorno);
		
		return retorno;
	}
	
	/**
	 * Recupera o histograma de cor da imagem
	 * @author Rafael Moraes
	 * @param bins - Número de bins da imagem
	 */
	public void recuperarHistogramaCor(final int bins){
		final ParameterBlock pb = new ParameterBlock();
		pb.addSource(this.imagemTransformada);
		//Área de interesse. Null para toda a imagem
		pb.add(null);
		//Taxas de amostragem horizontal e vertical
		pb.add(1); 
		pb.add(1);
		//Número de intervalos ou bins para cada banda da imagem
		pb.add(new int[]{bins});
		//Valores limiares inferior e superior
		//Pixels na imagem com valores abaixo do inferior ou acima do superior não serão considerados para o cálculo do histograma
		pb.add(new double[]{0});
		pb.add(new double[]{256});
		
		final PlanarImage dummyImage = JAI.create("histogram", pb);
		this.histograma = (Histogram)dummyImage.getProperty("histogram");
	}
	
	/**
	 * Retorna a média de cores fracas da imagem
	 * @author Rafael Moraes
	 * @return double média cores fracas
	 */
	public double coresFracas(){

		double total = 0;
		double[] histogramaNormalizado = new double[histograma.getNumBins(0)];
		double numTotalPixels = (width * height);
		for (int i = 0; i < ((int) (histograma.getNumBins(0) / 3)); i++) {
			double valor = histograma.getBinSize(0, i);
			histogramaNormalizado[i] = (valor / numTotalPixels);
			total += histogramaNormalizado[i];
		}
		return total;
	}
	
	/**
	 * Retorna a média de cores médias da imagem
	 * @author Rafael Moraes
	 * @return double média cores médias
	 */
	public double coresMedias(){

		double total = 0;
		double[] histogramaNormalizado = new double[histograma.getNumBins(0)];
		double numTotalPixels = (width * height);
		final int inicio = (int) (histograma.getNumBins(0) / 3);
		final int fim  = (int) ((histograma.getNumBins(0) / 3) * 2);
		for (int i =  inicio; i < fim; i++) {
			double valor = histograma.getBinSize(0, i);
			histogramaNormalizado[i] = (valor / numTotalPixels);
			total += histogramaNormalizado[i];
		}
		return total;
	}
	
	/**
	 * Retorna a média de cores fortes da imagem
	 * @author Rafael Moraes
	 * @return double média cores fortes
	 */
	public double coresFortes(){
		double total = 0;
		double[] histogramaNormalizado = new double[histograma.getNumBins(0)];
		double numTotalPixels = (width * height);
		final int inicio  = (int) ((histograma.getNumBins(0) / 3) * 2);
		final int fim = histograma.getNumBins(0);
		for (int i =  inicio; i < fim; i++) {
			double valor = histograma.getBinSize(0, i);
			histogramaNormalizado[i] = (valor / numTotalPixels);
			total += histogramaNormalizado[i];
		}
		return total;
	}
}
