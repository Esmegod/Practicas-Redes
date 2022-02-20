import java.net.*;
import java.io.*;

/**
 *
 * @author axele
 */
public class SRecibe {
    public static void main(String[] args){
        try{
            int pto = 8000;
            ServerSocket s = new ServerSocket(pto);
            s.setReuseAddress(true); //Si se llega a cerrar, connectar inmediatamente
            System.out.println("Servidor iniciado esperando por archivos..");
            
            //Crea la carpelta de los usuarios 
            File f = new File(""); //Crea un archivo, no esta apuntando a nada, por lo que se guarda por default en la  raiz del proyecto 
            String ruta = f.getAbsolutePath();
            String carpeta = "Usuarios";
            String ruta_archivos = ruta+"\\"+carpeta+"\\";
            
            File f2 = new File(ruta_archivos);
            if(!f2.exists()){
                f2.mkdirs();  
            }
            f2.setWritable(true);
            

            for(;;){
                Socket cl = s.accept();
                System.out.println("Cliente conectado desde "+cl.getInetAddress()+":"+cl.getPort());
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                //Lee el usuario y valida su carpeta
                String usuario = dis.readUTF();
                File auxUsuario = new File("Usuarios\\"+usuario);
                if(!auxUsuario.exists()){
                    auxUsuario.mkdirs();  
                }
                auxUsuario.setWritable(true);
                
               int opc = 0;
               while(opc != 2){
                    //Envio del arreglo de nombres de archivos y carpetas
                    carpeta = dis.readUTF();
                    ruta_archivos += carpeta + "\\"; //Se lleva el control del path
                    File a = new File(ruta_archivos);
                    File [] archivos = a.listFiles();
                    dos.writeInt(archivos.length); 
                    for(int i=0; i<archivos.length; i++){
                        dos.writeUTF(archivos[i].getName());
                    }
               }
               
                







                
        //         String nombre = dis.readUTF();
        //         long tam = dis.readLong();
        //             System.out.println("Comienza descarga del archivo "+nombre+" de "+tam+" bytes\n\n");
        //             DataOutputStream dos = new DataOutputStream(new FileOutputStream(ruta_archivos+nombre));
        //             long recibidos=0;
        //             int l=0, porcentaje=0;
        //             while(recibidos<tam){
        //           byte[] b = new byte[1500];
        //           l = dis.read(b);
        //           System.out.println("leidos: "+l);
        //           dos.write(b,0,l);
        //           dos.flush();
        //           recibidos = recibidos + l;
        //           porcentaje = (int)((recibidos*100)/tam);
        //           System.out.print("\rRecibido el "+ porcentaje +" % del archivo");
        //       }//while
        //       System.out.println("Archivo recibido..");
              dos.close();
              dis.close();
              cl.close();
         }//for
          
      }catch(Exception e){
          e.printStackTrace();
      }  
    }//main
}
