/*
 * Client.java -        Class for client to be created
 * @author		Kam Talebzadeh
 * @date		January 31, 2016
 *
 *      ** Code modified from http://cs.lmu.edu/~ray/notes/javanetexamples/ **
 */

package battleship;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client {
    
    // Create JFrame for player client
    private JFrame frame = new JFrame("Battleship");
    // Label to display messages sent between client and server
    private JLabel messageLabel = new JLabel("");
    // Frame attributes
    private JMenuBar menuBar;
    private JMenu menu1;
    private JMenuItem menuItem1, menuItem2;
    
    // Create array of Jpanels to create board
    private Square[] board = new Square[100];
    private Square currentSquare;
    
    // Server socket information
    private static int PORT = 8901;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    
    // Set color for mouse movement
    private Color defaultBackground;

    /**
     * Creates client object. Builds GUI and sets mouse listeners.
     * @throws Exception 
     */
    public Client() throws Exception {

        // Initialize networking
        socket = new Socket("localhost", PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Set label for server to client messages
        messageLabel.setBackground(Color.lightGray);
        frame.getContentPane().add(messageLabel, "South");
        
        // Create JPanel for board to be held on
        JPanel boardPanel = new JPanel();
        
        // Set the laytout to be
        // 10x10 squares
        // 1x1 borders
        boardPanel.setLayout(new GridLayout(10, 10, 1, 1));
        
        // Loop through squares building the board
        //  and give each a MouseAdapter and add to boardPanel
        for (int i = 0; i < board.length; i++) {
            final int j = i;
            board[i] = new Square();
            board[i].addMouseListener(new MouseAdapter() {
                 @Override
                /**
                 * Check when the mouse has entered into a specific grid block
                 * Change the color of the grid block when the mouse is within its boundaries
                 */
                public void mouseEntered(MouseEvent e) {
                    defaultBackground = board[j].getBackground();
                    board[j].setBackground(Color.darkGray);
                }
                @Override
                /**
                 * Check when the mouse has exited the grid block
                 * When the mouse exits, change the color to the original color
                 */
                public void mouseExited(MouseEvent e) {
                    if(board[j].getBackground() != Color.RED){
                        board[j].setBackground(defaultBackground);
                    }
                }
                @Override
                /**
                 * Check if mouse has clicked within a block
                 *  If clicked, set color to red to show a fire has been shot at this block
                 */
                public void mouseClicked(MouseEvent e) {
                    //board[j].setBackground(Color.RED);
                    currentSquare = board[j];
                    out.println("MOVE " + j);
                }
            });
            boardPanel.add(board[i]);
        }
        frame.getContentPane().add(boardPanel, "Center");
    }

    /**
    * Create menu bar for the battleship board frame
    */
    public void setJMenuBar(){
        // Create menu bar
        menuBar = new JMenuBar();
        
        // Create File option on menu bar
        menu1 = new JMenu("File");
        menu1.setMnemonic(KeyEvent.VK_A);
        
        // Add File option to menu bar
        menuBar.add(menu1);
        
        // Create restart option for a menu
        menuItem1 = new JMenuItem("Restart");
        
        // Add Action Listener to Restart option
        menuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Remove all from JFrame to rebuild it all
                frame.getContentPane().removeAll();
                /**
                 * This menu item needs to be completed with proper restart functionality
                 */
            }
        });
        
        // Add menu items to "File" menu
        menu1.add(menuItem1);
        
        // Create Quit option for menu bar
        menuItem2 = new JMenuItem("Quit");
        
        // Add Action Listener to Restart option
        menuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Quit JFrame
                System.exit(0);
            }
        });
        
        // Add menu items to "File" menu
        menu1.add(menuItem2);
        
        // Add menu bar to Frame
        frame.setJMenuBar(menuBar);
    }
    
    /**
     * Start client process. Run the play method for client and begin stream of communication with server.
     * @throws Exception 
     */
    public void play() throws Exception {
        String response;
        try {
            // Get incoming input
            response = in.readLine();
            // Check if input is a welcome message
            if (response.startsWith("WELCOME")) {
                char mark = response.charAt(8);
                frame.setTitle("Battleship - Player " + mark);
            }
            
            while (true) {
                response = in.readLine();
                // Loop through all valid messages coming through the socket and output the proper message to player.
                if (response.startsWith("VALID_MOVE")) {
                    int loc = Integer.parseInt(response.substring(11));
                    board[loc].setBackground(Color.RED);
                    board[loc].repaint();
                    // Player must wait for oponent to move
                    messageLabel.setText("Opponents turn, please wait");
                    currentSquare.repaint();
                } else if (response.startsWith("OPPONENT_MOVED")) {
                    // Opponent has moved, players turn again
                    int loc = Integer.parseInt(response.substring(15));
                    // If opponent moved, set that block to red on players board
                    board[loc].setBackground(Color.RED);
                    board[loc].repaint();
                    messageLabel.setText("Opponent moved, your turn");
                } else if (response.startsWith("MESSAGE")) {
                    // If message begins with MESSAGE, display message for player and opponent
                    messageLabel.setText(response.substring(8));
                } else{
                    break;
                }
            }
            // Quit
            out.println("QUIT");
        }
        finally {
            // Close socket
            socket.close();
        }
    }

    /**
     * Give player and opponent option to play again. If you chooses no, the other will be prompted to wait
     *  until another player has joined the connection
     * @return 
     */
    private boolean playAgain() {
        // Show dialog box asking for remath Yes/No
        int response = JOptionPane.showConfirmDialog(frame, "Want to play again?", "Battleship Rematch", JOptionPane.YES_NO_OPTION);
        frame.dispose();
        return response == JOptionPane.YES_OPTION;
    }

    /**
     * Square object to create board. Array of square objects will be used to create the 10x10 board.
     */
    static class Square extends JPanel {
        public Square() {
            setBackground(Color.GRAY);
        }
    }
    
    /**
    * Starts client. Runs the client as an application to communicate with the server.
    */
    public static void main(String[] args) throws Exception {
        while (true) {
            // Create client object
            Client client = new Client();
            // Set close operation
            client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // Set size
            client.frame.setSize(500, 500);
            // Add menu bar
            client.setJMenuBar();
            // Set location to center of screen
            client.frame.setLocationRelativeTo(null);
            // Set to non-resizable
            client.frame.setResizable(false);
            // Set to visible
            client.frame.setVisible(true);
            // Run client as application
            client.play();
            // If player chooses not to play again, close application
            if (!client.playAgain()) {
                break;
            }
        }
    }
}