import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Serveur {
    private static List<Room> rooms = new ArrayList<>();
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(5555);
            System.out.println("Server started on port 5555");
            Room général = new Room("général");
            rooms.add(général);
            while (true) {
                Socket client = server.accept();
                général.addClient(client);
                System.out.println("Client connected: " + client.getInetAddress());
                new Thread(() -> {
                    receiveMessages(client);
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void receiveMessages(Socket client) {
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
                        if(!(nomRoomExiste(nomRoom))){
                            List<Socket> l = new ArrayList<>();
                            l.add(client);
                            quitterSalon(client);
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
                    if(message.substring(1, 6).equals("JOIN ")){
                        String nomRoom = message.substring(6);
                        if(nomRoomExiste(nomRoom)){
                            List<Socket> l = new ArrayList<>();
                            l.add(client);
                            quitterSalon(client);
                            rooms.add(new Room(nomRoom, l));
                        }
                        else{
                            PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                            pw.println("Ce salon n'existe pas, vous pouvez le créer avec /ADD");
                        }
                    }
                }
                else{
                    sendMessageToAll(client, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessageToAll(Socket sender, String message) {
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