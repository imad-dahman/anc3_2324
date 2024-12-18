package sokoban.viewmodel;

import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import sokoban.model.Board4Play;

public class BoardViewModel4Play {

    private final GridViewModel4Play gridViewModel4Play;
    private final Board4Play board4Play;
    public BoardViewModel4Play(Board4Play board4Play) {
        this.board4Play = board4Play;
        this.gridViewModel4Play  = new GridViewModel4Play(board4Play);
    }

    public GridViewModel4Play getGridViewModel() {
        return gridViewModel4Play;
    }
    public IntegerProperty getGridColumnsProperty()
    {
        return board4Play.getGridColumnsProperty();
    }
    public LongBinding goalsReachedProperty() {
        return board4Play.ballCounterProperty();
    }

    public BooleanProperty GridChangedProperty() {
        return board4Play.GridChangedProperty();
    }



    public LongBinding totalPushedOnTargetProperty() {
        return board4Play.totalPushedOnTargetProperty();
    }
    public void changeTargetNumberRandomly(){
        board4Play.changeTargetNumberRandomly();
    }
    public IntegerProperty movesCounterProperty() {
        return board4Play.movesCounterProperty();
    }
    public BooleanProperty getMoveProperty(){
        return board4Play.getMoveProperty();
    }
    public BooleanProperty mushroomVisibleProperty() {
        return board4Play.mushroomVisibleProperty();
    }

    public void setMushroomVisible(boolean isVisible) {
        board4Play.setMushroomVisible(isVisible);
    }


    public void mushroomClicked() {
        board4Play.placeMushroom();
    }



}
