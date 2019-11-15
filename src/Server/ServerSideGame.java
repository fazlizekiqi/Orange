package Server;

import Database.Database;
import question.Question;

import java.io.IOException;
import java.util.List;

// klass med regler for spelet
public class ServerSideGame extends Thread {

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

    @Override
    public void run() {
        try {
            for (int currentRound = 1; currentRound <= totalRounds; currentRound++) {
                playRound();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void playRound() throws IOException {
        currentState = SELECTING_CATEGORY;
        while (true) {
            if (currentState == SELECTING_CATEGORY) {
                String categoryName = getCategoryName(currentPlayer);
                questions = questionDB.getQuestions(categoryName, questionsPerRound);
                currentState = ASKING_FIRST_PLAYER;
            } else if (currentState == ASKING_FIRST_PLAYER) {
                checkAnswer();
                currentState = ASKING_SECOND_PLAYER;
            } else if (currentState == ASKING_SECOND_PLAYER) {
                checkAnswer();
                currentState = ALL_QUESTIONS_ANSWERED;
            } else {
                // Avsluta ronden, börja på nästa
                sendResults(currentPlayer);
                sendResults(currentPlayer.oponentPlayer);
                return; // ?
            }
        }
    }

    private void checkAnswer() throws IOException {
        Question q = questions.get(questionNumber);
        String answer = askQuestion(q, currentPlayer);
        if (q.isRightAnswer(answer)) {
            currentPlayer.points++;
        }
        nextQuestion();

    }

    String askQuestion(Question question, ServerSidePlayer player) throws IOException {
        player.output.writeObject(question);
        return player.input.readLine();
    }

    String getCategoryName(ServerSidePlayer player) throws IOException {
        player.output.writeObject("Här frågar vi efter en kategori");
        return player.input.readLine();
    }

    void nextQuestion() {
        if (questionNumber < questions.size() - 1) {
            if (currentState == ASKING_FIRST_PLAYER &&questionNumber==questionsPerRound-1) {
                System.out.println("fazli");
                // Flytta state-logiken till playRound?
                currentPlayer = currentPlayer.oponentPlayer;
                currentState = SELECTING_CATEGORY;
            }
        }
        questionNumber++;
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
