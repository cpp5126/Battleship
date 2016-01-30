/*
 * BattleshipUIMouse.java -         Creates mouse adapter for the grid on the JFrame
 * @author                          Kam Talebzadeh
 * @date                            January 28, 2016
 */

package battleship;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JPanel;

public class BattleshipUIMouse extends JPanel {    
    // Initialize variable for original color
    private Color defaultBackground;
    
    public String coords;
    
    /**
     * Creates mouse adapter
     */
    public BattleshipUIMouse() {
        
        // Set background of JPanel
        setBackground(new Color(83, 128, 250));
        
        // Add mouse listener to JPanel
        // Create new Mouse Adapter
        addMouseListener(new MouseAdapter() {
            @Override
            /**
             * Check when the mouse has entered into a specific grid block
             * Change the color of the grid block when the mouse is within its boundaries
             */
            public void mouseEntered(MouseEvent e) {
                defaultBackground = getBackground();
                setBackground(Color.darkGray);
            }
            
            @Override
            /**
             * Check if mouse has clicked within a block
             *  If clicked, set color to red to show a fire has been shot at this block
             */
            public void mouseClicked(MouseEvent e) {
                setBackground(Color.RED);
                try{
                    Client.out.writeObject("Clicked: " + coords);
                    Client.out.flush();
                }catch(IOException ioException){
                    ioException.printStackTrace();
                }
            }

            @Override
            /**
             * Check when the mouse has exited the grid block
             * When the mouse exits, change the color to the original color
             */
            public void mouseExited(MouseEvent e) {
                if(getBackground() != Color.RED){
                    setBackground(defaultBackground);
                }
            }
        });
    }

    @Override
    /**
     * Get preferred size
     */
    public Dimension getPreferredSize() {
        return new Dimension(50, 50);
    }
    
    /**
     * Set coordinates of each grid block
     * @param row
     * @param col 
     */
    public void setCoords(int row, int col){
        String rows = Integer.toString(row);
        String cols = Integer.toString(col);
        coords = "Row: " + rows + " -- Col: " + cols;
    }
}