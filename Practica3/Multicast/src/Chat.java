
import javax.swing.*;
import java.awt.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Chat extends JFrame{
     JEditorPane editorPane;

    public static void main(String[] args) {
            Chat chat = new Chat();
    }

    public Chat(){
        setBounds(20, 20, 800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        Color gris = new Color(247,247,252);
        Color grisFuerte = new Color(176,176,184);
        //Azul: 64,136,252
        //Azul clarito: 223, 240, 255
        //Gris fuerte: 176, 176, 184
        
        //Creación de paneles 
        JPanel areaChat = new JPanel();
        JPanel areaUsuarios = new JPanel();
        JPanel areaMensajes = new JPanel();
        
        areaChat.setBounds(0, 0, 550, 300);
        areaChat.setBorder(BorderFactory.createMatteBorder(0,0,2,2,gris));
        areaChat.setBackground(Color.white);

        areaUsuarios.setBounds(550, 0, 250, 300);
        areaUsuarios.setBorder(BorderFactory.createMatteBorder(0,0,2,0,gris));
        areaUsuarios.setBackground(Color.white);

        areaMensajes.setBounds(0, 300, 800, 200);
        areaMensajes.setBackground(Color.white);
        
        //AreaChat 
        editorPane = new JEditorPane();
        JScrollPane scroll = new JScrollPane();
        scroll.setBorder(BorderFactory.createMatteBorder(0,0,2,2,gris));
        scroll.setViewportView(editorPane);
        editorPane.setBackground(Color.white);
        scroll.setBounds(0,0,550,300);
        getContentPane().add(scroll);

        //Area Mensaje
        areaMensajes.setLayout(new BorderLayout());
        JButton emojiButton = new JButton("Emoji");
        areaMensajes.add(emojiButton, BorderLayout.NORTH);
        JTextField mensaje = new JTextField();
        mensaje.setSize();
        areaMensajes.add(mensaje);
        

        
        add(areaChat);
        add(areaMensajes);
        add(areaUsuarios);

        setResizable(false);
        setVisible(true);
    }

}

class Recibe extends Thread {
    MulticastSocket socket;

    public Recibe(MulticastSocket m) {
        this.socket = m;
    }

    public void run() {
        try {
            String mensaje_medio = ""; 
            for (;;) {
                DatagramPacket p = new DatagramPacket(new byte[65535], 65535);
                socket.receive(p);
                mensaje_medio += new String(p.getData(), 0, p.getLength());
                
                //SetText
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

}

