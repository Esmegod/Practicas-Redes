/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.proyecto5;

import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author 52552
 */
public class Cliente {
    static int distancia = 3;
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

  
    public static int pedirRecurso(String recursoURL, int nivel){
        if(nivel > distancia)
            return 0;
        try{
            URL url = new URL(recursoURL);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            //Escribe en el flujo el archivo dependiendo de la url 
            DataInputStream dis = new DataInputStream(httpUrlConnection.getInputStream());
            int code = httpUrlConnection.getResponseCode();
            int length = httpUrlConnection.getContentLength();
            String type = httpUrlConnection.getContentType();
            if(code == 200){ //ok
                if(!type.contains("html")){//Es un recurso(pdf, jpeg, etc)
                    if(!arrD.contains(recursoURL)){
                        //http://148.204.58.221/axel/aplicaciones/22-2/practicas/practica3_chat.pdf
                        String nombre = "Archivo.pdf";
                        new Descargar(dis, nivel+1, nombre).start();
                    }
                    
                }else{//Es html
                    if(!arrPV.contains(recursoURL)){//Descarga el html y busca "a" e "img"
                        
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Error al conectarse");
            e.printStackTrace();
        }
        return 0;
    }
}
  
