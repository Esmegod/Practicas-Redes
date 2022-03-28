package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Chat extends JFrame implements KeyListener{

    WebView webview;
    JFXPanel fxPanel;
    String usuario = "";
    String imagen  = "";
    MulticastSocket m;
    JTextArea mensajeField;
    String [] iconosUsuarios = {"https://raw.githubusercontent.com/Esmegod/Practicas-Redes/main/Practica3/Multicast/img/arana.png",
                "https://raw.githubusercontent.com/Esmegod/Practicas-Redes/main/Practica3/Multicast/img/cocodrilo.png",
                "https://raw.githubusercontent.com/Esmegod/Practicas-Redes/main/Practica3/Multicast/img/gato.png",
                "https://raw.githubusercontent.com/Esmegod/Practicas-Redes/main/Practica3/Multicast/img/mapache.png",
                "https://raw.githubusercontent.com/Esmegod/Practicas-Redes/main/Practica3/Multicast/img/perro.png",
                "https://raw.githubusercontent.com/Esmegod/Practicas-Redes/main/Practica3/Multicast/img/vaca.png"};

    public int aleatorio(){
        int x = (int)(Math.random()*5);
        return x;
    }

    public Chat(){
        //Pestaña de usuarios
        usuario = JOptionPane.showInputDialog(null,"Ingrese su usuario");
        imagen = iconosUsuarios[aleatorio()];
        //Se obtiene el socket
        Socket multicastSocket = new Socket();
        this.m = multicastSocket.conectarse(usuario);
        
        //Se personaliza la venyana
        setBounds(20, 20, 800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setTitle("Chat de " + usuario);
        
        //Colores constantes
        Color gris = new Color(247,247,252);
        Color grisFuerte = new Color(176,176,184);
        //Azul: 64,136,252
        //Azul clarito: 223, 240, 255
        //Gris fuerte: 176, 176, 184
        
        //Creación de paneles 
        JPanel areaUsuarios = new JPanel();
        areaUsuarios.setBounds(550, 0, 250, 300);
        areaUsuarios.setBorder(BorderFactory.createMatteBorder(0,0,2,0,gris));
        areaUsuarios.setBackground(Color.white);
        add(areaUsuarios);
        
        JPanel areaMensajes = new JPanel();
        areaMensajes.setBounds(0, 300, 800, 200);
        areaMensajes.setBackground(Color.white);
        areaMensajes.setLayout(null);
        add(areaMensajes);
        
        //Se configura el area Chat con scroll y webView
        fxPanel = new JFXPanel();
        fxPanel.setBackground(Color.white);
        
        JScrollPane scroll = new JScrollPane();
        scroll.setBorder(BorderFactory.createMatteBorder(0,0,2,2,gris));
        scroll.setViewportView(fxPanel);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(0,0,550,300);
        getContentPane().add(scroll);
      
        //Elementos de Area Mensaje
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
        
        setResizable(false);
        setVisible(true);
        
        //Se inicia la ventana FX (WebView)
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
               initFX(fxPanel);
               Recibe r = new Recibe(m, webview.getEngine());
                r.start();
            }
        });
    }
    
    public void initFX(JFXPanel fxPanel){
        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        webview = new WebView();
        root.getChildren().add(webview);
        fxPanel.setScene(scene);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        String msj = "";
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            try{
                InetAddress grupo = InetAddress.getByName("230.1.1.1");
                msj = mensajeField.getText();
                String div = "<div class='msj'><p class='nombre'>" + usuario + "</p><div class='flex'>"+
                "<img src='" + imagen + "' alt='usuario' class='avatar'>"+
                "<div class='mensaje'>" + msj + "</div>"+
                "</div></div>";
                byte [] b = div.getBytes();
                DatagramPacket p = new DatagramPacket(b, b.length, grupo, 4000);
                m.send(p);
                mensajeField.setText("");
            }catch(Exception ex){
                ex.printStackTrace();
            }   
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}
}