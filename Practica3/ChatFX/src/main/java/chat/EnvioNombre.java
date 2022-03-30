package chat;
import java.net.MulticastSocket;

public class EnvioNombre extends Thread{
    String usuario;
    MulticastSocket m;

    public EnvioNombre(String usuario, MulticastSocket m){
        this.usuario = usuario;
        this.m = m;
    }

    public void run() {
        
    }
    
}
