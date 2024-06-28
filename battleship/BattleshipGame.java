package com.multiplayergame.battleship;

import com.multiplayergame.network.InternalGameServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BattleshipGame extends Application {

    @Override
    public void start(Stage stage)  {
      new MainMenuScreen();
    }

    public static void main(String[] args) {

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                if(args.length!=0)
                InternalGameServer.startServer(Integer.parseInt(args[0]));
                else
                    InternalGameServer.startServer(4444);
            }
        });
        thread.start();

        launch();
    }
}