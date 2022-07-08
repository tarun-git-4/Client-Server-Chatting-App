package ClinetServerMain;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import javafx.application.Platform;

public class ClientRead implements Runnable {
    Socket socket;
    BoysClients client;
    DataInputStream input;

    public ClientRead(Socket socket, BoysClients client) {
        this.socket = socket;
        this.client = client;
    }

    @Override 
    public void run() {
        while (true) {
            try {
                input = new DataInputStream(socket.getInputStream());

                String message = input.readUTF();

                Platform.runLater(() -> {
                    client.txtAreaDisplay.appendText(message + "\n");
                });
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}
