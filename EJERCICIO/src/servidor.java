/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * @author Esmeralda Godinez Montero
 */
public class servidor {
    public static void main(String[] args){
        try{
            ServerSocket servidor = new ServerSocket(8091);
            servidor.setReuseAddress(true); //no se para que es esto 
            System.out.println("Servidor iniciado ... esperando clientes");
            while(true){
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado desde " + cliente.getInetAddress() + ":" + cliente.getPort());
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(cliente.getOutputStream()));
                pw.println("no fer");
                pw.flush();
                pw.close();
                cliente.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
        
}
