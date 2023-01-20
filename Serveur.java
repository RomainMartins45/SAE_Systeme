import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

public class Serveur {
    public static List<Room> rooms = new ArrayList<>();
    public static Map<Socket, String> clients = new HashMap<>();
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(5555);
            Room général = new Room("général");
            rooms.add(général);
            while (true) {
                Socket client = server.accept();
                PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                pw.println("Choisissez votre pseudo");
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String nom = br.readLine();
                clients.put(client,nom);
                général.addClient(client);
                ThreadServeur thread = new ThreadServeur(client, nom,clients,rooms);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void envoieMessage(Socket sender, String message) {
        for (Room rooms : rooms) {
            if (rooms.clientDansRoom(sender)){
                for(Socket client : rooms.clients){
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
    }

    public static boolean nomRoomExiste(String nom){
        for(Room room : rooms){
            if(room.getNom().equals(nom)){
                return true;
            }
        }
        return false;
    }

    public static void quitterSalon(Socket client){
        for(Room room : rooms){
            if(room.clientDansRoom(client)){
                room.clients.remove(client);
            }          
        }
    }
}