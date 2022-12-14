import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client {
    private String nomUtil;

    public Client(String nom){
        this.nomUtil = nom;
    }

    public void mainClient(String adresse, int port) throws UnknownHostException, IOException{
        Socket clientSocket = new Socket(adresse, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        Scanner sc = new Scanner(System.in);
        ThreadEnvoyer envoi = new ThreadEnvoyer(sc.nextLine(),in,out,sc,this.nomUtil);
        envoi.start();
        ThreadRecevoir recevoir= new ThreadRecevoir(in.readLine(),in,out,clientSocket);
        recevoir.start();
    }

    public static void main(String []args) throws IOException{
        Scanner scan = new Scanner(System.in);
        System.out.println("Entrez votre pseudo");
        String nom = scan.nextLine();
        Client client = new Client(nom);
        client.mainClient("0.0.0.0", 6000);
    }
}