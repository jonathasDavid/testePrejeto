package gameNet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    private static final int PORT = 12345;
    private static final String[] QUESTIONS = {
            "Em que filme, um jovem se apaixona por uma mulher mais velha que é sua vizinha e inicia um caso proibido??",
            "Qual é o filme que explora a história de um romance entre um músico e uma imigrante que compartilham uma paixão pela música?",
            "Em que filme, um escritor contrata uma prostituta para acompanhá-lo em eventos sociais e acaba se apaixonando por ela?",
            "Qual é o filme que narra a história de um jovem casal que se apaixona em meio à guerra e à ocupação nazista?",
            "Em que filme, um músico de jazz e uma atriz se apaixonam em Los Angeles, enquanto perseguem seus sonhos na cidade?",
            "Qual filme explora um romance que se desenvolve ao longo de várias décadas e épocas diferentes, destacando a imortalidade do amor?",
            "Em que filme, um casal se apaixona durante um cruzeiro, mas o romance enfrenta desafios quando eles retornam à vida real?",
            "Qual é o filme que apresenta uma história de amor entre um inventor excêntrico e uma artista, com a Torre Eiffel como pano de fundo?",
            "Em que filme, um jovem casal enfrenta um dilema quando o rapaz é diagnosticado com câncer terminal, desafiando seu amor?",
            "Qual filme segue a jornada de um músico famoso que se apaixona por uma mulher que não pode ouvir sua música devido à surdez?"

            // Adicione mais perguntas conforme necessário
    };
    private static final String[] ANSWERS = {
            "O Graduado",
            "Apenas uma Vez",
            "Uma Linda Mulher",
            "Casablanca",
            "La La Land",
            "Te Amarei para Sempre",
            "Titanic",
            "Hugo",
            "Um Amor para Recordar",
            "Amor Sublime Amor"
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

                List<Integer> questionIndexes = new ArrayList<>();
                for (int i = 0; i < QUESTIONS.length; i++) {
                    questionIndexes.add(i);
                }
                Collections.shuffle(questionIndexes);  // Embaralhar os índices das perguntas

                int score = 0;
                for (int i = 0; i < questionIndexes.size() && i < 10; i++) {  // Limitar a 10 perguntas
                    int questionIndex = questionIndexes.get(i);
                    out.println(QUESTIONS[questionIndex]);
                    String clientResponse = in.readLine();
                    if (clientResponse != null && clientResponse.toLowerCase().equals(ANSWERS[questionIndex].toLowerCase())) {
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


