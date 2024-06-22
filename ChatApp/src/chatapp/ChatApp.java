package chatapp;

import chatapp.src.client.Client;
import chatapp.view.ClientGUI;
import javax.swing.SwingUtilities;

public class ChatApp {

    private String serverAddress;
    private int serverPort;
    private String username;
    
    public ChatApp(String username) {
        initChat(username);
    }
    
    public void runChat() {
        Client client = new Client(serverAddress, serverPort, username);
        SwingUtilities.invokeLater(() -> {
            ClientGUI gui = new ClientGUI(client, username);
            gui.setVisible(true);
            client.setGUI(gui);
            client.start();
        });
        
    }
    private void initChat(String username) {
        this.serverAddress = "localhost";
        this.serverPort = 12345;
        this.username = username;
    }

}
