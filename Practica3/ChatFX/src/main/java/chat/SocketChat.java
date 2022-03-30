package chat;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SocketChat {
    
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
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream dos = new ObjectOutputStream(baos);
            
            String usuarios = "Todos";
            int tipo = 2;
            String anuncio = "<div class='msj'><p class='nombreAnuncio'>Anuncio</p><div class='flex'>"+
                        "<img src='https://raw.githubusercontent.com/Esmegod/Practicas-Redes/main/Practica3/ChatFX/img/altavoz.png' alt='usuario' class='avatar'>"+
                        "<div class='anuncio'> " + usuario + " se ha unido al chat</div>"+
                        "</div></div>";
            dos.writeUTF(usuarios);
            dos.writeInt(tipo);
            dos.writeUTF(anuncio);
            dos.writeUTF(usuario);
            dos.flush();
            DatagramPacket p = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, ip, 4000);
            m.send(p);
            dos.close();
            baos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return m;
    }
     
}
