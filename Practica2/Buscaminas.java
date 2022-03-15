import javax.swing.*;

import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Buscaminas extends JFrame implements MouseListener{
    JLabel banderasLabel, banderaIconoLabel;
    JButton [][] botones;
    static Cliente cliente;

    public void buscaminas(){
        //Se inicializa la ventana
        setBounds(20, 20, 700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Se incializa el panel superior y sus componentes
        JPanel panelSup = new JPanel();

        banderaIconoLabel = new JLabel();
        ImageIcon icono = new ImageIcon("img/bandera.png");
        banderaIconoLabel.setIcon(new ImageIcon(icono.getImage().getScaledInstance(38,38,Image.SCALE_SMOOTH)));
        panelSup.add(banderaIconoLabel);
        panelSup.setBackground(Color.WHITE);
       
        banderasLabel = new JLabel("40");
        banderasLabel.setBounds(0,0,20,20);
        panelSup.add(banderasLabel);
        this.add(panelSup,"North");
        
        botones = new JButton [16][16]; 
        JPanel tablero = new JPanel(new GridLayout(16,16));
        Color colores[] = new Color[2];
        colores[0] = new Color(139, 92, 64);
        colores[1] = new Color(126, 84, 59);
        for(int i=0;i<16;i++)
			for(int j=0;j<16;j++){
                //	Crear boton
                botones [i][j] = new JButton();
                botones[i][j].setOpaque(true);
                botones[i][j].setBackground(colores[(j+i)%2]);
                botones[i][j].setBorderPainted(false);
                botones[i][j].setSize(11, 11);
                botones[i][j].setName(i+"-"+j);
                //Colocar en el panel
                tablero.add(botones[i][j]);
                //	Action Listener
                botones[i][j].addMouseListener(this);
			}
        tablero.setBackground(Color.WHITE);
        this.add(tablero, "Center");
        setResizable(false);
        setVisible(true);
    }

    
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        String coordenadas = ((JButton)e.getSource()).getName();
        if(SwingUtilities.isRightMouseButton(e)){//Se marca bandera
            int x = Integer.parseInt(coordenadas.split("-")[0]);
            int y = Integer.parseInt(coordenadas.split("-")[1]);
            cliente.enviarCoordenadas(x,y,true);
        }else{
            if(SwingUtilities.isLeftMouseButton(e)){//Se muestra valor
                int x = Integer.parseInt(coordenadas.split("-")[0]);
                int y = Integer.parseInt(coordenadas.split("-")[1]);
                cliente.enviarCoordenadas(x,y, false);
            }
        }
        ArrayList<Celda> celdas = cliente.recibirCeldas(); //Se obtienen las celdas afectadas por el movimiento
        modificarTablero(celdas); //Se hacen los cambios en el tablero
    }
 
    //Metodo para modificar las celdas afectadas por la jugada en el grid
    public void modificarTablero(ArrayList<Celda> celdas){
        for(int i=0; i<celdas.size(); i++){
            if(celdas.get(i).bandera){//Se marca como bandera
                if(botones[celdas.get(i).x][celdas.get(i).y].getIcon() == null){ //No hay bandera
                    ImageIcon icono = new ImageIcon("img/bandera.png");
                    botones[celdas.get(i).x][celdas.get(i).y].setIcon(new ImageIcon(icono.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH)));
                }else{ //Se elimina la marca de bandera
                    botones[celdas.get(i).x][celdas.get(i).y].setIcon(null);
                }
            }else{
                if(celdas.get(i).bomba){//Se acaba el juego
                    
                }else{//Se despliegan las celdas (Cargar imagenes)

                }
            }
        }
    }

    public static void main(String[] args) {
        //Mostrar ventana para ip y puerto
        cliente = new Cliente(1234, "127.0.0.1");
        Buscaminas b = new Buscaminas();
        b.buscaminas();
    }

    
    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {}

}
