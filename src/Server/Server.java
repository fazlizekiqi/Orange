package Server;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;

public class Server {
    static int questionsPerRound;
    static int totalRounds;

    public static void main(String[] args) throws IOException {
        ServerSocket listener = null;
        try {
           listener=new ServerSocket(56565);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Properties p = new Properties();
        try {
            p.load(new FileInputStream("properties/testing.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        questionsPerRound = Integer.parseInt(p.getProperty("questions", "2"));
        totalRounds = Integer.parseInt(p.getProperty("rounds", "2"));

        while(true){

            ServerSideGame game=new ServerSideGame(questionsPerRound, totalRounds);

            ServerSidePlayer player1 = new ServerSidePlayer(listener.accept(),"Player 1",game);
            ServerSidePlayer player2 = new ServerSidePlayer(listener.accept(),"Player 2",game);

            game.currentPlayer=player1;
            player1.setOponentPlayer(player2);
            player2.setOponentPlayer(player1);

            player1.start();

            player2.start();

        }
    }
}
