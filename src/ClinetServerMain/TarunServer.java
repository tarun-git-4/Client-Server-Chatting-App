package ClinetServerMain;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class TarunServer extends Application {
    public TextArea txtAreaDisplay;
    List<ServerMakeConnection> connectionList = new ArrayList<ServerMakeConnection>();

    @Override
    public void start(Stage primaryStage) {
        txtAreaDisplay = new TextArea();
        txtAreaDisplay.setEditable(false);
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(txtAreaDisplay);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 450, 500);
        primaryStage.setTitle("Server: JavaFx Text Chat App");
        primaryStage.setScene(scene);
        primaryStage.show();
        

        new Thread(() -> {
            try {

                ServerSocket serverSocket = new ServerSocket(ConnectionUtil.port);

                txtAreaDisplay.appendText("IITA BOYS HOSTEL SERVER STARTED AT " + new Date() + '\n');
                while (true) {
                    Socket socket = serverSocket.accept();

                    ServerMakeConnection connection = new ServerMakeConnection(socket,this);
                    connectionList.add(connection);

                    Thread thread = new Thread(connection);
                    thread.start();

                }
            } catch (IOException ex) {
                txtAreaDisplay.appendText(ex.toString() + '\n');
            }
        }).start();
    }


    public static void main(String[] args) {
        launch(args);
    }


    public void broadcast(String message) {
        for (ServerMakeConnection clientConnection : connectionList) {
            clientConnection.sendMessage(message);
        }
    }
}
