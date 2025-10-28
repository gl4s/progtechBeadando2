package hunting;

import hunting.controller.GameController;
import hunting.model.GameLogic;
import hunting.view.GamePanel;



public class Main {
    public static void main(String[] args){
        int startingSize = 5;
        GameLogic logics = new GameLogic(startingSize);
        GamePanel view = new GamePanel();
        GameController controller = new GameController(logics,view);

        view.setController(controller);
        controller.refreshView();
        view.setVisible(true);
    }
}
