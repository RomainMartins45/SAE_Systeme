import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Serveur {
    private String adresse;
    private String nomServeur;
    private List<Socket> listeClients;
    
    public Serveur(String adresse, String nomServeur){
        this.adresse = adresse;
        this.nomServeur = nomServeur;
        this.listeClients = new ArrayList<>();
    }

    public void mainServeur(int port) throws IOException{
        ServerSocket serveurSocket = new ServerSocket(port);
        while(true){
            Socket clientSocket = serveurSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            System.out.println("Client connecté");
            this.listeClients.add(clientSocket);
            Scanner sc = new Scanner(System.in);
            ThreadRecevoir recevoir= new ThreadRecevoir(in.readLine(),in,out,clientSocket);
            recevoir.start();
        }
    }


public static void main(String []args) throws IOException{
    Serveur serveur = new Serveur("0.0.0.0", "test");
    serveur.mainServeur(6000);
}}
