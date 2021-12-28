package fr.shayfox.persoidentitie.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import fr.shayfox.persoidentitie.Main;
import fr.shayfox.persoidentitie.utils.Charactere;
import fr.shayfox.persoidentitie.utils.CustomDialogs;
import fr.shayfox.persoidentitie.utils.FileClassPath;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PreferenceDialogController {

    @FXML
    public JFXButton Accept, Cancel;
    @FXML
    public StackPane mainroot, root;
    @FXML
    public ImageView Icon;
    @FXML
    public Label Title;
    @FXML
    public FlowPane pane;
    @FXML
    public JFXToolbar toolbar;
    @FXML
    public ScrollPane scrollpane;
    @FXML
    public HBox footer;

    private boolean changed = false;
    private boolean edit;
    private Charactere charactere;
    private StackPane parent;
    private Object tabeditController = null;


    @FXML
    void btnAcceptClicked(ActionEvent event) {
        if(changed && edit)SavePrefs();
        closeStage(event);
    }

    @FXML
    void btnCancelClicked(ActionEvent event) {
        if (changed) {
            int result = CustomDialogs.createQuestionStage("Attention", "les modifications apportées n'ont pas été enregistrer voulez-vous les enregistrer ?", new String[]{"Enregistrer", "Ne pas Enregistrer"});
            if (result == 0) {
                CustomDialogs.createValideDialog("Contenus Enregistré", "Les préférences ont été enregistrées avec succet !", parent, JFXDialog.DialogTransition.TOP);
                SavePrefs();
            }
        }
        closeStage(event);
    }

    public void initdata(Object tabeditiController, StackPane root, boolean edit){
        if(tabeditiController != null)tabeditController = tabeditiController;
        this.parent = root;
        this.edit = edit;

    }

    private void closeStage(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public StackPane makePane(String PreferenceName, Boolean exist, Charactere charactere, StackPane root){
        this.charactere = charactere;

        if(!exist)changed = true;
        StackPane child = new StackPane();

        JFXDepthManager.setDepth(child, 1);

        BorderPane content = null;

        FXMLLoader preferentpane = new FXMLLoader(FileClassPath.load("PreferencePanes.fxml", FileClassPath.Type.FXML));

        try { content = preferentpane.load(); } catch (IOException e) { e.printStackTrace(); }

        PreferencePanesController preferencePanesController = preferentpane.getController();

        String headerColor = getRandomColor((int) (Math.random() * 12));
        preferencePanesController.header.setStyle("-fx-background-color: " + headerColor + "; -fx-background-radius: 5 5 0 0");
        VBox.setVgrow(preferencePanesController.header, Priority.ALWAYS);
        preferencePanesController.footer.setStyle("-fx-background-color: " + headerColor + "; -fx-background-radius: 0 0 5 5");
        preferencePanesController.Title.setText(PreferenceName);

        if(exist){
            preferencePanesController.List.getItems().addAll(charactere.NewPreferences.get(PreferenceName));
            preferencePanesController.AddButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> changed = true);
            preferencePanesController.RemoveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> changed = true);
            preferencePanesController.Title.addEventHandler(KeyEvent.KEY_PRESSED, e -> changed = true);
        }else{
            preferencePanesController.List.getItems().add("Aucun");
        }


        child.getChildren().addAll(content);
        preferencePanesController.initdata(child, root, changed);

        if(!edit) {
            preferencePanesController.Title.setEditable(false);
            preferencePanesController.footer.getChildren().removeAll(preferencePanesController.AddButton, preferencePanesController.RemoveButton);
            preferencePanesController.footer.setOnContextMenuRequested(null);
            footer.getChildren().remove(Cancel);
        }

        //remove préférence

        preferencePanesController.edititem.setOnAction( e -> {
            changed = true;
            pane.getChildren().remove(child);
        });


        Platform.runLater(() -> scrollpane.requestLayout());

        JFXScrollPane.smoothScrolling(scrollpane);

        return child;
    }

    private void SavePrefs() {
        charactere.NewPreferences.clear();
        for (Node node: pane.getChildren()) {
            if(node instanceof StackPane){
                StackPane child = (StackPane) node;
                if(child.getChildren().get(0) instanceof MaterialDesignIconView)break;
                BorderPane child_2 = ((BorderPane) child.getChildren().get(0));
                StackPane children_1 = ((StackPane) child_2.getChildren().get(0));
                String key = ((TextField) children_1.getChildren().get(0)).getText();

                StackPane children_2 = ((StackPane) child_2.getChildren().get(1));
                BorderPane children_3 = ((BorderPane) children_2.getChildren().get(0));
                List<String> values = ((JFXListView) children_3.getChildren().get(0)).getItems();

                charactere.NewPreferences.put(key, values);
            }
        }

        if(tabeditController instanceof TabEditController)((TabEditController) tabeditController).changedEvent(root);
        if(tabeditController instanceof OtherIdentitieTabEditController) ((OtherIdentitieTabEditController) tabeditController).changedEvent(root);
    }

    private String getRandomColor(int i) {
        String color = "#FFFFFF";
        switch (i) {
            case 0:
                color = "#FF45E7";
                break;
            case 1:
                color = "#FF458F";
                break;
            case 2:
                color = "#FF4545";
                break;
            case 3:
                color = "#ED7526";
                break;
            case 4:
                color = "#4799FF";
                break;
            case 5:
                color = "#FFB545";
                break;
            case 6:
                color = "#FFFF45";
                break;
            case 7:
                color = "#87FE4F";
                break;
            case 8:
                color = "#45FF87";
                break;
            case 9:
                color = "#45D0F6";
                break;
            case 10:
                color = "#97D7FF";
                break;
            case 11:
                color = "#3985D1";
                break;
            case 12:
                color = "#E6593E";
                break;
            default:
                break;
        }
        return color;
    }
}