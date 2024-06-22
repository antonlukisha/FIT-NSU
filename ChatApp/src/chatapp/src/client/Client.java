package chatapp.src.client;

import chatapp.view.ClientGUI;
import chatapp.src.common.ChatMessage;
import chatapp.src.common.Ping;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private final String serverName;
    private final int serverPort;
    private final String username;
    private ClientGUI gui;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Thread serverListenerThread;
    private boolean isRunning;

    public Client(String serverName, int serverPort, String username) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.username = username;
        this.isRunning = true;
    }

    public void setGUI(ClientGUI gui) {
        this.gui = gui;
    }

    public void start() {
        try {
            this.socket = new Socket(serverName, serverPort);
            System.out.println("Connected to server: " + socket);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            sendMessage(new ChatMessage(ChatMessage.MessageType.LOGIN, username));

            if (serverListenerThread == null || !serverListenerThread.isAlive()) {
                serverListenerThread = new Thread(new ServerListener());
                serverListenerThread.start();
            } else {
                System.err.println("Server listener thread is already running.");
            }

        } catch (IOException exception) {
            System.err.println("Error connected to server: " + exception.getMessage());
        } 
    }

    public void sendMessage(ChatMessage message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException exception) {
            System.err.println("Error sending client message: " + exception.getMessage());
        }
    }

    public void closeSession() {
        if (serverListenerThread != null && serverListenerThread.isAlive()) {
            try {
                isRunning = false;
                serverListenerThread.interrupt();
                serverListenerThread.join();
            } catch (InterruptedException exception) {
                System.err.println("Error closing client: " + exception.getMessage());
            }
        }
    }

    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                while (isRunning) {
                    if (in != null) {
                        Object obj = in.readObject();
                        if (obj instanceof Ping) {
                            out.writeObject(new Ping());
                            out.flush();
                        } else if (obj instanceof ChatMessage message) {
                            if (message.getType() == ChatMessage.MessageType.LOGOUT || message.getType() == ChatMessage.MessageType.LOGIN) {
                                if (gui != null) {
                                    gui.addPost(message.getContent());
                                }
                            } else {
                                System.out.println("Received message: " + message.getContent());
                                if (gui != null) {
                                    gui.addMessage(message.getSender(), message.getContent());
                                }
                            }
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException exception) {
                if (isRunning) { 
                    System.err.println("Error messaging client: " + exception.getMessage());
                }
            }
        }
    }
}
