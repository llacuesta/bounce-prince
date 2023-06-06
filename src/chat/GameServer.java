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

    // Game Logic Booleans
    private boolean winnerFound = false;
    private boolean allDead = false;
    private int deadCount = 0;

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
            boolean isWin = Boolean.parseBoolean(tokens[7]);
            boolean isAlive = Boolean.parseBoolean(tokens[8]);
            String timeOfDeath = tokens[9];
            String timeOfWin = tokens[10];
            int yLevelOffset = Integer.parseInt(tokens[11]);

            // Checking values
            if (isWin) {
                this.winnerFound = true;
            }
            if (isAlive) {
                deadCount++;
            }
            if (deadCount == 3) {
                this.allDead = true;
            }

            // Setting Values
            gamePlayer = getPlayerByID(gamePlayerID);
            gamePlayer.getPlayer().setNum(playerNum);
            gamePlayer.getPlayer().setX(playerX);
            gamePlayer.getPlayer().setY(playerY);
            gamePlayer.getPlayer().setPlayerAction(playerAction);
            gamePlayer.getPlayer().setFlipX(flipX);
            gamePlayer.getPlayer().setFlipW(flipW);
            gamePlayer.getPlayer().setWin(isWin);
            gamePlayer.getPlayer().setAlive(isAlive);
            gamePlayer.getPlayer().setTimeOfDeath(timeOfDeath);
            gamePlayer.getPlayer().setTimeOfWin(timeOfWin);
            gamePlayer.setyLevelOffset(yLevelOffset);

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

    private int minOffset(List<GamePlayer> gamePlayers) {
        int minOffset = 0;
        for (GamePlayer gp : gamePlayers) {
            if (gp.getyLevelOffset() < minOffset) {
                minOffset = gp.getyLevelOffset();
            }
        }
        return minOffset;
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
            int minOffset = minOffset(gamePlayers);
            String message;

            if (winnerFound) {
                message = "WIN," + otherPlayers.size();
            } else if (allDead) {
                message = "END," + otherPlayers.size();
            } else {
                message = "OTS," + otherPlayers.size();
            }
            for (Player p : otherPlayers) {
                message += "," + p.getPlayerNum()
                        + "," + p.getX()
                        + "," + p.getY()
                        + "," + p.getPlayerAction()
                        + "," + p.getFlipX()
                        + "," + p.getFlipW()
                        + "," + p.isWin()
                        + "," + p.isAlive()
                        + "," + p.getTimeOfDeath()
                        + "," + p.getTimeOfWin()
                        + "," + minOffset;
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
    }
}
