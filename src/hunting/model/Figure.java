package hunting.model;

public abstract class Figure {
    private int row; // Sor (0-tól n-1-ig)
    private int col; // Oszlop (0-tól n-1-ig)

    public Figure(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPosition(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }

    // Figure típus megkülönb.
    public abstract String getType();
}
