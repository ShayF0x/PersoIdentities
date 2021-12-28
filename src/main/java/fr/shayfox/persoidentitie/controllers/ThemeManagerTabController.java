package fr.shayfox.persoidentitie.controllers;

import com.jfoenix.controls.*;
import fr.shayfox.persoidentitie.Main;
import fr.shayfox.persoidentitie.utils.CustomDialogs;
import fr.shayfox.persoidentitie.utils.FileClassPath;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ThemeManagerTabController implements Initializable {

    @FXML
    public StackPane container;
    @FXML
    public BorderPane borderpane;
    @FXML
    public VBox vBox;
    @FXML
    public JFXButton ThemeButton, FontButton;

    private final String APPDATA = System.getenv("APPDATA");
    private MainController mainController;

    private boolean Edited;
    private ArrayList<JFXButton> slideMenu;
    private long cooldown;

    private String classSelected;
    private ThemeSceneSettingsController childControllerTheme;
    private FontSceneSettingsController childControllerFont;
    public StackPane parentRoot;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void initData(MainController mainController, StackPane parentRoot) {
        this.parentRoot = parentRoot;
        this.mainController = mainController;

        Edited = false;
        slideMenu = new ArrayList<>(Arrays.asList(ThemeButton, FontButton));

        initComponent();
    }

    private void initComponent() {
        ThemeButton.setOnAction(e -> {
            changeSelection(ThemeButton);
        });

        FontButton.setOnAction(e -> {
            changeSelection(FontButton);
        });

        mainController.button_Save.setOnAction(e -> {
            if(!mainController.TabPane.getSelectionModel().getSelectedItem().getText().equalsIgnoreCase("Apparence"))return;
            Save();
            setEditingProperty(false);
            mainController.button_Save.setDisable(true);
        });
    }

    private void changeSelection(JFXButton currentButton) {
        if(cooldown > System.currentTimeMillis())return;
        cooldown = System.currentTimeMillis()+500;

        if(!mainController.button_Save.isDisabled()){
            String[] content = new String[]{"ne pas enregistrer", "enregistrer", "Annuler"};
            int result = CustomDialogs.createQuestionStage("Voulez vous changer de paramètre ?", "Les changement n'ont pas été sauvegarder voulez vous enregistrer ?", content);
            switch (result){
                case 0:
                    closeNoSave();
                    mainController.button_Save.setDisable(true);
                    break;
                case 1:
                    Save();
                    mainController.button_Save.setDisable(true);
                    break;
                case 2:
                    return;
            }
        }

        slideMenu.forEach(button -> button.setStyle(""));
        currentButton.setStyle("-fx-background-color: transparent");

        switch (currentButton.getText()){

            case "Thèmes":
                classSelected = "Thèmes";
                try {
                    FXMLLoader loader = new FXMLLoader(FileClassPath.load("aparrencescenes/ThemeSceneSettings.fxml", FileClassPath.Type.FXML));
                    Parent root = loader.load();
                    childControllerTheme = loader.getController();
                    childControllerTheme.initData(this);
                    root.translateXProperty().set(-100);
                    container.getChildren().add(root);

                    Timeline timeline = new Timeline();
                    KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
                    KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);

                    timeline.getKeyFrames().add(kf);

                    timeline.setOnFinished(event -> {
                        if(container.getChildren().size() > 1)container.getChildren().remove(0);
                    });

                    timeline.play();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Police":
                classSelected = "Police";
                try {
                    FXMLLoader loader = new FXMLLoader(FileClassPath.load("aparrencescenes/FontSceneSettings.fxml", FileClassPath.Type.FXML));
                    Parent root = loader.load();
                    childControllerFont = loader.getController();
                    childControllerFont.initData(this);
                    root.translateXProperty().set(-100);
                    container.getChildren().add(root);

                    Timeline timeline = new Timeline();
                    KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
                    KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);

                    timeline.getKeyFrames().add(kf);
                    timeline.setOnFinished(event -> {
                        if(container.getChildren().size() > 1)container.getChildren().remove(0);
                    });

                    timeline.play();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void closeRequest(Event e) {
        if(getEditingProperty()){
            String[] content = new String[]{"Fermer sans enregistrer", "Fermer et enregistrer", "Annuler"};
           int result = CustomDialogs.createQuestionStage("Voulez vous fermer cet Onglet ?", "Les changement n'ont pas été sauvegarder voulez vous enregistrer ?", content);
           switch (result){
               case 0:
                   closeNoSave();
                   if(mainController.TabPane.getTabs().size() <= 1)mainController.button_Save.setDisable(true);
               break;
               case 1:
                   Save();
                   if(mainController.TabPane.getTabs().size() <= 1)mainController.button_Save.setDisable(true);
               break;
               case 2:
                   e.consume();
               break;
               default:
                   throw new IllegalStateException("Unexpected value: " + result);
           }
        }
    }

    private void Save() {
        switch (classSelected){
            case "Thèmes":
                if(childControllerTheme==null)return;
                childControllerTheme.save();
                break;
            case "Police":
                if(childControllerFont==null)return;
                childControllerFont.save();
                break;
        }
    }

    private void closeNoSave() {

    }

    public void setEditingProperty(boolean bool){
        Edited = bool;
        mainController.button_Save.setDisable(!getEditingProperty());
    }

    public boolean getEditingProperty(){
        return Edited;
    }
}
