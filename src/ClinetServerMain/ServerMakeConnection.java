package ClinetServerMain;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javafx.application.Platform;


public  class ServerMakeConnection implements Runnable {

    Socket socket;
    TarunServer server;
    DataInputStream input;
    DataOutputStream output;

    public ServerMakeConnection(Socket socket, TarunServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {

        try {
            input = new DataInputStream(
                    socket.getInputStream());
            output = new DataOutputStream(
                    socket.getOutputStream());

            while (true) {
                String message = input.readUTF();
                server.broadcast(message);
                
                Platform.runLater(() -> {
                    server.txtAreaDisplay.appendText(message + "\n");
                });
            }
            
            

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }

    public void sendMessage(String message) {
          try {
            output.writeUTF(message);
            output.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        } 
       
    }

}
