package Server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public static void main(String[] args) {
        ServerSocket listener;
        try {
           listener=new ServerSocket(56565);
        } catch (IOException e) {
            e.printStackTrace();
        }


        while(true){

            //TODO: new ServerSideGame
            //TODO: new Player1
            //TODO: new Player2




        }
    }
}
