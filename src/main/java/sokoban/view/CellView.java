package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

public abstract class CellView extends StackPane {


    private final DoubleBinding widthProperty;





    CellView(DoubleBinding cellWidthProperty) {

        this.widthProperty = cellWidthProperty;

        setAlignment(Pos.CENTER);


        configureBindings();
    }





    private void configureBindings() {
        minWidthProperty().bind(widthProperty);
        minHeightProperty().bind(widthProperty);
    }



    public DoubleBinding widthPropertyProperty() {
        return widthProperty;
    }
}
