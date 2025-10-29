package hunting.view;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.Color;

public class GamePanel extends JPanel {
    private ButtonField[][] fields;
    private final int size;

    public GamePanel(int size) {
        this.size = size;
        setLayout(new GridLayout(size, size));
        this.fields = new ButtonField[size][size];

        initializeFields();

        setPreferredSize(new Dimension(size * 80, size * 80));
    }

    private void initializeFields() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ButtonField button = new ButtonField(i, j);
                fields[i][j] = button;
                add(button);
            }
        }
    }

    public ButtonField getButtonField(int row, int col) {
        if (row >= 0 && row < size && col >= 0 && col < size) {
            return fields[row][col];
        }
        return null;
    }

    /**
     * Beállítja a Controller-t (mint ActionListener-t) minden egyes ButtonField-re.
     */
    public void setFieldActionListener(ActionListener listener) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fields[i][j].addActionListener(listener);
            }
        }
    }

    /**
     * Beállítja egy adott mező szövegét a figura típusának megfelelően, és frissíti a színt.
     * Ezt a GameController hívja meg a Model állapotváltozása után.
     */
    public void setFieldContent(int row, int col, String figureType) {
        ButtonField field = getButtonField(row, col);
        if (field != null) {
            field.resetAppearance(); // Visszaállítja a színt/szegélyt

            if (figureType != null) {
                if (figureType.equals("HUNTER")) {
                    field.setText("H");
                    field.setForeground(Color.RED);
                } else if (figureType.equals("RUNNER")) {
                    field.setText("R");
                    field.setForeground(Color.BLUE);
                }
            } else {
                field.setText("");  // FIGURA NÉLKÜL: ÜRES MEZŐ BEÁLLÍTÁSA
            }
        }
    }

    /**
     * Visszaállítja az összes mező alapvető megjelenését (pl. a kiválasztott kiemelés megszüntetéséhez).
     * Ezt a GameController hívja meg minden kattintás elején.
     */
    public void resetAllFieldAppearances() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fields[i][j].resetAppearance();
            }
        }
    }
}
