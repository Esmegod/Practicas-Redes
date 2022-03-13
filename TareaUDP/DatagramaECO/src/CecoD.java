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
            String dir="2806:2f0:9960:e3d8:9997:e5d3:fd71:4381"; 
            InetAddress dst = InetAddress.getByName(dir);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream envioCMeta = new DataOutputStream(baos);
            
            int tam = 10;
            BufferedReader br= new BufferedReader(new InputStreamReader(System.in)); //
            
            DatagramSocket cl = new DatagramSocket();
            while(true){
                System.out.println("Escribe un mensaje, <Enter> para enviar, \"salir\" para terminar");
                String msj = br.readLine();
                if(msj.compareToIgnoreCase("salir")==0){
                    System.out.println("termina programa");
                    br.close();
                    cl.close();
                    System.exit(0);
                }else{
                    byte[] b = msj.getBytes();
                    if(b.length>tam){
                        int tp = (int)(b.length/tam);   
                        for(int j=0;j<tp;j++){
                            byte[] tmp =Arrays.copyOfRange(b, j*tam, ((j*tam)+(tam)));
                            System.out.println("tmp tam "+tmp.length);
                            envioCMeta.writeInt(j);
                            envioCMeta.writeInt(tp);
                            envioCMeta.writeInt(tmp.length);
                            envioCMeta.write(tmp);
                            envioCMeta.flush();
                            DatagramPacket p= new DatagramPacket(baos.toByteArray(),baos.toByteArray().length,dst,pto);
                            cl.send(p);
                            baos.flush();
                            System.out.println("Enviando fragmento "+(j)+" de "+tp+" desde:"+(j*tam)+" hasta "+((j*tam)+(tam))+ ": " + tmp.toString());

                        }//for
                        if(b.length%tam>0){ //bytes sobrantes  
                            //tp=tp+1;
                            int sobrantes = b.length%tam;
                            System.out.println("sobrantes:"+sobrantes);
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
                            System.out.println("Enviando fragmento "+(tp)+" de "+tp+" desde:"+(tp*tam)+" hasta "+((tp*tam)+(tam))+ ": " + tmp.toString());
                            
                        }//for
                    }//if
                    else{
                        envioCMeta.writeInt(1);
                        envioCMeta.writeInt(1);
                        envioCMeta.writeInt(b.length);
                        envioCMeta.write(b);
                        envioCMeta.flush();
                        DatagramPacket p=new DatagramPacket(baos.toByteArray(),baos.toByteArray().length,dst,pto);
                        cl.send(p);
                        baos.flush();
                        System.out.println("Enviando fragmento "+(1)+" de "+1+" desde:"+(0)+" hasta "+(b.length)+ ": " + b.toString());
                    }//else
            }//else
        }//while
    }catch(Exception e){
        e.printStackTrace();
    }//catch
    }//main
}
