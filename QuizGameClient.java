import java.io.*;
import java.net.*;

public class QuizGameClient {
    private static final String FILE = "server_info.txt"; // File containing the server IP address and port number

    public static void main(String[] args) {
        // Socket for connecting to the server
        String serverIP = null; 
        int nPort = 0;
        String defaultIP = "localhost";
        int defaultPort = 1234;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String ReadIP = reader.readLine(); // Read the IP address from the file
            int ReadPort = Integer.parseInt(reader.readLine().trim()); // Read the port number from the file
            serverIP = ReadIP; // Set the IP address
            nPort = ReadPort; // Set the port number
            System.out.println("Server IP: " + serverIP + ", Port: " + nPort);
        } catch (IOException e) { // If the file cannot be read, using the default IP address and port number
            System.out.println("Error reading file: " + e.getMessage());
            serverIP = defaultIP;
            nPort = defaultPort;
            System.out.println("Server IP: " + serverIP + ", Port: " + nPort); 
        }
        
        try (Socket socket = new Socket(serverIP, nPort)) { // Connect to the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in)); 
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Waiting for server messages...");

            // Print the welcome message from the server
            System.out.println(in.readLine());
            System.out.println(in.readLine());

            String serverMessage1; 
            String serverMessage2; 
            String serverMessage3;
            while (true){
                out.println(QuizGameProtocol.REQUEST_QUESTION); // request question from the server
                serverMessage2 = in.readLine(); // server response

                if (serverMessage2.equals(QuizGameProtocol.RESPONSE_QUESTION)){
                    serverMessage1 = in.readLine(); // get the question from the server message
                    serverMessage3 = in.readLine(); // get the question text from the server

                    if (serverMessage1.equals(QuizGameProtocol.FINISH)){ //
                        System.out.println(serverMessage3); // print the final score
                        break;
                    }
                    else if (serverMessage1.equals(QuizGameProtocol.QUESTION)){
                        System.out.println(serverMessage3); // print the question
                        System.out.print("Your answer: ");
                        String answer = userInput.readLine(); // get the user's answer
                        out.println(QuizGameProtocol.ANSWER); // send the answer to the server
                        out.println(answer); // 
                    }
                    serverMessage1 = in.readLine(); // get the server message
                    System.out.println(serverMessage1); // print the server message
                    System.out.print("\n");
                }            
            }
        } catch (IOException e) {
            System.err.println("Server connection lost or error occurred: " + e.getMessage());
            System.out.println("Please check the server or try again later.");      
        } finally {
            System.out.println("Connection closed.");
            System.exit(0);
        }
    }
}
