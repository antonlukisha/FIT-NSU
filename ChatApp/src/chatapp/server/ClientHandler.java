package chatapp.server;

import chatapp.src.common.ChatMessage;
import chatapp.src.common.Ping;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.Timer;

public class ClientHandler extends Thread {
    private static final int DELAY = 300;
    private final Socket clientSocket;
    private final Server server;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private String username;
    private int inPingPacks = 0;
    private int outPingPacks = 0;
    private boolean flag = false;
    private Timer timer;

    public ClientHandler(Socket clientSocket, Server server, ObjectInputStream in, ObjectOutputStream out) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.in = in;
        this.out = out;
    }
    
    public ClientHandler(Socket clientSocket, String username, Server server, ObjectInputStream in, ObjectOutputStream out) {
        this(clientSocket, server, in, out);
        this.username = username;
    }

    @Override
    public void run() {
        try {
            this.timer = new Timer(DELAY, (ActionEvent event) -> {
                try {
                    if (inPingPacks == outPingPacks) {
                        out.writeObject(new Ping());
                        out.flush();
                        outPingPacks++;
                        System.out.println(outPingPacks + " out ping to " + username);
                    } else {
                        throw new SocketException();
                    }
                } catch (SocketException exception) {
                    System.out.println("packages not clash");
                    System.out.println("Client " + username + " disconect");
                    server.removeClient(username);
                    server.broadcast(new ChatMessage(ChatMessage.MessageType.LOGOUT, username + " logout from chat"));
                    flag = true;
                    timer.stop();
                } catch (IOException exception) {
                    System.err.println(exception.getMessage());
                }
            });
            this.timer.start();
            out.writeObject(new Ping());
            this.outPingPacks++;
            System.out.println(outPingPacks + " out ping to " + username);
            handleClient();
        } catch (IOException exception) {
            System.err.println("Error handling client: " + exception.getMessage());
        } finally {
            try {
                clientSocket.close();
                server.removeClient(username);
                server.broadcast(new ChatMessage(ChatMessage.MessageType.LOGOUT, username + " logout from chat"));
                System.out.println("Client " + username + " disconect");
            } catch (IOException exception) {
                System.err.println("Error closing client socket: " + exception.getMessage());
            }
        }
    }

    private void handleClient() throws IOException {
        try {
            while (true) {
                if (this.flag) {
                    this.flag = false;
                    break;
                }
                Object obj = in.readObject();
                if (obj instanceof ChatMessage message) {
                    if (message == null || message.getType() == ChatMessage.MessageType.LOGOUT) {
                        break;
                    }
                    System.out.println(username + ": " + message.getContent());
                    server.broadcast(message);
                } else if (obj instanceof Ping) {
                    this.inPingPacks++;
                    System.out.println(this.inPingPacks + " in ping from " + username);
                }
            }
        } catch (ClassNotFoundException exception) {
            System.err.println("Error reading message: " + exception.getMessage());
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
