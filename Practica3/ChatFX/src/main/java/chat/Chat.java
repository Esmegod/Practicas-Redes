package chat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.net.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Chat extends JFrame implements KeyListener, ActionListener{

    WebView webview, webview2;
    JFXPanel fxPanel, fxPanelUsuarios;
    String usuario = "";
    String imagen  = "";
    MulticastSocket m;
    JTextPane mensajeField;
    JComboBox<String> usuariosPrivados;
    String encabezadoMsj = "";
    String textoPane = "";
    JDialog modalEmojis;
    JButton emojiButton;
    JButton imagenButton;
    JButton microButton;
    
    String [] iconosUsuarios = {"https://raw.githubusercontent.com/Esmegod/Practicas-Redes/main/Practica3/Multicast/img/vaca.png",
                "https://raw.githubusercontent.com/Esmegod/Practicas-Redes/main/Practica3/Multicast/img/arana.png",
                "https://raw.githubusercontent.com/Esmegod/Practicas-Redes/main/Practica3/Multicast/img/gato.png",
                "https://raw.githubusercontent.com/Esmegod/Practicas-Redes/main/Practica3/Multicast/img/mapache.png",
                "https://raw.githubusercontent.com/Esmegod/Practicas-Redes/main/Practica3/Multicast/img/perro.png",
                "https://raw.githubusercontent.com/Esmegod/Practicas-Redes/main/Practica3/Multicast/img/cocodrilo.png"};

    public int aleatorio(){
        int x = (int)(Math.random()*5);
        return x;
    }

    public Chat(){
        //Pestaña de usuarios
        usuario = JOptionPane.showInputDialog(null,"Ingrese su usuario");
        imagen = iconosUsuarios[aleatorio()];
        //Se obtiene el socket
        SocketChat multicastSocket = new SocketChat();
        this.m = multicastSocket.conectarse(usuario);
        //Se obtiene la ruta
        File f = new File("");
        String ruta = f.getAbsolutePath();
        encabezadoMsj = "<base href=\"file:"+ruta+"\\\"><style>p{display:inline}</style>";

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
        fxPanelUsuarios = new JFXPanel();
        fxPanelUsuarios.setBackground(Color.white);
        fxPanelUsuarios.setBounds(550, 0, 250, 300);
        fxPanelUsuarios.setBorder(BorderFactory.createMatteBorder(0,0,2,0,gris));
        add(fxPanelUsuarios);
        
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
        
        usuariosPrivados = new JComboBox<>();
        usuariosPrivados.setBounds(80, 8, 100, 30);
        areaMensajes.add(usuariosPrivados);
        usuariosPrivados.setOpaque(true);
        usuariosPrivados.setBackground(gris);
        usuariosPrivados.addItem("Todos");
  
        emojiButton = new JButton();
        ImageIcon emojiIcon = new ImageIcon("img/smile.png");
        emojiButton.setIcon(new ImageIcon(emojiIcon.getImage().getScaledInstance(25,25,Image.SCALE_SMOOTH)));
        emojiButton.setOpaque(true);
        emojiButton.setBorderPainted(false);
        emojiButton.setBackground(Color.white);
        emojiButton.setBounds(640, 8, 30, 30);
        emojiButton.addActionListener(this);
        areaMensajes.add(emojiButton);
        
        imagenButton = new JButton();
        ImageIcon imagenIcon = new ImageIcon("img/gallery.png");
        imagenButton.setIcon(new ImageIcon(imagenIcon.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH)));
        imagenButton.setOpaque(true);
        imagenButton.setBorderPainted(false);
        imagenButton.setBackground(Color.white);
        imagenButton.setBounds(680, 8, 30, 30);
        imagenButton.addActionListener(this);
        areaMensajes.add(imagenButton);
        
        microButton = new JButton();
        ImageIcon microIcon = new ImageIcon("img/microphone.png");
        microButton.setIcon(new ImageIcon(microIcon.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH)));
        microButton.setOpaque(true);
        microButton.setBorderPainted(false);
        microButton.setBackground(Color.white);
        microButton.setBounds(720, 8, 30, 30);
        microButton.addActionListener(this);
        areaMensajes.add(microButton);
        
        mensajeField = new JTextPane();
        mensajeField.setContentType("text/html");
        mensajeField.setBorder(BorderFactory.createMatteBorder(1,1,1,1,gris));
        mensajeField.setBounds(10,50, 765, 100);
        mensajeField.addKeyListener(this);
        //mensajeField.setText("<img src='file:D:\\Fer_Mtz\\Desktop\\3CM2\\Aplicaciones Red\\Practicas-Redes\\Practica3\\ChatFX\\emojis\\poo.png' />");
        areaMensajes.add(mensajeField);

        modalEmojis = new JDialog();
        modalEmojis.setSize(200,300);
        modalEmojis.setLayout(new GridLayout(6,5));
        JButton[] emojis = new JButton[30];
        String[] emojisNombres = {"amazed", "amused", "angel", "anger", "angry", "cold",
        "confused", "cool", "cry", "crying", "exploding", "flushed", "ghost", "greed",
        "happy", "in-love","laughing","love","mute","neutral","poo","puke","rolling","sad",
        "sleeping","smile","surprised","tongue","upside-down","zany"};        
        for(int i=0; i<30; i++){
            ImageIcon iconoEmoji = new ImageIcon("emojis/"+emojisNombres[i]+".png");
            emojis[i] = new JButton();
            emojis[i].setBackground(Color.white);
            emojis[i].setBorderPainted(false);
            emojis[i].setIcon(iconoEmoji);
            emojis[i].setName(emojisNombres[i]);
            emojis[i].addActionListener(this);
            modalEmojis.add(emojis[i]);
        }
        
        setResizable(false);
        setVisible(true);
        
        //Se inicia la ventana FX (WebView)
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel, fxPanelUsuarios);
                //Enviar m, combobox, webview 
                Recibe r = new Recibe(m, webview.getEngine(),webview2.getEngine(), usuario, usuariosPrivados);
                r.start();
            }
        });
    }
    
    public void initFX(JFXPanel fxPanel, JFXPanel fxPanelUsuarios){
        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        webview = new WebView();
        root.getChildren().add(webview);
        fxPanel.setScene(scene);

        StackPane root2 = new StackPane();
        Scene scene2 = new Scene(root2);
        webview2 = new WebView();
        root2.getChildren().add(webview2);
        fxPanelUsuarios.setScene(scene2);
    }
   
    @Override
    public void keyPressed(KeyEvent e) {}

    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        String msj = "";
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            try{
                InetAddress grupo = InetAddress.getByName("230.1.1.1");
                msj = mensajeField.getText().replace("<html>", "").replace("<head>", "").replace("<body>", "").replace("</html>", "").replace("</head>", "").replace("</body>", "").replace("<p style=\"margin-top: 0\">", "").replace("</p>", "").trim();
                String destinatario = (String) usuariosPrivados.getSelectedItem();
                String destinatariosMensaje = "";
                if(destinatario.equals("Todos")){
                    destinatariosMensaje = usuario;
                }else{
                    destinatariosMensaje = "Mensaje privado entre " + usuario + " y "+ destinatario; 
                }
                String mensaje = "<div class='msj'><p class='nombre'>"+destinatariosMensaje+"</p><div class='flex'>"+
                "<img src='" + imagen + "' alt='usuario' class='avatar'>"+
                "<div class='mensaje flex'>" + msj + "</div>"+
                "</div></div>";
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream dos = new ObjectOutputStream(baos);
                dos.writeUTF(destinatario);
                dos.writeUTF(usuario);
                dos.writeInt(1);
                dos.writeUTF(mensaje);
                dos.writeObject(null);
                dos.flush();
                DatagramPacket p = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, grupo, 4000);
                m.send(p);
                mensajeField.setText("");
                modalEmojis.dispose();
                dos.close();
                baos.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }   
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if((JButton)e.getSource() == emojiButton){
            modalEmojis.setLocationRelativeTo(emojiButton);;
            modalEmojis.setVisible(true);
        }else if((JButton)e.getSource() == imagenButton){
            System.out.println("Imagen");
        }else if((JButton)e.getSource() == microButton){
            System.out.println("Micro");
        }else{
            // "<img src='emojis\\"+emoji+".png' />"
            String emoji = ((JButton)e.getSource()).getName();
            textoPane = mensajeField.getText().replace("</p>","<img class='emoji' src='emojis\\"+emoji+".png' /></p>");
            mensajeField.setText(encabezadoMsj + textoPane);
        }   
        
    }
}