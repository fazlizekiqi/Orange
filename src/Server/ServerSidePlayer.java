package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerSidePlayer {
    private Socket socket;
    ServerSidePlayer oponentPlayer;
    int totPoints = 0;
    int questionNumber = 0;
    String name;
    ServerSideGame game;
    public List <Integer> scoreHistory = new ArrayList<>();

    BufferedReader input;
    ObjectOutputStream outputObject;

    ServerSidePlayer(Socket socket, String name, ServerSideGame game) {
        this.socket = socket;
        this.name = name;
        this.game = game;

        try {
            outputObject = new ObjectOutputStream(socket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputObject.writeObject("Welcome: "+name);
            outputObject.writeObject("Wait until the other player is connected!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }//Constructor

    public void setOponentPlayer(ServerSidePlayer oponentPlayer) {
        this.oponentPlayer = oponentPlayer;
    }
}
