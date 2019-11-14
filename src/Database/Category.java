package Database;

import Question.Question;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Category {

    ArrayList<Question> questions = new ArrayList<Question>();

    String firstLine;
    String secondLine;
    private Question tempQuestion;

    Category(String path) {
        try {
            File file = new File(path);
            BufferedReader in = new BufferedReader(new FileReader(file));
            while ((firstLine = in.readLine()) != null) {
                tempQuestion = new Question();
                tempQuestion.setQuestion(firstLine);
                secondLine = in.readLine();
                String[] altA = secondLine.split(",");
                tempQuestion.setRightAnswer(altA[0]);
                for (int i = 0; i < altA.length; i++) {
                    tempQuestion.alternatives.add(altA[i]);
                }
                Collections.shuffle(tempQuestion.alternatives);
                questions.add(tempQuestion);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
