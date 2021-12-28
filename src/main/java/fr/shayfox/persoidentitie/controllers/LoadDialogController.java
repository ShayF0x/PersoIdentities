package fr.shayfox.persoidentitie.controllers;

import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadDialogController implements Initializable {

    @FXML
    public StackPane Layout;
    @FXML
    public JFXSpinner Spinner;
    @FXML
    public JFXTextField TextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setStyle() {
        Spinner.progressProperty().set(0.0);
        Spinner.setStyle("-fx-fill: #0011CE");
    }
}
