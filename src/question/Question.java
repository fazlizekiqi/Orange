package question;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {

    static final long serialVersionUID = 42L;
    String question;
    String rightAnswer;
    public ArrayList<String> alternatives = new ArrayList<String>();

    public Question() {
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public boolean isRightAnswer(String s) {
        return s.equals(rightAnswer);
    }

    public ArrayList<String> getAlternatives() {
        return alternatives;
    }
}
