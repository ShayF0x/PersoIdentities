package fr.shayfox.persoidentitie.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import fr.shayfox.persoidentitie.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class DialogController implements Initializable {

    @FXML
    public Pane pane;
    @FXML
    public JFXButton acceptButton;

    @FXML
    public JFXDialogLayout Layout;

    @FXML
    public Label Title, Paragraph;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
