package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.*;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class Grid4Design extends Grid{

    private final BooleanProperty balJoueurProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty joueurExisteProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty ballExisteProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty boxExisteProperty = new SimpleBooleanProperty(false);

    private final LongBinding filledCellsCount;
    private final LongBinding boxCount;
    private final LongBinding cibleCount;
    private final BooleanProperty arequals = new SimpleBooleanProperty(false);
    private int cpt =0;


    public Grid4Design() {
        filledCellsCount = Bindings.createLongBinding(() -> Arrays
                .stream(getMatrix())
                .flatMap(Arrays::stream)
                .filter(cell -> !cell.isEmpty())
                .count());
        boxCount = Bindings.createLongBinding(() -> Arrays
                .stream(getMatrix())
                .flatMap(Arrays::stream)
                .filter(Cell::isBox)
                .count());
        cibleCount = Bindings.createLongBinding(() -> Arrays
                .stream(getMatrix())
                .flatMap(Arrays::stream)
                .filter(Cell::isCible)
                .count());
        boxExisteProperty.bind(boxCount.greaterThanOrEqualTo(1));
        ballExisteProperty.bind(cibleCount.greaterThanOrEqualTo(1));
        arequals.bind(cibleCount.isEqualTo(boxCount));
    }

    public Grid4Design(int width, int height) {
        super(width, height);
        filledCellsCount = Bindings.createLongBinding(() -> Arrays
                .stream(getMatrix())
                .flatMap(Arrays::stream)
                .filter(cell -> !cell.isEmpty())
                .count());
        boxCount = Bindings.createLongBinding(() -> Arrays
                .stream(getMatrix())
                .flatMap(Arrays::stream)
                .filter(Cell::isBox)
                .count());
        cibleCount = Bindings.createLongBinding(() -> Arrays
                .stream(getMatrix())
                .flatMap(Arrays::stream)
                .filter(Cell::isCible)
                .count());
        boxExisteProperty.bind(boxCount.greaterThanOrEqualTo(1));
        ballExisteProperty.bind(cibleCount.greaterThanOrEqualTo(1));
        arequals.bind(cibleCount.isEqualTo(boxCount));

    }

    public Grid4Design(Grid4Design design) {
        super(design.getGridColumnsProperty().get(), design.getGridRowsProperty().get());
        Cell[][] designMatrix = design.getMatrix();
        Cell[][] playMatrix = new Cell4Design[design.getGridRowsProperty().get()][design.getGridColumnsProperty().get()];
        for (int i = 0; i < getGridRowsProperty().get(); i++) {
            for (int j = 0; j < getGridColumnsProperty().get(); j++) {
                playMatrix[i][j] = new Cell4Design();
                for (Element element : designMatrix[i][j].getValue()) {
                    playMatrix[i][j].addElement(element);
                    if(playMatrix[i][j].getElement().getElement() == ElementType.joueur){
                        joueurExisteProperty.set(true);
                        setJoueur(new Joueur(i,j));
                    }

                }
            }
        }

        setMatrix(playMatrix);

        filledCellsCount = Bindings.createLongBinding(() -> Arrays
                .stream(getMatrix())
                .flatMap(Arrays::stream)
                .filter(cell -> !cell.isEmpty())
                .count());
        boxCount = Bindings.createLongBinding(() -> Arrays
                .stream(getMatrix())
                .flatMap(Arrays::stream)
                .filter(Cell::isBox)
                .count());
        cibleCount = Bindings.createLongBinding(() -> Arrays
                .stream(getMatrix())
                .flatMap(Arrays::stream)
                .filter(Cell::isCible)
                .count());
        boxExisteProperty.bind(boxCount.greaterThanOrEqualTo(1));
        ballExisteProperty.bind(cibleCount.greaterThanOrEqualTo(1));
        arequals.bind(cibleCount.isEqualTo(boxCount));

    }
    public LongBinding filledCellsCountProperty() {
        return  filledCellsCount;
    }

    public LongBinding boxCountProperty() {
        return  boxCount;
    }

    public BooleanProperty areCountersEqualProperty() {
        return arequals;
    }
    @Override
    void play(int line,int col,ElementType elementTypeSelected)
    {
        switch (elementTypeSelected) {
            case joueur -> {
                if (isValidToAddJoueur(line, col)) {
                    if (!isPlayerAdded()) {
                        getMatrix()[getJoueur().getXPosition()][getJoueur().getyPosition()].removeElement(getJoueur());

                    }
                    getJoueur().setxPosition(line);
                    getJoueur().setyPosition(col);
                    getMatrix()[line][col].addElement(getJoueur());
                }

            }
            case mur -> {
                if (isValidToAddMur(line, col)) {
                    getMatrix()[line][col].addElement(new Mur());
                }
            }
            case cible -> {

                if (isValidToAddCible(line, col)) {
                    getMatrix()[line][col].addElement(new Cible());
                }

            }
            case caisse -> {
                if (isValidToAddCaisse(line, col)) {
                    getMatrix()[line][col].addElement(new Caisse());
                }

            }
        }
        //gridChangedProperty.set(true);
        setGridChangedProperty(true);
        refresh();

    }



    private void refresh()
    {
        filledCellsCount.invalidate();
        boxCount.invalidate();
        cibleCount.invalidate();
        isPlayerAdded();
    }
    public void removeElement(int line, int col) {
        Element element = getMatrix()[line][col].getElement();
         if (element.getElement() == ElementType.cible) {
            if(getMatrix()[line][col].getValue().contains(getJoueur())){
                balJoueurProperty.set(false);
            }
        }
        getMatrix()[line][col].removeElement(element);
        refresh();
    }
    public  BooleanProperty isValidProperty() {
        return joueurExisteProperty;
    }
    public boolean isValidToAddMur(int line, int col) {
        return getMatrix()[line][col].getElement().getElement()== ElementType.terrain;
    }
    public boolean isValidToAddJoueur(int line, int col) {
        Boolean isTerrain = getMatrix()[line][col].getElement().getElement() == ElementType.terrain;
        Boolean isMur = getMatrix()[line][col].getElement().getElement() == ElementType.mur;
        Boolean isCible = getMatrix()[line][col].getElement().getElement() == ElementType.cible;


        if (isTerrain || isMur || isCible ) {
            if (isMur) {
                getMatrix()[line][col].removeElement(getMatrix()[line][col].getElement());
            }
            balJoueurProperty.set(false);
            return true;
        }

        return false;
    }
    public boolean isValidToAddCaisse(int line, int col) {

        Boolean isTerrain = getMatrix()[line][col].getElement().getElement() == ElementType.terrain;
        Boolean isMur = getMatrix()[line][col].getElement().getElement() == ElementType.mur;
        Boolean isCible = getMatrix()[line][col].getElement().getElement() == ElementType.cible;

        if (isTerrain|| isMur || isCible) {
            if (isMur) {
                getMatrix()[line][col].removeElement(getMatrix()[line][col].getElement());
            }
            return true;
        }
        return false;
    }
    public boolean isValidToAddCible(int line, int col) {

        Boolean isTerrain = getMatrix()[line][col].getElement().getElement() == ElementType.terrain;
        Boolean isMur = getMatrix()[line][col].getElement().getElement() == ElementType.mur;
        Boolean isJoueur = getMatrix()[line][col].getElement().getElement() == ElementType.joueur;
        Boolean isCaisse = getMatrix()[line][col].getElement().getElement() == ElementType.caisse;


        if (isMur) {
            getMatrix()[line][col].removeElement(getMatrix()[line][col].getElement());
            return true;
        }
        if ( isCaisse || isTerrain ) {
            return true;
        }
        if(isJoueur) {
            balJoueurProperty.set(true);
            return true;
        }
        return false;
    }
    private boolean isPlayerAdded() {
        for (int i = 0; i < getGridRowsProperty().get(); ++i) {
            for (int j = 0; j < getGridColumnsProperty().get(); ++j) {
                for (Element element : getMatrix()[i][j].getValue()) {
                    if (element.getElement() == ElementType.joueur) {
                        joueurExisteProperty.set(true);
                        return false;
                    }

                }
            }
        }
        joueurExisteProperty.set(false);
        return true;
    }
    public  BooleanProperty isValProperty() {
        return balJoueurProperty;
    }
    public  BooleanProperty isValidballProperty() {
        return ballExisteProperty;
    }
    public  BooleanProperty isValidboxProperty() {
        return boxExisteProperty;
    }
    public void save(File file) {

        char[][] matrixChar = new char[this.getGridRowsProperty().get()][this.getGridColumnsProperty().get()];

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (int i = 0; i < this.getGridRowsProperty().get(); i++) {
                    for (int j = 0; j < this.getGridColumnsProperty().get(); j++) {
                        TreeSet<Element> element = getMatrix()[i][j].getValue();
                        char symbol = getSymbolForElement(element);
                        matrixChar[i][j] = symbol;
                        writer.write(matrixChar[i][j]);
                    }
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }




    private  char getSymbolForElement(TreeSet<Element> elementList) {

        boolean hasCaisse = false;
        boolean hasCible = false;
        boolean hasJoueur = false;
        Element lastElement = null;


        for (Element element : elementList) {
            if (element.getElement() == ElementType.caisse) {
                hasCaisse = true;
            } else if (element.getElement() == ElementType.cible) {
                hasCible = true;
            } else if (element.getElement() == ElementType.joueur) {
                hasJoueur = true;
            }
        }

        if (hasCaisse && hasCible) {
            return '*';
        }

        if (hasJoueur && hasCible) {
            return '+';
        }

        for (Element element : elementList)
        {
            lastElement = element;
        }
        switch (lastElement.getElement()) {
            case mur:
                return '#';
            case cible:
                return '.';
            case caisse:
                return '$';
            case joueur:
                return '@';
            default:
                return ' ';
        }
    }

    public Grid4Design open(File file) {
        int numRows = 0;
        int numCols = 0;

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    numRows++;
                    if (line.length() > numCols) {
                        numCols = line.length();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Grid4Design newGird = new Grid4Design(numCols,numRows);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    char symbol = line.charAt(col);
                    Set<Element> elementTypes = getElementForSymbol(symbol);
                    for (Element elementType: elementTypes){
                        newGird.play(row,col,elementType.getElement());
                    }


                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newGird;
    }





    private Set<Element> getElementForSymbol(char symbol) {
        Set<Element> elementSet = new TreeSet<>();
        switch (symbol) {
            case '#':
                elementSet.add(new Mur());
                break;
            case '.':
                elementSet.add(new Cible());
                break;
            case '$':
                elementSet.add(new Caisse(++cpt));
                break;
            case '@':
                elementSet.add(new Joueur());
                break;
            case '*':
                elementSet.add(new Cible());
                elementSet.add(new Caisse(++cpt));
                break;
            case '+':
                elementSet.add(new Cible());
                elementSet.add(new Joueur());
                break;
            default:
                break;
        }
        return elementSet;
    }

    public ElementType getElementTypeAt(int line, int col) {
        return getMatrix()[line][col].getElement().getElement();
    }



//    public Cell[][] getMatrix() {
//        return matrix;
//    }
}
