import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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
            while ((message = br.readLine()) != null) {
                System.out.println("Received message from " + client.getInetAddress() + ": " + message);
                System.out.println(message);
                if(message.charAt(0) == '/'){
                    // Créer un salon
                    if(message.substring(1, 5).equals("ADD ")){
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

                    // Rejoindre un salon
                    else if(message.substring(1, 6).equals("JOIN ")){
                        String nomRoom = message.substring(6);
                        if(Serveur.nomRoomExiste(nomRoom)){
                            for(Room room : rooms){
                                if(room.getNom().equals(nomRoom)){
                                    Serveur.quitterSalon(client);
                                    room.addClient(client);
                                }
                            }
                        }
                        else{
                            PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                            pw.println("Ce salon n'existe pas, vous pouvez le créer avec /ADD");
                        }
                    }

                    // Affiche le nom de tout les salons
                    else if(message.equals("/SALONS")){
                        PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                        for(Room room : rooms){
                            pw.println(room.getNom());
                        }
                    }

                    // Envoyer un message privé à un utilisateur
                    else if(message.substring(1, 4).equals("MP ")){
                        int pseudoIndex = message.lastIndexOf(" ");
                        String pseudo = message.substring(4, pseudoIndex);
                        String msg = message.substring(pseudoIndex + 1);
                        for(Map.Entry<Socket,String> mapentry : clients.entrySet()){
                            if(mapentry.getValue().equals(pseudo)){
                                Socket s = mapentry.getKey();
                                PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
                                pw.println(nom + " : " + msg);
                            }
                        }
                    }
            }
                else{
                    Serveur.envoieMessage(client, nom + " : " + message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
