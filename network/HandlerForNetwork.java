package com.multiplayergame.network;

import com.multiplayergame.battleship.GameBoardScreen;
import com.multiplayergame.model.Commands;

import java.io.*;
import java.net.Socket;

public class HandlerForNetwork {

   private Socket socket;
   GameBoardScreen gameBoardScreen;
   BufferedReader reader;
   PrintWriter writer;
   boolean isServer;

   public HandlerForNetwork(Socket socket) {
      this.socket = socket;

      try {
         InputStream is=socket.getInputStream();
         OutputStream os=socket.getOutputStream();

         reader=new BufferedReader(new InputStreamReader(is));
         writer=new PrintWriter(new OutputStreamWriter(os));
      } catch (IOException e) {
         throw new RuntimeException(e);
      }

   }

   public void setGameBoardScreen(GameBoardScreen gameBoardScreen, boolean isServer) {
      this.gameBoardScreen = gameBoardScreen;
      this.isServer=isServer;

   }

   public void sendEnableTurnCommand()
   {
      writer.println(Commands.ENABLE_TURN);
      writer.flush();
      Thread thread=new Thread(new Runnable() {
         @Override
         public void run() {

            waitForInputs();
         }
      });

      thread.start();
   }

   public void waitForInputs() {

      try {
        String cmd= reader.readLine();
        if(cmd.equalsIgnoreCase(Commands.HAS_SHIP))
        {
           String data=reader.readLine();
           String token[]=data.split(":");
           int row=Integer.parseInt(token[0]);
           int col=Integer.parseInt(token[1]);

           boolean isShip=gameBoardScreen.hasShip(row,col);
           if(isShip)
              writer.println("yes");
           else
              writer.println("No");
           writer.flush();


        }
        cmd=reader.readLine();
        System.out.println(cmd);
        if(cmd.equalsIgnoreCase(Commands.ENABLE_TURN))
           gameBoardScreen.enableMyTurn();
      }
      catch (Exception e)
      {

         e.printStackTrace();
      }
   }

   public boolean sendHasShipCommand(int row, int column) {
      try {


         writer.println(Commands.HAS_SHIP);
         writer.println(row + ":" + column);
         writer.flush();
         String response = reader.readLine();
         if(response.equalsIgnoreCase("yes"))
            return true;
         else return false;
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }

      return false;
   }





}
