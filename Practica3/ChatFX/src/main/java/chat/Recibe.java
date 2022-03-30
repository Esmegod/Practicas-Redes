package chat;


import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import javax.swing.JEditorPane;

class Recibe extends Thread {
    MulticastSocket socket;
    JEditorPane editorPane;
    WebEngine webEngine;
    WebEngine webEngineUsuarios;
    String usuario;
    String mensaje = "<head><style>"+
        "*{font-family: Arial, Helvetica, sans-serif;}"+
        ".nombre{padding-left: 55px;color:  #b0b0b8;margin-bottom: 5px;}"+
        ".avatar{height: 30px;width: 30px;padding: 5px;border-radius: 100%;}"+
        ".mensaje{border-radius: 20px;background-color: #dff0ff;padding: 10px;margin-left: 10px;}"+
        ".anuncio{border-radius: 20px;background-color: #FFFFFF;padding-left:10px; margin-left: 10px; color:#97979E; font-weight:600;}"+
        ".nombreAnuncio{padding-left: 60px; color: #CD0000; font-weight:700; margin-bottom: 0px;}"+
        ".flex{display: flex;flex-direction: row;align-items: center;justify-content: left;}" +
        "</style></head>"; 
    String mensajeUsuarios = "";
    public Recibe(MulticastSocket m, WebEngine webEngine, WebEngine webEngineUsuarios, String usuario) {
        this.socket = m;
        this.webEngine = webEngine;
        this.webEngineUsuarios = webEngineUsuarios;
        this.usuario = usuario;
    }

    public void run() {
        System.out.println("Ha iniciado el hilo recibe");
        try {
            
            for (;;) {
                DatagramPacket p = new DatagramPacket(new byte[65535], 65535);
                socket.receive(p);
                DataInputStream dis = new  DataInputStream(new ByteArrayInputStream(p.getData()));
                String u =  dis.readUTF();     
                int tipoMensaje = dis.readInt();
                String mensaje_recibido = dis.readUTF();
                //Se valida el destinatario
                if(!u.equals("Todos")){ //Va dirigido a alguien en especifico 
                    if(u.equals(usuario)){//Se despliega el mensaje privado
                        
                    }
                }else{//Leo el mensaje
                    if(tipoMensaje == 1){//Es un mensaje normal
                        mensaje += mensaje_recibido;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run(){
                                webEngine.loadContent(mensaje);
                            }}
                        );  
                    }else if(tipoMensaje == 2){//Anuncio para el chat
                        mensaje += mensaje_recibido;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run(){
                                webEngine.loadContent(mensaje);
                            }}
                         ); 
                    }else if(tipoMensaje == 3){//Recibe nombre
                        
                    }else if(tipoMensaje == 4){//Anuncio para decir goodbye :c

                    }
                }  
                
            

                /*
                //String mensaje_recibido = new String(p.getData(), 0, p.getLength());
                if(mensaje_recibido.contains("se ha unido al chat")){//Es un anuncio de ingreso
                    mensaje_medio = "<div class='msj'><p class='nombreAnuncio'>Anuncio</p><div class='flex'>"+
                    "<img src='https://raw.githubusercontent.com/Esmegod/Practicas-Redes/main/Practica3/ChatFX/img/altavoz.png' alt='usuario' class='avatar'>"+
                    "<div class='anuncio'> " + mensaje_recibido + "</div>"+
                    "</div></div>";
                    // se manda la lista si fuiste el ultimo en conectarte 
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run(){
                            String usuario = mensaje_recibido.substring(0, mensaje_recibido.indexOf("se ha unido al chat"));
                            String mensaje_medioUsuarios = "<p>"+usuario+" conectado</p>";
                            mensajeUsuarios +=  mensaje_medioUsuarios;
                            webEngineUsuarios.loadContent(mensajeUsuarios);
                        }}
                    );  
                }else{//Es un mensaje normal
                    mensaje_medio = mensaje_recibido;  
                }
                mensaje = mensaje + mensaje_medio;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run(){
                        webEngine.loadContent(mensaje);
                    }}
                );*/
                dis.close();
             }
        }catch (Exception e) {
            System.out.println("Error en hilo recibir");
            e.printStackTrace();
        } 
    }
}