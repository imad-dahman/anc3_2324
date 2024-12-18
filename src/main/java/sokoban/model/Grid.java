package sokoban.model;

import javafx.beans.property.*;

import java.util.TreeSet;

public abstract class Grid {


     private final IntegerProperty gridColumns = new SimpleIntegerProperty(15);
     private final IntegerProperty gridRows = new SimpleIntegerProperty(10);
     private final BooleanProperty gridChangedProperty = new SimpleBooleanProperty(false);

     private  Cell[][] matrix;

    private  Joueur joueur = new Joueur() ;

    public Grid() {
        matrix=new Cell4Design[gridRows.get()][];
        for (int i = 0; i < gridRows.get(); ++i) {
            matrix[i] = new Cell4Design[gridColumns.get()];
            for (int j = 0; j < gridColumns.get(); ++j) {
                matrix[i][j] = new Cell4Design();
            }
        }
        gridRows.addListener((obs,old,newVal)-> refreshMatrix());
        gridColumns.addListener((obs,old,newVal)->refreshMatrix());
    }

    public Grid(int width, int height) {
        gridColumns.set(width);
        gridRows.set(height);
        matrix=new Cell4Design[gridRows.get()][];
        for (int i = 0; i < gridRows.get(); ++i) {
            matrix[i] = new Cell4Design[gridColumns.get()];
            for (int j = 0; j < gridColumns.get(); ++j) {
                matrix[i][j] = new Cell4Design();
            }
        }

        gridRows.addListener((obs,old,newVal)-> refreshMatrix());
        gridColumns.addListener((obs,old,newVal)->refreshMatrix());
    }



    private void refreshMatrix()
    {

       Cell[][] newmatrix = new Cell[gridRows.get()][gridColumns.get()];
        for (int i = 0; i < gridRows.get(); ++i) {
            for (int j = 0; j < gridColumns.get(); ++j) {
                if (i< matrix.length && j<matrix[i].length)
                    newmatrix[i][j]=matrix[i][j];
                else
                    newmatrix[i][j] = new Cell4Design();
            }
        }

        matrix = newmatrix;
    }

    public Cell[][] getMatrix() {
        return matrix;
    }

    public IntegerProperty getGridColumnsProperty()
    {
        return gridColumns;
    }


    public IntegerProperty getGridRowsProperty() {
        return gridRows;
    }
    public BooleanProperty GridChangedProperty() {
        return gridChangedProperty;
    }

    public void setGridChangedProperty(boolean gridChangedProperty) {
        this.gridChangedProperty.set(gridChangedProperty);
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public ReadOnlyObjectProperty<TreeSet<Element>> valueProperty(int line, int col) {
        return matrix[line][col].valueProperty();
    }

    public BooleanProperty isListChangedProperty(int line,int col)
    {
            return matrix[line][col].isListChangedProperty();
    }



   abstract void play(int line,int col,ElementType elementTypeSelected);
    public void setMatrix(Cell[][] matrix) {
        this.matrix = matrix;
    }
    public boolean isCellMushroom(int line, int col) {
        return getMatrix()[line][col].getElement().getElement() == ElementType.mushroom;
    }



}
