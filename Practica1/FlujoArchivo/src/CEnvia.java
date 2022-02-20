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
            DataInputStream dis = new DataInputStream(cl.getInputStream());

            //Se ingresa el usuario
            System.out.print("Bienvenido, ingrese su nombre de usuario: ");
            Scanner leer = new Scanner(System.in);
            String usuario = leer.nextLine().trim();
            dos.writeUTF(usuario); //Se envia el usuario
            dos.flush();
          

            //Menu
            int opc = 0;
            String carpeta = usuario;
            
            while(opc != 2){
                System.out.println("Bienvenido " + usuario);
                System.out.println("1. Subir aqui");
                System.out.println("2. Salir");
                //Despliega los archivos en la carpeta
                dos.writeUTF(carpeta); //Se envia el nombre de la carpeta a enlistas
                dos.flush();
                int NoArchivos = dis.readInt();
                String [] archivosListados = new String[NoArchivos];

                for(int i=0; i < NoArchivos; i++){
                    archivosListados[i] = dis.readUTF();
                    System.out.println((i+3) + ". " + archivosListados[i]);
                }
                
                System.out.print("\t\t Seleccionar ->");
                opc = leer.nextInt();
                
                //Opcion 1 (Subir archivo o carpeta)
                if(opc == 1){
                    System.out.print("\033[H\033[2J");  
                    System.out.flush();  
                    System.out.print("Seleccione la opcion");
                    System.out.println("1. Un solo Archivo");
                    System.out.println("2. Una Carpeta");
                    System.out.println("3. Varios Archivos");
                    System.out.print("\t\t Seleccionar ->");
                    opc = leer.nextInt();
                    if(opc == 1){
                        //Se sube un solo archivo
                        
                    }else if(opc == 2){
                        //Se sube una carpeta

                    }
                    else{
                        //Se suben varios archivos
                    }
                    opc = 1;
                }
                //Opcion (Selecciona archivo o carpeta) 
                else if(opc != 2){
                    //Se valida si es archivo o carpeta
                    if(archivosListados[opc-3].contains(".")){ //Es archivo
                        System.out.print("\033[H\033[2J");  
                        System.out.flush();  
                        System.out.print("Ha seleccionado el archivo: " + archivosListados[opc-3]);
                        System.out.println("1. Descargar");
                        System.out.println("2. Eliminar");
                        System.out.print("\t\t Seleccionar ->");
                        opc = leer.nextInt();
                        if(opc == 1){
                            //Se descarga

                        }else{ 
                            //Se elimina

                        }
                        opc = 1;
                    }else{//Es carpeta
                        carpeta = archivosListados[opc-3];
                    } 
                }        
            }
            
           
            // JFileChooser jf = new JFileChooser();
            
            // jf.setMultiSelectionEnabled(true);
            // int r = jf.showOpenDialog(null);
            // if(r==JFileChooser.APPROVE_OPTION){
            //     File f[] = jf.getSelectedFiles(); //Arreglo de files
                
                
            //     for(int i=0; i<f.length; i++){
            //         String nombre = f[i].getName();
            //         String path = f[i].getAbsolutePath();
            //         long tam = f[i].length();
            //         System.out.println("Preparandose pare enviar archivo "+path+" de "+tam+" bytes\n\n");
            //         DataInputStream dis2 = new DataInputStream(new FileInputStream(path));
            //         dos.writeUTF(nombre);
            //         dos.flush();
            //         dos.writeLong(tam);
            //         dos.flush();
            //         long enviados = 0;
            //         int l=0,porcentaje=0;
            //         while(enviados<tam){
            //             byte[] b = new byte[1500];
            //             l=dis2.read(b);
            //             System.out.println("enviados: "+l);
            //             dos.write(b,0,l);
            //             dos.flush();
            //             enviados = enviados + l;
            //             porcentaje = (int)((enviados*100)/tam);
            //             System.out.print("\rEnviado el "+porcentaje+" % del archivo");
            //         }//while
            //         System.out.println("\nArchivo enviado..");
            //         dis2.close();
            //     }
                dos.close();
                dis.close();
                cl.close();
           // }
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }//main
}
