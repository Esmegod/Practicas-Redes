import java.net.*;
import java.io.*;
/**
 *
 * @author axele
 */
public class SecoD{
    public static void main(String[] args){
    try{   
        int pto=1234, noPaquete, tPaquetes, tamPaquete, pAnterior=-1;
        String msj="";
        DatagramSocket s = new DatagramSocket(pto); //Asociar la aplicacion al puerto
        s.setReuseAddress(true); 
        System.out.println("Servidor iniciado... esperando datagramas..");
        for(;;){
            byte[] b = new byte[65535];
            DatagramPacket p = new DatagramPacket(b,b.length);
            s.receive(p);
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(p.getData()));
            noPaquete = dis.readInt();
            tPaquetes = dis.readInt();
            tamPaquete = dis.readInt();

            byte[] bMsj = new byte[tamPaquete];
            dis.read(bMsj);

            if(noPaquete == 0) pAnterior = -1;
            if(pAnterior == noPaquete-1){ //Estan llegando en orden 
                msj = new String(bMsj,"UTF-16");
                System.out.println("Se ha recibido datagrama " + noPaquete + " de " + tPaquetes + " de tamaño " + tamPaquete + ": " + msj);
            }
            else{//No estan llegando en orden
                System.out.println("---------------------");
                System.out.println("Paquete en desorden");
                msj = new String(bMsj, "UTF-16");
                System.out.println("Se ha recibido datagrama " + noPaquete + " de " + tPaquetes + " de tamaño " + tamPaquete + ": " + msj);
                System.out.println("---------------------");
            }
            pAnterior = noPaquete;
            dis.close();

        }//for
        }catch(Exception e){
            e.printStackTrace();
        }//catch        
    }//main
}
