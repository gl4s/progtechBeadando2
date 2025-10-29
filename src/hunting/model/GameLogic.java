package hunting.model;



public class GameLogic {
    private Board board;
    private int currentStep;
    private final int maxSteps; //12,20,28
    private boolean isHunterTurn;

    public static final String WINNER_HUNTER = "Hunter";
    public static final String WINNER_RUNNER = "Runner";
    public static final String WINNER_NONE = null;

    public GameLogic(int size){
        this.board = new Board(size);
        this.maxSteps = 4 * size;
        startNewGame();
    }

    public Board getBoard() {
        return board;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    public boolean isHunterTurn() {
        return isHunterTurn;
    }

    public void startNewGame(){
        this.board = new Board(board.getSize());
        this.currentStep = 0;
        this.isHunterTurn = true;
    }

    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        if(!board.isCoordsValid(toRow,toCol)){
            return false;
        }

        int rowDiff = Math.abs(fromRow - toRow);
        int colDiff = Math.abs(fromCol - toCol);

        if (!((rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1))){
            return false;
        }

        if(!board.isFieldEmpty(toRow,toCol)){
            return false;
        }

        Figure figure = board.getFigure(fromRow, fromCol);
        if (figure == null) {
            return false;
        }

        if (isHunterTurn) {
            return figure instanceof Hunter;
        } else {
            return figure instanceof Runner;
        }
    }

    private boolean isRunnerTrapped() {
        Runner runner = board.getRunner();
        if (runner == null) return true; // Hiba esetén feltételezzük a bekerítést

        int rRow = runner.getRow();
        int rCol = runner.getCol();

        // Lehetséges lépés eltolasok: (0, 1), (0, -1), (1, 0), (-1, 0)
        int[] dr = {0, 0, 1, -1}; //row
        int[] dc = {1, -1, 0, 0}; //col

        for (int i = 0; i < 4; i++) {
            int targetRow = rRow + dr[i];
            int targetCol = rCol + dc[i];

            // Ha van legalább egy valid és üres mezo, a Runner nem lesz bekerítve
            // Ellenőrzés:
            // 1. A célmező érvényes koordináta-e (a Board metódusa ellenőrzi a táblán belüli tartományt)
            // 2. A célmező üres-e (mert a Runner csak üres mezőre léphet)
            if (board.isFieldEmpty(targetRow, targetCol)) {
                return false;
            }
        }

        // Ha mind a 4 (vagy kevesebb, ha a szélen van) lehetséges lépés blokkolva van,
        // akkor a Runner be van kerítve.
        return true;
    }

    public String getWinner() {
        // 1. Lépésszám Vége (Runner győzelem)
        if (currentStep >= maxSteps) {
            return WINNER_RUNNER;
        }

        // 2. Bekerítés Ellenőrzése (Hunter győzelem)
        if (isRunnerTrapped()) {
            return WINNER_HUNTER;
        }

        return WINNER_NONE;
    }

    public boolean move(int fromRow, int fromCol, int toRow, int toCol) {
        if (getWinner() != WINNER_NONE) {
            return false; // Játék vége
        }

        if (isValidMove(fromRow, fromCol, toRow, toCol)) {
            board.moveFigure(fromRow, fromCol, toRow, toCol);

            // Következő körre váltás
            if (isHunterTurn) {
                // Ha a Hunterek léptek, jön a Runner, és nő a lépésszámláló
                currentStep++;
            }
            isHunterTurn = !isHunterTurn;

            return true;
        }
        return false;
    }
}
