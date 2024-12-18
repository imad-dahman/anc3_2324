package sokoban.viewmodel;

import sokoban.model.Board4Design;

public class GridViewModel4Design {

    private final Board4Design board4Design;


    GridViewModel4Design(Board4Design board) {
        this.board4Design = board;
    }
    public  int gridWidth() {
        return board4Design.getGridColumnsProperty().get();
    }

    public  int gridHeight() {
        return board4Design.getGridRowsProperty().get();

    }

    public CellViewModel4Design getCellViewModel(int line, int col) {
        return new CellViewModel4Design(line, col, board4Design);
    }
}
