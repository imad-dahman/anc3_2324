package sokoban.viewmodel;

import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import sokoban.model.Board4Design;
import sokoban.model.ElementType;
import sokoban.model.Grid4Design;

import java.io.File;

public class BoardViewModel4Design {


    private final GridViewModel4Design gridViewModel4Design;
    private Board4Design board;


    public BoardViewModel4Design(Board4Design board4Design) {
        this.board = board4Design;
       this.gridViewModel4Design  = new GridViewModel4Design(board);
    }


    public BooleanProperty ballAddedProperty() {
        return board.ballAddedProperty();
    }


    public void putMur() {
        board.putElement(ElementType.mur);
    }
    public void putCible() {
        board.putElement(ElementType.cible);
    }

    public void putTerrain() {
        board.putElement(ElementType.terrain);
    }

    public void putCaisse() {
        board.putElement(ElementType.caisse);
    }

    public BooleanProperty NumerAddedProperty() {
        return board.NumerAddedProperty();
    }
    public BooleanProperty boxAddedProperty() {
        return board.boxAddedProperty();
    }

    public BooleanProperty playerAddedProperty() {
        return board.playerAddedProperty();
    }



    public GridViewModel4Design getGridViewModel() {
        return gridViewModel4Design;
    }


    public void teleport() {
        board.teleport(ElementType.joueur);
    }
    public IntegerBinding maxFilledCells() {
        return board.maxFilledCells();
    }
    public LongBinding filledCellsCountProperty() {
        return board.filledCellsCountProperty();
    }

    public LongBinding boxCountProperty() {
        return board.boxCountProperty();
    }


    public IntegerProperty getGridColumnsProperty()
    {
        return board.getGridColumnsProperty();
    }

    public IntegerProperty getGridRowsProperty()
    {
        return board.getGridRowsProperty();
    }


    public void save(File file) {
        board.save(file);
    }

//    public Board4Design open(File file) {
//        Board4Design  newBoard =  board.open(file);
//        return newBoard;
//    }


        public void open(File file) {
        Board4Design newBoard = board.open(file);
        board = newBoard;
        }
    public BooleanProperty GridChangedProperty() {
        return board.GridChangedProperty();
    }


    public Grid4Design getGrid() {
        return board.getGrid();
    }

    public Board4Design getBoard() {
        return board;
    }
}
