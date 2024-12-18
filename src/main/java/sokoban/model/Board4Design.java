package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;

import java.io.File;
import java.util.TreeSet;

public class  Board4Design extends Board{

    private final ObjectProperty<Grid4Design> grid = new SimpleObjectProperty<>(new Grid4Design());
    private  IntegerBinding MAX_FILLED_CELLS;

    private  BooleanBinding isFull;
    private ElementType  elementTypeSelected ;

    public Board4Design() {
        initializeBindings();
    }

    public Board4Design(int width, int height)
    {
        super(width, height);
        grid.set(new Grid4Design(width,height));
        initializeBindings();
    }

    public Board4Design(Grid4Design design) {
        super();
        grid.set(new Grid4Design(design));
        initializeBindings();
    }
    private void initializeBindings() {
        MAX_FILLED_CELLS = Bindings.createIntegerBinding(() ->
                        (grid.get().getGridColumnsProperty().get() * grid.get().getGridRowsProperty().get()) / 2,
                grid.get().getGridColumnsProperty(), grid.get().getGridRowsProperty());

        isFull = grid.get().filledCellsCountProperty().isEqualTo(MAX_FILLED_CELLS);
    }

    public void play(int line, int col, ElementType elementSelected)
    {
        ElementType elementAtLocation = grid.get().getElementTypeAt(line,col);
        if(!isFull()  || elementSelected==ElementType.terrain){
            grid.get().play(line,col,elementSelected);
        }else if (isFull() && elementSelected == ElementType.cible) {
            if (elementAtLocation == ElementType.caisse || elementAtLocation == ElementType.joueur) {
                grid.get().play(line, col, elementSelected);
            }
        }
        if(grid.get().isValidProperty().get() && isFull() ){
            if( elementTypeSelected == ElementType.joueur && !grid.get().isValProperty().get() ){
                grid.get().play(line, col, elementSelected);
            }
        }
    }
    public void removeElement(int line, int col) {
            grid.get().removeElement(line,col);
    }
    public void teleport(ElementType elementTypeSelected) {
        this.elementTypeSelected = elementTypeSelected;
    }
    public Board4Design open(File file) {
        Grid4Design newGrid =  grid.get().open(file);
        grid.set(newGrid);
        MAX_FILLED_CELLS.invalidate();
        isFull = grid.get().filledCellsCountProperty().isEqualTo(MAX_FILLED_CELLS);
        grid.get().GridChangedProperty().set(false);
        return this;
    }

    public void save(File file) {
        grid.get().save(file);
        grid.get().GridChangedProperty().set(false);
    }
    public void putElement(ElementType elementType) {

        switch (elementType)
        {
            case mur -> this.elementTypeSelected=ElementType.mur;
            case cible -> this.elementTypeSelected=ElementType.cible;
            case terrain -> this.elementTypeSelected=ElementType.terrain;
            case caisse -> this.elementTypeSelected=ElementType.caisse;
        }
    }

    public ReadOnlyObjectProperty<TreeSet<Element>> valueProperty(int line, int col) {
        return grid.get().valueProperty(line, col);
    }

    public BooleanProperty isListChangedProperty(int line, int col)
    {
        return grid.get().isListChangedProperty(line, col);
    }
    public Boolean isFull() {
        return isFull.get();
    }
    public IntegerBinding maxFilledCells() {
        return MAX_FILLED_CELLS;
    }
    public LongBinding filledCellsCountProperty() {
        return grid.get().filledCellsCountProperty();
    }
    public LongBinding boxCountProperty() {
        return grid.get().boxCountProperty();
    }
    public BooleanProperty ballAddedProperty() {
        return grid.get().isValidballProperty();
    }

    public BooleanProperty NumerAddedProperty() {
        return grid.get().areCountersEqualProperty();
    }
    public BooleanProperty boxAddedProperty() {
        return grid.get().isValidboxProperty();
    }

    public BooleanProperty playerAddedProperty() {
        return grid.get().isValidProperty();
    }

    public IntegerProperty getGridColumnsProperty()
    {
        return grid.get().getGridColumnsProperty();
    }


    public IntegerProperty getGridRowsProperty() {
        return grid.get().getGridRowsProperty();
    }

    public BooleanProperty GridChangedProperty() {
        return grid.get().GridChangedProperty();
    }


    public Grid4Design getGrid() {
        return grid.get();
    }

    public ElementType getElementTypeSelected()
    {
        return elementTypeSelected;
    }


}
