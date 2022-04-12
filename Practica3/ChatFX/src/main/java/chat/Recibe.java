package chat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;

public class Recibe extends Thread {
    MulticastSocket socket;
    JEditorPane editorPane;
    WebEngine webEngine;
    WebEngine webEngineUsuarios;
    String usuario;
    String mensaje = ""; 
    String mensajeUsuarios = "";
    ArrayList<String> usuariosConectados = new ArrayList<>();
    JComboBox<String> combobox;
    ByteArrayOutputStream audioBytes = new ByteArrayOutputStream();
   
    
    String encabezadoConectados  ="<head><style>"+
        "@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;500;700&display=swap');"+
        "*{font-family: 'Roboto', Arial, Helvetica, sans-serif;}"+
        ".flex{display: flex;flex-direction: row;align-items: center;justify-content: left;}"+
        ".punto{background-color: #32cb00;height: 8px;width: 8px;border-radius: 100%;margin-right: 10px;}"+
        ".conectado{margin-bottom: 7px; margin-left: 12px;}"+
        ".nombreConectado{font-size: .8em; color:#808080;}"+ 
        ".conectadosTitulo{padding-left:70px; font-weight: 500; font-size: .9em; margin-bottom: 10px;}"+
        "</style></head><p class='conectadosTitulo'>En línea</p>"; 

    String mensajeMedioConectados = "";
    public Recibe(MulticastSocket m, WebEngine webEngine, WebEngine webEngineUsuarios, String usuario, JComboBox<String> combobox) {
        this.socket = m;
        this.webEngine = webEngine;
        this.webEngineUsuarios = webEngineUsuarios;
        this.usuario = usuario;
        File f = new File("");
        String ruta = f.getAbsolutePath();
        System.out.println(ruta);
        this.mensaje = "<head><base href=\"file:"+ruta+"\\\"><style>"+
       "@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;500;700&display=swap');"+
        "*{font-family: 'Roboto', Arial, Helvetica, sans-serif;}"+
        ".nombre{padding-left: 55px;color:  #b0b0b8;margin-bottom: 5px;}"+
        ".avatar{height: 30px;width: 30px;padding: 5px;border-radius: 100%;}"+
        ".mensaje{border-radius: 20px;background-color: #dff0ff;padding: 10px;margin-left: 10px; max-width: 400px; font-weight: 500;}"+
        ".anuncio{border-radius: 20px;background-color: #FFFFFF;padding-left:10px; margin-left: 10px; color:#97979E; font-weight:500; font-size: 1rem;}"+
        ".nombreAnuncio{padding-left: 60px; color: #CD0000; font-weight:700; margin-bottom: 0px;}"+
        ".flex{display: flex;flex-direction: row;align-items: center;justify-content: left;}" +
        ".msj{margin: 10px;}"+
        ".emoji{padding:0px 5px 0px 5px;}"+
        "audio::-webkit-media-controls-panel{background-color: #dff0ff;}"+
        "</style>"+
        "<script>function toBottom(){window.scrollTo(0, document.body.scrollHeight);}</script>"
        +"</head><body onload='toBottom()'>";
        this.combobox = combobox;
    }

    public void run() {
        System.out.println("Ha iniciado el hilo recibe");
        try {   
            for (;;) {
                DatagramPacket p = new DatagramPacket(new byte[65535], 65535);
                socket.receive(p);
                ObjectInputStream dis = new  ObjectInputStream(new ByteArrayInputStream(p.getData()));
                String destinatario =  dis.readUTF();
                String remitente = dis.readUTF();     
                int tipoMensaje = dis.readInt();
                String mensaje_recibido = dis.readUTF();
                Object obj = dis.readObject();
                
       
                if(!destinatario.equals("Todos")){ //Es mensaje privado
                    if(tipoMensaje == 1){
                        if(destinatario.equals(usuario) || remitente.equals(usuario)){//Se valida el usuario
                            mensaje += mensaje_recibido;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run(){
                                    webEngine.loadContent(mensaje);
                                }}); 
                        }
                    }else if(tipoMensaje==5){//Mensaje de tipo 5 (Se actualiza la lista de usuarios (en HTML y combobox)
                        if(destinatario.equals(usuario)){
                            usuariosConectados.clear();
                            if(obj instanceof ArrayList<?>){
                                for(Object o: (ArrayList<?>)obj){
                                    usuariosConectados.add((String)o);
                                }
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run(){
                                    combobox.removeAllItems();
                                    combobox.addItem("Todos");
                                    for(int i =0; i<usuariosConectados.size(); i++){
                                        mensajeMedioConectados += "<div class='conectado flex'><div class='punto'></div>"+
                                            "<div class='nombreConectado'>"+usuariosConectados.get(i)+"</div></div>";
                                            combobox.addItem(usuariosConectados.get(i));
                                    }
                                    webEngineUsuarios.loadContent(encabezadoConectados+mensajeMedioConectados);
                                    mensajeMedioConectados ="";
                                }
                            });
                        }                        
                    }
                    else{//Es mensaje privado de tipo 3 
                        if(destinatario.equals(usuario) || remitente.equals(usuario){
                            String nombre = recibeAudio(dis);
                            if(nombre != null){
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run(){
                                    System.out.println(nombre);
                                    String mensajeAudio = "<div class='msj'><p class='nombre'>"+remitente+" ha enviado un mensaje para " + destinatario + "</p><div class='flex'>"+
                                    "<img src='img\\ondas.png' alt='usuario' class='avatar'>"+
                                    "<div class='mensaje flex'><audio controls><source src='Audios"+usuario+"\\"+nombre+"' type=\"audio/mp3\">Not Supported</audio></div>"+
                                    "</div></div>";           
                                    mensaje += mensajeAudio;
                                    webEngine.loadContent(mensaje); 
                                }}); 
                            }
                        }
                        
                    }
                }else{//Es para todos
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
                        usuariosConectados.add(remitente);
                        if(usuariosConectados.size()>1){
                            if(usuariosConectados.get(usuariosConectados.size()-2).equals(usuario)){
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ObjectOutputStream oos = new ObjectOutputStream(baos);
                                oos.writeUTF(remitente);
                                oos.writeUTF(usuario);
                                oos.writeInt(5);
                                oos.writeUTF("");
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
                                //String pruebaImage = "<img src='file:D:\\Fer_Mtz\\Desktop\\mapache.png' alt='imagen'/>";
                                //String url  = "file:" + new File("img/mapache.png").getAbsolutePath();
                                // String pruebaImage = "<img src='tempChat\\mapache.png' alt='imagen'/>";
                                // String pruebaAudio = "<br><audio controls><source src='tempChat\\Magic.mp3' type=\"audio/mp3\">Not Supported</audio>";
                                combobox.removeAllItems();
                                combobox.addItem("Todos");
                                webEngine.loadContent(mensaje);
                                for(int i =0; i<usuariosConectados.size(); i++){
                                    mensajeMedioConectados += "<div class='conectado flex'><div class='punto'></div>"+
                                        "<div class='nombreConectado'>"+usuariosConectados.get(i)+"</div></div>";
                                        combobox.addItem(usuariosConectados.get(i));
                                }
                                webEngineUsuarios.loadContent(encabezadoConectados+mensajeMedioConectados);
                                mensajeMedioConectados ="";
                            }}); 
                    }else if(tipoMensaje == 3){//Recibe audiio  
                        String nombre = recibeAudio(dis);
                        if(nombre != null){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run(){
                                System.out.println(nombre);
                                String mensajeAudio = "<div class='msj'><p class='nombre'>"+remitente+" ha enviado un mensaje para todos</p><div class='flex'>"+
                                "<img src='img\\ondas.png' alt='usuario' class='avatar'>"+
                                "<div class='mensaje flex'><audio controls><source src='Audios"+usuario+"\\"+nombre+"' type=\"audio/mp3\">Not Supported</audio></div>"+
                                "</div></div>";           
                                mensaje += mensajeAudio;
                                webEngine.loadContent(mensaje); 
                            }}); 
                        }
                        
                    }else if(tipoMensaje == 4){//Anuncio para decir goodbye :c

                    }
                }  
                dis.close();
            }
        }catch (Exception e) {
            System.out.println("Error en hilo recibir");
            e.printStackTrace();
        } 
    }

    public String recibeAudio(ObjectInputStream dis){
        String nombreReturn = null;
        try{
            String nombre = dis.readUTF();
            boolean esExacto = dis.readBoolean();
            int noPaquete = dis.readInt();
            int totalPaquetes = dis.readInt();
            int tamPaquete = dis.readInt();
            byte[] bMsj = new byte[tamPaquete];
            dis.read(bMsj,0,tamPaquete);
            
            if((!esExacto && noPaquete==totalPaquetes) || (esExacto && noPaquete == totalPaquetes-1) || totalPaquetes==1){
                audioBytes.write(bMsj,0, bMsj.length);
                audioBytes.flush();
                File audioFile = new File("Audios"+usuario+"/"+nombre);
                FileOutputStream fos = new FileOutputStream(audioFile);
                fos.write(audioBytes.toByteArray());
                fos.close();
                audioBytes.reset();
                nombreReturn = nombre;
            }
            else{
                audioBytes.write(bMsj,0, bMsj.length);
                audioBytes.flush();
            }
         
        }catch(Exception e){
            System.out.println("Error al recibir paquete");
        }
        return nombreReturn;
    }
}