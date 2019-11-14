package Question;

import java.awt.*;
import java.util.ArrayList;

public class Question {

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
}
