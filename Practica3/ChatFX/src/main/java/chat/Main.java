package chat;

import java.net.MulticastSocket;
import javafx.scene.web.WebEngine;
import javax.swing.SwingUtilities;

public class Main {
    static WebEngine webEngine = null;
    
    public static void main(String[] args) throws InterruptedException {
        Socket multicastSocket = new Socket();
        MulticastSocket m = multicastSocket.conectarse();
       
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Chat chat = new Chat(m);
            }
        });
    }
}
