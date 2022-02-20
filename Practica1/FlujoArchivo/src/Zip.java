//Zip: https://www.youtube.com/watch?v=_G_HXByQu3w
//unZip: 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;


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

    public String zipDirectory(){
        String name = "";
        try{
            JFileChooser jf = new JFileChooser();
            jf.setMultiSelectionEnabled(true);
            jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int r = jf.showOpenDialog(null);
            if(r==JFileChooser.APPROVE_OPTION){
                FileOutputStream fos = new FileOutputStream("Directory.zip"); //Archivo zip resultante
                ZipOutputStream zipOut = new ZipOutputStream(fos); //Flujo para escribir en formato zip
                Path sourcePath = Paths.get(jf.getSelectedFile().getAbsolutePath()); //Convierte la direccion a objeto Path para el metodo walkFiletree
                
                Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() { //Metodo que visitia recursivamente todos los archivos
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException { //Metodo invocado al visitar el archivo https://docs.oracle.com/javase/tutorial/essential/io/walk.html
                        ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(file).toString());  //Agrega el nombre del archivo (ruta relativa a sourcePath)
                        zipOut.putNextEntry(zipEntry); //Agrega la entrada al flujo del zip
                        Files.copy(file, zipOut); //Copia los bytes del archivo al flujo de salida del zip
                        zipOut.closeEntry();
                        return FileVisitResult.CONTINUE; //Indica que continua el camino
                    }  
                });
                zipOut.close();
                name = jf.getSelectedFile().getName();
            }
        }catch(Exception e){
            System.out.println("Error al comprimir directorio: ");
            e.printStackTrace();
        }
        return name;
    }
    
    public void unzipDirectory(String ruta, String nombreCarpeta){
        try{
            
            String path = "Directory.zip"; //Ruta origen
            ZipInputStream zis = new ZipInputStream(new FileInputStream(new File(ruta+"\\"+path))); //Flujo de entrada del zip
            ZipEntry zipEntry = zis.getNextEntry(); //Se obtiene la primera entrada
            while(zipEntry != null){
                File file = new File(ruta +"\\"+nombreCarpeta+"\\"+zipEntry.getName());
                if(file.getParentFile() != null){ //Si el archivo que se va a crear tiene en su nombre un archivo padre, primero se crea
                    file.getParentFile().mkdirs();
                }
                int len;
                byte [] buffer = new byte[2048]; //bufer para ir escribiendo
                FileOutputStream fos = new FileOutputStream(file); 
                while((len = zis.read(buffer)) > 0){
                    fos.write(buffer,0,len); //Se escribe el archivo
                }
                fos.close();
                zipEntry = zis.getNextEntry(); //Se obtiene la siguiente entrada
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
