package chat;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Server implements Runnable {

	private ArrayList<ConnectionHandler> connections;
    private static ServerSocket server;
    private int port = 9876;
	private boolean done;
	private ExecutorService pool;

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

		connections = new ArrayList<>();
		done = false;
		this.port = port;
	}

	@Override
	public void run() {
		try{
	        server = new ServerSocket(this.port);
	        System.out.println("\nServer is Ready!!");

	        printIPAddresses();
			System.out.println("Port Number: " + this.port);

			pool = Executors.newCachedThreadPool();

			while(!done) {
				Socket client = server.accept();
				ConnectionHandler handler = new ConnectionHandler(client);
				connections.add(handler);
				pool.execute(handler);
			}
		} catch (Exception e) {
			// ignore
		}
	}

	private void printIPAddresses() {
		try {
			NetworkInterface networkInterface = NetworkInterface.getByName("eth0");
	        Enumeration<InetAddress> inetAddress = networkInterface.getInetAddresses();

	        for (InetAddress inetAdd : Collections.list(inetAddress)) {
	        	// only IPv4 eth0 IP address
	        	if(inetAdd instanceof Inet4Address && !inetAdd.isLoopbackAddress()){
	        		System.out.println("Server IP address: "+ inetAdd.toString().substring(1));
	        		break;
	        	}
	        }

		} catch (SocketException e) {
			// TODO Auto-generated catch block
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

	public void shutdownServer() {
		try {
			done = true;
			for (ConnectionHandler ch : connections) {
				ch.shutdown();
			}
			if(!server.isClosed()) {
				server.close();
			}
			pool.shutdown();
			System.out.println("Shutting down Server!!");
		}catch(IOException e) {
			// ignore
		}
	}

	class ConnectionHandler implements Runnable {

		private Socket client;
		private BufferedReader in;
		private PrintWriter out;

		public ConnectionHandler(Socket client) {
			this.client = client;
		}

		@Override
		public void run() {
			try{
				out = new PrintWriter(client.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));

				String nickname = in.readLine();
				System.out.println(nickname + " connected!");
				broadcast(nickname + " joined the chat!");

				String message;
				while((message = in.readLine()) != null) {
					if(message.startsWith("/quit")) {
						broadcast(nickname + " left the chat!");
						shutdown();
						if(connections.size() == 0) {
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
			}catch(IOException e) {
				// ignore
			}
		}
	}

	public static void main(String[] args) {

		Server server  = new Server();
        server.run();
	}
}
