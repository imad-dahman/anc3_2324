package sokoban.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class MoveCommand implements Command {
    private  Grid4Play grid;
    private int oldPlayerCol;
    private int oldPlayerLine;
    private int newPlayerLine;
    private int newPlayerCol;
    private int oldCrateLine;
    private int oldCrateCol;
    private int newCrateLine;
    private int newCrateCol;
    private boolean cratePushed = false;
    private IntegerProperty moves = new SimpleIntegerProperty();

    public MoveCommand()
    {}
    public MoveCommand(Grid4Play grid, int oldPlayerLine, int oldPlayerCol, int newPlayerLine, int newPlayerCol) {
        this.grid = grid;
        this.oldPlayerLine = oldPlayerLine;
        this.oldPlayerCol = oldPlayerCol;
        this.newPlayerLine = newPlayerLine;
        this.newPlayerCol = newPlayerCol;
        this.cratePushed = false;
    }

    public MoveCommand(Grid4Play grid, int oldPlayerLine, int oldPlayerCol, int newPlayerLine, int newPlayerCol, int oldCrateLine, int oldCrateCol, int newCrateLine, int newCrateCol) {
        this.grid = grid;
        this.oldPlayerLine = oldPlayerLine;
        this.oldPlayerCol = oldPlayerCol;
        this.newPlayerLine = newPlayerLine;
        this.newPlayerCol = newPlayerCol;
        this.oldCrateLine = oldCrateLine;
        this.oldCrateCol = oldCrateCol;
        this.newCrateLine = newCrateLine;
        this.newCrateCol = newCrateCol;
        this.cratePushed = true;
    }

    @Override
    public void execute() {


        grid.getJoueur().setxPosition(newPlayerLine);
        grid.getJoueur().setyPosition(newPlayerCol);

        if (cratePushed) {
            grid.moveCratee(oldCrateLine, oldCrateCol, newCrateLine - oldCrateLine, newCrateCol - oldCrateCol);
        }
    }


    @Override
    public void unexecute() {
        grid.getJoueur().setxPosition(oldPlayerLine);
        grid.getJoueur().setyPosition(oldPlayerCol);

        if (cratePushed) {
            grid.moveCrate(newCrateLine, newCrateCol, oldCrateLine , oldCrateCol );
        }
    }










}
