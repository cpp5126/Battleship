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
import java.util.*;
import javax.swing.*;

public class Client {
    
    // Create JFrame for player client
    private JFrame frame = new JFrame("Battleship");
    // Label to display messages sent between client and server
    private JLabel messageLabel = new JLabel("");
    private JLabel enemyLabel, playerLabel;
    
    private JPanel container = new JPanel();
    private JPanel northContainer = new JPanel();
    
    // Frame attributes
    private JMenuBar menuBar;
    private JMenu menu1;
    private JMenuItem menuItem1, menuItem2;
    
    // Create array of Jpanels to create board
    private Square[] board = new Square[200];
    private Square currentSquare;
    
    // Server socket information
    private final int PORT = 8901;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    
    // Set color for mouse movement
    private Color defaultBackground;
    
    // Create list of marked blocks
    int[] list;
    
    private String winner = "";

    /**
     * Creates client object. Builds GUI and sets mouse listeners.
     * @throws Exception 
     */
    public Client() throws Exception {

        // Initialize networking
        // Change localhost to get ipaddress of the hosted server -- type in "ipaddress" instead of local host
        // To play someone over server, server file is running on: 40.117.229.121
        socket = new Socket("localhost", PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        // Set panels for headers and board
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        northContainer.setLayout(new GridLayout(1, 2));
        
        // Set Label for player and enemy
        playerLabel = new JLabel("Player's Battleships");
        playerLabel.setBackground(Color.lightGray);
        // Align text to right side for right side board
        playerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        enemyLabel = new JLabel("Shots at Enemy");
        enemyLabel.setBackground(Color.lightGray);
        // Add both labels to label panel
        northContainer.add(enemyLabel);
        northContainer.add(playerLabel);
        // Add panel to north of GUI
        frame.getContentPane().add(northContainer, "North");
        
        // Set label for server to client messages
        messageLabel.setBackground(Color.lightGray);
        frame.getContentPane().add(messageLabel, "South");
        
        // Create JPanel for board to be held on
        JPanel boardPanel1 = new JPanel();
        
        // Set the laytout to be
        // 10x10 squares
        // 1x1 borders
        boardPanel1.setLayout(new GridLayout(10, 10, 1, 1));
        
        // Loop through squares building the board
        //  and give each a MouseAdapter and add to boardPanel
        for (int i = 0; i < 100; i++) {
            final int j = i;
            board[i] = new Square();
            board[i].setBackground(Color.darkGray);
            board[i].addMouseListener(new MouseAdapter() {
                 @Override
                /**
                 * Check when the mouse has entered into a specific grid block
                 * Change the color of the grid block when the mouse is within its boundaries
                 */
                public void mouseEntered(MouseEvent e) {
                    defaultBackground = board[j].getBackground();
                    board[j].setBackground(Color.GRAY);
                }
                @Override
                /**
                 * Check when the mouse has exited the grid block
                 * When the mouse exits, change the color to the original color
                 */
                public void mouseExited(MouseEvent e) {
                    if(board[j].getBackground() != Color.RED && board[j].getBackground() != Color.lightGray){
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
            boardPanel1.add(board[i]);
        }                
                
        // Create second frame for second 10x10 gid        
        // Create JPanel for board to be held on
        JPanel boardPanel2 = new JPanel();
        
        // Set the laytout to be
        // 10x10 squares
        // 1x1 borders
        boardPanel2.setLayout(new GridLayout(10, 10, 1, 1));
        
        // Loop through squares building the board
        //  and give each a MouseAdapter and add to boardPanel
        for (int i = 100; i < 200; i++) {
            final int j = i;
            board[i] = new Square();
            board[i].setBackground(Color.GRAY);
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
                    if(board[j].getBackground() != Color.RED && board[j].getBackground() != Color.lightGray && board[j].getBackground() != Color.BLUE){
                        board[j].setBackground(defaultBackground);
                    }
                }
            });
            boardPanel2.add(board[i]);
        }
        
        /**
        * #=========== THIS IS A TEST AREA FOR FUNCTIONALITY ===========#
        */
        // Create random generator
        Random generator = new Random();
        // Initialize list
        list = new int[9];
        // Set min and max
        int MIN = 101;
        int MAX = 199;
        
        int i = 0;
        // Add first ship - 4 blocks
        int random = generator.nextInt(MAX - MIN) + MIN;
        if(Arrays.asList(list).contains(random)){
            random = generator.nextInt(MAX - MIN) + MIN;
        }else{
            if(random % 10 > 5){
                for(int j = 0; j < 4; j++){
                    list[i] = random - j;
                    board[random - j].setBackground(Color.BLUE);
                    board[random - j].setMarked(); 
                    i++;
                }
            }else{
                for(int j = 0; j < 4; j++){
                    list[i] = random + j;
                    board[random + j].setBackground(Color.BLUE);
                    board[random + j].setMarked();
                    i++;
                }   
            }
        }
        
        // Add second ship - 3 blocks
        random = generator.nextInt(MAX - MIN) + MIN;
        if(Arrays.asList(list).contains(random)){
            random = generator.nextInt(MAX - MIN) + MIN;
        }else{
            if(random % 10 > 5){
                for(int j = 0; j < 3; j++){
                    list[i] = random - j;
                    board[random - j].setBackground(Color.BLUE);
                    board[random - j].setMarked();
                    i++;
                }
            }else{
                for(int j = 0; j < 3; j++){
                    list[i] = random + j;
                    board[random + j].setBackground(Color.BLUE);
                    board[random + j].setMarked();
                    i++;
                }   
            }
        }
        
        // Add second ship - 2 blocks
        random = generator.nextInt(MAX - MIN) + MIN;
        if(Arrays.asList(list).contains(random)){
            random = generator.nextInt(MAX - MIN) + MIN;
        }else{
            if(random % 10 > 5){
                for(int j = 0; j < 2; j++){
                    list[i] = random - j;
                    board[random - j].setBackground(Color.BLUE);
                    board[random - j].setMarked();
                    i++;
                }
            }else{
                for(int j = 0; j < 2; j++){
                    list[i] = random + j;
                    board[random + j].setBackground(Color.BLUE);
                    board[random + j].setMarked();
                    i++;
                }   
            }
        }
        /**
        * #=========== THIS IS A TEST AREA FOR FUNCTIONALITY ===========#
        */
        
        // Add panels to container panel to be added to main frame
        container.add(boardPanel1);
        container.add(boardPanel2);
        frame.getContentPane().add(container, "Center");
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
                out.println("LIST " + Arrays.toString(list));
                char mark = response.charAt(8);
                frame.setTitle("Battleship - Player " + mark);
            }
            
            while (true) {
                response = in.readLine();
                // Loop through all valid messages coming through the socket and output the proper message to player.
                if (response.startsWith("VALID_MOVE")) {
                    int loc = Integer.parseInt(response.substring(11));
                    board[loc].setBackground(Color.lightGray);
                    board[loc].repaint();
                    // Player must wait for oponent to move
                    messageLabel.setText("Opponents turn, please wait...");
                    currentSquare.repaint();
                } else if(response.startsWith("HIT_MOVE")){
                    int loc = Integer.parseInt(response.substring(9));
                    board[loc].setBackground(Color.RED);
                    board[loc].repaint();
                    // Player must wait for oponent to move
                    messageLabel.setText("Ship hit!! -- Opponents turn, please wait...");
                    currentSquare.repaint();
                } else if (response.startsWith("OPPONENT_MOVED")) {
                    // Opponent has moved, players turn again
                    int loc = Integer.parseInt(response.substring(15));
                    // If opponent moved, set that block to red on players board
                    if(board[loc + 100].getMarked() == true){
                        board[loc + 100].setBackground(Color.RED);
                    }else{
                        board[loc + 100].setBackground(Color.lightGray);
                    }
                    board[loc].repaint();
                    messageLabel.setText("Your turn");
                } else if (response.startsWith("MESSAGE")) {
                    // If message begins with MESSAGE, display message for player and opponent
                    messageLabel.setText(response.substring(8));
                } else if(response.startsWith("WIN")){
                    winner = response.substring(4);
                    break;
                }else {
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
        int response = JOptionPane.showConfirmDialog(frame, "Want to play again?", winner, JOptionPane.YES_NO_OPTION);
        frame.dispose();
        return response == JOptionPane.YES_OPTION;
    }

    /**
     * Square object to create board. Array of square objects will be used to create the 10x10 board.
     */
    static class Square extends JPanel {
        
        boolean marked = false;
        
        /**
         * Create gray squares
         */
        public Square() {
            setBackground(Color.GRAY);
        }
        
        /**
         * Setter to set marked blocks.
         */
        public void setMarked(){
            marked = true;
        }
        
        /**
         * Getter to get marked blocks.
         * @return 
         */
        public boolean getMarked(){
            return marked;
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
            client.frame.setSize(1000, 500);
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