package Database;

import question.Question;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private Map<String, Category> categories = new HashMap<>();
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

    Database() {
        categories.put("candy", candy);
        categories.put("egg", egg);
        categories.put("famous", famous);
        categories.put("random", random);
    }

    public List<Question> getQuestions(String wantedCategory, int numberOfQuestions) {
        Category c = categories.getOrDefault(
                wantedCategory.toLowerCase(),
                categories.get("random"));
        qwer += numberOfQuestions;
        return c.questions.subList(qwer, qwer + numberOfQuestions);

        /*if (wantedCategory.equalsIgnoreCase("candy")) {
            qwer += numberOfQuestions;
            return candy.questions.subList(qwer, qwer + numberOfQuestions);
        } else if (wantedCategory.equalsIgnoreCase("egg")) {
            qwer += numberOfQuestions;
            return egg.questions.subList(qwer, qwer + numberOfQuestions);
        } else if (wantedCategory.equalsIgnoreCase("famous")) {
            qwer += numberOfQuestions;
            return famous.questions.subList(qwer, qwer + numberOfQuestions);
        } else {
            qwer += numberOfQuestions;
        }
        return random.questions.subList(qwer, qwer + numberOfQuestions);*/
    }

    public static void main(String[] args) {
        new Database();
    }

    public void resetCount() {
        this.qwer = 0;
    }

    public void shuffleLists() {
        for (Category c : categories.values())
            Collections.shuffle(c.questions);

        /*Collections.shuffle(candy.questions);
        Collections.shuffle(egg.questions);
        Collections.shuffle(famous.questions);
        Collections.shuffle(random.questions);*/
    }
}