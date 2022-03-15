import java.io.*;
import java.net.*;

public class Servidor {

    public static Celda[][] crearTablero(){
        //Se crean las celdas
        Celda[][] tablero = new Celda[16][16];
        for(int i=0; i<16; i++){
            for(int j=0; j<16; j++){
                tablero[i][j] = new Celda();
            }
        }

        //Se colocan las bombas 
        for(int i=0; i<40; i++){
            int x = ((int)Math.random())%41;
            int y = ((int)Math.random())%41;
            if(!tablero[x][y].bomba){
                tablero[x][y].bomba = true;
                tablero[x][y].x = x;
                tablero[x][y].y = y;
                //Se calculan los valores adyacentes
                for(int j=x-1; j<=x+1; j++){
                    for(int k=y-1; k<=y+1; k++){
                        if(!tablero[j][k].bomba){
                            tablero[j][k].valor += 1;
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
        for(int i=0; i<16;i++){
            for(int j=0; j<16;j++){
                if(tablero[i][j].bomba) System.out.print(" B ");
                else System.out.print(" " + tablero[i][j].valor + " ");   
            }
            System.out.println("");
        }
    }
    
    public static void main(String[] args) {
        try{
            int puerto = 1234;
            DatagramSocket s = new DatagramSocket(puerto);
            s.setReuseAddress(true);
            System.out.println("Servidor iniciado...");
            
            for(;;){ //Acepta datagramas
                byte[] b = new byte[20];
                DatagramPacket p = new DatagramPacket(b, b.length);
                s.receive(p);
                DataInputStream dis = new DataInputStream(new ByteArrayInputStream(p.getData()));
                if(dis.readBoolean()){ //Es un nuevo juego 
                    Celda[][] tablero = crearTablero();
                    imprimeTablero(tablero);
                }else{ //Es una jugada
                    
                }

            }
            
        }catch(Exception e){
            System.out.println("Error al iniciar servidor");
            e.printStackTrace();
        }    
    }

    

}
