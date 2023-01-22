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
            Room général = new Room("general");
            rooms.add(général);
            while (true) {
                Socket client = server.accept();
                PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                boolean pseudoPrit = true;
                String nom = "bug";
                while(pseudoPrit){
                    pw.println("Choisissez votre pseudo");
                    BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    nom = br.readLine();
                    if(!(clients.containsValue(nom))){ 
                        pseudoPrit = false;
                    }
                    else{
                        pw.println("Pseudo déjà prit.");
                    }
                }
                clients.put(client,nom);
                général.addClient(client);
                pw.println("Vous êtes dans le groupe général, pour rejoindre un groupe utilisez /JOIN + nomdugroupe.");
                pw.println("Voici la liste des groupes (/SALONS)");
                for(Room room : rooms){
                    pw.println(room.getNom());
                }
                ThreadServeur thread = new ThreadServeur(client, nom,clients,rooms);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Envoie un message à tout les utilisateurs présent dans le groupe de l'envoyeur
    public static void envoieMessage(Socket envoyeur, String message) {
        for (Room rooms : rooms) {
            if (rooms.clientDansRoom(envoyeur)){
                for(Socket client : rooms.clients){
                    if (client != envoyeur) {
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