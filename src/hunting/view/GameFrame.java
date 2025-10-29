package hunting.view;

import javax.swing.*;
import java.awt.*;
import hunting.controller.GameController;

public class GameFrame extends JFrame {

    private GamePanel gamePanel;
    private JLabel statusLabel;
    private JMenuBar menuBar;
    private final int initialSize;

    public GameFrame(int size) {
        this.initialSize = size;

        setTitle("Vadászat Játék - " + size + "x" + size);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setupMenu();

        statusLabel = new JLabel("Új játék indítása...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(statusLabel, BorderLayout.NORTH);

        gamePanel = new GamePanel(initialSize);
        add(gamePanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Létrehozza a menüpontokat a táblaméretek alapján.
     */
    private void setupMenu() {
        menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Játék");

        // M: A Controller fogja kezelni az ActionCommand-okat
        JMenuItem new3x3 = createMenuItem("Új játék (3x3 - Max 12 lépés)", "NEW_3x3");
        JMenuItem new5x5 = createMenuItem("Új játék (5x5 - Max 20 lépés)", "NEW_5x5");
        JMenuItem new7x7 = createMenuItem("Új játék (7x7 - Max 28 lépés)", "NEW_7x7");

        gameMenu.add(new3x3);
        gameMenu.add(new5x5);
        gameMenu.add(new7x7);
        gameMenu.addSeparator();

        JMenuItem exit = createMenuItem("Kilépés", "EXIT");
        exit.addActionListener(e -> System.exit(0)); // A kilépés eseményét itt kezeljük
        gameMenu.add(exit);

        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Segédmetódus JMenuItem létrehozására.
     */
    private JMenuItem createMenuItem(String text, String command) {
        JMenuItem item = new JMenuItem(text);
        item.setActionCommand(command);
        return item;
    }

    /**
     * Beállítja a menüpontok ActionListener-eit. Ezt a Controller hívja meg.
     */
    public void setMenuListeners(GameController controller) {
        for (Component component : menuBar.getMenu(0).getMenuComponents()) {
            if (component instanceof JMenuItem) {
                JMenuItem item = (JMenuItem) component;
                if (!"EXIT".equals(item.getActionCommand())) {
                    item.addActionListener(controller);
                }
            }
        }
    }

    public void setStatus(String text) {
        statusLabel.setText(text);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
