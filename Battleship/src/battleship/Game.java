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
import java.util.ArrayList;

public class Game {
    // Create player object to hold current player
    Player currentPlayer = new Player();
    
    /**
     * Game object method. Creates all elements of board array to start as null.
     */
    public Game(){
        for(int i = 0; i < currentPlayer.board.length; i++){
            currentPlayer.board[i] = null;
        }
    }

    /**
     * Check if board has been filled up
     * @return 
     */
    public boolean boardFilledUp() {
        for (int i = 0; i < currentPlayer.board.length; i++) {
            if (currentPlayer.board[i] == null) {
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
        if (player == currentPlayer && currentPlayer.board[location] == null && location < 100) {
            // Set location of the board to what the player pressed
            currentPlayer.board[location] = currentPlayer;
            // Add location to list
            currentPlayer.hitList.add(location + 100);
            // Set player to opponent turn
            currentPlayer = currentPlayer.opponent;
            currentPlayer.enemyMove(location);
            return true;
        }
        return false;
    }
    
    /**
     * Check if player hit a block that had been marked.
     * @param location
     * @return 
     */
    public synchronized boolean getMarked(int location){
        int shotLoc = location + 100;
        for(int i = 0; i < currentPlayer.markedList.size(); i++){
            if(shotLoc == currentPlayer.markedList.get(i)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if a player has won. This will check if a players hit list has hit all the
     * marked blocks of opponents.
     */
    public void checkWin(){
        // Create opponent player object 
        Player player = currentPlayer.opponent;
        // Loop through player list
        for(int i : currentPlayer.markedList){
            if(player.hitList.contains(i)){
                player.gameWinner = true;
            }else{
                player.gameWinner = false;
                break;
            }
        }
    }

    /**
     * Player class
     * This class is within Game class to reference game player
     */
    class Player extends Thread {
        // Create array of 100 to create board
        private Player[] board = new Player[200];
        // Set mark for player objects
        char mark;
        // Create object to establish player oponent
        Player opponent;
        // Create socket and streams for networking
        Socket socket;
        BufferedReader input;
        PrintWriter output;
        
        // Create list of all hits
        private ArrayList<Integer> hitList = new ArrayList<>();
        // Create list of marked blocks
        private ArrayList<Integer> markedList = new ArrayList<>();
        // Boolean to check if player has won
        boolean gameWinner = false;

        /**
         * Player class for empty player initialization.
         */
        public Player(){
            // Leave empty
        }
        
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
                
                // Get list of marked blocks for each player
                String command = input.readLine();
                if (command.contains("LIST")){
                    //System.out.println(command);
                    String list = command.substring(5);
                    int first = 1;
                    int second = 4;
                    for(int i = 0; i < 8; i++){
                        //System.out.println(list.substring(first, second));
                        markedList.add(Integer.parseInt(list.substring(first, second)));
                        first += 5;
                        second += 5;
                    }
                }
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
                        // Get player location
                        int location = Integer.parseInt(command.substring(5));
                        // Check if valid move
                        if (playerMove(location, this)) {
                            // Check if block is "marked"
                            if(getMarked(location)){
                                output.println("HIT_MOVE " + location);
                                // Check if player won
                                checkWin();
                                if(gameWinner == true){
                                    opponent.output.println("WIN");
                                    output.println("WIN");
                                }
                            }else{
                                output.println("VALID_MOVE " + location);
                            }
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