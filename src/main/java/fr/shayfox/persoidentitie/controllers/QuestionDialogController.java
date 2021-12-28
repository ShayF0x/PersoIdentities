package fr.shayfox.persoidentitie.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class QuestionDialogController {

    @FXML
    public StackPane root, mainroot;
    @FXML
    public ImageView Icon;
    @FXML
    public Label Question;
    @FXML
    public Label Title;
    @FXML
    public HBox buttonlayout;
    @FXML
    public BorderPane borderpane;

    public int buttonpress;

    public void closeStage(MouseEvent event){
        Node source = (Node) event.getSource();
        buttonpress = buttonlayout.getChildren().indexOf(source);
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }





}