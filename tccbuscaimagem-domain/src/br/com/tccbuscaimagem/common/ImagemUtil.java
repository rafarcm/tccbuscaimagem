package br.com.tccbuscaimagem.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public final class ImagemUtil {
	
    public static void downloadImagem(final String urlParam, final String name) throws IOException {

        URL url = new URL(urlParam);
        InputStream in = url.openStream();
        OutputStream out = new BufferedOutputStream(new FileOutputStream(name));
        for (int b; (b = in.read()) != -1; ) {
            out.write(b);
        }
        out.close();
        in.close();
    }
    
    public static void deletarImagem(final String arquivo){
    	File arq = new File(arquivo);
    	arq.delete();
    	arq = null;
    }
}
