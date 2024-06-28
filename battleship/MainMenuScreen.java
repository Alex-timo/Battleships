package com.multiplayergame.battleship;

import com.multiplayergame.network.InternalGameServer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuScreen {

    public MainMenuScreen() {
        Button singlePlayerButton = new Button("Single Player");
        Button multiplayerButton = new Button("Multiplayer");
        Button helpButton = new Button("Help");


        VBox vbox = new VBox(singlePlayerButton, multiplayerButton, helpButton);
        vbox.setPadding(new Insets(10,10,10,10));
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 400, 300);





        Stage primaryStage=new Stage();
        primaryStage.setTitle("BattleShip");
        primaryStage.setScene(scene);
        primaryStage.show();



        //addListeners
        singlePlayerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                AIBoardScreen aiBoardScreen=new AIBoardScreen();

              GameBoardScreen gameBoardScreen=new GameBoardScreen(aiBoardScreen);
                aiBoardScreen.setPlayerBoard(gameBoardScreen);
                primaryStage.close();

            }
        });

        multiplayerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new MultiplayerConnectionScreen();
                primaryStage.close();
            }
        });
    }


}
