/*
 * Client.java -        Client for player to run game
 * @author		Kam Talebzadeh
 * @date		January 30, 2016
 */

package battleship;

import java.io.*;
import java.net.*;
import javax.swing.JFrame;

public class Client {
    // Create client socket
    Socket requestSocket;
    // Create static output stream for other classes to talk to client
    static ObjectOutputStream out;
    // Create input stream
    ObjectInputStream in;
    // Create message
    String message;
    
    Client(){}
    
    /**
     * Start client
     * @param args 
     */
    public static void main(String args[])
    {
        Client client = new Client();
        client.run();
    }
    
    /**
     * Run process of client through socket
     */
    void run(){
        try{
            // Connect to server through socket
            requestSocket = new Socket("localhost", 8901);
            
            // Get input and output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());
            
            // Establish connection for player
            System.out.println("Connected to server on port 8901");
            out.writeObject("Player connected");
            // Create frame for player
            JFrame frame = new BattleshipUI();
            while(true){}
            
        }catch(IOException e){
            //e.printStackTrace();
            System.out.println("Could not connect to server");
        }finally{
            // Close connection
            try{
                in.close();
                out.close();
                requestSocket.close();
            }
            catch(IOException e){
                //e.printStackTrace();
                System.out.println("Could not disconnect to server");
            }
        }
    }
}
