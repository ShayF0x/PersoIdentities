package fr.shayfox.persoidentitie.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FindPersoDialogController {

    @FXML
    public JFXButton Accept, Cancel;
    @FXML
    public StackPane root, mainroot;
    @FXML
    public ImageView Icon;
    @FXML
    public JFXTextField Question, Input;
    @FXML
    public Label Title;
    @FXML
    private Pane pane;

    private JFXPopup popup;
    public JFXListView<String> findlist = new JFXListView<String>();

    public List<String> FindList = new ArrayList<String>();


    public String text;
    private final String appdata = System.getenv("APPDATA");

    @FXML
    void btnAcceptClicked(ActionEvent event) {
        text = Input.getText().trim();

        closeStage(event);
    }

    @FXML
    void EnterKey(KeyEvent event){
        if(event.getCode().equals(KeyCode.ENTER)){
            if(!Input.getText().isEmpty()){
                if(findlist.getItems().contains(Input.getText())){
                    text = Input.getText().trim();
                }
            }

            closeStage(event);
        }else{
            if(!Input.getText().trim().isEmpty()){
                searchTxtfichesKeyReleased();
                if(popup == null)popup = new JFXPopup(findlist);
                popup.setStyle("-fx-focus-color: transparent;" + "-fx-faint-focus-color: transparent;");

                findlist.setOnMouseReleased(e -> mouseclick(e));

                if(!popup.isShowing())popup.show(pane, PopupVPosition.TOP, PopupHPosition.LEFT);
                if(findlist.getItems().size() <= 0)popup.hide();
            }else{
                if(popup != null)if(popup.isShowing())popup.hide();
            }

        }
    }

    private void mouseclick(MouseEvent e) {
        if(e.getClickCount() >= 2){
            popup.hide();
            Input.setText(findlist.getSelectionModel().getSelectedItem());
        }
    }

    private void searchTxtfichesKeyReleased() {searchFilterfiches(Input.getText());}

    private void searchFilterfiches(String searchTerm) {
        if(findlist.getItems().size() > 0)findlist.getItems().clear();
        if(FindList.size() > 0)FindList.clear();
        List<String> filteredItems= new ArrayList<>();
        ArrayList<String> stars= listDirectory(appdata+"/PersoIdentitie/fiches");
        stars.remove("images");

        stars.stream().forEach((star) -> {
            String starName=star.toString().toLowerCase();
            if (starName.contains(searchTerm.toLowerCase())) {
                filteredItems.add(star);
            }
        });

        FindList = filteredItems;
        appliedlistmod(FindList, findlist);
    }

    private void appliedlistmod(List<String> listmodel, JFXListView<String> list){
        String[] convert = (String[]) listmodel.stream().toArray(String[]::new);
        int count = 0;
        for (String s: convert) {
            list.getItems().add(count, s);
            count++;
        }
    }

    public ArrayList<String> listDirectory (String dir) {
        File file = new File(dir);
        File[] files = file.listFiles();
        ArrayList<String> tab = new ArrayList<>();
        if(files != null) {
            for (File value : files) {
                if (!value.isDirectory()) {
                    if(!value.getName().matches("^[\\(\\)].*$")) tab.add(value.getName().replaceFirst(".yml", " ").trim());
                }
            }
        }

        return tab;


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