package hunting.model;

public class Runner extends Figure{
    public Runner(int row, int col) {
        super(row,col);
    }

    @Override
    public String getType(){
        return "RUNNER";
    }
}
