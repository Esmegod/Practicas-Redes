import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import static java.net.StandardSocketOptions.SO_REUSEADDR;
import java.util.*;

public class ServidorWeb1{
    
    public static final int PUERTO=8000;
    ServerSocketChannel ssc;
    
    class Manejador{
        protected SocketChannel socket;
        protected PrintWriter pw;
        protected BufferedOutputStream bos;
        protected BufferedReader br;
        protected String FileName;
        protected String mime;

        public Manejador(SocketChannel _socketChannel) throws Exception{
            this.socket=_socketChannel;
        }

        public void administrarPeticion(){
            try{
                ByteBuffer b = ByteBuffer.allocate(10000000);
                int t = socket.read(b);
                if(t==-1){
                    return;
                }
                String peticion = new String(b.array(),0,t);
                System.out.println("t: " + t);
                System.out.println("Peticion:\n" + peticion);
                
                StringTokenizer st1= new StringTokenizer(peticion,"\n");
                String line = st1.nextToken();

                if(line.indexOf("?")==-1){ //No se solicita un recurso, sino la página principal
                    if(line.toUpperCase().startsWith("GET")){
                        getArch(line, st1);
                        if(FileName.compareTo("")==0){
                            SendA("index.html",mime);
                        }else{
                            SendA(FileName,mime);
                        }
                    }else if(line.toUpperCase().startsWith("POST")){
                        getArch(line, st1);
                        SendA(FileName,mime);
                    }else if(line.toUpperCase().startsWith("PUT")){ 
                        /**
                            Content-Length: 821242
                            Content-Type: application/pdf
                        **/
                        String token = "";
                        int bytes=0;
                        String mimePut = "";
                        while((token = st1.nextToken())!=null){
                            if(token.contains("Content-Length")){
                                String[] s = token.split(":");
                                System.out.println(s[1].trim());
                                bytes = Integer.valueOf(s[1].trim());
                                mimePut = st1.nextToken().split(":")[1].trim();
                                System.out.println(mimePut);
                                break;    
                            }
                        }
                        // Se inicia con la lectura del archivo
                        try{
                            String extension = mimePut.split("/")[1];
                            if(extension.equals("plain"))
                                extension = "txt";
                            st1.nextToken();
                            File f = new File("archivosRecibidos/archivo."+extension);
                            if(!f.getParentFile().exists()) f.getParentFile().mkdir();
                            FileOutputStream fos = new FileOutputStream(f);
                            int pos = t-bytes;   
                            byte[] contenido = Arrays.copyOfRange(b.array(), pos, t); 
                            fos.write(contenido);
                            fos.flush();
                            fos.close();
                            /***********************************************/
                            String sb = "";
                            sb = sb +"HTTP/1.0 201 created\n";
                            sb = sb +"Server: Mesfer Server/1.0 \n";
                            sb = sb +"Date: " + new Date()+" \n";
                            sb = sb +"\n";
                            ByteBuffer resPut = ByteBuffer.wrap(sb.getBytes());
                            socket.write(resPut);
                            /***********************************************/
                        
                        }catch(Exception e){
                            System.out.println("Error en lectura de archivo");
                            e.printStackTrace();
                            /***********************************************/
                            String sb = "";
                            sb = sb +"HTTP/1.0 500 error\n";
                            sb = sb +"Server: Mesfer Server/1.0 \n";
                            sb = sb +"Date: " + new Date()+" \n";
                            sb = sb +"\n";
                            ByteBuffer ePut = ByteBuffer.wrap(sb.getBytes());
                            socket.write(ePut);
                            /***********************************************/
                        }finally{
                            socket.close();
                        }
                        
                    }else if(line.toUpperCase().startsWith("DELETE")){
                        try{
                            getArch(line, st1);
                            File f = new File("archivosRecibidos/"+FileName);
                            if(f.exists() && f.canWrite()){
                                f.delete();
                                /***********************************************/
                                String sb = "";
                                sb = sb +"HTTP/1.0 202 accepted\n";
                                sb = sb +"Server: Mesfer Server/1.0 \n";
                                sb = sb +"Date: " + new Date()+" \n";
                                sb = sb +"\n";
                                ByteBuffer resDelete = ByteBuffer.wrap(sb.getBytes());
                                socket.write(resDelete);
                
                                /***********************************************/
                            }else{
                                //Respuesta de error para eliminar
                                /***********************************************/
                                String sb = "";
                                sb = sb +"HTTP/1.0 204 No content\n";
                                sb = sb +"Server: Mesfer Server/1.0 \n";
                                sb = sb +"Date: " + new Date()+" \n";
                                sb = sb +"\n";
                                ByteBuffer eDelete = ByteBuffer.wrap(sb.getBytes());
                                socket.write(eDelete);
                                /***********************************************/
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                             /***********************************************/
                            String sb = "";
                            sb = sb +"HTTP/1.0 500 error\n";
                            sb = sb +"Server: Mesfer Server/1.0 \n";
                            sb = sb +"Date: " + new Date()+" \n";
                            sb = sb +"\n";
                            ByteBuffer e2Delete = ByteBuffer.wrap(sb.getBytes());
                            socket.write(e2Delete);
                            /***********************************************/
                        }finally{
                            socket.close();
                        }
                    }else if(line.toUpperCase().startsWith("HEAD")){

                    }
                }
                else if(line.toUpperCase().startsWith("GET")){
                            StringTokenizer tokens = new StringTokenizer(line,"?");
                            String req_a = tokens.nextToken();
                            String req = tokens.nextToken();
                            System.out.println("Token1: "+req_a);
                            System.out.println("Token2: "+req);
                            String parametros = req.substring(0, req.indexOf(" "))+"\n";
                            System.out.println("parametros: "+parametros);
                            StringBuffer respuesta = new StringBuffer();

                            respuesta.append("HTTP/1.0 200 Okay \n");
                            String fecha= "Date: " + new Date()+" \n";
                            respuesta.append(fecha);
                            String tipo_mime = "Content-Type: text/html \n\n";
                            respuesta.append(tipo_mime);
                            respuesta.append("<html><head><title>SERVIDOR WEB</title></head>\n");
                            respuesta.append("<body bgcolor=\"#AACCFF\"><center><h1><br>Parametros Obtenidos..</br></h1><h3><b>\n");
                            respuesta.append(parametros);
                            respuesta.append("</b></h3>\n");
                            respuesta.append("</center></body></html>\n\n");
                            System.out.println("Respuesta: "+respuesta);
                            ByteBuffer resGet = ByteBuffer.wrap(respuesta.toString().getBytes());
                            socket.write(resGet);
                            socket.close();        
                    }else{
                           ByteBuffer resDelete = ByteBuffer.wrap("HTTP/1.0 501 Not Implemented\r\n".getBytes());
                            socket.write(resDelete);
                            socket.close();
                    }
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }//run
                        
        public void getArch(String line, StringTokenizer st){
            int i;
            int f;
            if(line.toUpperCase().startsWith("GET")){
                i=line.indexOf("/");
                f=line.indexOf(" ",i);
                FileName=line.substring(i+1,f);
                if(line.contains(".pdf")) mime = "application/pdf";
                else mime = "text/html";
            }
            else if(line.toUpperCase().startsWith("POST")){
                String token;
                while((token = st.nextToken())!=null){
                    if(token.contains("imagen-abril")){
                        //Ttoken = imagen-abril=2022-04-19
                        String[] s = token.split("=");
                        FileName = "abrilNasa/"+s[1] + ".jpg";
                        mime = "image/jpeg";
                        break;
                    }
                }    
            }else if(line.toUpperCase().startsWith("DELETE")){
                /*Datos: DELETE /archivo.pdf HTTP/1.1*/
                i=line.indexOf("/");
                f=line.indexOf(" ",i);
                FileName=line.substring(i+1,f);
            }else if(line.toUpperCase().startsWith("HEAD")){
                // Checar
                // i=line.indexOf("/");
                // f=line.indexOf(" ",i);
                // FileName=line.substring(i+1,f);
                // if(line.contains(".pdf")) mime = "application/pdf";
                // else mime = "text/html";
            }

        }
                        
        public void SendA(String arg, String mime){
            try{
                int b_leidos=0;
                DataInputStream dis2 = new DataInputStream(new FileInputStream(arg));
                byte[] buf = new byte[64];
                int x=0;
                File ff = new File(arg);			
                long tam_archivo = ff.length(),cont=0;
                /***********************************************/
                String sb = "";
                sb = sb +"HTTP/1.0 200 ok\n";
                sb = sb +"Server: Mesfer Server/1.0 \n";
                sb = sb +"Date: " + new Date()+" \n";
                sb = sb +"Content-Type: "+mime+" \n";
                sb = sb +"Content-Length: "+tam_archivo+" \n";
                sb = sb +"\n";
                
                ByteBuffer respuesta = ByteBuffer.wrap(sb.getBytes());
                socket.write(respuesta);
                /***********************************************/
                
                while(cont<tam_archivo){
                    x = dis2.read(buf);
                    ByteBuffer archivo = ByteBuffer.wrap(Arrays.copyOfRange(buf, 0, x));
                    socket.write(archivo);
                    cont=cont+x;
                }
                System.out.println("Ha terminado de enviarse " + arg);
                dis2.close();
                socket.close();
              
            }catch(Exception e){
                System.out.println("Error en SendA");
                System.out.println(e.getMessage());
            }
        }


                        
    }
		
        public ServidorWeb1(){
            try{
                System.out.println("Iniciando Servidor.......");
                this.ssc = ServerSocketChannel.open();
                ssc.configureBlocking(false);
                ssc.setOption(SO_REUSEADDR, true);
                InetSocketAddress i = new InetSocketAddress(PUERTO);
                ssc.socket().bind(i);
                Selector sel = Selector.open();
                ssc.register(sel,  SelectionKey.OP_ACCEPT);
                System.out.println("Servidor iniciado...");
                for(;;){
                    sel.select();
                    Iterator<SelectionKey> it = sel.selectedKeys().iterator();
                    while(it.hasNext()){
                        SelectionKey k = (SelectionKey)it.next();
                        it.remove();
                        if(k.isAcceptable()){
                            SocketChannel cl = ssc.accept();
                            System.out.println("Cliente conectado desde: " + cl.socket().getInetAddress() + ":" + cl.socket().getPort());
                            cl.configureBlocking(false);
                            cl.register(sel, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                            continue;
                        }
                        if(k.isReadable()){
                            SocketChannel ch = (SocketChannel)k.channel(); 
                            Manejador m = new Manejador(ch);

                            //El selector indica que un canal esta listo para leer
                            //¿Qué lees?
                            m.administrarPeticion();
                        
                        }
                        else if(k.isWritable()){
                            //El selector indica que el canal esta listo para escribir
                            //Escribes dependiendo de lo que leas en la petición 
                            
                        }

                    }
                }

            }catch(Exception e){
                System.out.println("Error al iniciar el servidor");
                e.printStackTrace();
            }
        }
    
    public static void main(String[] args){
        ServidorWeb1 sWEB=new ServidorWeb1();
    }
    
}