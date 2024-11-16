public class QuizGameProtocol {
    public static final String REQUEST_QUESTION = "request question"; // Client request to get a question
    public static final String RESPONSE_QUESTION = "response question"; // Server response to send a question

    public static final String QUESTION = "question"; // Server sends a question
    public static final String ANSWER = "answer"; // Client sends an answer
    public static final String FINISH = "finish"; // Indicates the quiz is finished
    
    // Check if the request is valid
    public static boolean isValidRequest(String request) {
        return REQUEST_QUESTION.equals(request);
    }
}
