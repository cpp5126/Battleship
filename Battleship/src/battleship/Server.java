/*
 * Server.java -        Server for client to connect to
 * @author		Kam Talebzadeh
 * @date		January 30, 2016
 */

package battleship;

import java.io.*;
import java.net.*;

public class Server {
    // Create server socket
    ServerSocket server;
    // Create two players as sockets
    Socket player1, player2;
    // Create input and output streams
    ObjectOutputStream out;
    ObjectInputStream in;
    // Create message
    String message;
    
    Server(){}
    
    /**
     * Start server
     * @param args 
     */
    public static void main(String args[])
    {
        // Create server object
        Server server = new Server();
        while(true){
            server.run();
        }
    }
    
    /**
     * Run process of server
     */
    void run(){
        try{
            // Create socket for client to connect to
            server = new ServerSocket(8901);
            System.out.println("Battleship Server is Running");
            
            // Wait for connection
            System.out.println("Waiting for connnection...");
            
            // Accept player 1 connection
            Player player1 = new Player(server.accept());
            
            // Inform player 1 you are waiting fro another player
            System.out.println("Waiting for opponent...");
            
            // Accept player 2 information
            Player player2 = new Player(server.accept());
                
            // Set player opponents
            player1.setOpponent(player2);
            player2.setOpponent(player1);
            
            // Start players
            player1.start();
            player2.start();
            
            // Read in data sent over socket from clients
            while(true){
                try{
                    message = (String)in.readObject();
                    System.out.println("client>" + message);
                }catch(ClassNotFoundException i){
                    System.err.println("Data received in unknown format");
                }
            }
            
        }catch(IOException e){
            // Do nothing
        }finally{
            // Close connection
            try{
                in.close();
                out.close();
                server.close();
            }
            catch(IOException e){
                // Do nothing
            }
        }
    }
}