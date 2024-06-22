package chatapp.view;

import chatapp.src.client.Client;
import chatapp.src.common.ChatMessage;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClientGUI extends JFrame {
    private final Client client;
    private final String username;
    private JPanel chatPanel;
    private JTextField messageField;
    private final Box vertical = Box.createVerticalBox();

    public ClientGUI(Client client, String username) {
        this.client = client;
        this.username = username;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(null);
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.sendMessage(new ChatMessage(ChatMessage.MessageType.LOGOUT, username));
                client.closeSession();
                System.exit(0);
            }
        });
        setSize(465, 750);
        setResizable(false);
        setLocation(800, 50);        
        setLocationRelativeTo(null);
        setBackground(new Color(240, 240, 240));
        
        JPanel contentPane = new JPanel();
        contentPane.setBounds(0, 0, 450, 70);
        contentPane.setBackground(new Color(64, 143, 237)); // Светлый фон
        contentPane.setLayout(null);
        add(contentPane);
        
        ImageIcon backImageIcon = new ImageIcon("C:\\Users\\Tony\\ChatApp\\src\\chatapp\\resources\\3.png");
        Image backImage = backImageIcon.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        backImageIcon = new ImageIcon(backImage);
        JLabel back = new JLabel(backImageIcon);
        back.setBounds(5, 20, 25, 25);
        contentPane.add(back);
        
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                client.sendMessage(new ChatMessage(ChatMessage.MessageType.LOGOUT, username));
                client.closeSession();
                System.exit(0);
            }
        });
        
        ImageIcon profileImageIcon = new ImageIcon("C:\\Users\\Tony\\ChatApp\\src\\chatapp\\resources\\profile.png");
        Image profileImage = profileImageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        profileImageIcon = new ImageIcon(profileImage);
        JLabel profile = new JLabel(profileImageIcon);
        profile.setBounds(40, 10, 50, 50);
        contentPane.add(profile);
        
        ImageIcon videoImageIcon = new ImageIcon("C:\\Users\\Tony\\ChatApp\\src\\chatapp\\resources\\video.png");
        Image videoImage = videoImageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        videoImageIcon = new ImageIcon(videoImage);
        JLabel video = new JLabel(videoImageIcon);
        video.setBounds(300, 20, 30, 30);
        contentPane.add(video);
        
        ImageIcon phoneImageIcon = new ImageIcon("C:\\Users\\Tony\\ChatApp\\src\\chatapp\\resources\\phone.png");
        Image phoneImage = phoneImageIcon.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
        phoneImageIcon = new ImageIcon(phoneImage);
        JLabel phone = new JLabel(phoneImageIcon);
        phone.setBounds(360, 20, 35, 30);
        contentPane.add(phone);
        
        ImageIcon morevertImageIcon = new ImageIcon("C:\\Users\\Tony\\ChatApp\\src\\chatapp\\resources\\3icon.png");
        Image morevertImage = morevertImageIcon.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
        morevertImageIcon = new ImageIcon(morevertImage);
        JLabel morevert = new JLabel(morevertImageIcon);
        morevert.setBounds(420, 20, 10, 25);
        contentPane.add(morevert);
        
        JLabel name = new JLabel(username);
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Tahoma", Font.BOLD, 18));
        contentPane.add(name);
        
        JLabel status = new JLabel("online");
        status.setBounds(110, 35, 100, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("Tahoma", Font.PLAIN, 14));
        contentPane.add(status);
        
        chatPanel = new JPanel();
        chatPanel.setBounds(5, 75, 440, 570);
        add(chatPanel);
        
        messageField = new JTextField();
        messageField.setBounds(5, 655, 310, 40);
        messageField.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        add(messageField);
        
        JButton sendButton = new JButton("Send");
        sendButton.setBounds(320, 655, 123, 40);
        sendButton.setBackground(new Color(64, 143, 237)); // Цвет Telegram
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Tahoma", Font.BOLD, 18));
        sendButton.addActionListener((ActionEvent event) -> {
            sendMessage();
        });
        add(sendButton);

        getContentPane().setBackground(new Color(240, 240, 240));
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            client.sendMessage(new ChatMessage(ChatMessage.MessageType.MESSAGE, username, message));
            messageField.setText("");
        }
    }

    public void addMessage(String sender, String message) {
        JPanel messagePanel = formatLabel(sender, message);
        chatPanel.setLayout(new BorderLayout());
        JPanel bar = new JPanel(new BorderLayout());
        if (sender.equals(username)) {
            bar.add(messagePanel, BorderLayout.LINE_END);
        } else {
            bar.add(messagePanel, BorderLayout.LINE_START);
        }
        vertical.add(bar);
        vertical.add(Box.createVerticalStrut(15));
        chatPanel.add(vertical, BorderLayout.PAGE_START); 
        repaint();
        invalidate();
        validate();
    }

    private JPanel formatLabel(String sender, String message) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + message + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        if (sender.equals(username)) {
            output.setBackground(new Color(52, 152, 219));
        } else {
            output.setBackground(new Color(204, 204, 204));
        }
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));
        
        panel.add(output);
        
        JLabel name = new JLabel();
        name.setText(sender);
        
        panel.add(name);
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        
        panel.add(time);
        
        return panel;
    }

    public void addPost(String content) {
        JLabel postPanel = new JLabel();
        postPanel.setText(content);
        vertical.add(postPanel);
        vertical.add(Box.createVerticalStrut(15));
        chatPanel.add(vertical, BorderLayout.PAGE_START); 
        repaint();
        invalidate();
        validate();
    }
}
