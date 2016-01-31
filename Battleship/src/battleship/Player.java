/*
 * Player.java -        Create player object
 * @author		Kam Talebzadeh
 * @date		January 30, 2016
 */

package battleship;

import java.io.*;
import java.net.*;

    class Player extends Thread {
        // Create player opponent object
        Player opponent;
        // Create socket
        Socket socket;
        // Create object stream input/output
        ObjectInputStream input;
        ObjectOutputStream output;
        // Create message
        String message;
        
        /**
         * Create one player for game
         * @param socket 
         */
        public Player(Socket socket) {
            // Set socket
            this.socket = socket;
            try {
                // Open streams
                output = new ObjectOutputStream(socket.getOutputStream());
                output.flush();
                input = new ObjectInputStream(socket.getInputStream());
                try{
                    // Accept messages over socket/ print them out
                    message = (String)input.readObject();
                    System.out.println("client>" + message);
                }catch(Exception i){
                    // Do nothing
                }
            } catch (IOException e) {
                // Do nothing
            }
        }

        /**
         * Set opponent for player
         * @param opponent 
         */
        public void setOpponent(Player opponent) {
            this.opponent = opponent;
        }

        /**
         * Start player object
         */
        public void run() {
            try {
                // Thread starts once two players have joined
                System.out.println("All players connected");

                // Get commands from client
                while(true){
                    try{
                        // Take message over socket and output
                        message = (String)input.readObject();
                        System.out.println("client>" + message);
                        // If the socket says NUM, this is a block that has been pressed
                        if(message.contains("Num")){
                            String str = message.replaceAll("\\D+","");
                            int blockNum = Integer.parseInt(str);
                            for(int i = 0; i < 100; i++){
//                                if(new BattleshipUIMouse().num == blockNum){
//                                    BattleshipUIMouse.setColor();
//                                }
                            }
                        }
                    }catch(Exception i){
                        // Do nothing
                    }
                }
            } catch (Exception e) {
                // Do nothing
            } finally {
                try {
                    // Close Socket
                    socket.close();
                }catch(IOException e){
                    // Do nothing
                }
            }
        }
    }