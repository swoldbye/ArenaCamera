package server;

import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private BufferedReader inputReader;
    private PrintWriter writer;

    public void startServer() throws IOException {
        ServerSocket server = new ServerSocket(ConnectionSettings.PORT);
        System.out.println("Awaiting client..");
        Socket sock = server.accept();
        System.out.println("CONNECTED");
        System.out.println("Setting up utilities..");
        try {
            writer = new PrintWriter(sock.getOutputStream(), true);
            inputReader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread inThread = new Thread(this::listen);
        System.out.print("1 / 2");
        Thread outThread = new Thread(this::write);
        System.out.print("2 / 2");
        System.out.println("Server setup complete..");
        inThread.start();
        outThread.start();
        System.out.println("Server communication may now begin.");
    }

    public void listen() {
        String input;

        try {
            while(true){
                input = inputReader.readLine();
                System.out.println(input);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write() {
        String sysIn;
        BufferedReader sysReader = new BufferedReader(new InputStreamReader(System.in));
        try{
            while((sysIn = sysReader.readLine()) != null){
                writer.println(sysIn);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server =  new Server();
        server.startServer();
    }

}
