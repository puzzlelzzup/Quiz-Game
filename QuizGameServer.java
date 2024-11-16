// Run server first, then run client
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizGameServer {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(20); // maximum 20 threads
        
        try (ServerSocket serverSocket = new ServerSocket(6789)) {
            System.out.println("Server start.. (port#=" + 6789 + ")");
			System.out.println("Waiting for client connection..");

            while(true) {
                Socket socket = serverSocket.accept(); // client connection
                System.out.println("Client connected..\n");
                pool.execute(new ClientHandler(socket));
            } 
            } catch (IOException e) {
                System.err.println("Server eroor:" + e.getMessage());
            } finally {
                pool.shutdown();
            }
        }
    private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                List<QuizGameQuestions.Question> questions = QuizGameQuestions.getQuestions(); // quiz
                int score = 0, questionIndex = 0;

                out.println("Welcome to the Quiz Game!");
                out.println("The total number of quiz questions is " + questions.size());

                String request;
                while ((request = in.readLine()) != null) { // client request read
                    if (!QuizGameProtocol.isValidRequest(request)) { // check request
                        out.println("Invalid request");
                        continue;
                    }
                    if (request.equals(QuizGameProtocol.REQUEST_QUESTION)) { // request question
                        if (questionIndex < questions.size()) { // remaining questions
                            QuizGameQuestions.Question question = questions.get(questionIndex); // next question
                            questionIndex++;
                            out.println(QuizGameProtocol.RESPONSE_QUESTION); // response question
                            out.println(QuizGameProtocol.QUESTION);
                            out.println("Question " + questionIndex +": " + question.getQuestionText());

                            String answer = in.readLine(); // client answer protocol
                            if  (answer.equals(QuizGameProtocol.ANSWER)) {
                                answer = in.readLine(); // client answer read
                                if (answer.equalsIgnoreCase(question.getAnswer())) { // check answer and score update
                                    score++;
                                    out.println("Correct! Your score is " + score);
                                } else {
                                    out.println("Incorrect! The correct answer is: " + question.getAnswer()+" Your current score is " + score);
                                }
                            }
                        } else { // no more questions
                            out.println(QuizGameProtocol.RESPONSE_QUESTION);
                            out.println(QuizGameProtocol.FINISH);
                            out.println("Your total score is: " + score);
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Disconnected.");
                }
                System.out.println("Disconnected.");
            }
        }
    }
}
