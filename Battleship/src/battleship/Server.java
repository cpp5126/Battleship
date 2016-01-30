/*
 * Server.java -        Server for client to connect to
 * @author		Kam Talebzadeh
 * @date		January 30, 2016
 */

package battleship;

import java.io.*;
import java.net.*;

public class Server {
    ServerSocket providerSocket;
    Socket connection = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    
    Server(){}
    
    public static void main(String args[])
    {
        Server server = new Server();
        while(true){
            server.run();
        }
    }
        
    void run(){
        try{
            // Create socket for client to connect to
            providerSocket = new ServerSocket(8901);
            System.out.println("Battleship Server is Running");
            
            // Wait for connection
            System.out.println("Waiting for connnection...");
            connection = providerSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());
            
            // Get input and output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            
            while(true){
                
            }
            
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            // Close connection
            try{
                in.close();
                out.close();
                providerSocket.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
