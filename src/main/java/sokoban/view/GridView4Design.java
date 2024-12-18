package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import sokoban.viewmodel.GridViewModel4Design;

 class GridView4Design extends GridView{



    public GridView4Design(GridViewModel4Design gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        super( gridWidth, gridHeight);
        setGRID_WIDTH(gridViewModel.gridWidth());
        setGRID_HEIGHT(gridViewModel.gridHeight());

        DoubleBinding cellWidth = gridWidth
                .subtract(getPADDING() * 2)
                .divide(getGRID_WIDTH());
        // Remplissage de la grille
        for (int i = 0; i < getGRID_HEIGHT() ; ++i) {
            for (int j = 0; j < getGRID_WIDTH(); ++j) {
                CellView cellView = new CellView4Design(gridViewModel.getCellViewModel(i, j), cellWidth);
                add(cellView, j, i);
            }
        }
    }
}
