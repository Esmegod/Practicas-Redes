import java.net.*;
import java.io.*;
import java.util.Arrays;

/**
 *
 * @author axele
 */
public class CecoD {
    public static void main(String[] args){
        try{  
            int pto=1234;
            String dir="2806:106e:d:8a7:7545:3f26:ae5d:96d", msj=""; 
            InetAddress dst = InetAddress.getByName(dir);
            int tam = 10;
            BufferedReader br= new BufferedReader(new InputStreamReader(System.in)); //
            DatagramSocket cl = new DatagramSocket();
            while(true){
                System.out.println("\n\t-----------------------------------------------------------------");
                System.out.println("Escribe un mensaje, <Enter> para enviar, \"salir\" para terminar");
                String msj2 = br.readLine();
                if(msj2.compareToIgnoreCase("salir")==0){
                    System.out.println("termina programa");
                    br.close();
                    cl.close();
                    System.exit(0);
                }else{
                    byte[] b = msj2.getBytes();
                    if(b.length>tam){
                        int tp = (int)(b.length/tam);   
                        for(int j=0;j<tp;j++){
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            DataOutputStream envioCMeta = new DataOutputStream(baos);
                            byte[] tmp =Arrays.copyOfRange(b, j*tam, ((j*tam)+(tam)));
                            envioCMeta.writeInt(j);
                            envioCMeta.writeInt(tp);
                            envioCMeta.writeInt(tmp.length);
                            envioCMeta.write(tmp);
                            envioCMeta.flush();
                            DatagramPacket p= new DatagramPacket(baos.toByteArray(),baos.toByteArray().length,dst,pto);
                            cl.send(p);
                            baos.flush();
                            msj = new String(tmp, "UTF-16");
                            System.out.println("Enviando fragmento "+(j)+" de "+tp+" desde:"+(j*tam)+" hasta "+((j*tam)+(tam))+ ": " + msj);
                            baos.close();
                            envioCMeta.close();
                        }//for
                        if(b.length%tam>0){ //bytes sobrantes  
                            //tp=tp+1;
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            DataOutputStream envioCMeta = new DataOutputStream(baos);
                            int sobrantes = b.length%tam;
                            byte[] tmp = Arrays.copyOfRange(b, tp*tam, ((tp*tam)+sobrantes));
                            System.out.println("tmp tam "+tmp.length);
                            envioCMeta.writeInt(tp);
                            envioCMeta.writeInt(tp);
                            envioCMeta.writeInt(tmp.length);
                            envioCMeta.write(tmp);
                            envioCMeta.flush();
                            DatagramPacket p = new DatagramPacket(baos.toByteArray(),baos.toByteArray().length,dst,pto);
                            cl.send(p);
                            baos.flush();
                            msj = new String(tmp, "UTF-16");
                            System.out.println("Enviando fragmento "+(tp)+" de "+tp+" desde:"+(tp*tam)+" hasta "+((tp*tam)+(tam))+ ": " + msj);
                            baos.close();
                            envioCMeta.close();
                            
                        }//for
                    }//if
                    else{
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        DataOutputStream envioCMeta = new DataOutputStream(baos);
                        envioCMeta.writeInt(1);
                        envioCMeta.writeInt(1);
                        envioCMeta.writeInt(b.length);
                        envioCMeta.write(b);
                        envioCMeta.flush();
                        DatagramPacket p=new DatagramPacket(baos.toByteArray(),baos.toByteArray().length,dst,pto);
                        cl.send(p);
                        baos.flush();
                        msj = new String(b, "UTF-16");
                        System.out.println("Enviando fragmento "+(1)+" de "+1+" desde:"+(0)+" hasta "+(b.length)+ ": " + msj);
                        baos.close();
                        envioCMeta.close();
                    }//else
            }//else
        }//while
    }catch(Exception e){
        e.printStackTrace();
    }//catch
    }//main
}
