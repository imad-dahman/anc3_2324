package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import sokoban.model.Board4Design;
import sokoban.viewmodel.BoardViewModel4Design;

import java.io.File;

 class FileMenuView extends BorderPane {

    private final BoardViewModel4Design boardViewModel ;


    BooleanProperty isValidWidthProperty = new SimpleBooleanProperty(false);
    BooleanProperty isValidWidthSchow = new SimpleBooleanProperty(false);

    BooleanProperty isValidHeightProperty = new SimpleBooleanProperty(false);
    BooleanProperty isValidHeighthSchow = new SimpleBooleanProperty(false);
    BooleanBinding isValidHeight;
    BooleanBinding isValidWidth;

    BooleanBinding isValidInput ;


    private final Label widthErrorLabel = new Label("Width must be at least 10.");
    private final Label heightErrorLabel = new Label("Height must be at least 10.");


    TextField widthField = new TextField("15");
    TextField heightField = new TextField("10");

    FileChooser fileChooser = new FileChooser();
    FileChooser fileChooser1 = new FileChooser();


    File file ;

    private  Board4Design newBoard;

    MenuBar menuBar = new MenuBar();
    Menu fileMenu = new Menu("File");
    MenuItem newMenuItem = new MenuItem("New");
    MenuItem openMenuItem = new MenuItem("Open");
    MenuItem saveMenuItem = new MenuItem("Save");
    MenuItem exitMenuItem = new MenuItem("Exit");
    ButtonType yesButton = new ButtonType("Oui");
    ButtonType noButton = new ButtonType("Non");

    private final Stage stage;

    public FileMenuView(Stage stage, BoardViewModel4Design boardViewModel) {

        this.boardViewModel= boardViewModel;
        this.stage = stage;

        layoutControls();
        configListners();
        configBindings();
    }

   private void layoutControls()
    {
        fileMenu.getItems().addAll(newMenuItem, openMenuItem, saveMenuItem, new SeparatorMenuItem(), exitMenuItem);
        menuBar.getMenus().add(fileMenu);
        this.setTop(menuBar);

    }



         private void configListners()
    {
        newMenuItem.setOnAction(event -> handleMenuItemAction(stage, true));
        exitMenuItem.setOnAction(event -> handleMenuItemAction(stage, false));
        saveMenuItem.setOnAction(event -> handleSaveMenuItemAction(stage));

        openMenuItem.setOnAction((e->
        {
            fileChooser1.setTitle("Open Grid");
            fileChooser1.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban Board Files (*.xsb)", "*.xsb"));
            File file1 = fileChooser1.showOpenDialog(stage);
            if (file1 != null) {
                boardViewModel.open(file1);
            }
//            if (file1 !=null)
//            {
//                newBoard =   boardViewModel.open(file1);
//                BoardViewModel4Design vm  = new BoardViewModel4Design(newBoard);
//                new BoardView4Design(stage,vm);
//            }
            if (file1 != null) {
                boardViewModel.open(file1);
                newBoard = boardViewModel.getBoard();
                BoardViewModel4Design vm = new BoardViewModel4Design(newBoard);
                new BoardView4Design(stage, vm);
            }

        }));
    }
    private void handleMenuItemAction(Stage stage, boolean showNewGameDialog) {
        if (boardViewModel.GridChangedProperty().get()) {
            Alert confirmationAlert = createConfirmationAlert();
            confirmationAlert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == yesButton) {
                    saveBoardAndClose(stage, showNewGameDialog);
                } else if (buttonType == noButton) {
                    if (showNewGameDialog) {
                        showNewGameDialog(stage);
                    } else {
                        stage.close();
                    }
                }
            });
        } else {
            if (showNewGameDialog) {
                showNewGameDialog(stage);
            } else {
                stage.close();
            }
        }
    }
    private void handleSaveMenuItemAction(Stage stage) {
        saveBoardAndShowNewGameDialog();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            boardViewModel.save(file);
        }
    }
    private void saveBoardAndClose(Stage stage, boolean showNewGameDialog) {
        saveBoardAndShowNewGameDialog();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            boardViewModel.save(file);
            if (showNewGameDialog) {
                showNewGameDialog(stage);
            } else {
                stage.close();
            }
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
    private void saveBoardAndShowNewGameDialog() {
        fileChooser.setTitle("Save Grid");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban Board Files (*.xsb)", "*.xsb"));
    }



    void widthError()
    {
        BooleanBinding widthValidationBinding = Bindings.createBooleanBinding(() -> {
            String text = widthField.getText();
            if (text !=null || !text.isEmpty() || text!="")
            {
                if (text == null || text.isEmpty())
                return false;
             else {
                int value = Integer.parseInt(text);
                return value>=10 && value<=50;

            }
            }
           return true;

        }, widthField.textProperty());


        isValidWidthProperty.bind(widthValidationBinding);

        BooleanBinding widthValidationBinding1 = Bindings.createBooleanBinding(() -> {
            String text = widthField.getText();
            if (text !=null || !text.isEmpty() || text!="")
            {
                try {
                    int value = Integer.parseInt(text);
                    if (value>50)
                        return true;
                    else
                        return false;
                }catch (Exception e)
                {
                    return false;
                }

            }
            return false;


        }, widthField.textProperty());

        isValidWidthSchow.bind(widthValidationBinding1);

        StringExpression messageValid = Bindings.when(isValidWidthSchow)
                .then("width must be at most 50")
                .otherwise("width must be at least 10");

        widthErrorLabel.textProperty().bind(messageValid);
    }

    void heightError()
    {
        BooleanBinding heightValidationBinding = Bindings.createBooleanBinding(() -> {
            String text = heightField.getText();
            if (text == null || text.isEmpty()) {
                return false;
            } else {
                int value = Integer.parseInt(text);
                return value>=10 && value<=50;
            }

        }, heightField.textProperty());


        isValidHeightProperty.bind(heightValidationBinding);

        BooleanBinding heightValidationBinding1 = Bindings.createBooleanBinding(() -> {
            String text = heightField.getText();
            try {
                int value = Integer.parseInt(text);
                if (value>50) {
                    return true;
                } else {
                    return false;
                }
            }catch (Exception e)
            {
                return false;

            }


        }, heightField.textProperty());

        isValidHeighthSchow.bind(heightValidationBinding1);

        StringExpression messageValid = Bindings.when(isValidHeighthSchow)
                .then("height must be at most 50")
                .otherwise("height must be at least 10");

        heightErrorLabel.textProperty().bind(messageValid);
    }

    void configBindings()
    {

        isValidWidth = widthField.textProperty().greaterThanOrEqualTo("10").and(widthField.textProperty().lessThanOrEqualTo("50"));
        isValidHeight = heightField.textProperty().greaterThanOrEqualTo("10").and(heightField.textProperty().lessThanOrEqualTo("50"));

        widthError();
        heightError();

        isValidInput = isValidWidth.and(isValidHeight);

        widthErrorLabel.visibleProperty().bind(isValidWidthProperty.not());
        widthErrorLabel.managedProperty().bind(widthErrorLabel.visibleProperty());


        heightErrorLabel.visibleProperty().bind(isValidHeightProperty.not());
        heightErrorLabel.managedProperty().bind(heightErrorLabel.visibleProperty());


    }



    private void showNewGameDialog(Stage stage) {
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Sokoban");
        dialog.setHeaderText("Give new game dimensions");

        ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().setAll(buttonTypeOK, ButtonType.CANCEL);



        int defaultWidth = 15;
        int defaultHeight = 10;
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(20, 150, 30, 30));
        gridPane.setPrefHeight(150);




        widthErrorLabel.setTextFill(Color.INDIANRED);
        heightErrorLabel.setTextFill(Color.INDIANRED);


        // Binding for OK button
        BooleanBinding invalidInput = isValidInput.not();

        Button okButton = (Button) dialog.getDialogPane().lookupButton(buttonTypeOK);
        okButton.disableProperty().bind(invalidInput);

        gridPane.add(new Label("Width:"), 0, 0);
        widthField.setText(String.valueOf(defaultWidth));
        gridPane.add(widthField, 1, 0);
        gridPane.add(widthErrorLabel, 0, 1, 2, 1);

        gridPane.add(new Label("Height:"), 0, 2);
        heightField.setText(String.valueOf(defaultHeight));
        gridPane.add(heightField, 1, 2);
        gridPane.add(heightErrorLabel, 0, 3, 2, 1);


        dialog.getDialogPane().setContent(gridPane);

        // Handle button click
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOK) {
                int width = Integer.parseInt(widthField.getText().trim());
                int height = Integer.parseInt(heightField.getText().trim());
                return new Pair<>(width, height);
            }
            return null;
        });


        dialog.showAndWait().ifPresent(result -> {
            int newWidth = result.getKey();
            int newHeight = result.getValue();

                Board4Design board = new Board4Design(newWidth,newHeight);
                BoardViewModel4Design vm = new BoardViewModel4Design(board);
                new BoardView4Design(stage, vm);


        });
    }





}
