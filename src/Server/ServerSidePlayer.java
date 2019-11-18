package Server;

import question.Question;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerSidePlayer extends Thread {

    Socket socket;
    ServerSidePlayer oponentPlayer;
    int points = 0;
    int questionNumber = 0;
    Question q;
    List<Question> questions;
    String answer;

    private static final int SELECTING_CATEGORY = 0;
    private static final int ASKING_QUESTIONS = 1;
    private static final int SWITCH_PLAYER = 2;
    private static final int ALL_QUESTIONS_ANSWERED = 3;
    int currentState = SELECTING_CATEGORY;

    String name;
    ServerSideGame game;
    BufferedReader input;
    ObjectOutputStream outputObject;

    ServerSidePlayer(Socket socket, String name, ServerSideGame game) {
        this.socket = socket;
        this.name = name;
        this.game = game;

        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputObject = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOponentPlayer(ServerSidePlayer oponentPlayer) {
        this.oponentPlayer = oponentPlayer;
    }


    @Override
    public void run() {
        try {
            while (true) {
                if (currentState == SELECTING_CATEGORY) {
                    game.currentPlayer.outputObject.writeObject("choose category");
                    game.selectCatagory(input.readLine());
                    questions = game.getQuestions();
                    currentState = ASKING_QUESTIONS;
                } else if (currentState == ASKING_QUESTIONS) {
                    if (game.allQuestionsAnswered()) {
                        currentState = SWITCH_PLAYER;
                    }
                    else {
                        handleQuestions();
                    }
                } else if (currentState == SWITCH_PLAYER) {
                    game.currentPlayer.outputObject.writeObject("wait for the opponent");
                    game.switchPlayer();
                    currentState = ASKING_QUESTIONS;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleQuestions() throws IOException {
        while (!game.allQuestionsAnswered()) {
            q = questions.get(game.currentPlayer.questionNumber);
            game.currentPlayer.outputObject.writeObject(q);
            System.out.println(q.getQuestion()); // spårutskrift
            answer = game.currentPlayer.input.readLine();
            if (q.isRightAnswer(answer)) {
                game.currentPlayer.points++;
            }
            System.out.println("poäng" + game.currentPlayer.points); // spårutskrift
            game.nextQuestion();// index ökar med 1
            System.out.println("questionNumber ökar till" + game.currentPlayer.questionNumber); // spårutskrift
        }
    }
}
