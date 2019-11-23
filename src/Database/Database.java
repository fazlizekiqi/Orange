package Database;

import question.Question;

import java.util.Collections;
import java.util.List;

public class Database {
    private int qwer = 0;

//    Category candy = new Category("catagoryFiles\\CandyQuiz.txt");
//    Category egg = new Category("catagoryFiles\\EggQuiz.txt");
//    Category famous = new Category("catagoryFiles\\FamousQuiz.txt");
//    Category random = new Category("catagoryFiles\\RandomQuiz.txt");

    //MAC OCH LINUX
    private Category candy = new Category("catagoryFiles/CandyQuiz.txt");
    private Category egg = new Category("catagoryFiles/EggQuiz.txt");
    private Category famous = new Category("catagoryFiles/FamousQuiz.txt");
    private Category random = new Category("catagoryFiles/RandomQuiz.txt");

    public List<Question> getQuestions(String wantedCategory, int antalFrågor) {
        if (wantedCategory.equalsIgnoreCase("candy")) {
            qwer += antalFrågor;
            return candy.questions.subList(qwer, qwer + antalFrågor);
        } else if (wantedCategory.equalsIgnoreCase("egg")) {
            qwer += antalFrågor;
            return egg.questions.subList(qwer, qwer + antalFrågor);
        } else if (wantedCategory.equalsIgnoreCase("famous")) {
            qwer += antalFrågor;
            return famous.questions.subList(qwer, qwer + antalFrågor);
        } else {
            qwer += antalFrågor;
        }
        return random.questions.subList(qwer, qwer + antalFrågor);
    }

    public static void main(String[] args) {
        new Database();
    }

    public void resetCount() {
        this.qwer = 0;
    }

    public void shuffleLists(){
        Collections.shuffle(candy.questions);
        Collections.shuffle(egg.questions);
        Collections.shuffle(famous.questions);
        Collections.shuffle(random.questions);
    }
}