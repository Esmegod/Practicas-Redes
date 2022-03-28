package chat;


import java.net.DatagramPacket;
import java.net.MulticastSocket;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import javax.swing.JEditorPane;

class Recibe extends Thread {
    MulticastSocket socket;
    JEditorPane editorPane;
    WebEngine webEngine;
    String mensaje = "<head><style>"+
        "*{font-family: Arial, Helvetica, sans-serif;}"+
        ".nombre{padding-left: 55px;color:  #b0b0b8;margin-bottom: 5px;}"+
        ".avatar{height: 30px;width: 30px;padding: 5px;border-radius: 100%;}"+
        ".mensaje{border-radius: 20px;background-color: #dff0ff;padding: 10px;margin-left: 10px;}"+
        ".flex{display: flex;flex-direction: row;align-items: center;justify-content: left;}" +
        "</style></head>";


    public Recibe(MulticastSocket m, WebEngine webEngine) {
        this.socket = m;
        this.webEngine = webEngine;
    }

    public void run() {
        System.out.println("Ha iniciado el hilo recibe");
        try {
            String mensaje_medio = "";
            for (;;) {
                DatagramPacket p = new DatagramPacket(new byte[65535], 65535);
                socket.receive(p);
                String mensaje_recibido = new String(p.getData(), 0, p.getLength());
                if(mensaje_recibido.contains("se ha unido al chat")){//Es un anuncio de ingreso
                    
                }else{//Es un mensaje normal
                    mensaje_medio = mensaje_recibido;  
                    mensaje = mensaje+mensaje_medio;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run(){
                            webEngine.loadContent(mensaje);
                        }});
                }
             }
        }catch (Exception e) {
            System.out.println("Erro en hilo recibir");
            e.printStackTrace();
        } 
    }
}