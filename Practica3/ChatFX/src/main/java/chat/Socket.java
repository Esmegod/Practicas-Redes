package chat;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Socket {
    
     public MulticastSocket conectarse(String usuario){
        int puerto = 4000;
        MulticastSocket m = null;
        InetAddress ip = null;
        try{
            ip  = InetAddress.getByName("230.1.1.1");
            m = new MulticastSocket(puerto);
            m.setReuseAddress(true);
            m.setTimeToLive(255);
            m.joinGroup(ip);
            
            String anuncio = usuario + "se ha unido al chat";
            byte [] b = anuncio.getBytes();
            DatagramPacket p = new DatagramPacket(b, b.length);
            m.send(p);
        }catch(Exception e){
            e.printStackTrace();
        }
        return m;
    }
     
}
