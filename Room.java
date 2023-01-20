import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private String nom;
    List<Socket> clients;

    public Room(String nom,List<Socket> clients){
        this.nom = nom;
        this.clients = clients;
    }

    public Room(String nom){
        this.nom = nom;
        this.clients = new ArrayList<>();
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
