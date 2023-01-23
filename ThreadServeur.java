import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class ThreadServeur extends Thread{
    List<Room> rooms = new ArrayList<>();
    Map<Socket, String> clients = new HashMap<>();
    Socket client;
    String nom;

    
    public ThreadServeur(Socket client,String nom,Map<Socket, String> clients,List<Room> rooms){
        this.client = client;
        this.nom = nom;
        this.clients = clients;
        this.rooms = rooms;
    }
    
    @Override
    public void run(){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String message;
            boolean continu = true;
            while ((message = br.readLine()) != null && continu) {
                if(message.charAt(0) == '/'){
                    try{

                    // Affiche le nom de tout les salons
                    if(message.equals("/SALONS")){
                        PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                        for(Room room : rooms){
                            pw.println(room.getNom());
                        }
                    }

                    // Affiche le nombre de gens dans le salon actuelle
                    else if(message.equals("/nbusers")){
                        PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                        for(Room room : rooms){
                            if(room.clients.contains(client)){
                                pw.println("Le salon " + room.getNom() + " comporte " + room.getNbusers() + " user(s).");
                            }
                        }
                    }

                    // Affiche le nombre de gens dans le salon actuelle
                    else if(message.equals("/users")){
                        PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                        for(Room room : rooms){
                            if(room.clients.contains(client)){
                                pw.println("Voici la liste des utilisateurs du salon " + room.getNom() + " :");
                                for (Socket users : room.clients){
                                    pw.println(clients.get(users));
                                }
                            }
                        }
                    }

                    // Date depuis création du salon actuelle
                    else if(message.equals("/uptime")){
                        PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                        for(Room room : rooms){
                            if(room.clients.contains(client)){
                                pw.println("Le salon " + room.getNom() + " est ouvert depuis " + room.dateDepuisCreation() + " minute(s).");
                            }
                        }
                    }

                    // Quitter
                    else if(message.equals("/quit")){
                        continu = false;
                        PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                        pw.println("Déconnexion");
                        for(Room room : rooms){
                            if(room.clients.contains(client)){
                                pw.println(room.clients.remove(client));
                            }
                        }
                        clients.remove(client);
                        break;
                    }

                    // Rejoindre un salon
                    else if(message.substring(1, 6).equals("JOIN ")){
                        String nomRoom = message.substring(6);
                        if(Serveur.nomRoomExiste(nomRoom)){
                            for(Room room : rooms){
                                if(room.getNom().equals(nomRoom)){
                                    Serveur.quitterSalon(client);
                                    room.addClient(client);
                                    PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                                    pw.println("Vous avez rejoint le salon " + room.getNom());
                                }
                            }
                        }
                        else{
                            PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                            pw.println("Ce salon n'existe pas, vous pouvez le créer avec /ADD");
                        }
                    }
                    
                    // Ajouter un salon
                    else if(message.substring(1, 5).equals("ADD ")){
                        String nomRoom = message.substring(5);
                        if(!(Serveur.nomRoomExiste(nomRoom))){
                            List<Socket> l = new ArrayList<>();
                            l.add(client);
                            Serveur.quitterSalon(client);
                            rooms.add(new Room(nomRoom, l));
                            PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                            pw.println("Le salon " + nomRoom + " a été créé et vous l'avez rejoint");
                        }
                        else{
                            PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                            pw.println("Ce salon existe déjà, vous pouvez le rejoindre avec /JOIN");
                        }
                    }

                    // Supprimer un salon
                    else if(message.substring(1, 5).equals("SUP ")){
                        String nomRoom = message.substring(5);
                        PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                        if(Serveur.nomRoomExiste(nomRoom)){
                            for(Room room : rooms){
                                if(room.getNom().equals(nomRoom)){
                                    if(room.getNbusers() == 0){
                                        rooms.remove(room);
                                        pw.println("Le salon " + nomRoom + " a été supprimé");
                                    }
                                    else{
                                        pw.println("D'autres utilisateurs utilisent ce salon, vous ne pouvez pas le supprimer.");
                                    }
                                }
                            }
                        }
                        else{
                            pw.println("Ce salon n'existe pas");
                        }
                    }

                    // Envoyer un message privé à un utilisateur
                    else if(message.substring(1, 4).equals("MP ")){
                        int pseudoIndex = 0;
                        for(int i = 4;message.charAt(i) != ' '; i++){
                            pseudoIndex = i;
                        }
                        String pseudo = message.substring(4, pseudoIndex+1);
                        String msg = message.substring(pseudoIndex + 1);
                        boolean envoieMsg = false;
                        for(Map.Entry<Socket,String> mapentry : clients.entrySet()){
                            if(mapentry.getValue().equals(pseudo)){
                                Socket s = mapentry.getKey();
                                PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                Date dateActuelle = new Date();
                                pw.println("(MP) "+ format.format(dateActuelle) + " " + nom + " : " + msg);
                                envoieMsg = true;
                            }
                        }
                        if(!(envoieMsg)){
                            PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                            pw.println("L'utilisateur " + pseudo + " n'existe pas");
                        }
                    }

                }
                    catch(StringIndexOutOfBoundsException e){
                        PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                        pw.println("Commande inconnue");
                    }
            }
                else{
                    // Message normal
                    
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date dateActuelle = new Date();
                    Serveur.envoieMessage(client, format.format(dateActuelle) + " " + nom + " : " + message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
