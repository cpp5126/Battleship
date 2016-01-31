/*
 * Game.java -          Class to create game
 * @author		Kam Talebzadeh
 * @date		January 31, 2016
 *
 *      ** Code modified from http://cs.lmu.edu/~ray/notes/javanetexamples/ **
 */

package battleship;

import java.io.*;
import java.net.*;

public class Game {
    // Create array of 100 to create board
    private Player[] board = new Player[100];
    // Create player object to hold current player
    Player currentPlayer;
    
    /**
     * Game object method. Creates all elements of board array to start as null.
     */
    public Game(){
        for(int i = 0; i < board.length; i++){
            board[i] = null;
        }
    }

    /**
     * Check if board has been filled up
     * @return 
     */
    public boolean boardFilledUp() {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == null) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Check to see if a player has moved. Take player block pressed and give direction to opponent client.
     * @param location
     * @param player
     * @return 
     */
    public synchronized boolean playerMove(int location, Player player) {
        // Check if the move was valid
        if (player == currentPlayer && board[location] == null) {
            // Set location of the board to what the player pressed
            board[location] = currentPlayer;
            // Set player to opponent turn
            currentPlayer = currentPlayer.opponent;
            currentPlayer.enemyMove(location);
            return true;
        }
        return false;
    }

    /**
     * Player class
     * This class is within Game class to reference game player
     */
    class Player extends Thread {
        // Set mark for player objects
        char mark;
        // Create object to establish player oponent
        Player opponent;
        // Create socket and streams for networking
        Socket socket;
        BufferedReader input;
        PrintWriter output;

        /**
         * Create player object. Check for other player.
         * @param socket
         * @param mark 
         */
        public Player(Socket socket, char mark) {
            // Set ocket and mark
            this.socket = socket;
            this.mark = mark;
            try {
                // Establish network streams
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("WELCOME " + mark);
                output.println("MESSAGE Waiting for opponent to connect...");
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        /**
         * Sets player opponent when player 2 connects.
         */
        public void setOpponent(Player opponent) {
            this.opponent = opponent;
        }

        /**
         * Checks if other player has moved/fired.
         */
        public void enemyMove(int location) {
            output.println("OPPONENT_MOVED " + location);
        }

        /**
         * Run thread for player object.
         */
        public void run() {
            try {
                // Thread starts once both players have connected.
                output.println("MESSAGE All players connected");

                // Tell player 1 its their turn.
                if (mark == '1') {
                    output.println("MESSAGE Your move");
                }

                // Get commands through socket.
                while (true) {
                    String command = input.readLine();
                    if (command.startsWith("MOVE")) {
                        int location = Integer.parseInt(command.substring(5));
                        if (playerMove(location, this)) {
                            output.println("VALID_MOVE " + location);
                        } else {
                            output.println("MESSAGE Not a valid move.");
                        }
                    } else if (command.startsWith("QUIT")) {
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                try {socket.close();} catch (IOException e) {}
            }
        }
    }
}