package com.multiplayergame.network;

import java.net.ServerSocket;
import java.net.Socket;

public class InternalGameServer {

   static Socket communicationSocket;

    public static void startServer(int port)
    {
        try {
            ServerSocket serverSocket=new ServerSocket(port);



            communicationSocket = serverSocket.accept();



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }



    public static Socket getCommunicationSocket() {
        return communicationSocket;
    }
}
