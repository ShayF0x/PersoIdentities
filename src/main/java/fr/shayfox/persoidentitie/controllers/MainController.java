package fr.shayfox.persoidentitie.controllers;

import com.jfoenix.controls.*;
import fr.shayfox.persoidentitie.Main;
import fr.shayfox.persoidentitie.utils.Charactere;
import fr.shayfox.persoidentitie.utils.CustomDialogs;
import fr.shayfox.persoidentitie.utils.FileClassPath;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainController implements Initializable {

    public String[] contentFiche;
    public String TagName;
    private String PropertiesFile;

    private VBox vBox;

    @FXML
    private MenuItem closewindows, createPerson, CreateTag, button_edit, button_theme, button_about;
    @FXML
    private SplitPane SpitPane;
    @FXML
    private JFXTogglePane findbox_pane;
    @FXML
    private ContextMenu edit_contextmenu, show_contextmenu, help_contextmenu;
    @FXML
    public CheckMenuItem checkbox_selectionlist;
    @FXML
    public JFXButton button_Save, button_settings_file, button_settings_preferences, button_settings_help;
    @FXML
    public JFXTabPane tabPane;
    @FXML
    public StackPane root;
    @FXML
    public JFXToolbar ToolBar;

    private final String appdata = System.getenv("APPDATA");
    private FindBoxController findBoxController;
    public static String ApplyTheme;
    private Item oldSelectedItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        checkconfig();
        button_Save.setDisable(true);

        /*##############################################################################
          TODO##############################ACTION/EVENT################################
          ##############################################################################
         */

        button_settings_file.addEventHandler(MouseEvent.MOUSE_RELEASED, e ->
                edit_contextmenu.show(button_settings_file, Side.TOP, 0, 0));
        button_settings_preferences.addEventHandler(MouseEvent.MOUSE_RELEASED, e ->
                show_contextmenu.show(button_settings_preferences, Side.TOP, 0, 0));
        button_settings_help.addEventHandler(MouseEvent.MOUSE_RELEASED, e ->
                help_contextmenu.show(button_settings_help, Side.TOP, 0, 0));

        button_edit.setOnAction( e ->{

            if(tabPane.getTabs().size() <= 0 || tabPane.getSelectionModel().getSelectedItem().getText().contains("(")){

                CustomDialogs.createErrorDialog("Erreur", "Aucun personnage selectionner, \nVeuiller ouvrir l'onglet du personnage voulut", root, JFXDialog.DialogTransition.TOP);

            }else{

                String OngletSelectedName = tabPane.getSelectionModel().getSelectedItem().getText();

                if(OngletSelectedName.contains("[Edit]")){

                    CustomDialogs.createErrorDialog("Erreur", "L'onglet selectionné est déjà en cour d'édition", root, JFXDialog.DialogTransition.TOP);

                }else{
                    tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedIndex());
                    try {
                        createeditedonglet(OngletSelectedName);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }

        });

        button_theme.setOnAction( e -> {
            AtomicBoolean find = new AtomicBoolean(false);
            tabPane.getTabs().stream().filter(tab -> tab.getText().equalsIgnoreCase("Apparence")).findFirst().ifPresent(tab -> {
                tabPane.getSelectionModel().select(tab);
                find.set(true);
            });

            if(!find.get()){
                try {
                    createThemeOnglet();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        button_about.setOnAction( e -> {
            AtomicBoolean find = new AtomicBoolean(false);
            tabPane.getTabs().stream().filter(tab -> tab.getText().equalsIgnoreCase("\u00c0 propos")).findFirst().ifPresent(tab -> {
                tabPane.getSelectionModel().select(tab);
                find.set(true);
            });

            if(!find.get()){
                try {
                    createAboutOnglet();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        checkbox_selectionlist.setOnAction(e -> {
            if(checkbox_selectionlist.isSelected()){
                if(Main.primaryStage.getWidth()<=1871){
                    e.consume();
                    checkbox_selectionlist.setSelected(false);
                    return;
                }
                setselectionlist(true);
                try {
                    Properties prop = MainController.load(appdata+"/PersoIdentitie/Stockage/config.properties");
                    prop.setProperty("affichagefichier", "true");
                    try {
                        prop.store(new FileOutputStream(appdata+"/PersoIdentitie/Stockage/config.properties"), "config");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                       }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else{
                setselectionlist(false);
                try {
                    Properties prop = MainController.load(appdata+"/PersoIdentitie/Stockage/config.properties");
                    prop.setProperty("affichagefichier", "false");
                    try {
                        prop.store(new FileOutputStream(appdata+"/PersoIdentitie/Stockage/config.properties"), "config");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        if(findBoxController != null){
            findBoxController.findList.setOnMouseClicked(e -> {
                if(e.getButton().equals(MouseButton.SECONDARY))return;
                if (e.getSource() == findBoxController.findList && oldSelectedItem != null && findBoxController.findList.getSelectionModel().getSelectedItem().equals(oldSelectedItem)) {
                    if (findBoxController.findList.getSelectionModel().getSelectedItem() == null) return;
                    if (findBoxController.findList.getSelectionModel().getSelectedItem().getName().equalsIgnoreCase("Aucune fiche n'a cette étiquette"))
                        return;

                    String selection = findBoxController.selectBox.getSelectionModel().getSelectedItem();
                    findBoxController.fileProfil = findBoxController.findList.getSelectionModel().getSelectedItem().getName();

                    findBoxController.findList.getSelectionModel().clearSelection();
                    if (selection.equalsIgnoreCase("Fiches")) {
                        try {
                            createOnglet(findBoxController.fileProfil);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }

                }
                if(findBoxController.findList.getSelectionModel().getSelectedItem() != null)oldSelectedItem = findBoxController.findList.getSelectionModel().getSelectedItem();

            });
        }

        closewindows.setOnAction(e -> Main.close());

        Main.primaryStage.setOnCloseRequest(e -> {
           if(tabPane.getTabs().size() >= 1){
               e.consume();
               CustomDialogs.createErrorDialog("Impossible de fermer la fenêtre", "Veuiller d'abord fermer tout les onglet ouvert", root, JFXDialog.DialogTransition.TOP);
           }
        });

        /*########################################################
		  TODO###############nouvelle Étiquette###################
		  ########################################################*/
        CreateTag.setOnAction(e -> {
            List<String> etiquettecontent;
            boolean exist = false;
            TagName = CustomDialogs.createInputStage("Nouvelle Étiquette", "Nom de l'étiquette:", "Nom...");
            if(TagName != null) {
                YamlFile file = new YamlFile(appdata+"/PersoIdentitie/Stockage/etiquette.yml");
                try {
                    file.load();
                    etiquettecontent = file.getStringList("listettiquette");
                    if(etiquettecontent.stream().anyMatch(TagName::equalsIgnoreCase)) {
                        exist = true;
                    }else {
                        exist = false;
                        file.addDefault(TagName,"");
                        etiquettecontent = file.getStringList("listettiquette");
                        etiquettecontent.add(TagName);
                        file.set("listettiquette", etiquettecontent);
                        file.save();
                    }
                }catch(Exception eee) {
                    eee.printStackTrace();
                }

                if(!exist) {
                    if(findBoxController.selectBox.getSelectionModel().getSelectedIndex() == 0)findBoxController.addTagList();

                    CustomDialogs.createValideDialog("Étiquette crée !", "l'étiquette "+ TagName + " à bien été crée", root, JFXDialog.DialogTransition.TOP);

                }else {

                    CustomDialogs.createErrorDialog("Erreur", "l'étiquette "+ TagName + " existe déjà !", root,  JFXDialog.DialogTransition.TOP);
                }

            }


        });
        /*########################################################
		  ########################################################
		  ########################################################*/

        /*########################################################
		  TODO##################nouvelle Fiche####################
		  ########################################################*/

        createPerson.setOnAction(e -> {
            String name, preName, world;

            contentFiche = CustomDialogs.createPersoInputStage("Création de fiche");
            boolean exist;

            if(contentFiche == null || contentFiche.length < 3)return;
            if((contentFiche[0].chars().allMatch(Character::isWhitespace) || contentFiche[1].chars().allMatch(Character::isWhitespace)) && contentFiche[2].chars().allMatch(Character::isWhitespace))return;

            preName = contentFiche[0].trim();
            name = contentFiche[1].trim();
            world = contentFiche[2].trim();
            if(preName.contains("(") || preName.contains(")") || name.contains("(") || name.contains(")") ||
            world.contains("(") || world.contains(")")){
                CustomDialogs.createErrorDialog("Erreur", "vous ne pouvez pas mettre de parenthèse dans le nom ou prénom de votre fiche", root, JFXDialog.DialogTransition.TOP);
                return;
            }

            Charactere charactere = new Charactere(preName+" "+name +(world.chars().allMatch(Character::isWhitespace) ? "" : " {"+world+"}"));
            exist = charactere.CreateFile();

            if(!exist) {
                if(findBoxController.selectBox.getSelectionModel().getSelectedIndex() == 1)findBoxController.addListPerso();

                CustomDialogs.createValideDialog("Personnage crée !", "le personnage "+ preName+" "+name + " à bien été crée", root, JFXDialog.DialogTransition.TOP);

                try {
                    createeditedonglet(preName+" "+name +(world.chars().allMatch(Character::isWhitespace) ? "" : " {"+world+"}"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }else {

                CustomDialogs.createErrorDialog("Erreur", "le personnage" + preName+" "+name + " existe déjà ", root, JFXDialog.DialogTransition.TOP);
            }

        });

        setTheme();

    }

    private void checkconfig() {
        //affichagefiche
        try {
            Properties prop = MainController.load(appdata+"/PersoIdentitie/Stockage/config.properties");
            PropertiesFile = prop.getProperty("affichagefichier");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(PropertiesFile.equalsIgnoreCase("false")) {
            checkbox_selectionlist.setSelected(false);
            setselectionlist(false);
        }else {
            checkbox_selectionlist.setSelected(true);
            setselectionlist(true);
        }
    }

    private void setselectionlist(boolean check) {
        if(check){
            try {
                FXMLLoader findBox = new FXMLLoader(FileClassPath.load("FindBox.fxml", FileClassPath.Type.FXML));

                vBox = findBox.load();
                findBoxController = findBox.getController();
                findBoxController.initData(this);
                findbox_pane.getChildren().addAll(vBox);

            } catch (IOException e) {
                e.printStackTrace();
            }
            vBox.setPrefWidth(200);
            vBox.setMinWidth(100);
            findbox_pane.setPrefWidth(262);
            findbox_pane.setMinWidth(100);
            findbox_pane.setMaxWidth(Region.USE_COMPUTED_SIZE);
            findbox_pane.setDisable(false);
        }else{
            findbox_pane.getChildren().remove(vBox);
            findbox_pane.setMaxWidth(0);
            findbox_pane.setMinWidth(0);
            findbox_pane.setDisable(true);
        }
    }

    public static Properties load(String filename) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(filename)) {
            properties.load(input);
            return properties;
        }
    }

    public void createeditedonglet(String fileprofil) throws IOException {
        FXMLLoader panel = new FXMLLoader(FileClassPath.load("TabEdit.fxml", FileClassPath.Type.FXML));
        BorderPane panelOnglet = panel.load();
        TabEditController tabController = panel.getController();
        tabController.initData(fileprofil, root, this);
        Tab tab = new Tab();
        tab.setText(fileprofil+" [Edit]");
        tab.setContent(panelOnglet);
        tab.selectedProperty().addListener(
                (newValue) -> {
                    if(tab.isSelected())button_Save.setDisable(!tabController.getEditingProperty());
        });
        tabPane.getTabs().add(tab);
        tabPane.setTabClosingPolicy(javafx.scene.control.TabPane.TabClosingPolicy.ALL_TABS);
        tab.setOnCloseRequest(e -> tabController.closeRequest(e));
        tabPane.autosize();
        SingleSelectionModel<Tab> singleSelectionModel = tabPane.getSelectionModel();
        singleSelectionModel.select(tabPane.getTabs().size()-1);
    }

    public void createThemeOnglet() throws IOException {
        FXMLLoader panel = new FXMLLoader(FileClassPath.load("ThemeManagerTab.fxml", FileClassPath.Type.FXML));
        BorderPane panelOnglet = panel.load();
        ThemeManagerTabController tabController = panel.getController();
        tabController.initData(this, root);
        Tab tab = new Tab();
        tab.setText("Apparence");
        tab.setContent(panelOnglet);
        tab.selectedProperty().addListener(
                (newValue) -> {
                    if(tab.isSelected())button_Save.setDisable(!tabController.getEditingProperty());
                });
        tabPane.getTabs().add(tab);
        tabPane.setTabClosingPolicy(javafx.scene.control.TabPane.TabClosingPolicy.ALL_TABS);
        tab.setOnCloseRequest(e -> tabController.closeRequest(e));
        tabPane.autosize();
        SingleSelectionModel<Tab> singleSelectionModel = tabPane.getSelectionModel();
        singleSelectionModel.select(tabPane.getTabs().size()-1);
    }

    public void createAboutOnglet() throws IOException {
        FXMLLoader panel = new FXMLLoader(FileClassPath.load("BlankTab.fxml", FileClassPath.Type.FXML));
        HBox panelOnglet = panel.load();
        BlankTabController tabController = panel.getController();
        tabController.initData(this, root);
        Tab tab = new Tab();
        tab.setText("\u00c0 propos");
        tab.setContent(panelOnglet);
        tab.selectedProperty().addListener(
                (newValue) -> {
                    if(tab.isSelected())button_Save.setDisable(true);
                });
        tabPane.getTabs().add(tab);
        tabPane.setTabClosingPolicy(javafx.scene.control.TabPane.TabClosingPolicy.ALL_TABS);
        tabPane.autosize();
        SingleSelectionModel<Tab> singleSelectionModel = tabPane.getSelectionModel();
        singleSelectionModel.select(tabPane.getTabs().size()-1);
    }

    public void createOnglet(String fileprofil) throws IOException {
        FXMLLoader panel = new FXMLLoader(FileClassPath.load("Tab.fxml", FileClassPath.Type.FXML));
        BorderPane panelOnglet = panel.load();
        TabController tabController = panel.getController();
        tabController.initData(fileprofil, root, this);
        Tab tab = new Tab();
        tab.setText(fileprofil);
        tab.setContent(panelOnglet);
        tabPane.getTabs().add(tab);
        tabPane.setTabClosingPolicy(javafx.scene.control.TabPane.TabClosingPolicy.ALL_TABS);
        tabPane.autosize();
        SingleSelectionModel<Tab> singleSelectionModel = tabPane.getSelectionModel();
        singleSelectionModel.select(tabPane.getTabs().size()-1);
    }

    public void setTheme(){
        ToolBar.getStyleClass().add("ToolBar");
    }

}
