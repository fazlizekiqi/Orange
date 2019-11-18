package Server;

import Database.Database;
import question.Question;

import java.util.List;

// klass med regler for spelet
public class ServerSideGame {

    Database db = new Database();
    ServerSidePlayer currentPlayer;
    private List<Question> questions;
    private int questionsPerRound;
    private int totalRounds;
    private int currentRound = 0;



    public int getQuestionsPerRound() {
        return questionsPerRound;
    }

    ServerSideGame(int questionsPerRound, int totalRounds) {
        this.questionsPerRound = questionsPerRound;
        this.totalRounds = totalRounds;
    }

    /**
     * Metoden tittar om alla runder är spelade för båda spelarna och vilken utav de som har flest poäng
     * om en av de har mer än den andra så finns det en vinnare
     * @return sant eller falskt
     */
    public boolean hasWinner() {
        if (isGameOver()){
            if (currentPlayer.points > currentPlayer.oponentPlayer.points || currentPlayer.points < currentPlayer.oponentPlayer.points) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metoden tittar om alla runder är spelade för båda spelarna och om spelarna har samma resulat i slutet
     * om de har samma resultat då är det oavgjort
     * @return
     */
    public boolean isTie() {
        if (isGameOver()) {
            if (currentPlayer.points == currentPlayer.oponentPlayer.points) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metoden kollar om rundan är över genom att kolla på båda spelarnas questionNumber
     * @return
     */
    public boolean isRoundOver(){
        if (currentPlayer.questionNumber == questionsPerRound  && currentPlayer.oponentPlayer.questionNumber == questionsPerRound) {
            currentPlayer.questionNumber = 0; // nollställer om rundan är över (Problemet är att det finns risk för
            //  att man kan få samma fråga igen om man väljer samma kategori)
            // en annan lösning är att man endast nollställer om questionNumber når list.size()
            currentPlayer.oponentPlayer.questionNumber = 0;
            ///currentRound++; ökar i selectCategory
            return true;
        }
        else
            return false;
    }

    /**
     * Metoden kollar om spelet är över
     * @return
     */
    public boolean isGameOver() {
        if (currentRound == totalRounds) {
            //currentRound = 0; // nollställa currentRound???
            return true;
        }
        return false;
    }

    /**
     * Metoden byter spelare endast om alla frågor är besvarade av nuvarnde spelaren
     */
    public void switchPlayer() {
        if (allQuestionsAnswered()) {
            currentPlayer = currentPlayer.oponentPlayer;
        }
    }

    /**
     * Metoden kollar om alla svar är besvarade för spelaren under rundan
     * om alla frågor är besvara returnerar den sant
     * @return
     */
    public boolean allQuestionsAnswered() {
        return currentPlayer.questionNumber == questionsPerRound;
    }

    /**
     * Metoden skickar endast nästa fråga om alla frågor inte är besvarade för nuvarande spelaren
     * @return
     */
    public void nextQuestion() {
        currentPlayer.questionNumber++;
    }

    /**
     *tar emot från kategori från clienten och
     * @param categoryName
     * @return : frågan
     */
    public void selectCatagory(String categoryName) {
        questions =  db.getQuestions(categoryName, questionsPerRound);
        currentRound++;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void gameLogic() {

    }
}