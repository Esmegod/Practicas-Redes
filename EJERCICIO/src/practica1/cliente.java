/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Esmeralda Godinez Montero
 */
public class cliente {
    public static void main(String[] args){
        try{
            String dir = "189.242.207.192"; //Dirección IPv4 
            String dir2 = "2806:106e:d:f59e:7545:3f26:ae5d:96d"; //Dirección IPv6
        
            int puerto = 1234; // ¿Por qué se utilizó ese puerto?
            Socket cliente = new Socket(dir2, puerto);
            BufferedReader br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            String mensaje = br.readLine();
            System.out.println("Mensaje recibido: " + mensaje);
            br.close();
            cliente.close();
        }   
        catch(Exception e){
            e.printStackTrace();
        } 
    }
}
