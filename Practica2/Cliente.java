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
            this.ip = InetAddress.getByName(ipS);
            initJuego();
        }
        catch(Exception e){
            e.printStackTrace();
        } 
    }

    public void initJuego(){
        try{            
           //Enviar paquete
            ByteArrayOutputStream baos  = new ByteArrayOutputStream();
            DataOutputStream envioC = new DataOutputStream(baos);
            envioC.writeBoolean(true); //initGame
            envioC.flush();
            DatagramPacket p = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, this.ip, this.puerto);
            cl.send(p);
            baos.close();
            envioC.close();
        }catch(Exception e){
            System.out.println("Error al enviar paquete inicial");
            e.printStackTrace();
        }
    }

    public void enviarCoordenadas(int x, int y, boolean marcaBandera) {
        try{            
           //Enviar paquete
            ByteArrayOutputStream baos  = new ByteArrayOutputStream();
            DataOutputStream envioC = new DataOutputStream(baos);
            envioC.writeBoolean(false); //InitGame
            envioC.writeInt(x);
            envioC.writeInt(y);
            envioC.writeBoolean(marcaBandera);
            envioC.flush();
            DatagramPacket p = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, this.ip, this.puerto);
            cl.send(p);
            baos.close();
            envioC.close();
        }catch(Exception e){
            System.out.println("Error al enviar coordenadas");
            e.printStackTrace();
        }
    }

    
}
