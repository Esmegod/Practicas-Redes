package com.mycompany.proyecto5;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Descargar extends Thread{
    String recursoURL;
    int length;
    String nombre;
    boolean isHTML;
    ArrayList<String> arrD = new ArrayList<String>();;
    
    public Descargar(String recursoURL, int length, String nombre, boolean isHTML){
        this.recursoURL = recursoURL; 
        this.length = length;
        this.nombre = nombre;
        this.isHTML = isHTML;
    }

    public void run(){
        try{
            URL url = new URL(recursoURL);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            DataInputStream dis = new DataInputStream(httpUrlConnection.getInputStream());
            File file = new File(nombre);
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);


            if(isHTML){
                String absPath = new File("").getAbsolutePath().replace("\\", "/");
                for(String link : findLinks(recursoURL, url.getHost())) {
                    arrD.add(link);
                }

                while(true) {
                    byte[] b = new byte[65535];
                    int t = dis.read(b);
                    if(t == -1){
                        break;
                    }
                    
                    String html = new String(b,0,t);
                    StringTokenizer st = new StringTokenizer(html, "\n");
                    String linea;
                    while(st.hasMoreTokens()){
                        linea = st.nextToken();
                        if(linea.contains("src=")){ //Se reemplazan imagenes y todo con src
                            int i = linea.indexOf("src=");
                            int j = linea.indexOf("\"", i+5);
                            if(j<0) j = linea.indexOf("'", i+5);
                            if(j>0){
                                String urlReemplazar = linea.substring(i+5, j);
                                String urlLink = urlReemplazar.replace("../", "").replace("./", "");
                                for(int k=0; k<arrD.size(); k++){ 
                                    if(arrD.get(k).contains(urlLink)){
                                        String reemplazo = arrD.get(k).replace("https:/", absPath).replace("http:/", absPath);
                                        linea = linea.replace(urlReemplazar, reemplazo);
                                    }
                                }
                            }
                        }
                        if(linea.contains("href=")){ //Se reemplazan links a paginas y todo con href
                            int i = linea.indexOf("href=");
                            int j = linea.indexOf("\"", i+6);
                            if(j<0) j = linea.indexOf("'", i+6);
                            if(j>0){
                                String urlReemplazar = linea.substring(i+6, j);
                                if(!urlReemplazar.contains(":")){
                                    String urlLink = urlReemplazar.replace("../", "").replace("./", "");
                                    if (!urlLink.startsWith("?")) {
                                        for (int k = 0; k < arrD.size(); k++) {
                                            if (arrD.get(k).contains(urlLink)) {
                                                String reemplazo = arrD.get(k).replace("https:/", absPath)
                                                        .replace("http:/", absPath).replace("%", "%25");
                                                if (urlReemplazar.endsWith("/")) {
                                                    reemplazo += "index.html";
                                                }
                                                System.out.println("UrlReemplazar: " + urlReemplazar);
                                                System.out.println("UrlReemplazo: " + reemplazo);
                                                linea = linea.replaceFirst(urlReemplazar, reemplazo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        fos.write(linea.getBytes());    
                    }
                    
                }
            }else{
                int contador = 0;
                while (contador < length) {
                    byte[] b = new byte[65535];
                    int t = dis.read(b);
                    fos.write(b, 0, t);
                    contador += t;
                }
            }
            
            fos.close();
            dis.close();
        }catch(Exception e){
            System.out.println("Error al descargar archivo");
            e.printStackTrace();
        }
    }


    private static Set<String> findLinks(String url, String dominio) throws java.io.IOException {
        Set<String> links = new HashSet<>();
        Document doc = Jsoup.connect(url)
                .timeout(3000)
                .get();
        Elements elements = doc.select("*[href]");
        Elements elements2 = doc.select("*[src]");
        for (Element element : elements) {
            String rutaAbsoluta = element.absUrl("href");
            if(rutaAbsoluta.contains(dominio)){
                links.add(rutaAbsoluta);
            }
        }
         for (Element element : elements2) {
            String rutaAbsoluta = element.absUrl("src");
            if(rutaAbsoluta.contains(dominio)){
                links.add(rutaAbsoluta);
            }
        }
        
        return links; 
    }
}

// file:///C:/Users/52552/Desktop/ESCOM/Sexto%20Semestre/Aplicaciones%20para%20comunicaciones%20en%20red/Practicas-Redes/Practica5/Proyecto5/148.204.58.221C:/Users/52552/Desktop/ESCOM/Sexto%20Semestre/Aplicaciones%20para%20comunicaciones%20en%20red/Practicas-Redes/Practica5/Proyecto5/148.204.58.221C:/Users/52552/Desktop/ESCOM/Sexto%20Semestre/Aplicaciones%20para%20comunicaciones%20en%20red/Practicas-Redes/Practica5/Proyecto5/148.204.58.221C:/Users/52552/Desktop/ESCOM/Sexto%20Semestre/Aplicaciones%20para%20comunicaciones%20en%20red/Practicas-Redes/Practica5/Proyecto5/148.204.58.221C:/Users/52552/Desktop/ESCOM/Sexto%20Semestre/Aplicaciones%20para%20comunicaciones%20en%20red/Practicas-Redes/Practica5/Proyecto5/148.204.58.221C:/Users/52552/Desktop/ESCOM/Sexto%20Semestre/Aplicaciones%20para%20comunicaciones%20en%20red/Practicas-Redes/Practica5/Proyecto5/148.204.58.221C:/Users/52552/Desktop/ESCOM/Sexto%20Semestre/Aplicaciones%20para%20comunicaciones%20en%20red/Practicas-Redes/Practica5/Proyecto5/148.204.58.221C:/Users/52552/Desktop/ESCOM/Sexto%20Semestre/Aplicaciones%20para%20comunicaciones%20en%20red/Practicas-Redes/Practica5/Proyecto5/148.204.58.221/axel/aplicaciones/22-2/?C=D;O=Aindex.html22-2/?C=S;O=Aindex.html22-2/?C=N;O=Aindex.html22-2/practicas/index.html22-2/Aplicaciones_Encuadre.pdfindex.html22-2/Ligas%2520clases_22-2.pdfindex.html22-2/?C=M;O=Aindex.htmlindex.html
// file:///C:/Users/52552/Desktop/ESCOM/Sexto%20Semestre/Aplicaciones%20para%20comunicaciones%20en%20red/Practicas-Redes/Practica5/Proyecto5/148.204.58.221/axel/aplicaciones/index.html