package sokoban.view;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public abstract class BoardView extends BorderPane {



    private int GRID_WIDTH ;


    private     final int SCENE_MIN_WIDTH = 800;
    private     final int SCENE_MIN_HEIGHT = 650;




    public BoardView(Stage primaryStage) {
    }

     abstract void start(Stage stage);



    abstract void createHeader() ;




     abstract void createGrid() ;

    public int getGRID_WIDTH() {
        return GRID_WIDTH;
    }

    public void setGRID_WIDTH(int GRID_WIDTH) {
        this.GRID_WIDTH = GRID_WIDTH;
    }

    public int getSCENE_MIN_WIDTH() {
        return SCENE_MIN_WIDTH;
    }

    public int getSCENE_MIN_HEIGHT() {
        return SCENE_MIN_HEIGHT;
    }
}
