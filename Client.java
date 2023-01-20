import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Choisissez un pseudo :");
        String nom = sc.nextLine();
        try {
            Socket socket = new Socket("localhost", 5555);
            System.out.println("Connected to server on port 5555");

            new Thread(() -> {
                receiveMessages(socket,nom);
            }).start();

            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            String message;
            while (true) {
                message = scanner.nextLine();
                pw.println(message);
            }
            } catch (IOException e) {
            e.printStackTrace();
            }
    }

    private static void receiveMessages(Socket socket,String nom) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;
            while ((message = br.readLine()) != null) {
                System.out.println(nom + " : " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
