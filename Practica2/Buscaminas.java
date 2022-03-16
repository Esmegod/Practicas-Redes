import javax.swing.*;

import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Buscaminas extends JFrame implements MouseListener{
    JLabel banderasLabel, banderaIconoLabel;
    JButton [][] botones;
    static Cliente cliente;
    int bandera = 40;
    int ganador = 0;
    Color tierrita = new Color(155,118,83);

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
        colores[0] = new Color(170, 215, 81);
        colores[1] = new Color(162, 209, 73);
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
        if(SwingUtilities.isRightMouseButton(e)){//Se detecta click derecho
            int x = Integer.parseInt(coordenadas.split("-")[0]);
            int y = Integer.parseInt(coordenadas.split("-")[1]);
            if(botones[x][y].getIcon() == null){// Se marca bandera
                bandera--;
                banderasLabel.setText(Integer.toString(bandera));
                ImageIcon icono = new ImageIcon("img/bandera.png");
                botones[x][y].setIcon(new ImageIcon(icono.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH)));
            }else{//Se desmarca una bandera      
                bandera++;
                banderasLabel.setText(Integer.toString(bandera));
                botones[x][y].setIcon(null);
            }
        }else{//Se detecta click izquierdo 
            if(SwingUtilities.isLeftMouseButton(e)){//Se muestra valor
                int x = Integer.parseInt(coordenadas.split("-")[0]);
                int y = Integer.parseInt(coordenadas.split("-")[1]);
                if(botones[x][y].getIcon() == null){
                    cliente.enviarCoordenadas(x,y);
                    ArrayList<Celda> celdas = cliente.recibirCeldas(); //Se obtienen las celdas afectadas por el movimiento
                    modificarTablero(celdas); //Se hacen los cambios en el tablero
                } 
            }   
        }
        
    }
 
    //Metodo para modificar las celdas afectadas por la jugada en el grid
    public void modificarTablero(ArrayList<Celda> celdas){
        String imagen; 
        for(int i=0; i<celdas.size(); i++){
            if(celdas.get(i).bomba){//Se acaba el juego
                ImageIcon icono = new ImageIcon("img/bomba.png");
                botones[celdas.get(i).x][celdas.get(i).y].setIcon(new ImageIcon(icono.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH)));
                System.out.println("Bombaa!!");
                JOptionPane.showConfirmDialog(null, "Has perdido", "Booom!", JOptionPane.DEFAULT_OPTION);
                cliente.enviarCoordenadas(-1, -1);
                System.exit(0);
            }else{//Se despliegan las celdas (Cargar imagenes)
                if(celdas.get(i).valor == 0){
                    if(botones[celdas.get(i).x][celdas.get(i).y].getBackground() != tierrita){
                        botones[celdas.get(i).x][celdas.get(i).y].setBackground(tierrita); 
                        ganador++;
                    }
                }else{
                    if(botones[celdas.get(i).x][celdas.get(i).y].getIcon() == null){
                        imagen = Integer.toString(celdas.get(i).valor);
                        ImageIcon icono = new ImageIcon("img/"+imagen+".png");
                        botones[celdas.get(i).x][celdas.get(i).y].setIcon(new ImageIcon(icono.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH)));
                        botones[celdas.get(i).x][celdas.get(i).y].setBackground(new Color(255,255,255));
                        ganador++;
                    }
                }
                System.out.println(ganador);
                if(ganador == 216){
                    JOptionPane.showMessageDialog(null, "Has ganado", "Yei!", JOptionPane.DEFAULT_OPTION);
                    cliente.enviarCoordenadas(-1, -1);
                    System.exit(0);
                }
            }
        }
    }

    public static void main(String[] args) {
        //Mostrar ventana para ip y puerto
        String ip = JOptionPane.showInputDialog("Ingrese la direccion ip del servidor");
        int puerto = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el puerto del servidor"));
        cliente = new Cliente(puerto, ip);
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
