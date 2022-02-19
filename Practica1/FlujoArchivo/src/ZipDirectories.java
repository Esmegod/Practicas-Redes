
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;

/**
 *
 * @author Fernando Mtz
 */
public class ZipDirectories {
     public static void main(String args[]){
       //zipDirectory();
       unzipDirectory();
    }
     
    public static void zipDirectory(){
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
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void unzipDirectory(){
        try{
            
            String path = "Directory.zip"; //Ruta origen
            ZipInputStream zis = new ZipInputStream(new FileInputStream(new File(path))); //Flujo de entrada del zip
            ZipEntry zipEntry = zis.getNextEntry(); //Se obtiene la primera entrada
            while(zipEntry != null){
                File file = new File(zipEntry.getName());
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
