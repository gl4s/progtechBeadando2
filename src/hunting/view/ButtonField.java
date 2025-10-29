package hunting.view;

import javax.swing.*;   //setBorder -> BorderFactory
import java.awt.*;      //setPreferredSize-> Dimension
import javax.swing.border.Border;

public class ButtonField extends JButton {
    private final int row;
    private final int col;
    private final Border defaultBorder;

    public ButtonField(int row, int col) {
        this.row = row;
        this.col = col;
        setPreferredSize(new Dimension(80, 80)); //def size
        setFont(new Font("Arial", Font.BOLD, 18));
        this.defaultBorder = BorderFactory.createLineBorder(Color.GRAY);

        resetAppearance();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    //color és border reset
    public void resetAppearance() {
        setBorder(defaultBorder);
        if ((row + col) % 2 == 0) {
            setBackground(new Color(230, 200, 160)); //vilagosabb
        } else {
            setBackground(new Color(180, 130, 90));  //sötétebb
        }
        setForeground(Color.BLACK);
        setEnabled(true);
    }

    public void highlightSelected() {
        setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
    }

    public void highlightTarget() {
        setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
    }
}