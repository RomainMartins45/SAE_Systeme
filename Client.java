import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 5555);

            new Thread(() -> {
                recupMessage(socket);
            }).start();

            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            String message;
            boolean continu = true;
            while (continu) {
                message = scanner.nextLine();
                pw.println(message);
                if(message.equals("/quit")){
                    continu = false;
                    break;
                }
            }
            } catch (IOException e) {
            e.printStackTrace();
            }
    }

    private static void recupMessage(Socket socket){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;
            while ((message = br.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
