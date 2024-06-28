package com.multiplayergame.battleship;

import com.multiplayergame.network.HandlerForNetwork;
import com.multiplayergame.network.InternalGameServer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.Socket;


public class MultiplayerConnectionScreen {


    TextField txtIp;
    TextField txtPort;
    Button btnConnect;
    Label lblWantToConnect;
    Button btnAccept;
    Button btnReject;
    Label lblErr;

    public MultiplayerConnectionScreen() {
        setUI();

    }

    private void setUI()
    {
         txtIp=new TextField();
         txtIp.setPromptText("Enter Ip address");
         txtPort=new TextField();
         txtPort.setPromptText("Enter Port");
         btnConnect=new Button("Connect");
        lblWantToConnect=new Label("A Player wants to challenge You?");
        btnAccept=new Button("Accept");
        btnReject=new Button("Reject");
        lblErr=new Label();


        GridPane gridPane=new GridPane();
        gridPane.setVgap(5);
        GridPane.setConstraints(txtIp,0,0);
        GridPane.setConstraints(txtPort,0,1);
        GridPane.setConstraints(btnConnect,1,2);
        gridPane.getChildren().addAll(txtIp,txtPort,btnConnect);
        gridPane.setAlignment(Pos.CENTER);

        HBox hBox=new HBox();
        hBox.setSpacing(2);
        hBox.getChildren().addAll(lblWantToConnect,btnAccept,btnReject);
        hBox.setPadding(new Insets(5,5,5,5));
        GridPane.setConstraints(hBox,0,4);

        gridPane.getChildren().addAll(hBox);
        hBox.setVisible(false);

        Scene scene=new Scene(gridPane,400,300);
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.show();
        btnConnect.requestFocus();

     Thread thread=new Thread(new Runnable() {
         @Override
         public void run() {
             boolean isAccepted=false;

             while (!isAccepted)
             {
                 if(InternalGameServer.getCommunicationSocket()!=null)
                 {
                     hBox.setVisible(true);
                     isAccepted=true;
                 }

                 try {
                     Thread.sleep(2000);
                 } catch (InterruptedException e) {
                     throw new RuntimeException(e);
                 }


             }


         }
     });

     thread.start();

     //add handlers to button
        btnAccept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                HandlerForNetwork handlerForNetwork=new HandlerForNetwork(InternalGameServer.getCommunicationSocket());

               GameBoardScreen board= new GameBoardScreen(handlerForNetwork);
               board.myTurn=false;
                handlerForNetwork.setGameBoardScreen(board,true);
                Thread thread1=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handlerForNetwork.waitForInputs();
                    }
                });

                thread1.start();
            }
        });

        btnConnect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String ip=txtIp.getText();
                String port=txtPort.getText();

                if(ip==null || ip.isEmpty() || port==null || port.isEmpty())
                {

                }
                else {
                    int portNumber=Integer.parseInt(port);
                    try {


                        Socket socket = new Socket(ip, portNumber);
                        if(socket.isConnected())
                        {
                            HandlerForNetwork handlerForNetwork=new HandlerForNetwork(socket);
                            GameBoardScreen board= new GameBoardScreen(handlerForNetwork);
                            board.myTurn=true;
                            handlerForNetwork.setGameBoardScreen(board,false);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();                    }
                }
            }
        });


    }


}
