package com.mycompany.proyecto5;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Descargar extends Thread{
    String recursoURL;
    int length;
    String nombre;
    
    public Descargar(String recursoURL, int length, String nombre){
        this.recursoURL = recursoURL; 
        this.length = length;
        this.nombre = nombre;
        System.out.println(nombre);
    }
    
    public void run(){
        try{
            URL url = new URL(recursoURL);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            DataInputStream dis = new DataInputStream(httpUrlConnection.getInputStream());
            
            // D:/Fer_Mtz/Desktop/3CM2/Aplicaciones Red/Practicas-Redes/Practica5/Proyecto5/148.204.58.221/icons/back.gif
            
            int contador = 0;
            File file = new File(nombre);
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            while(contador < length || length==-1){
                byte[] b = new byte[65535];
                int t = dis.read(b);
                if(t == -1){
                    break;
                }
                fos.write(b,0,t);
                contador+=t;
            }
            fos.close();
            dis.close();
        }catch(Exception e){
            System.out.println("Error al descargar archivo");
            e.printStackTrace();
        }
    }
}
