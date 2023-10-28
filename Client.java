package gameNet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTeste {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Bem-vindo ao jogo de perguntas e respostas!");
            System.out.println("Digite #iniciar para comeÃ§ar o jogo.");
            System.out.println("Digite #sair para encerrar o jogo.");
            System.out.println("Digite #help para acessar os monandos do programa.");


            String userCommand;
            while ((userCommand = userInput.readLine()) != null) {
                out.println(userCommand);
                String serverResponse = serverIn.readLine();
                System.out.println(serverResponse);

                if ("<sair>".equals(userCommand)) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
