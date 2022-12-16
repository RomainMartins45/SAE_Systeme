import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ThreadEnvoyer extends Thread{
    String msg;
    BufferedReader in;
    PrintWriter out;
    Scanner sc;
    String nom;

    public ThreadEnvoyer(String msg,BufferedReader in,PrintWriter out,Scanner sc,String nom){
        this.msg = msg;
        this.in = in;
        this.out = out;
        this.sc = sc;
        this.nom = nom;
    }

    public void run() {
            while(true){
                msg = sc.nextLine();
                out.println(this.nom + " : " + msg);
                out.flush();
            }
        }
    }
