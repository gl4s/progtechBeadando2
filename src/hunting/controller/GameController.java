package hunting.controller;

import hunting.model.GameLogic;
import hunting.model.Figure;
import hunting.view.ButtonField;
import hunting.view.GameFrame;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
//import java.awt.Window;

public class GameController implements ActionListener {
    private final GameLogic model;
    private final GameFrame view;

    private Point selectedPosition;

    public GameController(GameLogic model, GameFrame view) {
        this.model = model;
        this.view = view;
        this.selectedPosition = null;

        view.getGamePanel().setFieldActionListener(this); // cellák
        view.setMenuListeners(this); // menu itemek

        updateView();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();
        if (command != null && command.startsWith("NEW_")) {
            handleNewGameCommand(command);
            return;
        }

        if (e.getSource() instanceof ButtonField) {
            ButtonField clickedButton = (ButtonField) e.getSource();
            handleBoardClick(clickedButton.getRow(), clickedButton.getCol());
        }
    }

    private void handleNewGameCommand(String command) {
        int newSize = 0;
        if (command.endsWith("3x3")) {
            newSize = 3;
        } else if (command.endsWith("5x5")) {
            newSize = 5;
        } else if (command.endsWith("7x7")) {
            newSize = 7;
        }

        if (newSize > 0) {
            view.dispose();

            startNewGame(newSize);
        }
    }

    private void startNewGame(int size) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            GameLogic newModel = new GameLogic(size);
            GameFrame newView = new GameFrame(size);
            new GameController(newModel, newView);
            newView.setVisible(true);
        });
    }

    private void handleBoardClick(int row, int col) {
        // ha vége a menetnek nem engedünk több kattintást
        if (model.getWinner() != GameLogic.WINNER_NONE) {
            updateView();
            return;
        }

        // Először minden kiemelést törlünk, kivéve ha az adott mező egy új figure
        view.getGamePanel().resetAllFieldAppearances();

        if (selectedPosition == null) {
            // 1. KLIKK: Figura kiválasztása
            selectFigure(row, col);
        } else {
            // 2. KLIKK: Célmező / Kiválasztás visszavonása
            int fromRow = selectedPosition.x;
            int fromCol = selectedPosition.y;

            // ugyanazon mezőre kattintva újra visszavonjuk a kiválasztást
            if (fromRow == row && fromCol == col) {
                selectedPosition = null;
                updateView();
                return;
            }

            // Megpróbáljuk végrehajtani a lépést
            boolean moveSuccessful = model.move(fromRow, fromCol, row, col);

            if (moveSuccessful) {
                selectedPosition = null; // Sikeres lépés után nincs kiválasztott figura
                checkGameOver();
            } else {
                // Érvénytelen célmező, megpróbáljuk újra kiválasztani a kattintott figuret
                // pl. ha a Hunter egy másik Hunterre kattintott a célmező helyett
                selectFigure(row, col);
            }
            updateView();
        }
    }

    /**
     * Megpróbál figurát kiválasztani a soron következő játékos szabályai szerint.
     */
    private void selectFigure(int row, int col) {
        Figure figure = model.getBoard().getFigure(row, col);

        if (figure != null) {
            boolean isHunterSelected = figure instanceof hunting.model.Hunter;

            // csak a soron következő játékos figure-jét lehet kiválasztani
            if (isHunterSelected == model.isHunterTurn()) {
                selectedPosition = new Point(row, col);

                // vizualis kiemelés és érvényes lépések mutatása
                view.getGamePanel().getButtonField(row, col).highlightSelected();
                highlightValidMoves(row, col);
            }
        } else {
            // üres mezőre kattintott, mikor figurát kéne választani, töröljük a kiválasztást
            selectedPosition = null;
        }
    }

    /**
     * Kiemeli az érvényes célmezőket a kiválasztott figurához.
     */
    private void highlightValidMoves(int fromRow, int fromCol) {
        // Lehetséges lépés eltolások: (0, 1), (0, -1), (1, 0), (-1, 0)
        int[] dr = {0, 0, 1, -1};
        int[] dc = {1, -1, 0, 0};

        for (int i = 0; i < 4; i++) {
            int targetRow = fromRow + dr[i];
            int targetCol = fromCol + dc[i];

            // A GameLogic ellenőrzi a táblán belüli érvényességet és az üres mezőre lépést
            if (model.isValidMove(fromRow, fromCol, targetRow, targetCol)) {
                view.getGamePanel().getButtonField(targetRow, targetCol).highlightTarget();
            }
        }
    }

    /**
     * Frissíti a View-t a Model aktuális állapota alapján.
     */
    public void updateView() {
        view.getGamePanel().resetAllFieldAppearances();

        int size = model.getBoard().getSize();

        // figures megjelenítése
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Figure figure = model.getBoard().getFigure(i, j);
                String type = (figure != null) ? figure.getType() : null;
                view.getGamePanel().setFieldContent(i, j, type);
            }
        }

        // Ha van kiválasztott figure, azt kiemeljük újra, megmarad a kiemelés
        if (selectedPosition != null) {
            view.getGamePanel().getButtonField(selectedPosition.x, selectedPosition.y).highlightSelected();
            highlightValidMoves(selectedPosition.x, selectedPosition.y);
        }

        // status update
        String turn = model.isHunterTurn() ? "Támadó (H)" : "Menekülő (R)";
        String status = String.format("Lépés: %d/%d | Következik: %s",
                model.getCurrentStep(), model.getMaxSteps(), turn);
        view.setStatus(status);
    }

    private void checkGameOver() {
        String winner = model.getWinner();

        if (winner != null) {
            String message;

            if (winner.equals(GameLogic.WINNER_HUNTER)) {
                message = "Játék vége! Hunter nyert bekerítéssel!";
            } else if (winner.equals(GameLogic.WINNER_RUNNER)) {
                message = "Játék vége! Runner nyert, túlélte a max. lépésszámot (" + model.getMaxSteps() + ")!";
            } else {
                return; // még tart a jatek (bár a model.getWinner() null-t adna vissza)
            }

            JOptionPane.showMessageDialog(view, message, "Játék Vége", JOptionPane.INFORMATION_MESSAGE);

            model.startNewGame();
            selectedPosition = null;
            updateView();

            view.setStatus("Új játék elindítva! Következik: Támadó (H)");
        }
    }
}
