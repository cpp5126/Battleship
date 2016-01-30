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
    Socket requestSocket;
    static ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    
    Client(){}
    
    public static void main(String args[])
    {
        Client client = new Client();
        client.run();
    }
    
    void run(){
        try{
            // Request connection to server
            requestSocket = new Socket("localhost", 8901);
            
            // Get input and output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());
            
            // Start game over network
            System.out.println("Connected to server on port 8901");
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
