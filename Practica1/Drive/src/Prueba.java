import java.io.*;
/**
 *
 * @author esme y fer
 * Práctica 1 
 * 
 */

public class Prueba {
    public static void main(String[] args){
        File f = new File("C:\\Users\\52552\\Desktop\\ESCOM");
        File [] archivos = f.listFiles();
        for(int i=0; i<archivos.length; i++){
            System.out.println(archivos[i].getName());
        }
        
    }

}