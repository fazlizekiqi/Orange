package Server;

import Database.Database;
import question.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerSideGame extends Thread {
    private Database db = new Database();
    private ServerSidePlayer currentPlayer;
    private List<Question> questions;
    private int questionsPerRound;
    private int totalRounds;
    private int currentRound = 0;

    private static final int SELECTING_CATEGORY = 0;
    private static final int ASKING_QUESTIONS = 1;
    private static final int SWITCH_PLAYER = 2;
    private static final int ALL_QUESTIONS_ANSWERED = 3;
    private int currentState = SELECTING_CATEGORY;

    @Override
    public void run() {
        try {
            while (true) {
                if (currentState == SELECTING_CATEGORY) {
                    currentPlayer.getOpponent().outputObject.writeObject("Wait until other player chooses a category!");
                    choosingCategory();
                    currentState = ASKING_QUESTIONS;
                    currentPlayer.getOpponent().outputObject.writeObject("Wait until other player answer");
                } else if (currentState == ASKING_QUESTIONS) {
                    handleQuestions();
                    currentState = SWITCH_PLAYER;
                } else if (currentState == SWITCH_PLAYER) {
                    switchingPlayer();
                } else if (currentState == ALL_QUESTIONS_ANSWERED) {
                    sendPoints();
                    sendPointsHistory();
                    hasWinner();
                    resetGame();
                }
            }//While
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//run

    private void resetGame() throws IOException {
        if (isGameOver()) {
            currentPlayer.totPoints = 0;
            currentPlayer.getOpponent().totPoints = 0;
            currentRound = currentRound % totalRounds;
            sendPoints();
            db.resetCount();
            db.shuffleLists();
        }
    }

    void setPlayers(ServerSidePlayer playerOne, ServerSidePlayer playerTwo) {
        playerOne.setOpponent(playerTwo);
        playerTwo.setOpponent(playerOne);
        currentPlayer = playerOne;
    }

    private ServerSidePlayer getPlayerOne() {
        if (currentPlayer.name.equalsIgnoreCase("Player 1")) {
            return currentPlayer;
        } else {
            return currentPlayer.getOpponent();
        }
    }

    private ServerSidePlayer getPlayerTwo() {
        return getPlayerOne().getOpponent();
    }

    private void sendPointsHistory() throws IOException {
        ArrayList<List> listan = new ArrayList<>();
        listan.add(getPlayerOne().scoreHistory);
        //System.out.println("send points history test: " + listan);
        listan.add(getPlayerTwo().scoreHistory);
        //System.out.println("send points history test efter oponentplayer: " + listan);

        getPlayerOne().outputObject.reset();
        getPlayerOne().outputObject.writeObject(listan);
        getPlayerTwo().outputObject.reset();
        getPlayerTwo().outputObject.writeObject(listan);
    }

    private void sendPoints() throws IOException {
        Integer[] points = {getPlayerOne().totPoints, getPlayerTwo().totPoints};
        getPlayerOne().outputObject.writeObject(points);
        getPlayerTwo().outputObject.writeObject(points);
        currentState = SELECTING_CATEGORY;
    }

    private void switchingPlayer() throws IOException {
        if (isRoundOver()) {
            System.out.println(questions.size());
            currentState = ALL_QUESTIONS_ANSWERED;
            System.out.println(questions.size());
        } else {
            switchPlayer();
            currentPlayer.getOpponent().outputObject
                    .writeObject("Wait for the opponent");
            currentState = ASKING_QUESTIONS;
        }
    }


    private void choosingCategory() throws IOException {
        currentPlayer.outputObject.writeObject("Choose category :");
        String category = currentPlayer.input.readLine();
        selectCategory(category);
    }

    private void handleQuestions() throws IOException {
        Question q;
        int tempScore = 0;
        while (!allQuestionsAnswered()) {
            q = questions.get(currentPlayer.questionNumber);
            currentPlayer.outputObject.writeObject(q);
            String answer = currentPlayer.input.readLine();

            if (q.isRightAnswer(answer)) {
                currentPlayer.totPoints++;
                tempScore++;
            }
            nextQuestion();// index ökar med 1
        }//while
        currentPlayer.scoreHistory.add(tempScore);
        //   System.out.println(currentPlayer.scoreHistory.toString());
    }//handleQuestions

    ServerSideGame(int questionsPerRound, int totalRounds) {
        this.questionsPerRound = questionsPerRound;
        this.totalRounds = totalRounds;
    }

    public void hasWinner() throws IOException {
        if (isGameOver()) {
            if (currentPlayer.totPoints > currentPlayer.getOpponent().totPoints) {
                currentPlayer.outputObject.writeObject("YOU WIN");
                currentPlayer.getOpponent().outputObject.writeObject("YOU LOSE");
            } else if (currentPlayer.totPoints < currentPlayer.getOpponent().totPoints) {
                currentPlayer.outputObject.writeObject("YOU LOSE");
                currentPlayer.getOpponent().outputObject.writeObject("YOU WIN");
            } else {
                currentPlayer.outputObject.writeObject("YOU TIED");
                currentPlayer.getOpponent().outputObject.writeObject("YOU TIED");
            }
        }
    }

    public synchronized boolean isRoundOver() {
        if (currentPlayer.questionNumber == questionsPerRound
                && currentPlayer.getOpponent().questionNumber == questionsPerRound) {
            currentPlayer.questionNumber = 0; // nollställer om rundan är över (Problemet är att det finns risk för
            //  att man kan få samma fråga igen om man väljer samma kategori)
            // en annan lösning är att man endast nollställer om questionNumber når list.size()
            currentPlayer.getOpponent().questionNumber = 0;
            ///currentRound++; ökar i selectCategory
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean isGameOver() {
        if (currentRound == totalRounds) {
            //currentRound = 0; // nollställa currentRound???
            return true;
        }
        return false;
    }

    public synchronized void switchPlayer() {
        currentPlayer = currentPlayer.getOpponent();
    }

    public synchronized boolean allQuestionsAnswered() {
        return currentPlayer.questionNumber == questionsPerRound;
    }

    public synchronized void nextQuestion() {
        currentPlayer.questionNumber++;
    }

    public synchronized void selectCategory(String categoryName) {
        questions = db.getQuestions(categoryName, questionsPerRound);
        currentRound++;
    }
}