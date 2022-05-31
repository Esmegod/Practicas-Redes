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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author 52552
 */
public class Cliente {
    static final ArrayList<String> arrD = new ArrayList<String>();
    static ArrayList<String> arrPV = new ArrayList<String>(); //Arreglo con rutas absolutas
    static ExecutorService pool;
    static int poolSize=15;
    static int distancia = 1;

    public static void main(String[] args) {
        //wget -r 2 -t 15 http://148.204.58.221/axel/aplicaciones/22-2/
        //wget -r 3 -t 25 https://www.escom.ipn.mx
        System.out.println("Ingrese el comando: wget -r produndidad -t hilos url");
        Scanner leer = new Scanner(System.in);
        String comando = leer.nextLine();
        leer.close();
        String comandos[] = comando.split(" ");
        if(!comandos[0].equals("wget")){
            System.out.println("No se reconoce el comando");
            System.exit(0);
        }
        distancia = Integer.parseInt(comandos[2]);
        poolSize = Integer.parseInt(comandos[4]);
        pool = Executors.newFixedThreadPool(poolSize);
        String lineas [] = comando.split(" ");
        String urlString = lineas[lineas.length-1]; 
        // distancia = ;
        pedirRecurso(urlString, 0);
        pool.shutdown();
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
            
            System.out.println(recursoURL);
            System.out.println("lenght: " + length);
            System.out.println("type: " + type);
            if(type == null){
                System.out.println("Error en el encabezado");
                System.out.println("-------------------------------");
                return 0;
            }
            System.out.println("-------------------------------");
            
            if(code == 200){ //ok
                if(!type.contains("html")){//Es un recurso(pdf, jpeg, etc)
                    if(!arrD.contains(recursoURL)){
                        //https://www.escom.ipn.mx/docs/slider/cartelExpoESCOM2022.pdf
                        //Se convierte en rutaAbsoluta/pagina/docs/slider/cartelExpoESCOM2022.pdf
                        String absPath = new File("").getAbsolutePath().replace("\\", "/");
                        String host = url.getHost();
                        String path = url.getPath();
                        String nombre = absPath+"/"+host+path;
                        pool.execute(new Descargar(recursoURL, length, nombre, false));
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
                            path+="/index.html";
                        }
                        String nombre = absPath+"/"+host+path;
                        pool.execute(new Descargar(recursoURL, length, nombre, true));
                        //Obtener los links 
                        for (String link : findLinks(recursoURL, host)) {
                            pedirRecurso(link, nivel+1);
                            arrPV.add(link);
                        }
                        
                        
                    }
                }
            }else{
                System.out.println("Error code: " + httpUrlConnection.getResponseCode() + " con url " + recursoURL);
            }
        }catch(Exception e){
            System.out.println("Error al pedir recurso");
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
  
