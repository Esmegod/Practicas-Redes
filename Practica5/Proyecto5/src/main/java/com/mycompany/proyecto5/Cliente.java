/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.proyecto5;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author 52552
 */
public class Cliente {

    public static void main(String[] args) {
        //wget -r -t 10 --tries http://unapagina.io
        System.out.println("Ingrese el comando");
        Scanner leer = new Scanner(System.in);
        String comando = leer.nextLine();
        leer.close();
        String lineas [] = comando.split(" ");
        String url = lineas[lineas.length-1]; 
        
        try{
            InetAddress ip = InetAddress.getByName(url);
            Socket socket = new Socket();
            
        }catch(Exception e){
            System.out.println("Error al conectarse");
            e.printStackTrace();
        }
    

    }
}
