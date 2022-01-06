package fr.shayfox.persoidentitie.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import fr.shayfox.persoidentitie.utils.CustomDialogs;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FindBoxController implements Initializable {

    public String fileProfil;

    @FXML
    public JFXListView<Item> findList;

    public List<String> FindList = new ArrayList<String>();
    private List<String> contentRecherche = new ArrayList<>();

    @FXML
    public JFXComboBox<String> selectBox;

    @FXML
    private JFXTextField textField;
    private String appdata = System.getenv("APPDATA");

    private MenuItem editItem;
    private MenuItem deleteItem;
    private MenuItem seeItem;
    private MainController mainController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(MainController mainController){
        this.mainController = mainController;
        findList.setCellFactory(lv -> {
            ListCell<Item> cell = new ListCell<Item>() {
                @Override
                public void updateItem(Item item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
            cell.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
                if (isNowHovered && ! cell.isEmpty()) {
                    findList.setCursor(Cursor.HAND);
                } else {
                    findList.setCursor(Cursor.DEFAULT);
                }
            });

            ContextMenu contextMenu = new ContextMenu();
            editItem = new MenuItem("éditer");
            editItem.setOnAction(e -> {
                try {
                    mainController.createeditedonglet(findList.getSelectionModel().getSelectedItem().getName());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            contextMenu.getItems().add(editItem);

            seeItem = new MenuItem("voir");
            seeItem.setOnAction(e -> {
                try {
                    mainController.createOnglet(findList.getSelectionModel().getSelectedItem().getName());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            contextMenu.getItems().add(seeItem);

            deleteItem = new MenuItem("Supprimer");
            deleteItem.setOnAction(e -> {
                String fileName = findList.getSelectionModel().getSelectedItem().getName();
                int result = CustomDialogs.createQuestionStage("Supression de fiche", "voulez vous vraiment supprimer definitivement la fiche "+fileName+" ?", new String[]{"Confirmer", "Annuler"});
                System.out.println(result);
                switch (result){
                    case 0:
                        File file = new File(appdata+"/PersoIdentitie/fiches/"+fileName+".yml");
                        file.delete();

                        File path = new File(appdata+"/PersoIdentitie/fiches/images/"+fileName+".png");
                        if(path.exists()){
                            path.delete();
                        }
                        int number = 1;
                        File pathGalerie = new File(appdata+"/PersoIdentitie/fiches/images/"+fileName+"_"+number+".png");
                        while (pathGalerie.exists()) {
                            pathGalerie.delete();
                            number++;
                            pathGalerie = new File(appdata+"/PersoIdentitie/fiches/images/"+fileName+"_"+number+".png");
                        }

                        YamlFile yamlFile = new YamlFile(appdata+"/PersoIdentitie/Stockage/etiquette.yml");
                        try {
                            yamlFile.load();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        for (String str:yamlFile.getStringList("listettiquette")) {
                            ArrayList array = new ArrayList(yamlFile.getStringList(str));
                            if(array.contains(fileName))array.remove(fileName);
                        }

                        mainController.tabPane.getTabs().forEach(value -> {
                            if (value.getText().contains(fileName))mainController.tabPane.getTabs().remove(value);
                        });
                        findList.getItems().remove(findList.getSelectionModel().getSelectedItem());

                        CustomDialogs.createValideDialog("Fiche supprimée !", "la fiche "+fileName+" a été supprimer avec succet", mainController.root, JFXDialog.DialogTransition.TOP);

                        break;
                    case 1:
                        break;
                }

            });
            contextMenu.getItems().add(deleteItem);

            cell.setOnContextMenuRequested(event -> contextMenu.show(cell, event.getScreenX(), event.getScreenY()));

            return cell ;
        });

        findList.setFocusTraversable(false);

        selectBox.getItems().addAll("Etiquettes", "Fiches");
        selectBox.getSelectionModel().select(1);
        if(selectBox.getSelectionModel().getSelectedIndex() == 0){
            addTagList();
        }else{
            addListPerso();
        }

        selectBox.setOnAction(e -> {
            if(selectBox.getSelectionModel().getSelectedIndex() == 0){
                addTagList();
            }else{
                addListPerso();
            }
        });

        textField.setOnKeyReleased((KeyEvent e) -> {
            if(selectBox.getSelectionModel().getSelectedIndex() == 0) {

                if(e.getCode() == KeyCode.ENTER) {
                    try {
                        recherchelistetiquette();
                    } catch (InvalidConfigurationException | IOException e1) {
                        e1.printStackTrace();
                    }
                }else {
                    searchTxtetiquetteKeyReleased(e);
                }

            }else if(selectBox.getSelectionModel().getSelectedIndex() == 1) {
                searchTxtfichesKeyReleased(e);
            }

        });
        findList.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            if(findList.getSelectionModel().getSelectedItem().getName().equalsIgnoreCase("Aucune fiche n'a cette étiquette"))return;
            if(e.getSource() == findList && e.getClickCount() >= 2 ) {

                int selection = selectBox.getSelectionModel().getSelectedIndex();
                fileProfil = findList.getSelectionModel().getSelectedItem().getName();

                if(selection == 0) {
                    selectBox.getSelectionModel().select(1);
                    textField.setText(fileProfil);
                    try {
                        recherchelistetiquette();
                    } catch (InvalidConfigurationException | IOException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });
//        findlist.setOnMouseClicked(e -> {
//            System.out.println("1");
//            if(findlist.getSelectionModel().getSelectedItem().getName().equalsIgnoreCase("Aucune fiche n'a cette étiquette"))return;
//            System.out.println("2");
//            if(e.getSource() == findlist && e.getClickCount() >= 2 ) {
//                System.out.println("3");
//
//                int selection = selectbox.getSelectionModel().getSelectedIndex();
//                fileprofil = findlist.getSelectionModel().getSelectedItem().getName();
//
//                if(selection == 0) {
//
//                }else if(selection == 1) {
//                    System.out.println("4");
//                    selectbox.getSelectionModel().select(1);
//                    textfield.setText(fileprofil);
//                    try {
//                        recherchelistetiquette();
//                    } catch (InvalidConfigurationException | IOException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//
//            }
//        });
        textField.setMaxWidth(140);
        selectBox.setMaxWidth(125);
        HBox.setHgrow(selectBox, Priority.ALWAYS);
        HBox.setHgrow(textField, Priority.SOMETIMES);
    }

    public void addListPerso(){
        findList.getItems().clear();
        FindList.clear();

        @SuppressWarnings("unchecked")
        ArrayList<String> persolist = listDirectory(appdata+"/PersoIdentitie/fiches");
        String[] listperso = persolist.toArray(new String[persolist.size()]);

        for (int i = 0; i < listperso.length; i++) {
            FindList.add(listperso[i]);
        }
        appliedlistmod(FindList, findList);
    }
    public void addTagList(){
        findList.getItems().clear();
        FindList.clear();

        YamlFile file = new YamlFile(appdata+"/PersoIdentitie/Stockage/etiquette.yml");
        try {
            file.load();

        }catch(Exception e) {
            e.printStackTrace();
        }

        contentRecherche = file.getStringList("listettiquette");
        String[] listetiquette = contentRecherche.toArray(new String[contentRecherche.size()]);

        for (int i = 0; i < listetiquette.length; i++) {
            FindList.add(listetiquette[i]);
        }
        appliedlistmod(FindList, findList);
    }

    private void appliedlistmod(List listmodel, JFXListView list){
        String[] convert = (String[]) listmodel.stream().toArray(String[]::new);
        int count = 0;
        for (String s: convert) {
            list.getItems().add(new Item(s, count));
            count++;
        }
    }

    @SuppressWarnings("rawtypes")
    public ArrayList listDirectory (String dir) {
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

    private void recherchelistetiquette() throws InvalidConfigurationException, IOException {
        findList.getItems().clear();
        FindList.clear();

        String textfieltext = new String(textField.getText());
        List<String> ettiquettename = new ArrayList<String>();
        YamlFile file = new YamlFile(appdata+"/PersoIdentitie/Stockage/etiquette.yml");
        findList.getItems().clear();
        file.load();
        if(file.getStringList(textfieltext).isEmpty() == false) {

            ettiquettename = file.getStringList(textfieltext);

            String[] content = ettiquettename.toArray(new String[ettiquettename.size()]);
            findList.getItems().clear();
            FindList.clear();
            for (int i = 0; i < content.length; i++) {
                FindList.add(content[i]);
            }

        }else {
            FindList.add("Aucune fiche n'a cette étiquette");
        }
        appliedlistmod(FindList, findList);
    }

    private void searchTxtfichesKeyReleased(KeyEvent evt) {
        searchFilterfiches(textField.getText());
    }
    private void searchTxtetiquetteKeyReleased(KeyEvent evt) {
        searchFilteretiquette(textField.getText());
    }

    @SuppressWarnings("unchecked")
    private void searchFilterfiches(String searchTerm) {
        findList.getItems().clear();
        FindList.clear();
        List<String> filteredItems=new ArrayList<String>();
        ArrayList<String> stars= listDirectory(appdata+"/PersoIdentitie/fiches");
        stars.remove("images");

        stars.stream().forEach((star) -> {
            String starName=star.toString().toLowerCase();
            if (starName.contains(searchTerm.toLowerCase())) {
                filteredItems.add(star);
            }
        });

        FindList =filteredItems;
        appliedlistmod(FindList, findList);
    }

    private void searchFilteretiquette(String searchTerm) {
        findList.getItems().clear();
        FindList.clear();
        List<String> filteredItems=new ArrayList<String>();

        contentRecherche.stream().forEach((star) -> {
            String starName=star.toString().toLowerCase();
            if (starName.contains(searchTerm.toLowerCase())) {
                filteredItems.add(star);
            }
        });

        FindList =filteredItems;
        appliedlistmod(FindList, findList);
    }
}

class Item {
    private final int value ;
    private final String name ;

    public Item(String name, int value) {
        this.name = name ;
        this.value = value ;
    }

    public int getValue() {
        return value ;
    }

    public String getName() {
        return name ;
    }
}
