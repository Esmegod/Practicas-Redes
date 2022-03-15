import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Servidor {

    public static Celda[][] crearTablero(){
        //Se crean las celdas
        Celda[][] tablero = new Celda[16][16];
        for(int i=0; i<16; i++){
            for(int j=0; j<16; j++){
                tablero[i][j] = new Celda();
            }
        }
        int x,y;
        //Se colocan las bombas 
        for(int i=0; i<40; i++){
            x = (int)(Math.random()*16);
            y = (int)(Math.random()*16);
            if(!tablero[x][y].bomba){
                tablero[x][y].bomba = true;
                tablero[x][y].x = x;
                tablero[x][y].y = y;
                //Se calculan los valores adyacentes
                for(int j=x-1; j<=x+1; j++){
                    for(int k=y-1; k<=y+1; k++){
                        if(!(j<0 || j>15 || k<0 || k>15)){
                            if(!tablero[j][k].bomba){
                                tablero[j][k].valor += 1;
                            }
                        }
                    }
                }
            }else{
                i--;
            }
        }
        
        return tablero;        
    }
    
    public static void imprimeTablero(Celda[][] tablero){
        System.out.println("Imprime tablero");
        for(int i=0; i<16;i++){
            for(int j=0; j<16;j++){
                if(tablero[i][j].bomba) System.out.print(" B ");
                else System.out.print(" " + tablero[i][j].valor + " ");   
            }
            System.out.println("");
        }
    }

    public static ArrayList<Celda> jugada(int x, int y, boolean marcaBandera, Celda[][] tablero){
        ArrayList<Celda> celdas = new ArrayList<Celda>();
        tablero[x][y].x = x;
        tablero[x][y].y = y;
        if(marcaBandera){
            tablero[x][y].bandera = true;
            celdas.add(tablero[x][y]);
        }else{//Se realiza jugada
            /*
                if valor > 0 -> Agregar a ArrayList Celdas
                else mostrar adyacentes -> public ArrayList<Celda> determinarAdyacentes(int x, int y){
                                                    if valor==0
                                                    determinarAdyacentes recursivo?
                                                }
            */
        }
        return celdas;
    }
    
    public static void main(String[] args) {
        try{
            int puerto = 1234;
            DatagramSocket s = new DatagramSocket(puerto);
            s.setReuseAddress(true);
            System.out.println("Servidor iniciado...");
            Celda[][] tablero = new Celda[16][16];

            for(;;){ //Acepta datagramas
                byte[] b = new byte[20];
                DatagramPacket p = new DatagramPacket(b, b.length);
                s.receive(p);
                DataInputStream dis = new DataInputStream(new ByteArrayInputStream(p.getData()));
                if(dis.readBoolean()){ //Es un nuevo juego 
                    tablero = crearTablero();
                    imprimeTablero(tablero);
                }else{ //Es una jugada
                    int x = dis.readInt();
                    int y = dis.readInt();
                    boolean marcaBandera = dis.readBoolean(); 
                    ArrayList<Celda> celdas = jugada(x, y, marcaBandera, tablero); //Se obtienen las celdas afectadas por la jugada
                    enviarCeldas(p.getAddress(), p.getPort(), celdas, s); //Se envia el arreglo de celdas al cliente
                }
            }
        }catch(Exception e){
            System.out.println("Error al iniciar servidor");
            e.printStackTrace();
        }    
    }

    //Metodo para enviar las celdas
    public static void enviarCeldas(InetAddress ip, int puerto, ArrayList<Celda> celdas, DatagramSocket s){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(celdas);
            oos.flush();
            DatagramPacket p = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, ip, puerto);
            s.send(p);
        } catch (IOException e) {
            System.out.println("Error al enviar celdas");
            e.printStackTrace();
        }
    }   

    

}
