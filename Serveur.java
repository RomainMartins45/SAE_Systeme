import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Serveur {
    private static List<Socket> clients = new ArrayList<>();
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(5555);
            System.out.println("Server started on port 5555");

            while (true) {
                Socket client = server.accept();
                clients.add(client);
                System.out.println("Client connected: " + client.getInetAddress());
                new Thread(() -> {
                    receiveMessages(client);
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void receiveMessages(Socket client) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String message;
            while ((message = br.readLine()) != null) {
                System.out.println("Received message from " + client.getInetAddress() + ": " + message);
                sendMessageToAll(client, message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessageToAll(Socket sender, String message) {
        for (Socket client : clients) {
            if (client != sender) {
                try {
                    PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                    pw.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}