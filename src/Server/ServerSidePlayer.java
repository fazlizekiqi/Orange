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
            if (game.currentPlayer.equals(this)) {
                game.currentPlayer.outputObject.writeObject("choose category");
                game.selectCatagory(input.readLine());
                List<Question> questions = game.getQuestions();
                Question q;
                sendQuestions(questions);
                game.currentPlayer.outputObject.writeObject("wait for the opponent");
                game.switchPlayer();
                sendQuestions(questions);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendQuestions(List<Question> questions) throws IOException {
        Question q;
        while (game.currentPlayer.questionNumber < game.getQuestionsPerRound()) {
            System.out.println(game.currentPlayer.questionNumber);
            q = questions.get(game.currentPlayer.questionNumber);
            System.out.println(q.getQuestion());
            game.currentPlayer.outputObject.writeObject(q);
            String answer = game.currentPlayer.input.readLine();
            System.out.println(answer);
            if (q.isRightAnswer(answer)) {
                game.currentPlayer.points++;
            }
            System.out.println("poäng" + game.currentPlayer.points);
            game.nextQuestion();// index ökar med 1
            System.out.println("questionNumber ökar till" + game.currentPlayer.questionNumber);
        }
    }
}
