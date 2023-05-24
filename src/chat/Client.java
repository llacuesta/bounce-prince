package chat;

import ui.ChatOverlay;

import java.net.Socket;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Client extends JPanel implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private int port = 9876;
    private String servername = "172.18.250.230";
    private boolean done;
    private JTextField tf;
    private JButton b1;
    private JTextArea ta;

    public Client() {
//		int port = 0;
//		Scanner in = new Scanner(System.in);  // Create a Scanner object
//
//		while (port <= 1023) {
//
//			try{
//			    System.out.print("Enter port number: ");
//	            port = Integer.parseInt(in.nextLine());
//	            askIP();
//
//	        }catch(Exception e){
//	            System.out.println("\nEnter an integer!");
//	        }
//		}

        done = false;
        client = null;
    }

    private void askPort() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.println("\nCannot find (or disconnected from) Server");
            System.out.print("Enter port number: ");
            this.port = Integer.parseInt(in.nextLine());

        } catch(Exception e){
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
            e.printStackTrace();
        }
    }

    public void setTextArea(ChatOverlay overlay) {
        ta = overlay.chatTextArea;
    }

    private void setClient() {
        try {
            client = new Socket();
            client.connect(new InetSocketAddress(this.servername, this.port), 200);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setClientValues(String ip, int port) {
        this.servername = ip;
        this.port = port;
    }

    @Override
    public void run() {

        setClient();

        try {
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String inMessage;
            while ((inMessage = in.readLine()) != null) {
                ta.append(inMessage+ "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startClient() {
        Thread t = new Thread(this);
        t.start();
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
            e.printStackTrace();
        }
    }

//    public void serverHandler(JPanel jp) {
//        tf = new JTextField(20);
//        b1 = new JButton("Send");
//        jp.add(tf);
//        jp.add(b1);
//
//        ta = new JTextArea(8, 25);
//        JScrollPane scrollPane = new JScrollPane(ta);
//        ta.setEditable(false);
//        jp.add(scrollPane);
//
//        b1.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String message = tf.getText();
//                tf.setText("");
//                out.println(message);
//            }
//        });
//    }

    public void serverHandler(String message) {
        out.println(message);
    }
}