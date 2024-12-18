package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sokoban.model.Board4Play;
import sokoban.model.Grid4Design;
import sokoban.viewmodel.BoardViewModel4Design;
import sokoban.viewmodel.BoardViewModel4Play;

import java.io.File;

public class BoardView4Design extends BoardView{

    private final BoardViewModel4Design boardViewModel4Design;


    private final Label headerLabel = new Label("");
    private final HBox headerBox = new HBox();

    private static final Image groundImage = new Image("ground.png");
    private static final Image boxImage = new Image("box.png");
    private static final Image playerImage = new Image("player.png");
    private static final Image wallImage = new Image("wall.png");
    private static final Image goalImage = new Image("goal.png");

    private static final Button playButton = new Button("PLAY");
    ButtonType yesButton = new ButtonType("Oui");
    ButtonType noButton = new ButtonType("Non");


    StackPane groundPane = new StackPane(new ImageView(groundImage));
    StackPane boxPane = new StackPane(new ImageView(boxImage));
    StackPane playerPane = new StackPane(new ImageView(playerImage));
    StackPane wallPane = new StackPane(new ImageView(wallImage));
    StackPane goalPane = new StackPane(new ImageView(goalImage));
    VBox headerContent = new VBox();
    VBox sideBox = new VBox(10, groundPane, boxPane, playerPane, wallPane, goalPane,playButton);

    BooleanProperty isBoxPaneClicked = new SimpleBooleanProperty(false);
    BooleanProperty isPlayerPaneClicked = new SimpleBooleanProperty(false);
    BooleanProperty isWallPaneClicked = new SimpleBooleanProperty(false);
    BooleanProperty isGoalPaneClicked = new SimpleBooleanProperty(false);
    BooleanProperty isGroundPaneClicked = new SimpleBooleanProperty(false);
    private final Label title = new Label("Please correct the following error(s) :");
    private final Label playerError = new Label("+ A player is required");
    private final Label ballError = new Label("+ At least one target is required");
    private final Label boxError = new Label("+ At least one box is required");
    private final Label NumbreBoxetBall = new Label("+ Number of boxes and targets must equal");
    private final Stage primaryStage;
    private Board4Play  board4Play;
    private BoardViewModel4Play vm;

   protected static  Grid4Design grid4Design;

    public BoardView4Design(Stage primaryStage, BoardViewModel4Design boardViewModel) {
        super(primaryStage);
        this.boardViewModel4Design = boardViewModel;
        this.primaryStage = primaryStage;
        grid4Design = boardViewModel4Design.getGrid();
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
        stage.titleProperty().bind(Bindings.when(boardViewModel4Design.GridChangedProperty())
                .then("Sokoban (*)")
                .otherwise("Sokoban"));
        configListners(stage);
        configBindings();
        createHeader();
        createErreur();
        createGrid();
        createSide();

        createFile(stage);
        configBorderPan();
    }

    @Override
    void createHeader() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(headerBox);
        mainLayout.setCenter(getCenter());
        mainLayout.setLeft(sideBox);
        setCenter(mainLayout);
        headerLabel.textProperty().bind(boardViewModel4Design.filledCellsCountProperty()
                .asString("Number of filled cells: %d of " + boardViewModel4Design.maxFilledCells().get()));
        headerLabel.getStyleClass().add("header");
        headerLabel.setStyle("-fx-font-weight: bold;");
        headerBox.getChildren().add(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        setTop(headerBox);
    }

    @Override
     void createGrid() {
        setGRID_WIDTH(boardViewModel4Design.getGridColumnsProperty().get());
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


        GridView gridView = new GridView4Design(boardViewModel4Design.getGridViewModel(), gridWidth,gridHeight);

        gridView.minHeightProperty().bind(gridHeight);
        gridView.minWidthProperty().bind(gridWidth);
        gridView.maxHeightProperty().bind(gridHeight);
        gridView.maxWidthProperty().bind(gridWidth);
        setCenter(gridView);


    }

    private void createFile(Stage stage){
        FileMenuView fileMenuView = new FileMenuView(stage,boardViewModel4Design);
        setTop(fileMenuView);
    }

    void configListners(Stage stage)
    {
        playerPane.setOnMouseClicked(e->{
            boardViewModel4Design.teleport();
            updateClickStatus(isPlayerPaneClicked);
        });
        wallPane.setOnMouseClicked(e->
        {
            boardViewModel4Design.putMur();
            updateClickStatus(isWallPaneClicked);
        });
        goalPane.setOnMouseClicked(e->
        {
            boardViewModel4Design.putCible();
            updateClickStatus(isGoalPaneClicked);
        });
        groundPane.setOnMouseClicked(e->
        {
            boardViewModel4Design.putTerrain();
            updateClickStatus(isGroundPaneClicked);
        });
        boxPane.setOnMouseClicked(e->
        {
            boardViewModel4Design.putCaisse();
            updateClickStatus(isBoxPaneClicked);
        });
        playButton.setOnMouseClicked(e->
        {
            handlePlayButtonClick(stage);
        });
    }

    FileChooser fileChooser = new FileChooser();
    private void handlePlayButtonClick(Stage stage) {
        Alert confirmationAlert = createConfirmationAlert();

        confirmationAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == yesButton) {
                saveBoardAndShowNewGameDialog(stage);
            } else if (buttonType == noButton) {
                openBoard4Play();
            }
        });

    }



    private  void openBoard4Play(){
        board4Play  = new Board4Play(grid4Design);
        vm = new BoardViewModel4Play(board4Play);
        new BoardView4Play(this.primaryStage, vm);
    }
    private void saveBoardAndShowNewGameDialog(Stage stage) {
        fileChooser.setTitle("Save Grid");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban Board Files (*.xsb)", "*.xsb"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            boardViewModel4Design.save(file);
            openBoard4Play();
        }
    }
    private Alert createConfirmationAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Your board has been modified");
        alert.setContentText("Do you want to save your changes ?");
        ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);
        return alert;
    }


    void configBindings()
    {

        boxPane.styleProperty().bind(Bindings.when(isBoxPaneClicked)
                .then("-fx-border-color: blue; -fx-border-width: 2;")
                .otherwise("-fx-border-color: white; -fx-border-width: 2;"));

        playerPane.styleProperty().bind(Bindings.when(isPlayerPaneClicked)
                .then("-fx-border-color: blue; -fx-border-width: 2;")
                .otherwise("-fx-border-color: white; -fx-border-width: 2;"));
        goalPane.styleProperty().bind(Bindings.when(isGoalPaneClicked)
                .then("-fx-border-color: blue; -fx-border-width: 2;")
                .otherwise("-fx-border-color: white; -fx-border-width: 2;"));
        wallPane.styleProperty().bind(Bindings.when(isWallPaneClicked)
                .then("-fx-border-color: blue; -fx-border-width: 2;")
                .otherwise("-fx-border-color: white; -fx-border-width: 2;"));
        groundPane.styleProperty().bind(Bindings.when(isGroundPaneClicked)
                .then("-fx-border-color: blue; -fx-border-width: 2;")
                .otherwise("-fx-border-color: white; -fx-border-width: 2;"));

    }


    private void updateClickStatus(BooleanProperty clickedProperty) {
        isPlayerPaneClicked.set(clickedProperty == isPlayerPaneClicked);
        isBoxPaneClicked.set(clickedProperty == isBoxPaneClicked);
        isGroundPaneClicked.set(clickedProperty == isGroundPaneClicked);
        isWallPaneClicked.set(clickedProperty == isWallPaneClicked);
        isGoalPaneClicked.set(clickedProperty == isGoalPaneClicked);
    }

    void bindingErrors()
    {
        playerError.visibleProperty().bind(boardViewModel4Design.playerAddedProperty().not());
        playerError.managedProperty().bind(playerError.visibleProperty());
        ballError.visibleProperty().bind(boardViewModel4Design.ballAddedProperty().not());
        ballError.managedProperty().bind(ballError.visibleProperty());
        boxError.visibleProperty().bind(boardViewModel4Design.boxAddedProperty().not());
        boxError.managedProperty().bind(boxError.visibleProperty());
        NumbreBoxetBall.visibleProperty().bind(boardViewModel4Design.NumerAddedProperty().not());
        NumbreBoxetBall.managedProperty().bind(NumbreBoxetBall.visibleProperty());
        headerBox.getChildren().add(headerContent);
        title.visibleProperty().bind(
                boardViewModel4Design.playerAddedProperty()
                        .and(boardViewModel4Design.ballAddedProperty())
                        .and(boardViewModel4Design.boxAddedProperty())
                        .and(boardViewModel4Design.NumerAddedProperty())
                        .not()
        );

        playButton.disableProperty().bind(title.visibleProperty());
    }

    void createErreur(){

        headerContent.getChildren().addAll(headerLabel,title, playerError,ballError,boxError,NumbreBoxetBall);
        playerError.setTextFill(Color.INDIANRED);
        title.setTextFill(Color.INDIANRED);
        ballError.setTextFill(Color.INDIANRED);
        boxError.setTextFill(Color.INDIANRED);
        NumbreBoxetBall.setTextFill(Color.INDIANRED);
        NumbreBoxetBall.setVisible(false);
        NumbreBoxetBall.setManaged(false);
        VBox.setMargin(playerError, new Insets(5, 0, 0, 0));

        bindingErrors();

    }
    void createSide()
    {
        this.getChildren().add(sideBox);
        sideBox.setPadding(new Insets(20));

    }
    void configBorderPan(){
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(headerBox);
        mainLayout.setCenter(getCenter());
        mainLayout.setLeft(sideBox);
        setCenter(mainLayout);
    }



}
