package chat;

import java.net.Socket;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public class Client implements Runnable {

	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
    private int port;
    private String servername;
	private boolean done;

	public Client() {
		int port = 0;
		Scanner in = new Scanner(System.in);  // Create a Scanner object

		while (port <= 1023) {

			try{
			    System.out.print("Enter port number: ");
	            port = Integer.parseInt(in.nextLine());
	            askIP();

	        }catch(Exception e){
	            System.out.println("\nEnter an integer!");
	        }
		}

		done = false;
		this.port = port;
		client = null;
	}

	private void askPort() {
		try{
			Scanner in = new Scanner(System.in);
			System.out.println("\nCannot find (or disconnected from) Server");
		    System.out.print("Enter port number: ");
            this.port = Integer.parseInt(in.nextLine());

        }catch(Exception e){
            System.out.println("\nEnter an integer!");
            askPort();
        }
	}

	private void askIP() {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter server IP address: ");
	    this.servername = in.nextLine();

	    try {
            int num = Integer.parseInt(this.servername);
            System.out.println("\nInvalid IP Address.");
            askIP();
        } catch (NumberFormatException e) {
        	// exit
        }
	}

	private void setClient() {
		try{
			client = new Socket();
			client.connect(new InetSocketAddress(this.servername, this.port), 200);

		} catch (Exception e) {
			askPort();
			askIP();
			setClient();
		}
	}

	@Override
	public void run() {

		setClient();

		try {
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));

			InputHandler inHandler = new InputHandler();
			Thread t = new Thread(inHandler);
			t.start();

			System.out.print("\nPlease enter a nickname: ");
			String inMessage = in.readLine();

			while((inMessage = in.readLine()) != null) {
				System.out.println(inMessage);
			}
		} catch (IOException e) {
			// ignore
		}
	}

	public void shutdown() {
		done = true;
		try {
			in.close();
			out.close();
			if(!client.isClosed()) {
				client.close();
			}
		} catch(IOException e) {
			// ignore
		}
	}

	class InputHandler implements Runnable {

		@Override
		public void run() {
			try {
				BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
				while(!done) {
					String message = inReader.readLine();
					if(message.equals("/quit")) {
						out.println(message);
						inReader.close();
						shutdown();
					} else {
						out.println(message);
					}
				}
			} catch (IOException e) {
				//
			}
		}
	}

	public static void main(String[] args) {
		Client client  = new Client();
		client.run();
	}
}
