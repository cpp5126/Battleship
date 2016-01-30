/*
 * BattleshipUI.java -      Creates Battleship frame
 * @author                  Kam Talebzadeh
 * @date                    January 28, 2016
 */

package battleship;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BattleshipUI extends JFrame{
    
    // Initialize variables
    private JMenuBar menuBar;
    private JMenu menu1;
    private JMenuItem menuItem1;
    
    /**
     * Creates the frame for the battleship board
     */
    public BattleshipUI() {
        // Set title of JFrame
        setTitle("Battleship");
        // Set default close operator
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set layout of Frame
        setLayout(new BorderLayout());
        // Call menu bar method
        setJMenuBar();
        // Add BattleshipUIPane class as JPanel
        add(new BattleshipUIPane());
        // Pack the JFrame
        pack();
        // Set location of Frame
        setLocationRelativeTo(null);
        // Set Frame visible
        setVisible(true);
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
        
        // Create pause option for a menu
        menuItem1 = new JMenuItem("Restart");
        
        // Add Action Listener to Restart option
        menuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Remove all from JFrame to rebuild it all
                getContentPane().removeAll();
                // Call menu bar method
                setJMenuBar();
                // Add BattleshipUIPane class as JPanel
                add(new BattleshipUIPane());
                // Pack the JFrame
                pack();
                // Revalidate the JFrame
                revalidate();
                // Repaint the JFrame
                repaint();
            }
        });
        
        // Add menu items to "File" menu
        menu1.add(menuItem1);
        
        // Add menu bar to Frame
        setJMenuBar(menuBar);
    }
}