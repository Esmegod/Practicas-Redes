//Zip: https://www.youtube.com/watch?v=_G_HXByQu3w
//unZip: 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;

public class Zip {
    public static void main(String args[]){
        //zipFiles();
        //unzipFiles();
    }
    
    public void zipFiles(){
        try{
            JFileChooser jf = new JFileChooser();
            jf.setMultiSelectionEnabled(true);
            jf.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int r = jf.showOpenDialog(null);
            if(r==JFileChooser.APPROVE_OPTION){
                File f[] = jf.getSelectedFiles(); //Arreglo de files
                
                FileOutputStream fos = new FileOutputStream("Files.zip"); //Archivo zip resultante
                ZipOutputStream zipOut = new ZipOutputStream(fos); //Flujo para escribir en formato zip
                
                for(int i=0; i < f.length; i++){ //Se itera cada archivo para agregar al zip
                    FileInputStream fis = new FileInputStream(f[i]); //Flujo para leer el archivo
                    ZipEntry zipEntry = new ZipEntry(f[i].getName()); //Representa una entrada zip del archivo
                    zipOut.putNextEntry(zipEntry); //Agrega la entrada al flujo de salida zip
                    byte[] bytes = new byte[2048]; //buffer para leer el contenido del archivo
                    int length; //Variable para el control de la cantidad de bytes leidos
                    while((length = fis.read(bytes)) >= 0){ //Regresa -1 si ya se leyo todo el archivo
                        zipOut.write(bytes, 0, length);
                    }
                    fis.close(); //Se cierra el archivo actual
                }
                zipOut.close();
                fos.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void unzipFiles(String ruta){
        try{
            byte [] buffer = new byte[2048]; //bufer para ir escribiendo
            ZipInputStream zis = new ZipInputStream(new FileInputStream(new File(ruta+"\\"+"Files.zip"))); //Flujo de entrada del zip
            ZipEntry zipEntry = zis.getNextEntry(); //Variable de control de cada entrada zip
            while(zipEntry != null){
                if(zipEntry.isDirectory()){
                    continue;
                }else{
                    File file = new File(ruta +"\\"+zipEntry.getName()); //Se crea la instancia del archivo en dicha entrada zip
                    FileOutputStream fos = new FileOutputStream(file); //Flujo para la escritura del archivo
                    int len;
                    while((len = zis.read(buffer)) > 0){
                        fos.write(buffer,0,len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry(); //Se obtiene la siguiente entrada zip
            }
            zis.closeEntry();
            zis.close();
            
        }catch(Exception e){
            System.out.println("Error al descomprimir: " + e.getMessage());
        }
    }
}
