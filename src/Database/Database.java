package Database;

import Question.Question;
import java.util.LinkedList;
import java.util.List;

public class Database {


    Category candy = new Category("src/CandyQuiz.txt");
    Category egg = new Category("src/EggQuiz.txt");
    Category famous = new Category("src/FamousQuiz.txt");
    Category random = new Category("src/RandomQuiz.txt");


    public List<Question> getQuestions(String searchedCategory, int antalFrågor) {

        if (searchedCategory.equalsIgnoreCase("candy")) {
            return candy.questions.subList(0, antalFrågor);
        }else if(searchedCategory.equalsIgnoreCase("egg"))
            return egg.questions.subList(0, antalFrågor);
        else if(searchedCategory.equalsIgnoreCase("famous"))
            return famous.questions.subList(0, antalFrågor);
        else
            return random.questions.subList(0, antalFrågor);

    }

    public static void main(String[] args) {
        // Bara för att testa filsökning
        new Database();
    }

}
