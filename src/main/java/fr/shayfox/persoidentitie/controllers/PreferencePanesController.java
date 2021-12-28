package fr.shayfox.persoidentitie.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import fr.shayfox.persoidentitie.utils.CustomDialogs;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PreferencePanesController implements Initializable {
    @FXML
    public StackPane header, body;
    @FXML
    public JFXTextField Title;
    @FXML
    public JFXListView List;
    @FXML
    public BorderPane footer, root;
    @FXML
    public JFXButton AddButton, RemoveButton;
    public ContextMenu contextMenu;

    private StackPane parent, Mainroot;
    public MenuItem edititem;
    private String Result;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void initdata(StackPane parent, StackPane root, boolean changed){
        this.Mainroot = root;
        this.parent = parent;
        AutoResizeWidht();


        //message flottant et context menu pour supprimer la préférence

        contextMenu = new ContextMenu();
        edititem = new MenuItem("supprimer la préférence");

        contextMenu.getItems().add(edititem);

        footer.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(footer, event.getScreenX(), event.getScreenY());
            }
        });

        Tooltip tooltip = new Tooltip("clique gauche pour supprimer la préférence");
        Tooltip.install(footer, tooltip);
    }

    @FXML
    void AddButtonClicked(ActionEvent event) {
        Result = CustomDialogs.createInputStage("Ajouter en Préférence un\\une " + Title.getText(), "Nom de la\\le " + Title.getText());
        if(Result != null && !Result.isEmpty()){
            if(List.getItems().contains(Result))return;
            if(List.getItems().contains("Aucun"))List.getItems().remove("Aucun");
            List.getItems().add(Result);
        }
    }

    @FXML
    void RemoveButtonClicked(ActionEvent event) {
        if(List.getSelectionModel().getSelectedItem() != null){
            List.getItems().remove(List.getSelectionModel().getSelectedItem());
            if(List.getItems().size() <= 0)List.getItems().add("Aucun");
        }else{
            CustomDialogs.createErrorDialog("Error", "Aucune Préférence n'a été sélectionné", Mainroot, JFXDialog.DialogTransition.CENTER);
        }
    }

    @FXML
    void TextChanged(KeyEvent event) {
       AutoResizeWidht();
    }

    private void AutoResizeWidht() {
        if((Title.getText().length() * 6.33) + 40 > 65){
            parent.setMinWidth((Title.getText().length() * 6.33) + 40);
            parent.setMaxWidth((Title.getText().length() * 6.33) + 40);
        } else {
            parent.setMinWidth(65);
            parent.setMaxWidth(65);
        }
    }
}
