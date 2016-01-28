/*
 * BattleshipUIPane.java -      Creates 10x10 grid with mouse adapter
 * @author                      Kam Talebzadeh
 * @date                        January 28, 2016
 */

package battleship;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

public class BattleshipUIPane extends JPanel {

    public BattleshipUIPane() {
        // Set layout for JPanel
        setLayout(new GridBagLayout());
        
        // Create GridBag Constraints
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Loop through 10 rows and 10 columns to create a 10x10 grid
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                // Set the X grid of the GridBag Constraint to be the value of the column
                gbc.gridx = col;
                // Set the Y grid of the GridBag Constraint to be the value of the row
                gbc.gridy = row;

                // Create mouse adapter from BattleshipUIMouse class
                // Done within loop to create a new one for each grid block
                BattleshipUIMouse battleshipUIMouse = new BattleshipUIMouse();

                // Create border for squared grid
                // Done within loop to create a new one for each grid block
                Border border = null;
                
                // If-Else statement to create borders around each grid block
                // Looped through a second time based on columns to check which sides need a line
                if (row < 9) {
                    if (col < 9) {
                        border = new MatteBorder(1, 1, 0, 0, Color.GRAY);
                    } else {
                        border = new MatteBorder(1, 1, 0, 1, Color.GRAY);
                    }
                }else{
                    if (col < 9) {
                        border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
                    } else {
                        border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                    }
                }
                // Set border to mouse adapter for that grid block
                battleshipUIMouse.setBorder(border);
                // Add mouse adapter and GridBag Constraint of each grid block to the JPanel
                add(battleshipUIMouse, gbc);
            }
        }
    }
}
