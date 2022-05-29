/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.proyecto5;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author 52552
 */
public class Cliente {
    static int distancia = 2;
    static ArrayList<String> arrD = new ArrayList<String>();
    static ArrayList<String> arrPV = new ArrayList<String>(); //Arreglo con rutas absolutas

    public static void main(String[] args) {
        //wget -r -t 10 --tries http://148.204.58.221
        //wget -r -t 10 --tries https://www.escom.ipn.mx/docs/slider/cartelExpoESCOM2022.pdf
        System.out.println("Ingrese el comando");
        Scanner leer = new Scanner(System.in);
        String comando = leer.nextLine();
        leer.close();
        String lineas [] = comando.split(" ");
        String urlString = lineas[lineas.length-1]; 
        // distancia = ;
        pedirRecurso(urlString, 0);
// 
    }

    /*
        int func buscarRecursos(recurso (a o img), nivel){
            Petición get 

            if(nivel>distancia){
                return 0;
            }
            else{	
                if(no es html){
                    busca si no existe en arrD[]
                    si no existe
                        descarga y agrega a arrD
                    si existe 
                        return 0;
                }
                else{ //si es html
                    busca si no existe en arrPV[] //busca que no la hayamos visitado antes 
                    si no la hemos visitado antes 
                        busca todas la <A> e IMG
                        para cada <A> e IMG manda a llamar a la misma funcion buscarRecursos( <A> o img)
                }
            }
        }
    */
    
    public static int pedirRecurso(String recursoURL,  int nivel){
        if(nivel > distancia)
            return 0;
        try{
            if(!recursoURL.endsWith("/") && !recursoURL.contains(".")){
                recursoURL+="/";
            }
            
            URL url = new URL(recursoURL);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            int code = httpUrlConnection.getResponseCode();
            int length = httpUrlConnection.getContentLength();
            String type = httpUrlConnection.getContentType();
            
            System.out.println("lenght: " + length);
            System.out.println("type: " + type);
            
            if(code == 200){ //ok
                if(!type.contains("html")){//Es un recurso(pdf, jpeg, etc)
                    if(!arrD.contains(recursoURL)){
                        //https://www.escom.ipn.mx/docs/slider/cartelExpoESCOM2022.pdf
                        //Se convierte en rutaAbsoluta/pagina/docs/slider/cartelExpoESCOM2022.pdf
                        String absPath = new File("").getAbsolutePath().replace("\\", "/");
                        String host = url.getHost();
                        String path = url.getPath();
                        String nombre = absPath+"/"+host+path;
                        System.out.println(nombre);
                        new Descargar(recursoURL, length, nombre).start();
                        arrD.add(recursoURL);
                    }
                    else return 0; 
                }else{//Es html
                    if(!arrPV.contains(recursoURL)){//Descarga el html y busca referencias
                        //Crea ruta para el almacenamiento local 
                        String absPath = new File("").getAbsolutePath().replace("\\", "/");      
                        String host = url.getHost();
                        String path = url.getPath();
                        if(!path.contains(".")){
                            path+="index.html";
                        }
                        String nombre = absPath+"/"+host+path;
                        System.out.println("Ubicacion: " + nombre);
                        new Descargar(recursoURL, length, nombre).start();
                        //Obtener los links 
                        for (String link : findLinks(recursoURL, host)) {
                            System.out.println(link);
                            pedirRecurso(link, nivel+1);
                            arrPV.add(link);
                        }
                        
                        
                    }
                }
            }else{
                System.out.println("Error code: " + httpUrlConnection.getResponseCode() + " con url " + recursoURL);
            }
        }catch(Exception e){
            System.out.println("Error al conectarse");
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }
    
     private static Set<String> findLinks(String url, String dominio) throws java.io.IOException {
        Set<String> links = new HashSet<>();
        Document doc = Jsoup.connect(url)
                .timeout(3000)
                .get();
        Elements elements = doc.select("a[href]");
        Elements elements2 = doc.select("img[src]");
        Elements elements3 = doc.select("link[href]");
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
        for (Element element : elements3) {
         String rutaAbsoluta = element.absUrl("href");
            if(rutaAbsoluta.contains(dominio)){
                links.add(rutaAbsoluta);
            }
        }
        return links; 
    }
}
  
