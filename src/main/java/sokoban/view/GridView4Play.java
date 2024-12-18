package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import sokoban.viewmodel.GridViewModel4Play;

 class GridView4Play extends GridView{

    GridView4Play(GridViewModel4Play gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        super( gridWidth, gridHeight);
        
        setGRID_WIDTH(gridViewModel.gridWidth());
        setGRID_HEIGHT(gridViewModel.gridHeight());

        DoubleBinding cellWidth = gridWidth
                .subtract(getPADDING() * 2)
                .divide(getGRID_WIDTH());


        // Remplissage de la grille
        for (int i = 0; i < getGRID_HEIGHT() ; ++i) {
            for (int j = 0; j < getGRID_WIDTH(); ++j) {
                CellView4Play cellView = new CellView4Play(gridViewModel.getCellViewModel(i, j), cellWidth);
                add(cellView, j, i);
            }
        }
    }
     private boolean mushroomVisible = false;
     public void showMushroom(Boolean hideMushroom) {
         ObservableList<Node> children = this.getChildren();
         for (int i = 0; i < children.size(); i++) {
             Node child = children.get(i);

             CellView cellView = (CellView) child;
             StackPane stackPane = (StackPane) cellView.getChildren().get(0);

             for (Node node : stackPane.getChildren()) {
                 if (node.getClass().equals(ImageView.class)) { // car il n y a pas que des ImageViews mais aussi des label pour l'id des boxs
                     ImageView imageView = (ImageView) node;
                     if (imageView.getImage() != null && imageView.getImage().getUrl().endsWith("file:src/main/resources/mushroom.png")) {
                         if (hideMushroom){
                             imageView.setOpacity(0);//l'opacité à 0 pour rendre l'imageView invisible
                             mushroomVisible = false;
                         }else{
                             imageView.setOpacity(1);//l'opacité à 1 pour rendre l'imageView visible
                             mushroomVisible = true;
                         }

                     }
                 }
             }
         }
     }

     public boolean isMushroomVisible() {
         return mushroomVisible;
     }





 }
