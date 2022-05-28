/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.proyecto5;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author 52552
 */
public class Cliente {
    int distancia = 3;

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

    ArrayList<String> arrD;
    ArrayList<String> arrPV = new ArrayList<String>();
    public int pedirRecurso(String recursoURL, int nivel){

        if(nivel > distancia)
            return 0;
        
        try{
            URL url = new URL(recursoURL);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
           
            //Escribe en el flujo el archivo dependiendo de la url 
            DataInputStream dis = new DataInputStream(httpUrlConnection.getInputStream());
            int length = httpUrlConnection.getContentLength();
            String type = httpUrlConnection.getContentType();
            int code = httpUrlConnection.getResponseCode();
            if(code == 200){

                if(!type.contains("html")){//No es html
                    // String [] urlSeparada = recursoURL.split("/");
                    // String nombre = 
                    // if(!arrPV.contains(recursoURL)){
                    //     descargaArchivo(nombre, length, dis);
                    // }
                    
                }else{//Es html
                    if(!arrPV.contains(recursoURL)){
                        // descargaArchivo(nombre, length, dis);
                    }
                }
                
                

            }else{
                System.out.println("Code != 200");
            }

            
        }catch(Exception e){
            System.out.println("Error al conectarse");
            e.printStackTrace();
        }
        
    }
    

    public static void descargaArchivo(String nombre, int length, DataInputStream dis) throws IOException{
            int contador = 0;
            FileOutputStream fos = new FileOutputStream(new File(nombre));
            while(contador < length){
                byte[] b = new byte[65535];
                int t = dis.read(b);
                fos.write(b,0,t);
                contador+=t;
            }
            fos.close();
    }
}
