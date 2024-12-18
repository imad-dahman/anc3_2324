package sokoban.viewmodel;

import javafx.beans.property.IntegerProperty;
import sokoban.model.Board4Design;

public class BoardViewModel {

    private final GridViewModel gridViewModel;
    private  Board4Design board;


    public BoardViewModel(Board4Design board) {
        this.board = board;
        gridViewModel = new GridViewModel(board);
    }



    public IntegerProperty getGridColumnsProperty()
    {
        return board.getGridColumnsProperty();
    }



}
