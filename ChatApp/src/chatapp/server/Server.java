package chatapp.server;

import chatapp.src.common.ChatMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private final int port;
    private final Map<String, ObjectOutputStream> clients = new HashMap<>();

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                System.out.println("Waiting for clients...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                String username = ((ChatMessage)in.readObject()).getContent();
                clients.put(username, out);
                System.out.println("New client registered: " + username);
                broadcast(new ChatMessage(ChatMessage.MessageType.LOGIN, username + " login to chat"));

                new ClientHandler(clientSocket, username, this, in, clients.get(username)).start();
            }
        } catch (IOException | ClassNotFoundException exception) {
            System.err.println(exception.getMessage());
        }
    }

    public void broadcast(ChatMessage message) {
        for (ObjectOutputStream client : clients.values()) {
            try {
                client.writeObject(message);
                client.flush();
            } catch (IOException exception) {
                System.err.println(exception.getMessage());
            }
        }
    }
    
    public void sendPingMessage(String username) {
        try {
            clients.get(username).writeObject(new ChatMessage(ChatMessage.MessageType.PING, "ping"));
            clients.get(username).flush();
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }
    
    public void removeClient(String username) {
        clients.remove(username);
    }
    
    public static void main(String[] args) {
        int port = ServerConfig.getPort();
        if (port == -1) {
            System.err.println("Failed to start server. Port is not configured.");
        }
        Server server = new Server(port);
        server.start();
    }
}
