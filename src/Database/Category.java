package Database;

import question.Question;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Category {
    ArrayList<Question> questions = new ArrayList<>();

    Category(String path) {
        try {
            File file = new File(path);
            BufferedReader in = new BufferedReader(new FileReader(file));
            String firstLine, secondLine;
            Question tempQuestion;

            while ((firstLine = in.readLine()) != null) {
                tempQuestion = new Question();
                tempQuestion.setQuestion(firstLine);
                secondLine = in.readLine();
                String[] altA = secondLine.split(",");
                tempQuestion.setRightAnswer(altA[0]);
                tempQuestion.alternatives.addAll(Arrays.asList(altA));
                Collections.shuffle(tempQuestion.alternatives);
                questions.add(tempQuestion);
            }

            Collections.shuffle(questions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
