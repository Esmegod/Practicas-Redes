import java.net.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.JFileChooser;
/**
 *
 * @author esme y fer
 * Práctica 1 
 * 
 */

public class CEnvia {
    public static void main(String[] args){
        //Conexión con el servidor 
        try{
            int pto = 8000;
            String dir = "127.0.0.1";
            Socket cl = new Socket(dir,pto);
            System.out.println("Conexion con servidor establecida");
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream()); //Flujo de datos con el servidor

            //Se ingresa el usuaurio
            System.out.println("Bienvenido, ingrese su nombre de usuario");
            Scanner leer = new Scanner(System.in);
            String usuario = leer.nextLine();
            dos.writeUTF(usuario); //Se envia el usuario
            dos.flush();

            

            /*
            
            1.- Archivo1.pdf
            2.- Carpeta1
            3.- Archivo2.csv
            4.- Carpeta2
            
            5.- Agregar nuevo archivo  
            
            seleccionar -> 5
            
            */
            
            JFileChooser jf = new JFileChooser();
            
            jf.setMultiSelectionEnabled(true);
            int r = jf.showOpenDialog(null);
            if(r==JFileChooser.APPROVE_OPTION){
                File f[] = jf.getSelectedFiles(); //Arreglo de files
                
                
                for(int i=0; i<f.length; i++){
                    String nombre = f[i].getName();
                    String path = f[i].getAbsolutePath();
                    long tam = f[i].length();
                    System.out.println("Preparandose pare enviar archivo "+path+" de "+tam+" bytes\n\n");
                    DataInputStream dis = new DataInputStream(new FileInputStream(path));
                    dos.writeUTF(nombre);
                    dos.flush();
                    dos.writeLong(tam);
                    dos.flush();
                    long enviados = 0;
                    int l=0,porcentaje=0;
                    while(enviados<tam){
                        byte[] b = new byte[1500];
                        l=dis.read(b);
                        System.out.println("enviados: "+l);
                        dos.write(b,0,l);
                        dos.flush();
                        enviados = enviados + l;
                        porcentaje = (int)((enviados*100)/tam);
                        System.out.print("\rEnviado el "+porcentaje+" % del archivo");
                    }//while
                    System.out.println("\nArchivo enviado..");
                    dis.close();
                }
                dos.close();
                cl.close();
            }//if
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }//main
}
