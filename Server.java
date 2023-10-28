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

import static java.lang.System.out;

public class ServerTeste {
    private static final int PORT = 12345;
    private static final String[] QUESTIONS = {
            "Em que filme, um jovem se apaixona por uma mulher mais velha que é sua vizinha e inicia um caso proibido?",
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

    private static final String COMMAND_HELP = "#help";
    private static final String COMMAND_START = "#iniciar";
    private static final String COMMAND_EXIT = "#sair";

    private static final String COMMAND_DISCONECT = "#disconect";

    private static List<PrintWriter> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            out.println("Server is listening on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                out.println("Accepted connection from client: " + clientSocket.getInetAddress().getHostAddress());
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                clients.add(out);

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    String clientInputt = in.readLine();
                    if (COMMAND_START.equals(clientInputt)) {
                        new ClientHandler(clientSocket, out).start();
                    } else if (COMMAND_EXIT.equals(clientInputt)) {
                        out.println("Você escolheu sair. Desconectando...");
                        out.close();
                        in.close();
                        clientSocket.close();
                        break;
                    }else if (COMMAND_HELP.equals(clientInputt)) {
                        out.println("Comandos disponíveis: " + COMMAND_START + ", " + COMMAND_EXIT + ", " + COMMAND_HELP);
                        String clienteInputeDois = in.readLine();
                        if(clienteInputeDois.equals(COMMAND_START)){
                            new ClientHandler(clientSocket, out).start();
                            break;
                        }
                    }else if (COMMAND_EXIT.equals(clientInputt)) {
                        out.println("Você escolheu sair. Desconectando...");
                        out.close();
                        in.close();
                        clientSocket.close();
                        break;
                    }

                    else {
                        out.println("Comando inválido. Digite " + COMMAND_HELP + " para ver a lista de comandos.");
                    }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter clientOut;

        public ClientHandler(Socket socket, PrintWriter out) {
            this.clientSocket = socket;
            this.clientOut = out;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                int score = 0;
                for (int i = 0; i < QUESTIONS.length; i++) {
                    clientOut.println(QUESTIONS[i]);
                    String clientResponse = in.readLine();
                    if (clientResponse != null && clientResponse.toLowerCase().equals(ANSWERS[i].toLowerCase())) {
                        score++;
                    }
                }

                clientOut.println("Você acertou " + score + " perguntas!");
                clientOut.println("Digite '" + COMMAND_START + "' para jogar novamente ou '" + COMMAND_EXIT + "' para sair.");

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clients.remove(clientOut);
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out.println("Connection closed for client: " + clientSocket.getInetAddress().getHostAddress());
            }
        }
    }

}
