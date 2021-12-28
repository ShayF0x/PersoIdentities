package fr.shayfox.persoidentitie.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ComboBoxDialogController {

    @FXML
    public JFXButton Accept, Cancel;
    @FXML
    public StackPane root, mainroot;
    @FXML
    public ImageView Icon;
    @FXML
    public JFXTextField Question;
    @FXML
    public JFXComboBox<String> Input;
    @FXML
    public Label Title;

    public String text;

    @FXML
    void btnAcceptClicked(ActionEvent event) {
        text = Input.getSelectionModel().getSelectedItem();

        closeStage(event);
    }

    @FXML
    void EnterKey(KeyEvent event){
        if(event.getCode().equals(KeyCode.ENTER)){
            text = Input.getSelectionModel().getSelectedItem();

            closeStage(event);
        }
    }

    @FXML
    void btnCancelClicked(ActionEvent event) {
        text = null;

        closeStage(event);
    }

    private void closeStage(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void closeStage(KeyEvent event){
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }





}