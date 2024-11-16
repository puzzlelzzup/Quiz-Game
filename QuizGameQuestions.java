import java.util.*;

public class QuizGameQuestions {
    private static final List<Question> questionList = new ArrayList<>();

    static {
        questionList.add(new Question("What is a small segment of a larger message in networking?", "Packet"));
        questionList.add(new Question(
            "What is system of rules that explain the correct conduct and procedures to be followed in formal situations?", "Protocol"));
        questionList.add(new Question("What device connects multiple networks and directs data packets between them?", "Router"));
        questionList.add(new Question("Which protocol secures data transfer over the internet?", "HTTPS"));
        questionList.add(new Question("What is a network inside a network?", "Subnet"));
    }

    public static List<Question> getQuestions() {
        return questionList;
    }

    public static class Question {
        private final String questionText;
        private final String answer;

        public Question(String questionText, String answer) { // question and answer
            this.questionText = questionText;
            this.answer = answer;
        }

        public String getQuestionText() {
            return questionText;
        }

        public String getAnswer() {
            return answer;
        }
    }
}
