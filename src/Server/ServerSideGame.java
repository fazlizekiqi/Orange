package Server;

import java.io.IOException;

// klass med regler for spelet
public class ServerSideGame {

    ServerSidePlayer currentPlayer;
    String[] questions; // tills vi har en riktig klass
    int questionNumber = 0;
    int playerOnePoints = 0;
    int playerTwoPoints = 0;

    static int SELECTING_CATEGORY = 0;
    static int ASKING_QUESTIONS = 1;
    static int ALL_QUESTIONS_ANSWERED = 2;
    int currentState = SELECTING_CATEGORY;

    // TODO Avgör hur exceptions ska hanteras
    // TODO synchronized?

    void gameLoop() throws IOException {
        // TODO Flytta till en lämplig metod (konstruktor? run? main?)
        while (true) {
            if (currentState == SELECTING_CATEGORY) {
                String category = getCategory();
                // Hämta rondens frågor
            } else if (currentState == ASKING_QUESTIONS) {
                // Notera nuvarande spelaren?
                String answer = askQuestion();
                if (answer.equals("Det rätta svaret")) {
                    // Lägg till poäng
                }
                currentPlayer = currentPlayer.oponentPlayer;
            }
        }
    }

    String askQuestion() throws IOException {
        currentPlayer.output.writeObject(questions[questionNumber]);
        return currentPlayer.input.readLine();
    }

    String getCategory() throws IOException {
        currentPlayer.output.writeObject("Här frågar vi efter en kategori");
        return currentPlayer.input.readLine();
    }

    void nextQuestion() {
        if (questionNumber < questions.length) {
            questionNumber++;
        } else {
            currentState = ALL_QUESTIONS_ANSWERED;
        }
    }

    void sendResults() throws IOException {
        // TODO Formatera resultaten ordentligt
        Integer[] results = {playerOnePoints, playerTwoPoints};
        currentPlayer.output.writeObject(results);
        currentPlayer.oponentPlayer.output.writeObject(results);
    }

}
