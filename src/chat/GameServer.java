package chat;

import entities.Player;
import main.Game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class GameServer implements Runnable {

    // Server Attributes
    private int playerCount = 0;
    private int numPlayers;
    private List<GamePlayer> gamePlayers;

    private DatagramSocket serverSocket = null;
    private int port = 9876;

    private Thread t = new Thread(this);

    // Constructor
    public GameServer() {
        try {
            serverSocket = new DatagramSocket(port);
            serverSocket.setSoTimeout(50000);
            gamePlayers = new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            System.exit(-1);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void startGameServer() {
        System.out.println("Game Server started!");
        t.start();
    }

    @Override
    public void run() {
        while (true) {
            // Getting data from players
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                serverSocket.receive(packet);
                processPacket(packet);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void processPacket(DatagramPacket packet) {
        String message = new String(packet.getData()).trim();
        InetAddress clientAddress = packet.getAddress();
        int clientPort = packet.getPort();
        GamePlayer gamePlayer;

        // Parse the message to get player details and location
        String[] tokens = message.split(",");

        // If connect me packet, create new player
        if (tokens[0].equals("Connect me")) {
            float playerX = Float.parseFloat(tokens[1]);
            float playerY = Float.parseFloat(tokens[2]);
            int playerNum = Integer.parseInt(tokens[3]);
            int gamePlayerID = Integer.parseInt(tokens[4]);

            // Create a new player
            playerCount++;
            Player player = new Player(playerX, playerY, (int) (50 * Game.PLAYER_SCALE), (int) (37 * Game.PLAYER_SCALE), playerCount);
            gamePlayer = new GamePlayer(player, clientAddress, clientPort, playerCount);
            gamePlayers.add(gamePlayer);

            // Respond to client to give it its gamePlayerID
            try {
                byte[] id = Integer.toString(playerCount).getBytes();
                DatagramPacket res = new DatagramPacket(id, id.length, packet.getAddress(), packet.getPort());
                serverSocket.send(res);
            } catch (Exception e) {
                System.out.println("Unable to respond");
            }

            System.out.println("Client "+ gamePlayer.getPlayerID() +" connected: " + packet.getAddress() + " " + packet.getPort());
        } else {
            // Getting Values
            int playerNum = Integer.parseInt(tokens[0]);
            int gamePlayerID = Integer.parseInt(tokens[1]);
            float playerX = Float.parseFloat(tokens[2]);
            float playerY = Float.parseFloat(tokens[3]);
            int playerAction = Integer.parseInt(tokens[4]);
            int flipX = Integer.parseInt(tokens[5]);
            int flipW = Integer.parseInt(tokens[6]);
//            boolean isRight = Boolean.parseBoolean(tokens[7]);
//            boolean isLeft = Boolean.parseBoolean(tokens[8]);
//            boolean isJump = Boolean.parseBoolean(tokens[9]);
//            boolean isMoving = Boolean.parseBoolean(tokens[10]);
//            boolean isInAir = Boolean.parseBoolean(tokens[11]);

            // Setting Values
            gamePlayer = getPlayerByID(gamePlayerID);
            gamePlayer.getPlayer().setNum(playerNum);
            gamePlayer.getPlayer().setX(playerX);
            gamePlayer.getPlayer().setY(playerY);
            gamePlayer.getPlayer().setPlayerAction(playerAction);
            gamePlayer.getPlayer().setFlipX(flipX);
            gamePlayer.getPlayer().setFlipW(flipW);
//            gamePlayer.getPlayer().setRight(isRight);
//            gamePlayer.getPlayer().setLeft(isLeft);
//            gamePlayer.getPlayer().setJump(isJump);
//            gamePlayer.getPlayer().setMoving(isMoving);
//            gamePlayer.getPlayer().setInAir(isInAir);

            // Broadcast the player's movement to all clients
            List<Player> otherPlayers = new ArrayList<>();

            for (GamePlayer gp : gamePlayers) {
                if (gp != gamePlayer) {
                    try {
                        // Sender to others
                        if (!gp.getAddress().equals(clientAddress) || gp.getPort() != clientPort) {
                            otherPlayers.add(gp.getPlayer());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            broadcastMovement(otherPlayers, packet);
        }
    }

    private GamePlayer getPlayerByID(int num) {
        for (GamePlayer player : gamePlayers) {
            if (player.getPlayerID() == num) {
                return player;
            }
        }
        return null;
    }

    private void broadcastMovement(List<Player> otherPlayers, DatagramPacket senderPacket) {
        if (otherPlayers.size() == 0) {
            try {
                byte[] buffer = "NOP,".getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, senderPacket.getAddress(), senderPacket.getPort());

                // Send the packet to other players
                serverSocket.send(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String message = "OTS," + otherPlayers.size();
            for (Player p : otherPlayers) {
                try {
                	message += "," + p.getPlayerNum()
                            + "," + p.getX()
                            + "," + p.getY()
                            + "," + p.getPlayerAction()
                            + "," + p.getFlipX()
                            + "," + p.getFlipW();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, senderPacket.getAddress(), senderPacket.getPort());

            // Others to sender
            try {
				serverSocket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        // Sending playerData back to sender
//        try {
//            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
//            objectStream.writeObject(otherPlayers);
//            objectStream.flush();
//
//            byte[] data = byteStream.toByteArray();
//            DatagramPacket packet = new DatagramPacket(data, data.length, senderAddress, senderPort);
//            serverSocket.send(packet);
//        } catch (Exception e) {
//            System.out.println("Unable to send player data: " + e);
//        }

//        sender.updateOtherPlayerData(otherGamePlayers);
    }
}
