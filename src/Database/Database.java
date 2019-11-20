package Database;

import question.Question;
import java.util.List;

public class Database {


//    Category candy = new Category("catagoryFiles\\CandyQuiz.txt");
//    Category egg = new Category("catagoryFiles\\EggQuiz.txt");
//    Category famous = new Category("catagoryFiles\\FamousQuiz.txt");
//    Category random = new Category("catagoryFiles\\RandomQuiz.txt");

    //MAC OCH LINUX
    Category candy = new Category("catagoryFiles/CandyQuiz.txt");
    Category egg = new Category("catagoryFiles/EggQuiz.txt");
    Category famous = new Category("catagoryFiles/FamousQuiz.txt");
    Category random = new Category("catagoryFiles/RandomQuiz.txt");


    public List<Question> getQuestions(String wantedCategory, int antalFrågor) {

        if (wantedCategory.equalsIgnoreCase("candy")) {
            return candy.questions.subList(0, antalFrågor);
        }else if(wantedCategory.equalsIgnoreCase("egg"))
            return egg.questions.subList(0, antalFrågor);
        else if(wantedCategory.equalsIgnoreCase("famous"))
            return famous.questions.subList(0, antalFrågor);
        else
            return random.questions.subList(0, antalFrågor);

    }

    public static void main(String[] args) {

        new Database();
    }

}
