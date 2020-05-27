package br.com.tccbuscaimagem.extratores;

import java.awt.Point;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.TiledImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;

/**
 * Classe que contém os métodos comuns para todos os Extratores.
 * @author Rafael Moraes
 */
public class Extrator {
	
	/**
	 * Converte as cores da imagem para cinza.
	 * @author Rafael Moraes
	 * @param imagem - PlanarImage
	 * @return PlanarImage (Imagem cinza)
	 */
	public PlanarImage converterCorParaCinza(final PlanarImage imagem) { 
		double[][] matrix = { 
				{ 0.114D, 0.587D, 0.299D, 0 }, 
				{ 0.114D, 0.587D, 0.299D, 0 }, 
				{ 0.114D, 0.587D, 0.299D, 0 } 
		}; 

		final ParameterBlock pb = new ParameterBlock(); 
		pb.addSource(imagem); 
		pb.add(matrix); 
		return JAI.create("bandcombine", pb, null); 
	} 
	
	/**
	 * Detecta bordas na imagem
	 * @param imagem - PlanarImage
	 * @return PlanarImage (Imagem com as bordas em destaque)
	 */
	public PlanarImage detectarBordasImagem(final PlanarImage imagem){
		
		float[] kernelMatriz = {-1, -2, -1, 0, 0, 0, 1, 2, 1};
		
		KernelJAI kernel = new KernelJAI(3,3,kernelMatriz);
		
		return JAI.create("convolve", imagem, kernel);
	}
	
	/**
	 * Remove pequenas regiões da imagem.
	 * @author Rafael Moraes
	 * @param imagem - PlanarImage
	 * @return PlanarImage (Imagem dilatada)
	 */
	public PlanarImage dilatar(final PlanarImage imagem) { 
		final float[] kernelMatrix = { 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0 };

		final KernelJAI kernel = new KernelJAI(5, 5, kernelMatrix);

		ParameterBlock p = new ParameterBlock();
		p.addSource(imagem);
		p.add(kernel);
		
		return JAI.create("dilate", p, null);
	} 
	
	/**
	 * Aumenta regiões da imagem
	 * @author Rafael Moraes
	 * @param imagem - PlanarImage
	 * @return PlanarImage (Imagem com erosão)
	 */
	public PlanarImage erodir(final PlanarImage imagem){

		final float[] kernelMatrix = { 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0 };

		final KernelJAI kernel = new KernelJAI(5, 5, kernelMatrix);
		
		ParameterBlock p = new ParameterBlock();
		p.addSource(imagem);
		p.add(kernel);
		
		return JAI.create("erode", p, null);
	}
	
	/**
	 * Aplica um filtro na imagem suavizando os valores de seus pixels.
	 * @author Rafael Moraes
	 * @param mascara - Tamanho da mascará para a imagem. Ex: 3 = 3x3
	 * @return PlanarImage (Imagem suavizada com filtro)
	 */
	public PlanarImage filtroMedia(final PlanarImage imagem, final int mascara){
		final int masc2 = mascara * mascara;
		final float[] kernelMatrix = new float[masc2];
		
		for (int i = 0; i < kernelMatrix.length; i++) {
			kernelMatrix[i] = 1f/masc2;
		}
		
		final KernelJAI kernel = new KernelJAI(mascara, mascara, kernelMatrix);
		return JAI.create("convolve", imagem, kernel);
	}
	
	/**
	 * Binariza a imagem. Transforma os valores dos pixels e 1(branco) ou 0(preto)
	 * @author Rafael Moraes
	 * @param imagem - PlanarImage
	 * @param bins - Número de bins da imagem
	 * @return PlanarImage (Imagem binarizada)
	 */
	public PlanarImage[] binarizarImagem(final PlanarImage imagem){
	
		ParameterBlock pb = new ParameterBlock();
		PlanarImage[] RGBBands = new PlanarImage[3];
		PlanarImage[] RGBBandsBin = new PlanarImage[3];

		//Binariza as bandas em separado
		for(int band = 0; band < 3;band++){
			pb = new ParameterBlock();
			pb.addSource(imagem);
			pb.add(new int[]{band});
			RGBBands[band] = JAI.create("bandselect",pb);
			
			pb = new ParameterBlock();
			pb.addSource(RGBBands[band]);
			pb.add(127.0);

			RGBBandsBin[band] = JAI.create("binarize", pb);
		}
		
		return RGBBandsBin;
	}
	
	/**
	 * Cria uma nova imagem.
	 * @author Rafael Moraes
	 * @param width - Largura
	 * @param height - Altura
	 * @param labels - Valores dos pixels
	 * @return PlanarImage (Imagem criada)
	 */
	public PlanarImage criarImagemJAI(final int width, final int height, final int[][] labels) {

		// Create a byte data sample model.
		final SampleModel sampleModel = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, width, height, 1);
		
		// Create a compatible ColorModel.
		final ColorModel colorModel = PlanarImage.createColorModel(sampleModel);

		// Create a TiledImage using the SampleModel and ColorModel.
		final TiledImage tiledImage = new TiledImage(0, 0, width, height, 0, 0, sampleModel, colorModel);

		for (int th = tiledImage.getMinTileY(); th <= tiledImage.getMaxTileY(); th++){
			for (int tw = tiledImage.getMinTileX(); tw <= tiledImage.getMaxTileX(); tw++) {
				final WritableRaster wr = tiledImage.getWritableTile(tw, th);
				for (int ih = 0; ih < height; ih++){
					for (int iw = 0; iw < width; iw++) {
						int w = wr.getMinX() + iw;
						int h = wr.getMinY() + ih;
						
						int[] a = {labels[w][h]};
						wr.setPixel(w, h, a);
					}
				}
			}
		}

		return tiledImage;
	}
	
	public PlanarImage criarImagem(final int width, final int height, int[][] imagem) {
		int count = 0;
		int[] imageData = new int[width*height];
		for(int h = 0; h < height; h++){
			for(int w=0;w<width;w++){
				imageData[count++] = imagem[w][h];
			}
		}

		DataBufferInt dbuffer = new DataBufferInt(imageData, width * height);
		SampleModel sampleModel =
			RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, width, height,1);
		ColorModel colorModel = PlanarImage.createColorModel(sampleModel);
		WritableRaster raster =
			RasterFactory.createWritableRaster(sampleModel,dbuffer,new Point(0,0));
		TiledImage tiledImage = new TiledImage(0,0,width,height,0,0,sampleModel,colorModel);
		tiledImage.setData(raster);
		return JAI.create("filestore", tiledImage, "floatpattern.JPG");
	}

	public int[][] removerBordaDaImagem(final int width, final int height, final PlanarImage image){
		final RandomIter iterator = RandomIterFactory.create(image,null);
		
		int[] pixel = new int[image.getNumBands()];
		int[][] newImag = new int[image.getWidth()][image.getHeight()];
		
		for(int h = 0; h < height; h++){
			for(int w = 0; w < width; w++){
				iterator.getPixel(w,h,pixel);

				newImag[w][h] = pixel[0];
			}
		}
		for(int w = 0; w < width; w++){
						
			newImag[w][0] = 1;
		}
		for(int h = 0; h < height; h++){
			
			newImag[0][h] = 1;
		}
		
		for(int w = 0; w < width; w++){
			
			newImag[w][height-1] = 1;
		}
		for(int h = 0; h < height; h++){
			
			newImag[width-1][h] = 1;
		}
				
		return newImag;
	}
}