
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Chat extends JFrame implements KeyListener{
    JEditorPane editorPane;
    String usuario = "Esme";
    String imagen = "mapache";
    MulticastSocket m;
    JTextArea mensajeField;

    public static void main(String[] args) throws InterruptedException {
           
        Chat chat = new Chat();
        MulticastSocket m = chat.conectarse(); 
        chat.m = m;
        Recibe r = new Recibe(m, chat.editorPane);
        r.start();
        r.join();
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
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(0,0,550,300);
        getContentPane().add(scroll);

        //Area Mensaje
        areaMensajes.setLayout(null);
        JLabel comboLabel = new JLabel("Enviar a:");
        comboLabel.setBounds(10, 8, 100, 30);
        areaMensajes.add(comboLabel);

        JComboBox<String> usuariosPrivados = new JComboBox<>();
        usuariosPrivados.setBounds(80, 8, 100, 30);
        areaMensajes.add(usuariosPrivados);
        usuariosPrivados.setOpaque(true);
        usuariosPrivados.setBackground(gris);
        usuariosPrivados.addItem("Todos");
        usuariosPrivados.addItem("Esme");
        usuariosPrivados.addItem("Fer"); 

        JButton emojiButton = new JButton();
        ImageIcon emojiIcon = new ImageIcon("img/smile.png");
        emojiButton.setIcon(new ImageIcon(emojiIcon.getImage().getScaledInstance(25,25,Image.SCALE_SMOOTH)));
        emojiButton.setOpaque(true);
        emojiButton.setBorderPainted(false);
        emojiButton.setBackground(Color.white);
        emojiButton.setBounds(640, 8, 30, 30);
        areaMensajes.add(emojiButton);
        
        JButton imagenButton = new JButton();
        ImageIcon imagenIcon = new ImageIcon("img/gallery.png");
        imagenButton.setIcon(new ImageIcon(imagenIcon.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH)));
        imagenButton.setOpaque(true);
        imagenButton.setBorderPainted(false);
        imagenButton.setBackground(Color.white);
        imagenButton.setBounds(680, 8, 30, 30);
        areaMensajes.add(imagenButton);
        
        JButton microButton = new JButton();
        ImageIcon microIcon = new ImageIcon("img/microphone.png");
        microButton.setIcon(new ImageIcon(microIcon.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH)));
        microButton.setOpaque(true);
        microButton.setBorderPainted(false);
        microButton.setBackground(Color.white);
        microButton.setBounds(720, 8, 30, 30);
        areaMensajes.add(microButton);
        
        mensajeField = new JTextArea();
        mensajeField.setBorder(BorderFactory.createMatteBorder(1,1,1,1,gris));
        mensajeField.setBounds(10,50, 765, 100);
        mensajeField.addKeyListener(this);
        areaMensajes.add(mensajeField);
        
        add(areaChat);
        add(areaMensajes);
        add(areaUsuarios);

        setResizable(false);
        setVisible(true);
    }

    public MulticastSocket conectarse(){
        int puerto = 4000;
        MulticastSocket m = null;
        InetAddress ip = null;
        try{
            ip  = InetAddress.getByName("230.1.1.1");
            m = new MulticastSocket(puerto);
            m.setReuseAddress(true);
            m.setTimeToLive(255);
            m.joinGroup(ip);
        }catch(Exception e){
            e.printStackTrace();
        }
        return m;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        String msj = "";
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            try{
                InetAddress grupo = InetAddress.getByName("230.1.1.1");
                msj = mensajeField.getText();
                String div = "<div class='msj'><p class='nombre'>" + usuario + "</p><div class='flex'>"+
                "<img src='img/" + imagen + ".png' alt='usuario' class='avatar'>"+
                "<div class='mensaje'>" + msj + "</div>"+
                "</div></div>";
                byte [] b = div.getBytes();
                DatagramPacket p = new DatagramPacket(b, b.length, grupo, 4000);
                m.send(p);
            }catch(Exception ex){
                ex.printStackTrace();
            }   
        }
    }

    
    @Override
    public void keyReleased(KeyEvent e) {}

    

}

class Recibe extends Thread {
    MulticastSocket socket;
    JEditorPane editorPane;
    String mensaje = "<head><style>"+
        "*{font-family: Arial, Helvetica, sans-serif;}"+
        ".nombre{padding-left: 55px;color:  #b0b0b8;margin-bottom: 5px;}"+
        ".avatar{height: 30px;width: 30px;padding: 5px;border-radius: 100%;}"+
        ".mensaje{border-radius: 20px;background-color: #dff0ff;padding: 10px;margin-left: 10px;}"+
        ".flex{display: flex;flex-direction: row;align-items: center;justify-content: left;}" +
        "</style></head>";


    public Recibe(MulticastSocket m, JEditorPane editorPane) {
        this.socket = m;
        this.editorPane = editorPane;
    }

    public void run() {
        try {
            String mensaje_medio = "";
            for (;;) {
                DatagramPacket p = new DatagramPacket(new byte[65535], 65535);
                socket.receive(p);
                mensaje_medio =  new String(p.getData(), 0, p.getLength());
                editorPane.setText(mensaje+=mensaje_medio);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}


/*
class Envia extends Thread {
    MulticastSocket socket;
    BufferedReader br;

    public Envia(MulticastSocket m, BufferedReader br) {
        this.socket = m;
        this.br = br;
    }

    public void run() {
        try {
            String dir = "230.1.1.1";
            int pto = 4000;
            InetAddress gpo = InetAddress.getByName(dir);

            for (;;) {
                System.out.println("Escribe un mensaje para ser enviado:");
                String mensaje = br.readLine();
                byte[] b = mensaje.getBytes();
                DatagramPacket p = new DatagramPacket(b, b.length, gpo, pto);
                socket.send(p);
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/

