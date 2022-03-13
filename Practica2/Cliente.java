import java.net.*;
import java.io.*;

public class Cliente{
    int puerto;
    InetAddress ip;
    DatagramSocket cl;
    
    public Cliente(int puerto, String ipS){
        
        try{
            cl = new DatagramSocket();
            this.puerto = puerto;
            ip = InetAddress.getByName(ipS);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public void enviarPaquete(int x, int y) {
            
        try{            
           //Enviar paquete
 
        }catch(Exception e){
            System.out.println("Error al ejecutar Buscaminas");
            e.printStackTrace();
        }
        
        
    }
}
