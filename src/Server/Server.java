package Server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket listener = null;
        try {
           listener=new ServerSocket(56565);
        } catch (IOException e) {
            e.printStackTrace();
        }


        while(true){

            ServerSideGame game=new ServerSideGame();

            ServerSidePlayer player1=new ServerSidePlayer(listener.accept(),"Player 1",game);
            ServerSidePlayer player2=new ServerSidePlayer(listener.accept(),"Player 2",game);

            game.currentPlayer=player1;
            player1.setOponentPlayer(player2);
            player2.setOponentPlayer(player1);

            player1.start();
            player2.start();


        }
    }
}
