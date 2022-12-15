import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadRecevoir extends Thread{
    private String msg ;
    BufferedReader in;
    PrintWriter out;
    ServerSocket serveurSocket;
    Socket clientSocket;

    
    public ThreadRecevoir(String msg,BufferedReader in,PrintWriter out,Socket clientSocket,ServerSocket serveurSocket ){
        this.msg = msg;
        this.in = in;
        this.out = out;
        this.clientSocket = clientSocket;
        this.serveurSocket = serveurSocket;
    }
    
    @Override
    public void run(){
        try {
            msg = in.readLine();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        while(msg!=null){
            System.out.println("Client : "+msg);
            try {
                msg = in.readLine();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            }
            System.out.println("Client déconnecté");
            out.close();
            try {
                clientSocket.close();
                serveurSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
