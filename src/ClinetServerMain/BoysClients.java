package ClinetServerMain;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class BoysClients extends Application {
    TextField txtName;
    TextField txtInput;
    ScrollPane scrollPane;
    public TextArea txtAreaDisplay;


    DataOutputStream output = null;

    @Override
    public void start(Stage primaryStage) {
        VBox vBox = new VBox();

        scrollPane = new ScrollPane();
        HBox hBox = new HBox();

        txtAreaDisplay = new TextArea();
        txtAreaDisplay.setEditable(false);
        scrollPane.setContent(txtAreaDisplay);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        txtName = new TextField();
        txtName.setPromptText("Name");
        txtName.setTooltip(new Tooltip("Write your name. "));
        txtInput = new TextField();
        txtInput.setPromptText("New message");
        txtInput.setTooltip(new Tooltip("Write your message. "));
        Button btnSend = new Button("Send");
        btnSend.setOnAction(new ButtonListener());

        hBox.getChildren().addAll(txtName, txtInput, btnSend);
        hBox.setHgrow(txtInput, Priority.ALWAYS);

        vBox.getChildren().addAll(scrollPane, hBox);
        vBox.setVgrow(scrollPane, Priority.ALWAYS);

        Scene scene = new Scene(vBox, 450, 500);
        primaryStage.setTitle("Client: JavaFx Text Chat App");
        primaryStage.setScene(scene);
        primaryStage.show();


        try {
            Socket socket = new Socket(ConnectionUtil.host, ConnectionUtil.port);

            txtAreaDisplay.appendText("Welcome to your personal chat room \n");
            output = new DataOutputStream(socket.getOutputStream());

            ClientRead task = new ClientRead(socket, this);
            Thread thread = new Thread(task);
            thread.start();
        } catch (IOException ex) {

            txtAreaDisplay.appendText(ex.toString() + '\n');
        }

    }


    public static void main(String[] args) {
        launch(args);
    }

    private class ButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            try {
                String username = txtName.getText().trim();
                String message = txtInput.getText().trim();

                if (username.length() == 0) {
                    username = "Unknown";
                }
                if (message.length() == 0) {
                    return;
                }

                output.writeUTF("[" + username + "]: " + message + "");
                output.flush();

                txtInput.clear();
            } catch (IOException ex) {
                System.err.println(ex);
            }

        }
    }

}
