package chat;

import entities.Player;
import gamestates.Create;
import gamestates.Join;
import gamestates.State;
import main.Game;

import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GamePlayer implements Runnable {

    // Instance Attributes
    private DatagramSocket socket;
    private Player player;
    private ArrayList<Player> otherPlayers;
    private InetAddress address;
    private int port;
    private State ui;
    private int gamePlayerID = 0;
    private int yLevelOffset = 0;

    private final Thread t = new Thread(this);

    // Constructors
    public GamePlayer(Player player, InetAddress address, int port, int gamePlayerID) {
        try {
            socket = new DatagramSocket();
            this.address = address;
            this.port = port;
            this.player = player;
            this.gamePlayerID = gamePlayerID;
            this.otherPlayers = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GamePlayer(Player player, InetAddress address, int port) {
        try {
            socket = new DatagramSocket();
            this.address = address;
            this.port = port;
            this.player = player;
            this.otherPlayers = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPlayerDetails() {
        try {
            // Sending player details
            String message = player.getPlayerNum()
                    + "," + gamePlayerID
                    + "," + player.getX()
                    + "," + player.getY()
                    + "," + player.getPlayerAction()
                    + "," + player.getFlipX()
                    + "," + player.getFlipW()
                    + "," + this.ui.getyLevelOffset();
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receivePlayerDetails() {
        try {
            // Receiving number of players
            byte[] receiveBuffer = new byte[1024];
            DatagramPacket playerPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(playerPacket);
            String playerData = new String(playerPacket.getData()).trim();

            // Parsing message
            String[] tokens = playerData.split(",");
            if (tokens[0].equals("OTS")) {

            	String packetData = tokens[1];
            	tokens = Arrays.copyOfRange(tokens, 2, tokens.length);

            	for (int j = 0; j < Integer.parseInt(packetData); j++) {
            		int index = j*7;
                    // Getting Values
                    int playerNum = Integer.parseInt(tokens[0+index]);
                    float playerX = Float.parseFloat(tokens[1+index]);
                    float playerY = Float.parseFloat(tokens[2+index]);

                    Player player = getPlayerByNum(playerNum);

                    if (player != null) {

                    	int playerAction = Integer.parseInt(tokens[3+index]);
                        int flipX = Integer.parseInt(tokens[4+index]);
                        int flipW = Integer.parseInt(tokens[5+index]);
                        int yLevelOffset = Integer.parseInt(tokens[6+index]);

                        player.setX(playerX);
                        player.setY(playerY);
                        player.setPlayerAction(playerAction);
                        player.setFlipX(flipX);
                        player.setFlipW(flipW);

                        this.yLevelOffset = yLevelOffset;
                        this.ui.setyLevelOffset(yLevelOffset);

                    } else {
                        player = new Player(playerX, playerY, (int) (50 * Game.PLAYER_SCALE), (int) (37 * Game.PLAYER_SCALE), playerNum);
                        otherPlayers.add(player);

                        System.out.println("Added player " + playerNum);
                    }
            	}
            }




            // Update number of players
            ui.setOtherPlayers(otherPlayers);
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        }
    }

    public void connectToServer() {
        try {
            DatagramSocket socket = new DatagramSocket();

            // Connect to the server
            String message = "Connect me," + player.getX() + "," + player.getY() + "," + player.getPlayerNum() + "," + gamePlayerID;
            byte[] connectData = message.getBytes();
            DatagramPacket connectPacket = new DatagramPacket(connectData, connectData.length, address, port);
            socket.send(connectPacket);

            // Update the ID of the class
            byte[] receiveBuffer = new byte[1024];
            DatagramPacket idResponsePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(idResponsePacket);
            gamePlayerID = Integer.parseInt(new String(idResponsePacket.getData(), 0, idResponsePacket.getLength()));
            player.setNum(gamePlayerID);
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    // Getters
    public Player getPlayer() {
        return player;
    }

    public int getPlayerID() {
        return gamePlayerID;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public void setyLevelOffset(int yLevelOffset) {
        this.yLevelOffset = yLevelOffset;
    }

    private Player getPlayerByNum(int num) {
        for (Player player : otherPlayers) {
            if (player.getPlayerNum() == num) {
                return player;
            }
        }
        return null;
    }

    public void setState(State state) {
        this.ui = state;
    };

    public State getState() {
        return this.ui;
    };

    public int getyLevelOffset() {
        return yLevelOffset;
    }

    @Override
    public void run() {
        while (true) {
            // Check if the player has moved
            sendPlayerDetails();
            receivePlayerDetails();
        }
    }

    public void startGamePlayer() {
        System.out.println("New Player started!");
        connectToServer();
        t.start();
    }

//    public void updateOtherPlayerData(List<GamePlayer> otherGamePlayers) {
//        otherPlayers.clear();
//        for (GamePlayer gamePlayer : otherGamePlayers) {
//            otherPlayers.add(gamePlayer.getPlayer());
//        }
//    }

//    private void receivePlayerDetails() {
//        byte[] receivedData = new byte[1024]; // Adjust the buffer size as per your requirements
//
//        try {
//            DatagramPacket packet = new DatagramPacket(receivedData, receivedData.length);
//            socket.receive(packet);
//
//            byte[] data = packet.getData();
//            ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
//            ObjectInputStream objectStream = new ObjectInputStream(byteStream);
//
//            otherPlayers.clear();
//            otherPlayers = (ArrayList<Player>) Arrays.asList((Player[]) objectStream.readObject());
//        } catch (Exception e) {
//            System.out.println("Unable to receive player data");
//        }
//
//        System.out.println("Other players Received");
//        for (Player p : otherPlayers) {
//            System.out.println("playerNum: " + p.getPlayerNum());
//        }
//    }
}