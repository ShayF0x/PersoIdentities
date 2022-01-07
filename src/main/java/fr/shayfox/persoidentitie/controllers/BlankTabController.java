package fr.shayfox.persoidentitie.controllers;

import com.jfoenix.controls.JFXButton;
import fr.shayfox.persoidentitie.updater.Updater;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class BlankTabController implements Initializable {

    @FXML
    public Label author, version;
    @FXML
    public JFXButton updateButton;

    private MainController mainController;

    private long cooldown;
    public StackPane parentRoot;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void initData(MainController mainController, StackPane parentRoot) {
        this.parentRoot = parentRoot;
        this.mainController = mainController;

        initComponent();
    }

    private void initComponent() {
        if(this.getClass().getPackage().getImplementationVendor() != null)author.setText(this.getClass().getPackage().getImplementationVendor());
        else author.setText("ShayFox_[beta]");
        if(this.getClass().getPackage().getImplementationVersion() != null)version.setText(this.getClass().getPackage().getImplementationVersion().replaceAll("-SNAPSHOT", ""));
        else version.setText("1.3.0[beta]");
        updateButton.setOnAction(e -> {
            if(cooldown-System.currentTimeMillis() <= 0){
                new Updater(mainController);
                cooldown = System.currentTimeMillis()+5000;
            }
        });
    }
}
