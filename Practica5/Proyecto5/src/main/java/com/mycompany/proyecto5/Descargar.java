package com.mycompany.proyecto5;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;

public class Descargar extends Thread{
    DataInputStream dis;
    int length;
    String nombre;
    
    public Descargar(DataInputStream dis, int length, String nombre){
        this.dis = dis; 
        this.length = length;
        this.nombre = nombre;
    }
    
    public void run(){
        try{
            int contador = 0;
            FileOutputStream fos = new FileOutputStream(new File(nombre));
            while(contador < length){
                byte[] b = new byte[65535];
                int t = dis.read(b);
                fos.write(b,0,t);
                contador+=t;
            }
            fos.close();
        }catch(Exception e){
            System.out.println("Error al descargar archivo");
            e.printStackTrace();
        }
         
    }
}
