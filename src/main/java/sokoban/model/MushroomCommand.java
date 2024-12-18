package sokoban.model;

public class MushroomCommand implements Command {
    private Grid4Play grid;
    public MushroomCommand(Grid4Play grid){
        this.grid=grid;
    }
    @Override
    public void execute() {
        int oldMushroomLine = grid.getMushroomLine();
        int oldMushroomCol = grid.getMushroomCol();
        int newMushroomLine = grid.getMushroomLine();
        int newMushroomCol = grid.getMushroomCol();


        grid.play(oldMushroomLine, oldMushroomCol, new Terrain().getElement());
        grid.play(newMushroomLine, newMushroomCol, new Mushroom().getElement());

        grid.setMushroomLine(newMushroomLine);
        grid.setMushroomCol(newMushroomCol);
    }

    @Override
    public void unexecute() {

    }
}
