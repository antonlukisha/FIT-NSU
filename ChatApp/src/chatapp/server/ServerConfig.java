package chatapp.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerConfig {
    private static final String CONFIG_FILE = "C:\\Users\\Tony\\ChatApp\\src\\chatapp\\resources\\server.properties";

    public static int getPort() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            return Integer.parseInt(properties.getProperty("port"));
        } catch (IOException | NumberFormatException exception) {
            System.err.println(exception.getMessage());
            return -1;
        }
    }
}

