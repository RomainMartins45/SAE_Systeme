import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Serveur {
    private String adresse;
    private String nomServeur;
    
    public Serveur(String adresse, String nomServeur){
        this.adresse = adresse;
        this.nomServeur = nomServeur;
    }

    public void mainServeur(int port) throws IOException{
        ServerSocket serveurSocket = new ServerSocket(port);
        Socket clientSocket = serveurSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        Scanner sc = new Scanner(System.in);
        ThreadEnvoyer envoi = new ThreadEnvoyer(sc.nextLine(),clientSocket);
        envoi.start();
        ThreadRecevoir recevoir= new ThreadRecevoir(in.readLine(),clientSocket);
        recevoir.start();
        System.out.println("Client déconnecté");
        out.close();
        clientSocket.close();
        serveurSocket.close();
    }


public static void main(String []args) throws IOException{
    Serveur serveur = new Serveur("0.0.0.0", "test");
    serveur.mainServeur(6000);

}}
