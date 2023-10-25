package gameNet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 12345;
    private static final String[] QUESTIONS = {
            "Em que filme, um jovem se apaixona por uma mulher mais velha que é sua vizinha e inicia um caso proibido??",
            "Qual é a capital da França?",
            "Quem escreveu 'Romeu e Julieta'?",
            // Adicione mais perguntas conforme necessário
    };
    private static final String[] ANSWERS = {
            "O Graduado",
            "paris",
            "william shakespeare"
            // Adicione mais respostas correspondentes às perguntas (em minúsculas)
    };

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from client: " + clientSocket.getInetAddress().getHostAddress());
                new ClientHandler(clientSocket).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                int score = 0;
                for (int i = 0; i < QUESTIONS.length; i++) {
                    out.println(QUESTIONS[i]);
                    String clientResponse = in.readLine();
                    if (clientResponse != null && clientResponse.toLowerCase().equals(ANSWERS[i].toLowerCase())) {
                        score++;
                    }
                }

                out.println("Você acertou " + score + " perguntas!");
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


