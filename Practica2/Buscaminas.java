import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Buscaminas extends JFrame implements ActionListener{
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
                botones [i][j] =new JButton();
                botones[i][j].setOpaque(true);
                botones[i][j].setBackground(colores[(j+i)%2]);
                botones[i][j].setBorderPainted(false);
                botones[i][j].setSize(11, 11);
                botones[i][j].setName(i+"-"+j);
                //Colocar en el panel
                tablero.add(botones[i][j]);
                //	Action Listener
                botones[i][j].addActionListener(this);
                
			}
        tablero.setBackground(Color.WHITE);
        this.add(tablero, "Center");

        setResizable(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String coordenadas = ((JButton)e.getSource()).getName();
        int x = Integer.parseInt(coordenadas.split("-")[0]);
        int y = Integer.parseInt(coordenadas.split("-")[1]);
        cliente.enviarCoordenadas(x,y);
    }

    public static void main(String[] args) {
        //Mostrar ventana para ip y puerto
        cliente = new Cliente(1234, "127.0.0.1");
        Buscaminas b = new Buscaminas();
        b.buscaminas();
    }
}
