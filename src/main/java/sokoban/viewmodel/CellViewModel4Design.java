package sokoban.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import sokoban.model.Board4Design;
import sokoban.model.Element;

import java.util.TreeSet;

public class CellViewModel4Design {
    private final int line, col;
    private final Board4Design board4Design;
    CellViewModel4Design(int line, int col, Board4Design board4Design) {
        this.line = line;
        this.col = col;
        this.board4Design = board4Design;
    }

    public void play() {
        if (board4Design.getElementTypeSelected()!=null)
        board4Design.play(line, col,board4Design.getElementTypeSelected());
    }
    public ReadOnlyObjectProperty<TreeSet<Element>> valueProperty() {
        return board4Design.valueProperty(line, col);
    }

    public BooleanProperty isListChangedProperty()
    {
        return board4Design.isListChangedProperty(line, col);
    }


    public void removeElement() {
        board4Design.removeElement(line, col);
    }
}
