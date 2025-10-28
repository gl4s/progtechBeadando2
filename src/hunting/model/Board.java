package hunting.model;

public class Board {
    private final int size;
    private Figure[][] fields;

    public Board(int size) {
        if (size % 2 == 0) {
            throw new IllegalArgumentException("A tábla méretének páratlannak kell lennie (3, 5, 7).");
        }
        this.size = size;
        this.fields = new Figure[size][size];
        setupInitPos();
    }

    private void setupInitPos(){
        // Kezdeti állapot törlése
        fields = new Figure[size][size];
        int center = size / 2; // Pl.: 5x5 esetén 2

        // 1. Runner (Menekülő) - középen
        Runner runner = new Runner(center, center);
        fields[center][center] = runner;

        // 2. Hunterek (Támadók) - négy sarokban
        fields[0][0] = new Hunter(0, 0);                 // Bal felső
        fields[0][size - 1] = new Hunter(0, size - 1);     // Jobb felső
        fields[size - 1][0] = new Hunter(size - 1, 0);     // Bal alsó
        fields[size - 1][size - 1] = new Hunter(size - 1, size - 1); // Jobb alsó
    }

    public int getSize() {
        return size;
    }

    public Figure getFigure(int row, int col) {
        if (isCoordsValid(row, col)) {
            return fields[row][col];
        }
        return null;
    }

    public boolean isCoordsValid(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public boolean isFieldEmpty(int row, int col) {
        return isCoordsValid(row, col) && fields[row][col] == null;
    }

    public Runner getRunner() {
        // Keresse meg a Runner-t a táblán (ha a GameLogic nem tartja számon külön)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (fields[i][j] != null && fields[i][j] instanceof Runner) {
                    return (Runner) fields[i][j];
                }
            }
        }
        return null;
    }

    public void moveFigure(int fromRow, int fromCol, int toRow, int toCol) {
        Figure figure = fields[fromRow][fromCol];
        if (figure != null) {
            // 1. Töröljük a régi helyről
            fields[fromRow][fromCol] = null;

            // 2. Beállítjuk az új helyre
            fields[toRow][toCol] = figure;
            figure.setPosition(toRow, toCol); // Frissítjük a figura objektum pozícióját is
        }
    }
}
