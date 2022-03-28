package chat;

import java.net.InetAddress;
import java.net.MulticastSocket;

public class Socket {
    
     public MulticastSocket conectarse(){
        int puerto = 4000;
        MulticastSocket m = null;
        InetAddress ip = null;
        try{
            ip  = InetAddress.getByName("230.1.1.1");
            m = new MulticastSocket(puerto);
            m.setReuseAddress(true);
            m.setTimeToLive(255);
            m.joinGroup(ip);
        }catch(Exception e){
            e.printStackTrace();
        }
        return m;
    }
     
}
