import java.net.*;
import java.util.ArrayList;
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
            cl.setSoTimeout(5000);
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

    public void enviarCoordenadas(int x, int y) {
        try{            
           //Enviar paquete
            ByteArrayOutputStream baos  = new ByteArrayOutputStream();
            DataOutputStream envioC = new DataOutputStream(baos);
            envioC.writeBoolean(false); //InitGame
            envioC.writeInt(x);
            envioC.writeInt(y);
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

    public ArrayList<Celda>  recibirCeldas() {
        ArrayList<Celda> celdas = new ArrayList<Celda>();
        try{
            byte[] b = new byte[65535];
            DatagramPacket p = new DatagramPacket(b, b.length);
            this.cl.receive(p);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(p.getData()));
            Object obj = ois.readObject(); //Se lee el objeto
            if(obj instanceof ArrayList<?>){ //Se verifica si el objeto recibido es de tipo ArrayList
                for(Object o: (ArrayList<?>)obj){ //Se itera cada elemento del ArrayList
                    celdas.add((Celda)o); //Se castea cada elemento del arrayList a tipo Celda
                }
            }
            ois.close();
        }catch(Exception e){
            System.out.println("Error al recibir celdas");
        }
        return celdas;
    }
    
}
