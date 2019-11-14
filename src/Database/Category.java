package Database;

import Question.Question;

import java.io.*;
import java.util.ArrayList;

public class Category {
    ArrayList<Question> alternatives = new ArrayList<Question>();
    String firstLine;
    String secondLine;
    private Question tempQuestion;

    Category(String path){
     try {
         File file = new File(path);
         BufferedReader in = new BufferedReader(new FileReader(file));
         while((firstLine = in.readLine()) != null){
             tempQuestion.setQuestion(firstLine);

         }
     }
     catch (FileNotFoundException e) {
         e.printStackTrace();
     } catch (IOException e) {
         e.printStackTrace();
     }
    }
}
