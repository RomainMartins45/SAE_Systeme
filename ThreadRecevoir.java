import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadRecevoir extends Thread{
    private String msg ;
    private Socket socket;
    
    public ThreadRecevoir(String msg,Socket socket){
        this.msg = msg;
        this.socket = socket;
    }
    
    @Override
    public void run(){
        while(msg!=null){
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            PrintWriter out;
            try {
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            out.println("Client : "+msg);
            msg = in.readLine();
        }
        System.out.println("Client déconnecté");
    }
}