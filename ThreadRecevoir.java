import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ThreadRecevoir extends Thread{
    private String msg ;
    BufferedReader in;
    PrintWriter out;
    ServerSocket serveurSocket;
    Socket clientSocket;
    private List<Socket> listeClients;

    
    public ThreadRecevoir(String msg,BufferedReader in,PrintWriter out,Socket clientSocket,List<Socket> l){
        this.msg = msg;
        this.in = in;
        this.out = out;
        this.clientSocket = clientSocket;
        this.listeClients = l;
    }
    
    @Override
    public void run(){
        try {
            msg = in.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        while(msg!=null){
            for(Socket socket : this.listeClients){
                Serveur.sendMessageToAll(socket, msg);
            }
            try {
                msg = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
            System.out.println("Client déconnecté");
            out.close();
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
