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
import javax.swing.JPanel;

public class BattleshipUIMouse extends JPanel {    
    // Initialize variable for original color
    private Color defaultBackground;
    
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
             * Check when the mouse has exited the grid block
             * When the mouse exits, change the color to the original color
             */
            public void mouseExited(MouseEvent e) {
                setBackground(defaultBackground);
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
}