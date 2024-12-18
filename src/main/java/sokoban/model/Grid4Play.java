package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;


import java.util.*;

public class Grid4Play extends Grid{

    private final Map<Caisse,Integer> listCaisses = new HashMap<>();
    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;
    private final BooleanProperty mushroomExisteProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty cibleExisteProperty = new SimpleBooleanProperty(false);




    private final BooleanProperty moveProperty = new SimpleBooleanProperty(false) ;

    private final IntegerProperty totalPushedOnTargetProperty = new SimpleIntegerProperty(0);
    public final IntegerProperty movesCounterProperty = new SimpleIntegerProperty(0);

    private final LongBinding numberOfBallsInGrid;
    public final LongBinding numberOfBoxOnCible;
    private Mushroom mushroom;
    private BooleanProperty ExistMushroom = new SimpleBooleanProperty(false);
    private int mushroomLine;
    private int mushroomCol;
    private final BooleanProperty mushroomVisible = new SimpleBooleanProperty(false);
    private int compteur=0;
    private int comptecible = 0;

    private int idMur=0;
    private HashMap<Integer,Mur> listMur = new HashMap<>();
    private HashMap<Integer,Cible> lisCible = new HashMap<>();

    public Grid4Play() {
        super();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        numberOfBallsInGrid = Bindings.createLongBinding(() -> Arrays
                .stream(getMatrix())
                .flatMap(Arrays::stream)
                .filter(Cell::isCible)
                .count());
        numberOfBoxOnCible = Bindings.createLongBinding(() -> Arrays
                .stream(getMatrix())
                .flatMap(Arrays::stream)
                .filter(Cell::hasballAndBox)
                .count());
    }


    public Grid4Play(int width, int height) {
        super(width, height);
        configListners();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        numberOfBallsInGrid = Bindings.createLongBinding(() -> Arrays
                .stream(getMatrix())
                .flatMap(Arrays::stream)
                .filter(Cell::isCible)
                .count());
        numberOfBoxOnCible = Bindings.createLongBinding(() -> Arrays
                .stream(getMatrix())
                .flatMap(Arrays::stream)
                .filter(Cell::hasballAndBox)
                .count());

    }
    public Grid4Play(Grid4Design design) {
        super(design.getGridColumnsProperty().get(), design.getGridRowsProperty().get());
        configListners();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();

        Cell[][] designMatrix = design.getMatrix();
        Cell[][] playMatrix = new Cell4Play[design.getGridRowsProperty().get()][design.getGridColumnsProperty().get()];
        for (int i = 0; i < getGridRowsProperty().get(); i++) {
            for (int j = 0; j < getGridColumnsProperty().get(); j++) {
                playMatrix[i][j] = new Cell4Play();
                for (Element element : designMatrix[i][j].getValue()) {
                    if (element.getElement() == ElementType.caisse)
                        playMatrix[i][j].addElement(new Caisse(++compteur));
                    if(element.getElement() == ElementType.cible){
                        playMatrix[i][j].addElement(new Cible(++comptecible));
                        cibleExisteProperty.set(true);
                    }
//                    if (element.getElement() == ElementType.mur)
//                    {
//                        playMatrix[i][j].addElement(new Mur());
//                        listMur.put(++idMur,new Mur(i,j));
//
//                    }
                    else
                        playMatrix[i][j].addElement(element);
                }
            }
        }
        numberOfBallsInGrid = Bindings.createLongBinding(() -> Arrays
                .stream(getMatrix())
                .flatMap(Arrays::stream)
                .filter(Cell::isCible)
                .count());

//        numberOfBoxOnCible = Bindings.createLongBinding(() -> Arrays
//                .stream(getMatrix())
//                .flatMap(Arrays::stream)
//                .filter(Cell::hasballAndBox)
//                .count());
        numberOfBoxOnCible = Bindings.createLongBinding(() -> Arrays
                .stream(getMatrix())
                .flatMap(Arrays::stream)
                .filter(Cell::hasMatchingBoxAndTarget)
                .count());

//        int i=0;
//
//        do {
//            Mur changed =   listMur.get(GetRandom(listMur.size()));
//            playMatrix[changed.getX()][changed.getY()].removeElement(changed);
//            playMatrix[changed.getX()][changed.getY()].addElement(new MurTraversable());
//            i++;
//        }while ( i < listMur.size()/10);




        setMatrix(playMatrix);
    }
    public void changeTargetNumberSequentially() {
        int targetNumber = 1; // Initialiser le numéro de la première cible

        for (int i = 0; i < getGridRowsProperty().get(); i++) {
            for (int j = 0; j < getGridColumnsProperty().get(); j++) {
                if (getMatrix()[i][j].getElement().getElement() == ElementType.cible) {
                    Cible target = (Cible) getMatrix()[i][j].getElement();
                    target.setId(targetNumber); // Attribuer le numéro à la cible
                    targetNumber++; // Passer au numéro suivant pour la prochaine cible
                }
            }
        }
    }

    public boolean containsTarget() {
        return cibleExisteProperty.get();
    }







    public LongBinding ballCounterProperty() {
        System.out.println(numberOfBallsInGrid.get());
        return numberOfBallsInGrid;
    }


    @Override
    void play(int line, int col, ElementType elementTypeSelected) {

    }
    private int generateRandomComptecible(int max) {
        Random random = new Random();
        return random.nextInt(max) + 1; // Génère un nombre aléatoire entre 1 et max (inclus)
    }



    private int GetRandom(int nbrMur)
    {
        int max = nbrMur;
        int min = 1;
        int range = max - min + 1;

            int rand = (int)(Math.random() * range) + min;

            return rand;
    }



    public boolean moveCrates(int x, int y, int xChange, int yChange) {
        if (x + xChange < 0 || x + xChange >= getGridRowsProperty().get() || y + yChange < 0 || y + yChange >= getGridColumnsProperty().get()) {
            return false;
        }
        Cell currentCell = getMatrix()[x][y];
        Cell nextCell = getMatrix()[x + xChange][y + yChange];
        if ((!nextCell.isEmpty() &&
                !nextCell.isCible()
            &&
                !nextCell.isCible() && !nextCell.isBox()
                ||
                currentCell.isBox() && nextCell.isBox()) && !currentCell.isCible() && nextCell.getElement().getElement()!=ElementType.mur
                ||
                currentCell.hasballAndBox() && nextCell.hasballAndBox()
                ||
                currentCell.isBox() && nextCell.getElement().getElement()==ElementType.mur
                ||
                currentCell.hasballAndBox() && nextCell.isBox()
        ) {
            return false;
        }
        if (currentCell.getElement().getElement() == ElementType.caisse && !nextCell.isBox() || currentCell.getElement().getElement() == ElementType.cible && !isGameCompleted() && !nextCell.isBox() || nextCell.isMurTraversable() ) {
           if (currentCell.getCaisse()!=null)
           {
               Caisse caisse = (Caisse) currentCell.getCaisse();
               currentCell.removeElement(caisse);
               nextCell.addElement(caisse);
           }
            return true;

        }

        numberOfBoxOnCible.invalidate();
        return true;
    }


    public boolean moveCrate(int x, int y, int xChange, int yChange) {

        Cell currentCell = getMatrix()[x][y];
        Cell nextCell = getMatrix()[xChange][yChange];
            if (currentCell.getCaisse()!=null)
            {
                Caisse caisse = (Caisse) currentCell.getCaisse();
                currentCell.removeElement(caisse);
                nextCell.addElement(caisse);
            }



        numberOfBoxOnCible.invalidate();
        return true;

    }
    public boolean moveCratee(int x, int y, int xChange, int yChange) {
        Cell currentCell = getMatrix()[x][y];
        Cell nextCell = getMatrix()[x + xChange][y + yChange];
        if (currentCell.getCaisse() != null) {
            Caisse caisse = (Caisse) currentCell.getCaisse();

            currentCell.removeElement(caisse);
            nextCell.addElement(caisse);
        }

        numberOfBoxOnCible.invalidate();
        return true;
    }


    public boolean isValidToMoveUp() {
        int x = getJoueur().getXPosition();
        int y = getJoueur().getyPosition();

        if (x <= 0) {
            return false;
        }
        if (getMatrix()[x - 1][y].getElement().getElement() == ElementType.caisse ||( getMatrix()[x - 1][y].getElement().getElement() == ElementType.cible && !isGameCompleted())){
            return moveCrates(x - 1, y, -1, 0);
        }
        return getMatrix()[x - 1][y].getElement().getElement() != ElementType.mur;
    }

    public boolean isValidToMoveDown() {
        int x = getJoueur().getXPosition();
        int y = getJoueur().getyPosition();
        if (x >= getGridRowsProperty().get() - 1) {
            return false;
        }
        if (getMatrix()[x + 1][y].getElement().getElement() == ElementType.caisse|| (getMatrix()[x +1][y].getElement().getElement() == ElementType.cible && !isGameCompleted())) {
            return moveCrates(x + 1, y, 1, 0);
        }
        return getMatrix()[x + 1][y].getElement().getElement() != ElementType.mur;
    }

    public boolean isValidToMoveLeft() {
            int x = getJoueur().getXPosition();
            int y = getJoueur().getyPosition();
        if (y <= 0) {
            return false;
        }
        if (getMatrix()[x][y - 1].getElement().getElement() == ElementType.caisse|| (getMatrix()[x][y - 1].getElement().getElement() == ElementType.cible && !isGameCompleted())) {
            return moveCrates(x, y - 1, 0, -1);
        }
        return getMatrix()[x][y - 1].getElement().getElement() != ElementType.mur;
    }

    public boolean isValidToMoveRight() {
        int x = getJoueur().getXPosition();
        int y = getJoueur().getyPosition();
        if (y >= getGridColumnsProperty().get() - 1) {
            return false;
        }
        if (getMatrix()[x][y + 1].getElement().getElement() == ElementType.caisse||(getMatrix()[x][y + 1].getElement().getElement() == ElementType.cible && !isGameCompleted())) {
            return moveCrates(x, y + 1, 0, 1);
        }
        return getMatrix()[x][y + 1].getElement().getElement() != ElementType.mur;
    }




    void configListners() {
    getJoueur().xPositionProperty().addListener((obs, old, newVal) -> {
        int oldX = old.intValue();
        int newY = getJoueur().getyPosition();
        if (isValidPosition(newVal.intValue(), newY) && isValidPosition(oldX, newY)) {
            getMatrix()[oldX][newY].removeElement(getJoueur());
            getMatrix()[newVal.intValue()][newY].addElement(getJoueur());
        }
    });

    getJoueur().yPositionProperty().addListener((obs, old, newVal) -> {
        int newX = getJoueur().getXPosition();
        int oldY = old.intValue();
        if (isValidPosition(newX, newVal.intValue()) && isValidPosition(newX, oldY)) {
            getMatrix()[newX][oldY].removeElement(getJoueur());
            getMatrix()[newX][newVal.intValue()].addElement(getJoueur());
        }
    });
    }

    boolean isValidPosition(int x, int y) {
        return x >= 0 && x < getGridRowsProperty().get() && y >= 0 && y < getGridColumnsProperty().get();
    }



public void moveDown() {

    if (isValidToMoveDown() && !isGameCompleted()) {
        int oldPlayerLine = getJoueur().getXPosition();
        int oldPlayerCol = getJoueur().getyPosition();
        int newPlayerLine = oldPlayerLine + 1;
        int newPlayerCol = oldPlayerCol;
        MoveCommand moveCommand = new MoveCommand();

        if (newPlayerLine+1<this.getGridRowsProperty().get())
        {
            if (this.getMatrix()[newPlayerLine+1][newPlayerCol].getElement().getElement() == ElementType.caisse) {
                int dx = newPlayerLine - oldPlayerLine;
                int dy = newPlayerCol - oldPlayerCol;
                int oldCrateLine = newPlayerLine;
                int oldCrateCol = newPlayerCol;
                int newCrateLine = newPlayerLine + dx;
                int newCrateCol = newPlayerCol + dy;

                moveCommand = new MoveCommand(this, oldPlayerLine, oldPlayerCol, newPlayerLine, newPlayerCol, oldCrateLine, oldCrateCol, newCrateLine, newCrateCol);
            } else {
                moveCommand = new MoveCommand(this, oldPlayerLine, oldPlayerCol, newPlayerLine, newPlayerCol);
            }
        }
        else
        {
            if (this.getMatrix()[newPlayerLine][newPlayerCol].getElement().getElement() == ElementType.caisse) {
                int dx = newPlayerLine - oldPlayerLine;
                int dy = newPlayerCol - oldPlayerCol;
                int oldCrateLine = newPlayerLine;
                int oldCrateCol = newPlayerCol;
                int newCrateLine = newPlayerLine + dx;
                int newCrateCol = newPlayerCol + dy;

                moveCommand = new MoveCommand(this, oldPlayerLine, oldPlayerCol, newPlayerLine, newPlayerCol, oldCrateLine, oldCrateCol, newCrateLine, newCrateCol);
            } else {
                moveCommand = new MoveCommand(this, oldPlayerLine, oldPlayerCol, newPlayerLine, newPlayerCol);
            }
        }


        addCommand(moveCommand);
        numberOfBoxOnCible.invalidate();
        if (!isGameCompleted()) {
            movesCounterProperty.set(movesCounterProperty.get() + 1);
        }
    }
}

    public void moveUp() {
    if (isValidToMoveUp() && !isGameCompleted()) {
        int oldPlayerLine = getJoueur().getXPosition();
        int oldPlayerCol = getJoueur().getyPosition();
        int newPlayerLine = oldPlayerLine - 1;
        int newPlayerCol = oldPlayerCol;

        MoveCommand moveCommand = new MoveCommand();
        if (newPlayerLine-1>=0)
        {
            if (this.getMatrix()[newPlayerLine-1][newPlayerCol].getElement().getElement() == ElementType.caisse) {
                int dx = newPlayerLine - oldPlayerLine;
                int dy = newPlayerCol - oldPlayerCol;
                int oldCrateLine = newPlayerLine;
                int oldCrateCol = newPlayerCol;
                int newCrateLine = newPlayerLine + dx;
                int newCrateCol = newPlayerCol + dy;
                moveCommand = new MoveCommand(this, oldPlayerLine, oldPlayerCol, newPlayerLine, newPlayerCol, oldCrateLine, oldCrateCol, newCrateLine, newCrateCol);
            } else {
                moveCommand = new MoveCommand(this, oldPlayerLine, oldPlayerCol, newPlayerLine, newPlayerCol);
            }
        }
        else
        {
            if (this.getMatrix()[newPlayerLine][newPlayerCol].getElement().getElement() == ElementType.caisse) {
                int dx = newPlayerLine - oldPlayerLine;
                int dy = newPlayerCol - oldPlayerCol;
                int oldCrateLine = newPlayerLine;
                int oldCrateCol = newPlayerCol;
                int newCrateLine = newPlayerLine + dx;
                int newCrateCol = newPlayerCol + dy;
                moveCommand = new MoveCommand(this, oldPlayerLine, oldPlayerCol, newPlayerLine, newPlayerCol, oldCrateLine, oldCrateCol, newCrateLine, newCrateCol);
            } else {
                moveCommand = new MoveCommand(this, oldPlayerLine, oldPlayerCol, newPlayerLine, newPlayerCol);
            }
        }


        addCommand(moveCommand);
        numberOfBoxOnCible.invalidate();
        if (!isGameCompleted()) {
            movesCounterProperty.set(movesCounterProperty.get() + 1);
        }
    }
}


    public void moveRight() {
        if (isValidToMoveRight() && !isGameCompleted()) {
            int oldPlayerLine = getJoueur().getXPosition();
            int oldPlayerCol = getJoueur().getyPosition();
            int newPlayerLine = oldPlayerLine;
            int newPlayerCol = oldPlayerCol + 1;

            MoveCommand moveCommand = new MoveCommand();
            if (newPlayerCol+1 < getGridColumnsProperty().get())
            {
                if (this.getMatrix()[newPlayerLine][newPlayerCol+1].getElement().getElement() == ElementType.caisse) {
                    int dx = newPlayerLine - oldPlayerLine;
                    int dy = newPlayerCol - oldPlayerCol ;
                    int oldCrateLine = newPlayerLine;
                    int oldCrateCol = newPlayerCol;
                    int newCrateLine = newPlayerLine + dx;
                    int newCrateCol = newPlayerCol + dy;

                    moveCommand = new MoveCommand(this, oldPlayerLine, oldPlayerCol, newPlayerLine, newPlayerCol, oldCrateLine, oldCrateCol, newCrateLine, newCrateCol);
                } else {
                    moveCommand = new MoveCommand(this, oldPlayerLine, oldPlayerCol, newPlayerLine, newPlayerCol);
                }
            }
            else
            {
                if (this.getMatrix()[newPlayerLine][newPlayerCol].getElement().getElement() == ElementType.caisse) {
                    int dx = newPlayerLine - oldPlayerLine;
                    int dy = newPlayerCol - oldPlayerCol ;
                    int oldCrateLine = newPlayerLine;
                    int oldCrateCol = newPlayerCol;
                    int newCrateLine = newPlayerLine + dx;
                    int newCrateCol = newPlayerCol + dy;

                    moveCommand = new MoveCommand(this, oldPlayerLine, oldPlayerCol, newPlayerLine, newPlayerCol, oldCrateLine, oldCrateCol, newCrateLine, newCrateCol);
                } else {
                    moveCommand = new MoveCommand(this, oldPlayerLine, oldPlayerCol, newPlayerLine, newPlayerCol);
                }
            }


            addCommand(moveCommand);


            numberOfBoxOnCible.invalidate();
            if (!isGameCompleted()) {
                movesCounterProperty.set(movesCounterProperty.get() + 1);
            }
        }
    }

    public void moveLeft() {
        if (isValidToMoveLeft() && !isGameCompleted()) {
            int oldPlayerLine = getJoueur().getXPosition();
            int oldPlayerCol = getJoueur().getyPosition();
            int newPlayerLine = oldPlayerLine;
            int newPlayerCol = oldPlayerCol - 1;


            MoveCommand moveCommand = new MoveCommand();
            if (newPlayerCol-1>=0)
            {
                if (this.getMatrix()[newPlayerLine][newPlayerCol-1].getElement().getElement() == ElementType.caisse) {
                    int dx = newPlayerLine - oldPlayerLine;
                    int dy = newPlayerCol - oldPlayerCol;
                    int oldCrateLine = newPlayerLine;
                    int oldCrateCol = newPlayerCol;
                    int newCrateLine = newPlayerLine + dx;
                    int newCrateCol = newPlayerCol + dy;

                    moveCommand = new MoveCommand(this, oldPlayerLine, oldPlayerCol, newPlayerLine, newPlayerCol, oldCrateLine, oldCrateCol, newCrateLine, newCrateCol);
                } else {
                    moveCommand = new MoveCommand(this, oldPlayerLine, oldPlayerCol, newPlayerLine, newPlayerCol);
                }
            }
            else
            {
                if (this.getMatrix()[newPlayerLine][newPlayerCol].getElement().getElement() == ElementType.caisse) {
                    int dx = newPlayerLine - oldPlayerLine;
                    int dy = newPlayerCol - oldPlayerCol;
                    int oldCrateLine = newPlayerLine;
                    int oldCrateCol = newPlayerCol;
                    int newCrateLine = newPlayerLine + dx;
                    int newCrateCol = newPlayerCol + dy;

                    moveCommand = new MoveCommand(this, oldPlayerLine, oldPlayerCol, newPlayerLine, newPlayerCol, oldCrateLine, oldCrateCol, newCrateLine, newCrateCol);
                } else {
                    moveCommand = new MoveCommand(this, oldPlayerLine, oldPlayerCol, newPlayerLine, newPlayerCol);
                }
            }


            addCommand(moveCommand);

            numberOfBoxOnCible.invalidate();
            if (!isGameCompleted()) {
                movesCounterProperty.set(movesCounterProperty.get() + 1);
            }
        }
    }




    public IntegerProperty movesCounterProperty() {
        return movesCounterProperty;
    }
    public BooleanProperty getMoveProperty(){
        return moveProperty;
    }

    public boolean isGameCompleted() {
        boolean gameCompleted = numberOfBallsInGrid.getValue() == numberOfBoxOnCible.getValue();
        if (gameCompleted) {
            moveProperty.set(true);
        }

        return gameCompleted;
    }


    public LongBinding totalPushedOnTargetProperty() {
        return numberOfBoxOnCible;
    }


    private void checkTarget(int row, int col) {
        if (row >= 0 && row < getGridRowsProperty().get() && col >= 0 && col < getGridColumnsProperty().get() && getMatrix()[row][col] != null && getMatrix()[row][col].getElement() != null) {
            if (getMatrix()[row][col].getElement().getElement() == ElementType.cible) {
                for (Element element : getMatrix()[row][col].getValue()) {
                    if (element.getElement() == ElementType.caisse && !getMoveProperty().get()) {
                        Caisse caisse = (Caisse) element;
                        if (!listCaisses.containsValue(caisse.getId())) {
                            listCaisses.put(caisse, caisse.getId());
                            totalPushedOnTargetProperty.set(totalPushedOnTargetProperty.get() + 1);
                        }
                    }
                }
            }
        }
    }
    public void undo() {
        if (!undoStack.isEmpty()) {
            movesCounterProperty.set(movesCounterProperty.get()+5);
            Command command = undoStack.pop();
            command.unexecute();
            redoStack.push(command);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            movesCounterProperty.set(movesCounterProperty.get()+1);
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }

    }





    public void addCommand(Command command){
        command.execute();
        undoStack.add(command);
        redoStack.clear();
        redoStack.addAll(undoStack);
    }

    public BooleanProperty mushroomVisibleProperty() {
        return mushroomVisible;
    }

    public void setMushroomVisible(boolean isVisible) {
        mushroomVisible.set(isVisible);

    }


    private int mushroomLine(int height) {
        Random random = new Random();
        return random.nextInt(height);
    }

    private int mushroomCol(int width) {
        Random random = new Random();
        return random.nextInt(width);
    }


    public int getMushroomLine() {
        return mushroomLine;
    }


    public int getMushroomCol() {
        return mushroomCol;
    }

    public void setMushroomLine(int mushroomLine) {
        this.mushroomLine = mushroomLine;
    }

    public void setMushroomCol(int mushroomCol) {
        this.mushroomCol = mushroomCol;
    }

    public void moveMushroom() {
            getMatrix()[mushroom.getX()][mushroom.getY()].removeElement(mushroom);
            mushroom.setX(mushroomLine(getGridRowsProperty().get()));
            mushroom.setY(mushroomCol(getGridRowsProperty().get()));
            getMatrix()[mushroom.getX()][mushroom.getY()].addElement(mushroom);

        movesCounterProperty.set(movesCounterProperty.get()+20);
        mushroomVisible.set(false);

        this.mushroomLine = mushroomLine(getGridRowsProperty().get());
        this.mushroomCol = mushroomCol(getGridColumnsProperty().get());
    }







    public void addMush() {

        if (!ExistMushroom.get())
        mushroom = new Mushroom(mushroomLine(getGridRowsProperty().get()),mushroomCol(getGridRowsProperty().get()));


        if(!mushroomVisible.get()){

            getMatrix()[mushroom.getX()][mushroom.getY()].addElement(mushroom);
            ExistMushroom.set(true);
                movesCounterProperty.set(movesCounterProperty.get()+10);


            }
            else {
            getMatrix()[mushroom.getX()][mushroom.getY()].removeElement(mushroom);

            }



    }







}
