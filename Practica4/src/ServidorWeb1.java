import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ServidorWeb1{
    
    public static final int PUERTO=8000;
    ServerSocket ss;
    
    class Manejador extends Thread{
        protected Socket socket;
        protected PrintWriter pw;
        protected BufferedOutputStream bos;
        protected BufferedReader br;
        DataOutputStream dos;
        DataInputStream dis;
        protected String FileName;
        protected String mime;

        public Manejador(Socket _socket) throws Exception{
            this.socket=_socket;
        }

        public void run(){
            try{
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                byte[] b = new byte[10000000];
                int t = dis.read(b);
                if(t==-1){
                    return;
                }
                String peticion = new String(b,0,t);
                System.out.println("t: " + t);

                if(peticion==null){
                    StringBuffer sb = new StringBuffer();
                    sb.append("<html><head><title>Servidor WEB\n");
                    sb.append("</title><body bgcolor=\"#AACCFF\"<br>Linea Vacia</br>\n");
                    sb.append("</body></html>\n");
                    dos.write(sb.toString().getBytes());
                    dos.flush();
                    socket.close();
                    return;
                }

                System.out.println("Cliente Conectado desde: "+socket.getInetAddress());
                System.out.println("Por el puerto: "+socket.getPort());
                System.out.println("Datos: "+peticion+"\r\n\r\n");

                StringTokenizer st1= new StringTokenizer(peticion,"\n");
                String line = st1.nextToken();

                if(line.indexOf("?")==-1){ //No se solicita un recurso, sino la página principal
                    if(line.toUpperCase().startsWith("GET")){
                        getArch(line, st1);
                        if(FileName.compareTo("")==0){
                            SendA("index.html",dos,mime);
                        }else{
                            SendA(FileName,dos,mime);
                        }
                    }else if(line.toUpperCase().startsWith("POST")){
                        getArch(line, st1);
                        SendA(FileName,dos, mime);
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
                            FileOutputStream fos = new FileOutputStream(new File("archivosRecibidos/archivo."+extension));
                            int pos = t-bytes;   
                            byte[] contenido = Arrays.copyOfRange(b, pos, t); 
                            fos.write(contenido);
                            fos.flush();
                            fos.close();
                            /***********************************************/
                            String sb = "";
                            sb = sb +"HTTP/1.0 201 created\n";
                            sb = sb +"Server: Mesfer Server/1.0 \n";
                            sb = sb +"Date: " + new Date()+" \n";
                            sb = sb +"\n";
                            dos.write(sb.getBytes());
                            dos.flush();
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
                            dos.write(sb.getBytes());
                            dos.flush();
                            /***********************************************/
                        }finally{
                            dos.close();
                        }
                    }
                }else if(line.toUpperCase().startsWith("GET")){
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
                        dos.write(respuesta.toString().getBytes());
                        dos.flush();
                        dos.close();
                        socket.close();
                }else{
                        dos.write("HTTP/1.0 501 Not Implemented\r\n".getBytes());
                        dos.flush();
                        dos.close();
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
                while((token = st.nextToken())!=null)
                    if(token.contains("imagen-abril")){
                        //Ttoken = imagen-abril=2022-04-19
                        String[] s = token.split("=");
                        FileName = "abrilNasa/"+s[1] + ".jpg";
                        mime = "image/jpeg";
                        break;
                    }
                }
            }
        }
                        
        public void SendA(String arg, DataOutputStream dos1, String mime){
            try{
                int b_leidos=0;
                DataInputStream dis2 = new DataInputStream(new FileInputStream(arg));
                byte[] buf = new byte[1024];
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
                dos1.write(sb.getBytes());
                dos1.flush();
                /***********************************************/
                while(cont<tam_archivo){
                    x = dis2.read(buf);
                    dos1.write(buf,0,x);
                    cont=cont+x;
                    dos1.flush();
                }
                dis2.close();
                dos1.close();
            }catch(Exception e){
                System.out.println("Error en SendA");
                System.out.println(e.getMessage());
            }
        }
                        
    
		
    public ServidorWeb1() throws Exception{
        System.out.println("Iniciando Servidor.......");
        this.ss=new ServerSocket(PUERTO);
        System.out.println("Servidor iniciado:---OK");
        System.out.println("Esperando por Cliente....");
        for(;;){
            Socket accept=ss.accept();
            new Manejador(accept).start();
        }
    }
    
    public static void main(String[] args) throws Exception{
        ServidorWeb1 sWEB=new ServidorWeb1();
    }
	
}