package hunting;

import hunting.model.GameLogic;
import hunting.view.GameFrame;
import hunting.controller.GameController;

public class Main {
    public static void main(String[] args) {
        final int STARTER_SIZE = 5;

        // Swing GUI init call on EDT
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                GameLogic model = new GameLogic(STARTER_SIZE);
                GameFrame view = new GameFrame(STARTER_SIZE);
                new GameController(model, view);
                view.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
