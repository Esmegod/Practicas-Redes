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
                        if(linea.contains("src=")){
                            int i = linea.indexOf("src=");
                            int j = linea.indexOf("\"", i+5);
                            String urlReemplazar = linea.substring(i+5, j);
                            String urlLink = urlReemplazar.replace("../", "").replace("./", "");
                            for(int k=0; k<arrD.size(); k++){ 
                                if(arrD.get(k).contains(urlLink)){
                                    String reemplazo = arrD.get(k).replace("https:/", absPath).replace("http:/", absPath);
                                    linea = linea.replace(urlReemplazar, reemplazo);
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
