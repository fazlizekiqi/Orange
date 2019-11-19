package Server;

import Database.Database;
import java.io.IOException;
import question.Question;

import java.util.List;


public class ServerSideGame extends Thread {

    Database db = new Database();
    ServerSidePlayer currentPlayer;
    private List<Question> questions;
    private int questionsPerRound;
    private int totalRounds;
    private int currentRound = 0;


    private static final int SELECTING_CATEGORY = 0;
    private static final int ASKING_QUESTIONS = 1;
    private static final int SWITCH_PLAYER = 2;
    private static final int ALL_QUESTIONS_ANSWERED = 3;
    int currentState = SELECTING_CATEGORY;

    @Override
    public void run() {
        try {

            while (true) {

                if (currentState == SELECTING_CATEGORY) {
                     currentPlayer.oponentPlayer.outputObject.writeObject("Wait until other player chooses a category!");
                    choosingCategory();
                    currentState = ASKING_QUESTIONS;
                    currentPlayer.oponentPlayer.outputObject.writeObject("Wait until other player answer");
                } else if (currentState == ASKING_QUESTIONS) {
                    handleQuestions();
                    currentState = SWITCH_PLAYER;
                } else if (currentState == SWITCH_PLAYER) {
                    switchingPlayer();
                }else if(currentState==ALL_QUESTIONS_ANSWERED){
                    Integer[] points={currentPlayer.points,currentPlayer.oponentPlayer.points};
                    currentPlayer.outputObject.writeObject(points);
                    currentPlayer.oponentPlayer.outputObject.writeObject(points);
                    currentState = SELECTING_CATEGORY;
                }
            }//While

        } catch (IOException e) {
            e.printStackTrace();
        }
    }//run

    private void switchingPlayer() throws IOException {
        if (isRoundOver()) {
            currentState=ALL_QUESTIONS_ANSWERED;
        } else {
            switchPlayer();
            currentPlayer.oponentPlayer.outputObject
                .writeObject("Wait for the opponent");
            currentState = ASKING_QUESTIONS;
        }
    }

    private void choosingCategory() throws IOException {
        currentPlayer.outputObject.writeObject("Choose category :");
        String category = currentPlayer.input.readLine();
        currentPlayer.game.selectCatagory(category);
    }

    private void handleQuestions() throws IOException {
        while (!allQuestionsAnswered()) {
            Question q = questions.get(currentPlayer.questionNumber);
            currentPlayer.outputObject.writeObject(q);
            String answer = currentPlayer.input.readLine();
            if (q.isRightAnswer(answer)) {
                currentPlayer.points++;
            }
            currentPlayer.game.nextQuestion();// index ökar med 1
        }//while
    }//handleQuestions


    ServerSideGame(int questionsPerRound, int totalRounds) {
        this.questionsPerRound = questionsPerRound;
        this.totalRounds = totalRounds;
    }


    public synchronized boolean hasWinner() {
        if (isGameOver()) {
            if (currentPlayer.points > currentPlayer.oponentPlayer.points
                || currentPlayer.points < currentPlayer.oponentPlayer.points) {
                return true;
            }
        }
        return false;
    }


    public synchronized boolean isTie() {
        if (isGameOver()) {
            if (currentPlayer.points == currentPlayer.oponentPlayer.points) {
                return true;
            }
        }
        return false;
    }


    public synchronized boolean isRoundOver() {
        if (currentPlayer.questionNumber == questionsPerRound
            && currentPlayer.oponentPlayer.questionNumber == questionsPerRound) {
            currentPlayer.questionNumber = 0; // nollställer om rundan är över (Problemet är att det finns risk för
            //  att man kan få samma fråga igen om man väljer samma kategori)
            // en annan lösning är att man endast nollställer om questionNumber når list.size()
            currentPlayer.oponentPlayer.questionNumber = 0;
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
        currentPlayer = currentPlayer.oponentPlayer;

    }


    public synchronized boolean allQuestionsAnswered() {
        return currentPlayer.questionNumber == questionsPerRound;
    }


    public synchronized void nextQuestion() {
        currentPlayer.questionNumber++;
    }


    public synchronized void selectCatagory(String categoryName) {
        questions = db.getQuestions(categoryName, questionsPerRound);
        currentRound++;
    }

    public List<Question> getQuestions() {
        return questions;
    }


}