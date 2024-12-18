package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sokoban.model.Board4Design;
import sokoban.viewmodel.BoardViewModel4Design;
import sokoban.viewmodel.BoardViewModel4Play;

import static sokoban.view.BoardView4Design.grid4Design;


public class BoardView4Play extends BoardView{

    private final BoardViewModel4Play boardViewModel4Play;
    private  final Button finishButton = new Button("Finish");

    private final Label title = new Label("Score");
    private final Label movesLabel = new Label("");
    private final Label goalsLabel = new Label("");
    private final Label winningLabel = new Label("");
    private  final Button mushroomBtn = new Button();
    private final Button murmurous = new Button("Change Target");

    private final VBox hBox = new VBox(15,finishButton,mushroomBtn, murmurous);
    private final HBox headerBox = new HBox();
    private final Stage primaryStage;
    private Board4Design board4Design;
    private BoardViewModel4Design boardVM4Design;


    public BoardView4Play(Stage primaryStage, BoardViewModel4Play boardViewModel) {
        super(primaryStage);
        this.boardViewModel4Play = boardViewModel;
       this.primaryStage = primaryStage;




        start(primaryStage);

    }

    @Override
    void start(Stage stage) {
        configMainComponents(stage);
        Scene scene = new Scene(this, getSCENE_MIN_WIDTH() , getSCENE_MIN_HEIGHT());
        stage.setScene(scene);
        stage.show();
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());
    }

    private void configMainComponents(Stage stage) {
        stage.titleProperty().bind(Bindings.when(boardViewModel4Play.GridChangedProperty())
                .then("Sokoban")
                .otherwise("Sokoban"));
        createGrid();
        createHeader();
        addFinishButton();
        manageMushroomButton();
    }

    boolean mushroomVisible = false;

    private void manageMushroomButton() {
        mushroomBtn.setFocusTraversable(false);
        mushroomBtn.textProperty().bindBidirectional(boardViewModel4Play.mushroomVisibleProperty(), new StringConverter<Boolean>() {
            @Override
            public String toString(Boolean bool) {
                return bool ? "Hide Mushroom" : "Show Mushroom";
            }

            @Override
            public Boolean fromString(String string) {
                return null;
            }
        });

        mushroomBtn.setOnAction(event -> {
            if (mushroomBtn.textProperty().get().equals("Show mushroom")) {
                boardViewModel4Play.setMushroomVisible(true);


            } else if (mushroomBtn.textProperty().get().equals("Hide mushroom")) {
                boardViewModel4Play.setMushroomVisible(false);

            }
        });


        mushroomBtn.setOnAction(event -> {
            boolean isVisible = boardViewModel4Play.mushroomVisibleProperty().get();
            boardViewModel4Play.mushroomClicked();

            boardViewModel4Play.setMushroomVisible(!isVisible);

        });

    }









    @Override
    void createHeader() {
        VBox headerBox = new VBox();
        headerBox.getStyleClass().add("header");
        headerBox.setAlignment(Pos.CENTER);

        movesLabel.textProperty().bind(boardViewModel4Play.movesCounterProperty().asString("Number of moves played: %d"));
        goalsLabel.textProperty().bind(boardViewModel4Play.totalPushedOnTargetProperty().asString("Number of goals reached: %d of " + boardViewModel4Play.goalsReachedProperty().get()));
        winningLabel.textProperty().bind(boardViewModel4Play.movesCounterProperty().asString(" You won in : %d moves, congratulations !! "));
        bindingErrors();
        headerBox.getChildren().addAll(title, movesLabel, goalsLabel,winningLabel);
        headerBox.setStyle("-fx-font-weight: bold;");
        setTop(headerBox);
    }
    void bindingErrors() {
        winningLabel.visibleProperty().bind(boardViewModel4Play.getMoveProperty());
        winningLabel.managedProperty().bind(winningLabel.visibleProperty().not());
        finishButton.setOnMouseClicked(e->
                {
                    board4Design = new Board4Design(grid4Design);
                    boardVM4Design = new BoardViewModel4Design(board4Design);
                    new BoardView4Design(this.primaryStage,boardVM4Design);
                }
                );
        murmurous.setOnMouseClicked(e-> {
            boardViewModel4Play.changeTargetNumberRandomly();
        });




    }


    @Override
     void createGrid() {
        setGRID_WIDTH(boardViewModel4Play.getGridColumnsProperty().get());
        DoubleBinding gridWidth = Bindings.createDoubleBinding(
                () -> {
                    var size = Math.min(widthProperty().get(), heightProperty().get() - headerBox.heightProperty().get());
                    return Math.floor(size / getGRID_WIDTH()) * getGRID_WIDTH();
                },
                widthProperty(),
                heightProperty()
        );

        DoubleBinding gridHeight = Bindings.createDoubleBinding(
                () -> {

                    var size = Math.min(widthProperty().get(), heightProperty().get() - headerBox.heightProperty().get());
                    return Math.floor(size / getGRID_WIDTH()) * getGRID_WIDTH();
                },
                widthProperty(),
                heightProperty()
        );


        GridView4Play gridView = new GridView4Play(boardViewModel4Play.getGridViewModel(), gridWidth,gridHeight);

        gridView.minHeightProperty().bind(gridHeight);
        gridView.minWidthProperty().bind(gridWidth);
        gridView.maxHeightProperty().bind(gridHeight);
        gridView.maxWidthProperty().bind(gridWidth);
        setCenter(gridView);
    }
    public void addFinishButton() {
        getChildren().add(hBox);
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(headerBox);
        mainLayout.setCenter(getCenter());
        mainLayout.setLeft(hBox);
        setCenter(mainLayout);
    }



}
