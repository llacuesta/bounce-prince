package chat;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public class Server implements Runnable {

    private ArrayList<ConnectionHandler> connections;
    private static ServerSocket server;
    private int port = 9876;
    private boolean done;
    private ExecutorService pool;
    private String ip;
    private boolean started = false;

    public Server() {
//		int port = 0;
//		Scanner in = new Scanner(System.in);  // Create a Scanner object
//
//		while (port <= 1023) {
//			System.out.println("Make sure to use valid ports (greater than 1023)");
//
//			try{
//			    System.out.print("Enter port number: ");
//	            port = Integer.parseInt(in.nextLine());
//
//	        }catch(Exception e){
//	            //e.printStackTrace();
//	            System.out.println("Enter an integer!");
//	        }
//		}

        printIPAddresses();
        connections = new ArrayList<>();
        done = false;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(this.port);
            System.out.println("\nServer is Ready!!");
            System.out.println("Port Number: " + this.port);
            started = true;

            pool = Executors.newCachedThreadPool();

            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printIPAddresses() {
        InetAddress ip;
        String address;

        try {
            ip = InetAddress.getLocalHost();
            address = ip.getHostAddress();
            this.ip = address;
            System.out.println("Your current IP address : " + this.ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String message) {
        for (ConnectionHandler ch : connections) {
            if (ch != null) {
                ch.sendMessage(message);
            }
        }
    }

    public void startServer() {
        Thread t = new Thread(this);
        t.start();
    }

    public void shutdownServer() {
        try {
            done = true;
            for (ConnectionHandler ch : connections) {
                ch.shutdown();
            }
            if (!server.isClosed()) {
                server.close();
            }
            pool.shutdown();
            System.out.println("Shutting down Server!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getIP() {
        return this.ip;
    }

    public String getPort() {
        return Integer.toString(this.port);
    }

    public boolean getStarted() { return this.started; }

    class ConnectionHandler implements Runnable {

        private Socket client;
        private BufferedReader in;
        private PrintWriter out;

        public ConnectionHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                out.println("Type in a nickname to start chatting!");
                String nickname = in.readLine();
                System.out.println(nickname + " connected!");
                broadcast(nickname + " joined the chat!");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/quit")) {
                        broadcast(nickname + " left the chat!");
                        shutdown();
                        if (connections.size() == 0) {
                            shutdownServer();
                        }
                    } else {
                        System.out.println(nickname + ": " + message);
                        broadcast(nickname + ": " + message);
                    }
                }

            } catch (IOException e) {
                shutdown();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public void shutdown() {
            try {
                in.close();
                out.close();
                if(!client.isClosed()) {
                    client.close();
                }
                connections.remove(this);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}