package fr.shayfox.persoidentitie.controllers;

import com.jfoenix.controls.*;
import fr.shayfox.persoidentitie.Main;
import fr.shayfox.persoidentitie.utils.Charactere;
import fr.shayfox.persoidentitie.utils.CustomDialogs;
import fr.shayfox.persoidentitie.utils.FileClassPath;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TabController implements Initializable {

    @FXML
    public JFXTextField NameLabel, PrenameLabel, GenreLabel, MarriedNameLabel, BornDateLabel, DeathDateLabel, BornCityLabel, DeathCityLabel,
            RaceLabel, LevelLabel, AffiliateElementLabel, AstrologicalSignLabel, LunarSignLabel,
            AscendingLabel, ChineseSignLabel, DecanLabel, AnimalTotemLabel, PlanetLabel, MainResidencePlaceLabel;

    @FXML
    public JFXListView<String> FamiliesList, OtherNameList, DefaultList, QualityList, PowersList, OtherIdentitiesList;
    @FXML
    public JFXTextArea PhysicalAbilityArena, TypeDetailsArena, CaractereArena, StoryArena, PhysicalCaracteristicsArena, StoryRoleArena, OtherThingsArena;
    @FXML
    public JFXChipView<String> TagsView;
    @FXML
    public ImageView ImageView;
    @FXML
    public FlowPane ImageFlowPane;
    @FXML
    public JFXButton button_Galerie, PreferenceButton;

    private StackPane root;
    private DialogController dialogController;
    private DialogGalerieController dialogGalerieController;
    private final String appdata = System.getenv("APPDATA");

    public String filename;
    private MainController mainController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void initData(String FileProfile, StackPane stackPane, MainController mainController) {
        this.mainController = mainController;
        root = stackPane;
        filename = FileProfile.trim();
        Charactere charactere = new Charactere(filename);
        charactere.loadPropertie();

        //TODO#################ETIQUETTES#####################
        for (String tags:charactere.ListVar.get("Tags")) {
            TagsView.getChips().add(tags);
        }
        TagsView.setMouseTransparent(true);

        //TODO################IMAGEVIEW#######################
        //TODO a refaire
        Image defautimage = new Image(FileClassPath.load("noIdentitie.png", FileClassPath.Type.IMAGE).toString());
        ImageView.setImage(defautimage);
        File path = new File(appdata+"/PersoIdentitie/fiches/images/"+filename+".png");
        if(path.exists()) {
            Image image = new Image(path.toURI().toString());
            double heightc = image.getHeight();
            double widhtc = image.getWidth();
            widhtc = widhtc/218;
            heightc = heightc/widhtc;
            int height = (int)heightc;
            int width = (int)(image.getWidth()/widhtc);
            ImageView.setImage(image);
            ImageView.setFitHeight(height);
            ImageView.setFitWidth(width);
        }
        ImageView.setOnMouseClicked(e -> {
            if(!path.exists())return;
            Image image = new Image(path.toURI().toString());
            JFXDialog dialog;
            FXMLLoader rootdialog = new FXMLLoader(FileClassPath.load("Dialog.fxml", FileClassPath.Type.FXML));
            JFXDialogLayout dialog_layout = null;
            try {
                dialog_layout = rootdialog.load();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            dialogController = rootdialog.getController();
            dialog = new JFXDialog(root, dialog_layout, JFXDialog.DialogTransition.LEFT);

            dialogController.Layout.getChildren().clear();
            dialogController.Layout.setMaxWidth(image.getWidth());
            dialogController.Layout.setMaxHeight(image.getHeight());
            dialogController.Layout.getChildren().add(new ImageView(image));
            dialog.setTransitionType(JFXDialog.DialogTransition.LEFT);
            dialog.show();

        });

        //TODO################GALERIE#######################
        //ajouter les images
        int numbergalerieimg = 1;
        File pathgalerie = new File(appdata+"/PersoIdentitie/fiches/images/"+filename+"_"+numbergalerieimg+".png");
        while (pathgalerie.exists()) {
            Image image = new Image(pathgalerie.toURI().toString());
            ImageView img = new ImageView(image);
            int width = 50;
            int height= 50;
            if(image.getHeight() > image.getWidth()){
                double heightc = image.getHeight();
                double widhtc = image.getWidth();
                heightc = heightc/50;
                widhtc = widhtc/heightc;
                width = (int)widhtc;
                height = (int)(image.getHeight()/heightc);
            }else if(image.getWidth() > image.getHeight()){
                double heightc = image.getHeight();
                double widhtc = image.getWidth();
                widhtc = widhtc/50;
                heightc = heightc/widhtc;
                height = (int)heightc;
                width = (int)(image.getWidth()/widhtc);
            }
            image = new Image(pathgalerie.toURI().toString(), width, height, true, true);
            img.setImage(image);
            img.setPreserveRatio(true);
            img.setFitWidth(width);
            img.setFitHeight(height);
            ImageFlowPane.getChildren().add(img);
            numbergalerieimg ++;
            pathgalerie = new File(appdata+"/PersoIdentitie/fiches/images/"+filename+"_"+numbergalerieimg+".png");
        }
        button_Galerie.setOnAction(e -> {
            if(new File(appdata+"/PersoIdentitie/fiches/images/"+filename+"_1.png").exists()){
                JFXDialog dialog;
                FXMLLoader rootdialog = new FXMLLoader(FileClassPath.load("DialogGalerie.fxml", FileClassPath.Type.FXML));
                StackPane dialog_layout = null;
                try {
                    dialog_layout = rootdialog.load();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                dialogGalerieController = rootdialog.getController();
                dialogGalerieController.init(FileProfile);
                dialog = new JFXDialog(root, dialog_layout, JFXDialog.DialogTransition.CENTER);
                dialog.setOverlayClose(false);
                JFXDialog finalDialog = dialog;
                dialogGalerieController.Close.setOnAction(a -> finalDialog.close());
                dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);

                dialog.show();

            }
        });

        //TODO####################LesListes##################
        for (String value:charactere.ListVar.get("OthersNames")) {
            OtherNameList.getItems().add(value);
        }
        for (String value:charactere.ListVar.get("OtherIdentities")) {
            OtherIdentitiesList.getItems().add(value);
        }
        for (String value:charactere.ListVar.get("Powers")) {
            PowersList.getItems().add(value);
        }
        for (String value:charactere.ListVar.get("Default")) {
            DefaultList.getItems().add(value);
        }
        for (String value:charactere.ListVar.get("Quality")) {
            QualityList.getItems().add(value);
        }
        for (String value:charactere.ListVar.get("Families")) {
            FamiliesList.getItems().add(value);
        }

        //TODO####################LesArenas##################

        PhysicalAbilityArena.setText(charactere.StringVar.get("PhysicalAbility"));
        TypeDetailsArena.setText(charactere.StringVar.get("TypeDetails"));
        CaractereArena.setText(charactere.StringVar.get("Caractere"));
        StoryArena.setText(charactere.StringVar.get("Story"));
        PhysicalCaracteristicsArena.setText(charactere.StringVar.get("PhysicalCaracteristics"));
        StoryRoleArena.setText(charactere.StringVar.get("StoryRole"));
        OtherThingsArena.setText(charactere.StringVar.get("OtherThings"));

        //TODO###################LesTextsField#################

        NameLabel.setText(charactere.StringVar.get("Name"));
        PrenameLabel.setText(charactere.StringVar.get("Prename"));
        GenreLabel.setText(charactere.StringVar.get("Gender"));
        MarriedNameLabel.setText(charactere.StringVar.get("MarriedName"));
        BornDateLabel.setText(charactere.StringVar.get("BornDate"));
        DeathDateLabel.setText(charactere.StringVar.get("DeathDate"));
        BornCityLabel.setText(charactere.StringVar.get("BornCity"));
        DeathCityLabel.setText(charactere.StringVar.get("DeathCity"));
        RaceLabel.setText(charactere.StringVar.get("Race"));
        LevelLabel.setText(charactere.StringVar.get("Level"));
        AffiliateElementLabel.setText(charactere.StringVar.get("AffiliateElement"));
        AstrologicalSignLabel.setText(charactere.StringVar.get("AstrologicalSign"));
        LunarSignLabel.setText(charactere.StringVar.get("LunarSign"));
        AscendingLabel.setText(charactere.StringVar.get("Ascanding"));
        ChineseSignLabel.setText(charactere.StringVar.get("ChineseSign"));
        DecanLabel.setText(charactere.StringVar.get("Decan"));
        AnimalTotemLabel.setText(charactere.StringVar.get("AnimalTotem"));
        PlanetLabel.setText(charactere.StringVar.get("Planet"));
        MainResidencePlaceLabel.setText(charactere.StringVar.get("MainResidencePlace"));

        //TODO######################ACTION####################
        OtherIdentitiesList.setOnMouseClicked(e -> {
            if(e.getClickCount() >= 2){
                try {
                    createOtherIdentitietab("(" + filename + ")" + OtherIdentitiesList.getSelectionModel().getSelectedItem());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

        });

        PreferenceButton.setOnAction(e ->
                CustomDialogs.createPreferenceDialog(filename, charactere, root));


    }

    private void createOtherIdentitietab(String fileprofil) throws IOException {
        FXMLLoader panel = new FXMLLoader(FileClassPath.load("OtherIdentitieTab.fxml", FileClassPath.Type.FXML));
        BorderPane panelOnglet = panel.load();
        OtherIdentitieTabController tabController = panel.getController();
        tabController.initData(fileprofil, root);
        Tab tab = new Tab();
        tab.setText(fileprofil);
        tab.setContent(panelOnglet);
        mainController.TabPane.getTabs().add(tab);
        mainController.TabPane.setTabClosingPolicy(javafx.scene.control.TabPane.TabClosingPolicy.ALL_TABS);
        tab.setOnCloseRequest(e -> System.out.println("onglet closing "));
        mainController.TabPane.autosize();
        SingleSelectionModel<Tab> singleSelectionModel = mainController.TabPane.getSelectionModel();
        singleSelectionModel.select(mainController.TabPane.getTabs().size()-1);
    }
}
