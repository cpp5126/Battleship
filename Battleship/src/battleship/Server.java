/*
 * Server.java -        Server for client to connect to
 * @author		Kam Talebzadeh
 * @date		January 31, 2016
*/

package battleship;

import java.net.*;

public class Server {

    /**
     * Run server to open socket for clients to connect to.
     */
    public static void main(String[] args) throws Exception {
        // Open socket port 8901
        ServerSocket server = new ServerSocket(8901);
        System.out.println("Battleship server running");
        
        // Accept connections from two players
        try{
            while(true){
                // Create game object
                Game game = new Game();
                
                // Accept connections from players 1 and 2
                Game.Player player1 = game.new Player(server.accept(), '1');
                Game.Player player2 = game.new Player(server.accept(), '2');
                
                // Set opponents for players 1 and 2
                player1.setOpponent(player2);
                player2.setOpponent(player1);
                
                // Get current player at start of game
                game.currentPlayer = player1;
                
                // Start game for both players
                player1.start();
                player2.start();
            }
        }finally{
            // Close socket
            server.close();
        }
    }
}