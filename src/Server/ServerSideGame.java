package Server;

import Database.Database;
import Question.Question;

import java.io.IOException;
import java.util.List;

// klass med regler for spelet
public class ServerSideGame {

    Database questionDB = new Database();
    ServerSidePlayer currentPlayer;
    private List<Question> questions;
    private int questionNumber = 0;
    private int questionsPerRound;
    private int totalRounds;

    private static final int SELECTING_CATEGORY = 0;
    private static final int ASKING_FIRST_PLAYER = 1;
    private static final int ASKING_SECOND_PLAYER = 2;
    private static final int ALL_QUESTIONS_ANSWERED = 3;
    private int currentState = SELECTING_CATEGORY;

    // TODO Avgör hur exceptions ska hanteras
    // TODO synchronized?

    ServerSideGame(int questionsPerRound, int totalRounds) {
        this.questionsPerRound = questionsPerRound;
        this.totalRounds = totalRounds;
    }

    void gameLoop() throws IOException {
        for (int currentRound = 1; currentRound <= totalRounds; currentRound++) {
            playRound();
        }
    }

    void playRound() throws IOException {
        currentState = SELECTING_CATEGORY;
        while (true) {
            if (currentState == SELECTING_CATEGORY) {
                String categoryName = getCategoryName(currentPlayer);
                // Hämta rondens frågor
                // Något i stil med Database.getQuestions(antal, kategori)
                currentState = ASKING_FIRST_PLAYER;
            } else if (currentState == ASKING_FIRST_PLAYER ||
            currentState == ASKING_SECOND_PLAYER) {
                String answer = askQuestion(questions.get(questionNumber), currentPlayer);
                if (answer.equals("Det rätta svaret")) {
                    currentPlayer.points++;
                }
                nextQuestion();
            } else if (currentState == ALL_QUESTIONS_ANSWERED) {
                // Avsluta ronden, börja på nästa
                sendResults(currentPlayer);
                sendResults(currentPlayer.oponentPlayer);
                return; // ?
            }
        }
    }

    String askQuestion(Question question, ServerSidePlayer player) throws IOException {
        currentPlayer.output.writeObject(question);
        return player.input.readLine();
    }

    String getCategoryName(ServerSidePlayer player) throws IOException {
        player.output.writeObject("Här frågar vi efter en kategori");
        return player.input.readLine();
    }

    void nextQuestion() {
        if (questionNumber < questions.size()) {
            questionNumber++;
        } else {
            questionNumber = 0; // Inför nästa rond
            if (currentState == ASKING_FIRST_PLAYER) {
                // Flytta state-logiken till playRound?
                currentPlayer = currentPlayer.oponentPlayer;
                currentState = ASKING_SECOND_PLAYER;
            } else {
                currentState = ALL_QUESTIONS_ANSWERED;
            }
        }
    }

    // OBS Resultatens inbördes ordning
    void sendResults(ServerSidePlayer player) {
        // TODO Formatera resultaten ordentligt
        Integer[] results = {player.points, player.oponentPlayer.points};
        try {
            player.output.writeObject(results);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
