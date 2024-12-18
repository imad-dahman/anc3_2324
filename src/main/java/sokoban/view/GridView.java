package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

 abstract class GridView extends GridPane {

    private   final int PADDING = 20;

    private     int GRID_WIDTH ;

    private     int GRID_HEIGHT ;


    GridView( DoubleBinding gridWidth,DoubleBinding gridHeight) {

      //  setStyle("-fx-background-color: lightgrey");





        setGridLinesVisible(false);
        setPadding(new Insets(PADDING));





    }

     public int getPADDING() {
         return PADDING;
     }

     public int getGRID_WIDTH() {
         return GRID_WIDTH;
     }

     public int getGRID_HEIGHT() {
         return GRID_HEIGHT;
     }

     public void setGRID_WIDTH(int GRID_WIDTH) {
         this.GRID_WIDTH = GRID_WIDTH;
     }

     public void setGRID_HEIGHT(int GRID_HEIGHT) {
         this.GRID_HEIGHT = GRID_HEIGHT;
     }
 }
