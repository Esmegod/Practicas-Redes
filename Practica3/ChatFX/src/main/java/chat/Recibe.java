package chat;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

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
    ArrayList<String> usuariosConectados = new ArrayList<>();
    String encabezadoConectados  ="<head><style>"+
        "*{font-family: Arial, Helvetica, sans-serif;}"+
        ".flex{display: flex;flex-direction: row;align-items: center;justify-content: left;}"+
        ".punto{background-color: #32cb00;height: 8px;width: 8px;border-radius: 100%;margin-right: 10px;}"+
        ".conectado{margin: 10px;}"+
        "</style></head><p><b>Usuarios conectados</b></p>"; 

    String mensajeMedioConectados = "";
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
                ObjectInputStream dis = new  ObjectInputStream(new ByteArrayInputStream(p.getData()));
                String u =  dis.readUTF();     
                int tipoMensaje = dis.readInt();
                String mensaje_recibido = "";
                if(tipoMensaje != 5){
                    mensaje_recibido = dis.readUTF();
                }
                //Se valida el destinatario
                if(!u.equals("Todos")){ //Va dirigido a alguien en especifico 
                    if(u.equals(usuario)){//Se valida que sea a mi usuario
                        if(tipoMensaje == 1){//se despliega el mensaje privado
                            
                        }else{//Mensaje de tipo 5 (Se actualiza el html y combox)
                            usuariosConectados.clear();
                            Object obj = dis.readObject();
                            if(obj instanceof ArrayList<?>){
                                for(Object o: (ArrayList<?>)obj){
                                    usuariosConectados.add((String)o);
                                }
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run(){
                                    for(int i =0; i<usuariosConectados.size(); i++){
                                        mensajeMedioConectados += "<div class='conectado flex'><div class='punto'></div>"+
                                            "<div class='nombreConectado'>"+usuariosConectados.get(i)+"</div></div>";
                                    }
                                    webEngineUsuarios.loadContent(encabezadoConectados+mensajeMedioConectados);
                                    mensajeMedioConectados ="";
                                }}
                             );
                        }
                    }
                }else{//Se lee el mensaje
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
                        String nuevoUsuario = dis.readUTF();
                        usuariosConectados.add(nuevoUsuario);
                        if(usuariosConectados.size()>1){
                            if(usuariosConectados.get(usuariosConectados.size()-2).equals(usuario)){
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ObjectOutputStream oos = new ObjectOutputStream(baos);
                                oos.writeUTF(nuevoUsuario);
                                oos.writeInt(5);
                                oos.writeObject(usuariosConectados);
                                oos.flush();
                                InetAddress grupo = InetAddress.getByName("230.1.1.1");
                                DatagramPacket paqueteLista = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, grupo,4000);
                                socket.send(paqueteLista);
                                oos.close();
                                baos.close();
                            }
                        }
                        
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run(){
                                webEngine.loadContent(mensaje);
                                for(int i =0; i<usuariosConectados.size(); i++){
                                    mensajeMedioConectados += "<div class='conectado flex'><div class='punto'></div>"+
                                        "<div class='nombreConectado'>"+usuariosConectados.get(i)+"</div></div>";
                                }
                                webEngineUsuarios.loadContent(encabezadoConectados+mensajeMedioConectados);
                                mensajeMedioConectados ="";
                            }}
                         ); 

                         //Aqui ocurre
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