package Server;

import java.io.IOException;

// klass med regler for spelet
public class ServerSideGame {

    ServerSidePlayer currentPlayer;
    private String[] questions; // tills vi har en riktig klass
    private int questionNumber = 0;
    private int totalRounds;

    private static final int SELECTING_CATEGORY = 0;
    private static final int ASKING_FIRST_PLAYER = 1;
    private static final int ASKING_SECOND_PLAYER = 2;
    private static final int ALL_QUESTIONS_ANSWERED = 3;
    private int currentState = SELECTING_CATEGORY;

    // TODO Avgör hur exceptions ska hanteras
    // TODO synchronized?

    void gameLoop() throws IOException {
        for (int currentRound = 1; currentRound <= totalRounds; currentRound++) {
            playRound();
        }
    }

    void playRound() throws IOException {
        // TODO Flytta till en lämplig metod (konstruktor? run? main?)
        currentState = SELECTING_CATEGORY;
        while (true) {
            if (currentState == SELECTING_CATEGORY) {
                String category = getCategory(currentPlayer);
                // Hämta rondens frågor
                // Något i stil med Database.getQuestions(antal, kategori)
            } else if (currentState == ASKING_FIRST_PLAYER ||
            currentState == ASKING_SECOND_PLAYER) {
                // Notera nuvarande spelaren?
                String answer = askQuestion(currentPlayer);
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

    String askQuestion(ServerSidePlayer player) throws IOException {
        currentPlayer.output.writeObject(questions[questionNumber]);
        return player.input.readLine();
    }

    String getCategory(ServerSidePlayer player) throws IOException {
        player.output.writeObject("Här frågar vi efter en kategori");
        return player.input.readLine();
    }

    void nextQuestion() {
        if (questionNumber < questions.length) {
            questionNumber++;
        } else {
            questionNumber = 0; // Inför nästa rond
            if (currentState == ASKING_FIRST_PLAYER) {
                currentState = ASKING_SECOND_PLAYER;
                currentPlayer = currentPlayer.oponentPlayer;
            } else {
                currentState = ALL_QUESTIONS_ANSWERED;
            }
        }
    }

    void sendResults(ServerSidePlayer player) throws IOException {
        // TODO Formatera resultaten ordentligt
        Integer[] results = {player.points, player.oponentPlayer.points};
        player.output.writeObject(results);
    }

}
