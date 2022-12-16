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

    
    public ThreadRecevoir(String msg,BufferedReader in,PrintWriter out,Socket clientSocket){
        this.msg = msg;
        this.in = in;
        this.out = out;
        this.clientSocket = clientSocket;
    }
    
    @Override
    public void run(){
        try {
            msg = in.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        while(msg!=null){
            System.out.println(msg);
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
