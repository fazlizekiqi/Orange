package Server;

import java.io.*;
import java.net.Socket;

public class ServerSidePlayer {

    Socket socket;
    ServerSidePlayer oponentPlayer;
    int points;
    String name;
    ServerSideGame game;

    BufferedReader input;
    ObjectOutputStream output;

    ServerSidePlayer(Socket socket, String name, ServerSideGame game) {
        this.socket = socket;
        this.name = name;
        this.game = game;

        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOponentPlayer(ServerSidePlayer oponentPlayer) {
        this.oponentPlayer = oponentPlayer;
    }

    /*@Override
    public void run() {



    }*/
}
