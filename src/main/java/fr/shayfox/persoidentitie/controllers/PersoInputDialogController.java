package fr.shayfox.persoidentitie.controllers;

import com.jfoenix.controls.JFXButton;
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

public class PersoInputDialogController {

    @FXML
    public JFXButton Accept, Cancel;
    @FXML
    public StackPane root, mainroot;
    @FXML
    public ImageView Icon;
    @FXML
    public JFXTextField Input, Input2, Input3;
    @FXML
    public Label Title;

    public String Name;
    public String PreName;
    public String World;
    public boolean cancelled;

    @FXML
    void btnAcceptClicked(ActionEvent event) {
        cancelled = false;

        PreName = (Input.getText().chars().allMatch(Character::isWhitespace) ? " " :Input.getText().trim());
        Name = (Input2.getText().chars().allMatch(Character::isWhitespace) ? " " :Input2.getText().trim());
        World = (Input3.getText().chars().allMatch(Character::isWhitespace) ? " " :Input3.getText().trim());

        closeStage(event);
    }

    @FXML
    void EnterKeyPrename(KeyEvent event){
        if(event.getCode().equals(KeyCode.ENTER)){
            Input2.requestFocus();
        }
    }
    @FXML
    void EnterKeyName(KeyEvent event){
        if(event.getCode().equals(KeyCode.ENTER)){
            Input3.requestFocus();
        }
    }
    @FXML
    void EnterKeyWorld(KeyEvent event){
        if(event.getCode().equals(KeyCode.ENTER)){
            Input.requestFocus();
        }
    }

    @FXML
    void btnCancelClicked(ActionEvent event) {
        cancelled = true;

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