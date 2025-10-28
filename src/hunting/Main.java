package hunting;

import hunting.controller.GameController;
import hunting.model.GameLogics;
import hunting.view.GamePanel;

public class Main {
    public static void main(String[] args){
        int startingSize = 5;
        GameLogics logics = new GameLogics(startingSize);
        GamePanel view = new GamePanel();
        GameController controller = new GameController(logics,view);

        view.setController(controller);
        controller.refreshView();
        view.setVisible(true);
    }
}
