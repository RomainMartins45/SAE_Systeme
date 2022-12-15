import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadEnvoyer extends Thread{
    String msg;
    Socket socket;

    public ThreadEnvoyer(String msg,Socket socket){
        this.msg = msg;
        this.socket = socket;
    }

    public void run() {
            while(true){
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                out.println(msg);
                out.flush();
            }
        }
    }
