import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Room {
    private String nom;
    List<Socket> clients;
    Date dateCrea = new Date();

    public Room(String nom,List<Socket> clients){
        this.nom = nom;
        this.clients = clients;
    }

    public Room(String nom){
        this.nom = nom;
        this.clients = new ArrayList<>();
    }

    public int getNbusers(){
        return this.clients.size();
    }

    public String getNom(){
        return this.nom;
    }

    public void addClient(Socket client){
        this.clients.add(client);
    }

    public boolean clientDansRoom(Socket client){
        return this.clients.contains(client);
    }

    public long dateDepuisCreation(){
        long dateActuelle  = new Date().getTime();
        return ((dateActuelle - dateCrea.getTime())/1000)/60;
    }

    public void sendMessageToRoom(Socket sender, String message) {
        for (Socket client : this.clients) {
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
