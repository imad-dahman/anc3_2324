package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import sokoban.model.Element;
import sokoban.model.ElementType;
import sokoban.viewmodel.CellViewModel4Design;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

 class CellView4Design extends CellView{
    protected final CellViewModel4Design viewModel;

    private final List<ImageView> imageViewList = new ArrayList<>();

    private final static Map<ElementType, Image> imageMap  = new TreeMap<>();


    static {

        imageMap.put(ElementType.joueur,new Image("player.png"));
        imageMap.put(ElementType.terrain,new Image("ground.png"));
        imageMap.put(ElementType.cible,new Image("goal.png"));
        imageMap.put(ElementType.caisse,new Image("box.png"));
        imageMap.put(ElementType.mur,new Image("wall.png"));
    }
    public CellView4Design(CellViewModel4Design cellViewModel, DoubleBinding cellWidthProperty) {
        super(cellWidthProperty);
        viewModel = cellViewModel;
        MouseActions();
        layoutControls();
        configListners();
    }

    private void MouseActions()
    {
        this.setOnMouseClicked(e ->{
            if (e.getButton() == MouseButton.PRIMARY)
                viewModel.play();
            else
                viewModel.removeElement();

        } );


        this.setOnDragDetected(e -> {
            startFullDrag();
            e.consume();
        });

        this.setOnMouseDragOver(e -> {
            if (e.getButton() == MouseButton.PRIMARY)
                viewModel.play();
            else
                viewModel.removeElement();
            e.consume();
        });
    }

    private void layoutControls() {

        for (Element element : viewModel.valueProperty().get())
        {
            ImageView imageView = new ImageView(getImageElement(element));
            imageView.fitWidthProperty().bind(widthPropertyProperty());
            imageView.setPreserveRatio(true);
            imageViewList.add(imageView);
        }

        getChildren().addAll(imageViewList);

    }

    private void configListners()
    {
        viewModel.isListChangedProperty().addListener((obs, old, newVal)->
        {
            getChildren().removeAll(imageViewList);
            imageViewList.clear();
            for (Element element : viewModel.valueProperty().get())
            {

                ImageView imageView = new ImageView(getImageElement(element));
                imageView.fitWidthProperty().bind(widthPropertyProperty());
                imageView.setPreserveRatio(true);
                imageViewList.add(imageView);

            }
            getChildren().addAll(imageViewList);
        });
        hoverProperty().addListener((obs, oldVal, newVal) -> {
            if (imageViewList.size() > 1) {
                imageViewList.get(1).setOpacity(newVal ? 0.5 : 1.0);
            } else {
                imageViewList.get(0).setOpacity(newVal ? 0.5 : 1.0);
            }
        });

    }

    private Image getImageElement(Element elementType)
    {
        return imageMap.get(elementType.elementTypeProperty().get());
    }

}
