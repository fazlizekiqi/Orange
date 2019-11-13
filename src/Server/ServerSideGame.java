package Server;

import java.io.IOException;

// klass med regler for spelet
public class ServerSideGame {
    ServerSidePlayer currentPlayer;
    String[] questions; // tills vi har en riktig klass
    int questionNumber = 0;
    int playerOnePoints = 0;
    int playerTwoPoints = 0;

    // TODO Avgör hur exceptions ska hanteras

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
            // Ronden över, vad göra?
        }
    }

    void sendResults() throws IOException {
        // TODO Formatera resultaten ordentligt
        Integer[] results = {playerOnePoints, playerTwoPoints};
        currentPlayer.output.writeObject(results);
        currentPlayer.oponentPlayer.output.writeObject(results);
    }

}
