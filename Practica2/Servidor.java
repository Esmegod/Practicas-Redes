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
                tablero[i][j].x = i;
                tablero[i][j].y = j;
            }
        }
        int x,y;
        //Se colocan las bombas 
        for(int i=0; i<40; i++){
            x = (int)(Math.random()*16);
            y = (int)(Math.random()*16);
            if(!tablero[x][y].bomba){
                tablero[x][y].bomba = true;
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

    public static ArrayList<Celda> jugada(int x, int y, Celda[][] tablero){
        //Objetivo: Enviar celdas que se revelan 
        ArrayList<Celda> celdas = new ArrayList<Celda>();
        if(tablero[x][y].bomba){//Si es bomba se envía para que pierda 
            celdas.add(tablero[x][y]);
        }else{//Si la celda es un numero se valida la jugada
            if(tablero[x][y].valor > 0){//Si es numero se muestra
                celdas.add(tablero[x][y]);
            }else{ //Si es cero se obtienen sus adyacentes
                obtenerAdyacentes(x,y,celdas, tablero);
            } 
        }
        return celdas;
    }


    public static void obtenerAdyacentes(int x, int y, ArrayList<Celda> adyacentes, Celda[][] tablero){
        for(int i=x-1; i<=x+1;i++){
            for(int j=y-1; j<=y+1; j++){
                if(!(i<0 || i>15 || j<0 || j>15)){
                    if(!tablero[i][j].bomba){
                        adyacentes.add(tablero[i][j]);
                        if(tablero[i][j].valor==0 && !tablero[i][j].adyacenciaEncontrada){
                            tablero[i][j].adyacenciaEncontrada = true;
                            obtenerAdyacentes(i, j, adyacentes, tablero);
                        }
                    }
                }
            }
        }
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
                s.connect(p.getAddress(), p.getPort());
                DataInputStream dis = new DataInputStream(new ByteArrayInputStream(p.getData()));
                if(dis.readBoolean()){ //Es un nuevo juego 
                    tablero = crearTablero();
                    imprimeTablero(tablero);
                }else{ //Es una jugada
                    int x = dis.readInt();
                    int y = dis.readInt();
                    if(x==-1 && y==-1){//Se termina el juego
                        s.disconnect(); 
                    }else{
                        ArrayList<Celda> celdas = jugada(x, y, tablero); //Se obtienen las celdas afectadas por la jugada
                        enviarCeldas(p.getAddress(), p.getPort(), celdas, s); //Se envia el arreglo de celdas al cliente
                    }
                }
                dis.close();
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
            baos.close();
            oos.close();
        } catch (IOException e) {
            System.out.println("Error al enviar celdas");
            e.printStackTrace();
        }
    }   

    

}
