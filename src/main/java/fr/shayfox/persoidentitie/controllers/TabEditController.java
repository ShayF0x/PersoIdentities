package fr.shayfox.persoidentitie.controllers;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fr.shayfox.persoidentitie.Main;
import fr.shayfox.persoidentitie.utils.Charactere;
import fr.shayfox.persoidentitie.utils.CustomDialogs;
import fr.shayfox.persoidentitie.utils.FileClassPath;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.simpleyaml.configuration.file.YamlFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TabEditController implements Initializable {
    @FXML
    public JFXTextField NameLabel, PrenameLabel, MarriedNameLabel, BornCityLabel, DeathCityLabel,
            RaceLabel, AffiliateElementLabel, AstrologicalSignLabel, LunarSignLabel,
            AscendingLabel, ChineseSignLabel, AnimalTotemLabel, PlanetLabel, MainResidencePlaceLabel, DeathDateLabel, BornDateLabel;
    @FXML
    public DatePicker DeathDateButton, BornDateButton;

    @FXML
    public JFXListView<String> FamiliesList, OtherNameList, DefaultList, QualityList, PowersList, OtherIdentitiesList;
    @FXML
    public JFXTextArea CaractereArena, StoryArena, PhysicalCaracteristicsArena, StoryRoleArena, OtherThingsArena;
    @FXML
    public TextArea PhysicalAbilityArena, TypeDetailsArena;
    @FXML
    public JFXChipView<String> TagsView;
    @FXML
    public JFXComboBox<String> GenreComboBox, LevelComboBox, DecanComboBox;
    @FXML
    public ImageView ImageView;
    @FXML
    public FlowPane ImageFlowPane;
    @FXML
    public JFXButton PreferenceButton, ChangeImage,
            AddFamiliesButton, RemoveFamiliesButton,
            AddOtherNameButton,  RemoveOtherNameButton,
            AddDefaultButton, RemoveDefaultButton,
            AddQualityButton, RemoveQualityButton,
            AddPowersButton, RemovePowersButton,
            AddOtherIdentitiesButton, RemoveOtherIdentitiesButton, editOtherIdentitie;


    private StackPane root;
    private DialogController dialogController;
    private final String appdata = System.getenv("APPDATA");
    public String filename;
    private Charactere charactere;
    public MainController mainController;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private ArrayList<String> AddImages = new ArrayList<>();
    private ArrayList<String> RemoveImages = new ArrayList<String>();
    private ArrayList<String> AddOtherIdentities = new ArrayList<String>();
    private ArrayList<String> RemoveOtherIdentities = new ArrayList<>();
    private ArrayList<String> SaveImageAdd = new ArrayList<>();
    private ArrayList<String> SaveImageRemove = new ArrayList<>();
    private List<String> TagslistSuggestion = new ArrayList<>();
    private List<String> Tagslist = new ArrayList<>();
    private final List<Node> componentsList = new ArrayList<>();
    private ArrayList<String> log = new ArrayList<>();
    private String Result;
    public boolean edited;


    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void initData(String FileProfile, StackPane stackPane, MainController mainController) {
        this.mainController = mainController;
        root = stackPane;
        filename = FileProfile;
        charactere = new Charactere(filename);
        charactere.loadPropertie();

        initComponent();


    }

    private void initComponent() {

        //TODO#############SAVE#COMPONENT#####################
        saveAllComponent();


        //TODO#################ETIQUETTES#####################
        for (String tags:charactere.ListVar.get("Tags")) {
            TagsView.getChips().add(tags);
        }
        TagslistSuggestion = getTagList();
        TagslistSuggestion.removeAll(charactere.ListVar.get("Tags"));
        Tagslist.addAll(charactere.ListVar.get("Tags"));
        TagsView.getSuggestions().addAll(TagslistSuggestion);



        //TODO################IMAGEVIEW#######################
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
            StackPane pane = new StackPane();
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
            pane.setPrefWidth(width);
            pane.setPrefHeight(height);
            pane.getChildren().add(img);
            ImageFlowPane.getChildren().add(pane);
            numbergalerieimg ++;
            pathgalerie = new File(appdata+"/PersoIdentitie/fiches/images/"+filename+"_"+numbergalerieimg+".png");
        }

        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.USER_PLUS);
        icon.setFill(Color.rgb(0, 147, 14, 0.25));
        icon.setSize("40");
        ImageFlowPane.getChildren().add(icon);

        setImageDeleteIcon();


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
        MarriedNameLabel.setText(charactere.StringVar.get("MarriedName"));
        BornCityLabel.setText(charactere.StringVar.get("BornCity"));
        DeathCityLabel.setText(charactere.StringVar.get("DeathCity"));
        RaceLabel.setText(charactere.StringVar.get("Race"));
        AffiliateElementLabel.setText(charactere.StringVar.get("AffiliateElement"));
        AstrologicalSignLabel.setText(charactere.StringVar.get("AstrologicalSign"));
        LunarSignLabel.setText(charactere.StringVar.get("LunarSign"));
        AscendingLabel.setText(charactere.StringVar.get("Ascanding"));
        ChineseSignLabel.setText(charactere.StringVar.get("ChineseSign"));
        AnimalTotemLabel.setText(charactere.StringVar.get("AnimalTotem"));
        PlanetLabel.setText(charactere.StringVar.get("Planet"));
        MainResidencePlaceLabel.setText(charactere.StringVar.get("MainResidencePlace"));

        //TODO######################LesComboBox#################

        String[] niv = {"0","0.5","1","1.5","2","2.5","3","3.5","4","4.5","5"};
        String[] decan = {"1","2","3"};
        String[] Gender = {"Masculin", "Feminin", "Autre"};
        for (String value:niv) {
            LevelComboBox.getItems().add(value);
        }
        if(!charactere.StringVar.get("Level").equalsIgnoreCase("Non Renseigné"))
            LevelComboBox.getSelectionModel().select(charactere.StringVar.get("Level"));

        for (String value:decan) {
            DecanComboBox.getItems().add(value);
        }
        if(!charactere.StringVar.get("Decan").equalsIgnoreCase("Non Renseigné"))
            DecanComboBox.getSelectionModel().select(charactere.StringVar.get("Decan"));

        for (String value:Gender) {
            GenreComboBox.getItems().add(value);
        }
        if(!charactere.StringVar.get("Gender").equalsIgnoreCase("Non Renseigné"))
            GenreComboBox.getSelectionModel().select(charactere.StringVar.get("Gender"));

        //TODO######################DatesPickers##################
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if(!charactere.StringVar.get("BornDate").equalsIgnoreCase("Non Renseigné") && !charactere.StringVar.get("BornDate").equalsIgnoreCase("dd/MM/yyyy") && isValidFormat(charactere.StringVar.get("BornDate"))) {
            BornDateLabel.setText(String.valueOf(LocalDate.parse(charactere.StringVar.get("BornDate"), dateFormatter).format(dateFormatter)));
        }else{
            BornDateLabel.setText("dd/MM/yyyy");
        }
        if(!charactere.StringVar.get("DeathDate").equalsIgnoreCase("Non Renseigné") && !charactere.StringVar.get("DeathDate").equalsIgnoreCase("dd/MM/yyyy") && isValidFormat(charactere.StringVar.get("DeathDate"))) {
            DeathDateLabel.setText(String.valueOf(LocalDate.parse(charactere.StringVar.get("DeathDate"), dateFormatter).format(dateFormatter)));
        }else{
            DeathDateLabel.setText("dd/MM/yyyy");
        }
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {

            @Override
            public String toString(LocalDate date) {
                if(date != null){
                    return dateFormatter.format(date);
                }else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if(string != null && !string.trim().isEmpty()){
                    return LocalDate.parse(string, dateFormatter);
                }else{
                    return null;
                }
            }
        };
        DeathDateButton.setConverter(converter);
        BornDateButton.setConverter(converter);

        DeathDateButton.addEventHandler(DatePicker.ON_SHOWING, e -> {
            if(DeathDateLabel.getText().trim().isEmpty() || DeathDateLabel.getText() == null || DeathDateLabel.getText().equalsIgnoreCase("dd/MM/yyyy") || !DeathDateLabel.getText().matches("\\d{2}/\\d{2}/\\d{4}"))return;
            DeathDateButton.setValue(LocalDate.parse(DeathDateLabel.getText(), dateFormatter));
        });

        BornDateButton.addEventHandler(DatePicker.ON_SHOWING, e -> {
            if(BornDateLabel.getText().trim().isEmpty() || BornDateLabel.getText() == null || BornDateLabel.getText().equalsIgnoreCase("dd/MM/yyyy") || !BornDateLabel.getText().matches("\\d{2}/\\d{2}/\\d{4}"))return;
            BornDateButton.setValue(LocalDate.parse(BornDateLabel.getText(), dateFormatter));
        });

        DeathDateButton.addEventHandler(DatePicker.ON_HIDING, e -> {
            if(DeathDateButton.getValue() != null)DeathDateLabel.setText(DeathDateButton.getValue().format(dateFormatter));
        });
        BornDateButton.addEventHandler(DatePicker.ON_HIDING, e -> {
            if(BornDateButton.getValue() != null)BornDateLabel.setText(BornDateButton.getValue().format(dateFormatter));
         });
        //TODO#############ACTIONLISTENER###########################

        ChangeImage.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image (*.png), (*.jpg), (*.jpeg), (*.bmp)","*.png", "*.jpg", "*.jpeg", "*.bmp");
            fileChooser.getExtensionFilters().add(extensionFilter);
            File file = fileChooser.showOpenDialog(Main.primaryStage);
            if(file != null){
                try {
                    BufferedImage bufferedimage = ImageIO.read(file);
                    ImageIO.write(bufferedimage, "png", new File(appdata+"/PersoIdentitie/fiches/images/"+filename+"1.png"));
                    Image image = new Image(file.toURI().toString());
                    double heightc = image.getHeight();
                    double widhtc = image.getWidth();
                    widhtc = widhtc/218;
                    heightc = heightc/widhtc;
                    int height = (int)heightc;
                    int width = (int)(image.getWidth()/widhtc);
                    ImageView.setImage(image);
                    ImageView.setFitHeight(height);
                    ImageView.setFitWidth(width);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                changedEvent(ChangeImage);
            }
        });

        icon.setOnMouseEntered(e -> icon.setFill(Color.rgb(0, 147, 14, 1)));
        icon.setOnMouseExited(e -> icon.setFill(Color.rgb(0, 147, 14, 0.25)));
        icon.setOnMouseClicked(e -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image (*.png), (*.jpg), (*.jpeg), (*.bmp)","*.png", "*.jpg", "*.jpeg", "*.bmp");
            fileChooser.getExtensionFilters().add(extensionFilter);
            File file = fileChooser.showOpenDialog(Main.primaryStage);

            if(file != null){
                ArrayList<String> filecount = new ArrayList<>();
                int count = 1;
                try {
                    while (new File(appdata+"/PersoIdentitie/fiches/images/"+filename+"_"+count+".png").exists()) {
                        filecount.add(String.valueOf(count));
                        count++;
                    }
                    AddImages.add(filename+"_"+(filecount.size()+1)+".png");
                    SaveImageAdd.add(filename+"_"+(filecount.size()+1)+".png");

                    BufferedImage bufferedimage = ImageIO.read(file);
                    ImageIO.write(bufferedimage, "png", new File(appdata+"/PersoIdentitie/fiches/images/"+filename+"_"+(filecount.size()+1)+".png"));

                    File Path = new File(appdata+"/PersoIdentitie/fiches/images/"+filename+"_"+(filecount.size()+1)+".png");
                    Image image = new Image(Path.toURI().toString());
                    StackPane pane = new StackPane();
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
                    image = new Image(Path.toURI().toString(), width, height, true, true);
                    img.setImage(image);
                    img.setPreserveRatio(true);
                    img.setFitWidth(width);
                    img.setFitHeight(height);
                    pane.setPrefWidth(width);
                    pane.setPrefHeight(height);
                    pane.getChildren().add(img);
                    ImageFlowPane.getChildren().add(ImageFlowPane.getChildren().size()-1, pane);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                changedEvent(ImageFlowPane);
            }
            setImageDeleteIcon();
        });

        PreferenceButton.setOnAction( e -> CustomDialogs.createEditingPreferenceDialog(this, filename, charactere, root));

        //TODO#################ListAddAction################

        AddPowersButton.setOnAction(e -> {
            Result = CustomDialogs.createInputStage("Ajouter un Pouvoir", "Nom du Pouvoir: ");
            if(Result != null && !Result.isEmpty()){
                if(PowersList.getItems().contains(Result))return;
                if(PowersList.getItems().contains("Aucun"))PowersList.getItems().remove("Aucun");
                PowersList.getItems().add(Result);
            }
        });

        RemovePowersButton.setOnAction(e -> {
            if(PowersList.getSelectionModel().getSelectedItem() != null){
                PowersList.getItems().remove(PowersList.getSelectionModel().getSelectedItem());
                if(PowersList.getItems().size() <= 0)PowersList.getItems().add("Aucun");
            }else{
                CustomDialogs.createErrorDialog("Error", "Aucun pouvoir n'a été sélectionné", root, JFXDialog.DialogTransition.CENTER);
            }
        });

        AddQualityButton.setOnAction(e -> {
            Result = CustomDialogs.createInputStage("Ajouter une Qualité", "Nom de la qualité: ");
            if(Result != null && !Result.isEmpty()){
                if(QualityList.getItems().contains(Result))return;
                if(QualityList.getItems().contains("Aucun"))QualityList.getItems().remove("Aucun");
                QualityList.getItems().add(Result);
            }
        });
        RemoveQualityButton.setOnAction(e -> {
            if(QualityList.getSelectionModel().getSelectedItem() != null){
                QualityList.getItems().remove(QualityList.getSelectionModel().getSelectedItem());
                if(QualityList.getItems().size() <= 0)QualityList.getItems().add("Aucun");
            }else{
                CustomDialogs.createErrorDialog("Error", "Aucune qualité n'a été sélectionnée", root, JFXDialog.DialogTransition.CENTER);
            }
        });

        AddDefaultButton.setOnAction(e -> {
            Result = CustomDialogs.createInputStage("Ajouter un Defaut", "Nom du défaut: ");
            if(Result != null && !Result.isEmpty()){
                if(DefaultList.getItems().contains(Result))return;
                if(DefaultList.getItems().contains("Aucun"))DefaultList.getItems().remove("Aucun");
                DefaultList.getItems().add(Result);
            }
        });
        RemoveDefaultButton.setOnAction(e -> {
            if(DefaultList.getSelectionModel().getSelectedItem() != null){
                DefaultList.getItems().remove(DefaultList.getSelectionModel().getSelectedItem());
                if(DefaultList.getItems().size() <= 0)DefaultList.getItems().add("Aucun");
            }else{
                CustomDialogs.createErrorDialog("Error", "Aucun defaut n'a été sélectionné", root, JFXDialog.DialogTransition.CENTER);
            }
        });

        AddOtherNameButton.setOnAction(e -> {
            Result = CustomDialogs.createInputStage("Ajouter un autres Prénom", "Nom du prénom: ");
            if(Result != null && !Result.isEmpty()){
                if(OtherNameList.getItems().contains(Result))return;
                if(OtherNameList.getItems().contains("Aucun"))OtherNameList.getItems().remove("Aucun");
                OtherNameList.getItems().add(Result);
            }
        });
        RemoveOtherNameButton.setOnAction(e -> {
            if(OtherNameList.getSelectionModel().getSelectedItem() != null){
                OtherNameList.getItems().remove(OtherNameList.getSelectionModel().getSelectedItem());
                if(OtherNameList.getItems().size() <= 0)OtherNameList.getItems().add("Aucun");
            }else{
                CustomDialogs.createErrorDialog("Error", "Aucun autre prénom n'a été sélectionné", root, JFXDialog.DialogTransition.CENTER);
            }
        });

        AddFamiliesButton.setOnAction(e -> {

                File file = new File(appdata+"/PersoIdentitie/fiches");
                File[] files = file.listFiles();
                ArrayList<String> tab = new ArrayList<>();
                if(files != null) {
                    for(int i = 0; i < files.length; i++) {
                        if(files[i].isDirectory() == true) {

                            tab.add(files[i].getName().replaceFirst(".yml", " ").trim());

                        }else if(files[i].isDirectory() == false) {
                            tab.add(files[i].getName().replaceFirst(".yml", " ").trim());
                        }
                    }
                }
                tab.remove("images");
                ArrayList<String> array = new ArrayList<>(FamiliesList.getItems().size());
                for (int i = 0; i < FamiliesList.getItems().size(); i++) {
                    array.add(FamiliesList.getItems().get(i));
                }
                if(array.contains("Aucun"))array.remove("Aucun");
                ArrayList<String> arrayremove = new ArrayList<String>(FamiliesList.getItems().size());
                for (int j = 0; j < array.size(); j++) {
                    String selectname = array.get(j).trim();
                    String[] selectnamelist = selectname.split(" ");
                    arrayremove.add(selectnamelist[0]+" "+selectnamelist[1]);
                }
                tab.removeAll(arrayremove);

                Result = CustomDialogs.createFindPersoDialog("Création d'un membre de famille");
            if(Result != null && !Result.chars().allMatch(Character::isWhitespace)) {
                String resulttype = CustomDialogs.createInputStage("Ajout d'un Membre de la famille", "Quel est le lien entre les deux personne ? (cousin,soeur...)");
                if(resulttype == null)return;
                Result = Result.trim();
                resulttype = resulttype.trim();
                if(Result != null && !Result.chars().allMatch(Character::isWhitespace) && resulttype != null && !resulttype.chars().allMatch(Character::isWhitespace)) {
                    if(FamiliesList.getItems().contains("Aucun"))FamiliesList.getItems().remove("Aucun");
                    String resultfinal = Result+" ("+resulttype+")";
                    FamiliesList.getItems().add(resultfinal);

                }
            }
        });

        RemoveFamiliesButton.setOnAction(e -> {
            if(FamiliesList.getSelectionModel().getSelectedItem() != null){
                FamiliesList.getItems().remove(FamiliesList.getSelectionModel().getSelectedItem());
                if(FamiliesList.getItems().size() <= 0)FamiliesList.getItems().add("Aucun");
            }else{
                CustomDialogs.createErrorDialog("Error", "Aucun membre de famille n'a été sélectionné", root, JFXDialog.DialogTransition.CENTER);
            }
        });

        AddOtherIdentitiesButton.setOnAction(e -> {
            Result = CustomDialogs.createInputStage("Ajouter une Identité", "Nom de l'identité et Prénom: ");
            boolean exist = false;

            if(Result != null){

                if(Result.chars().allMatch(Character::isWhitespace))return;
                if(Result.contains("(") || Result.contains(")")){
                    CustomDialogs.createErrorDialog("Erreur", "vous ne pouvez pas mettre de parenthèse dans le nom ou prénom de votre identité", root, JFXDialog.DialogTransition.TOP);
                    return;
                }
                if(!Result.matches("^[a-zA-Z]+[ \\-']?[[a-z]+[ \\-']?]*[a-zA-Z \\-]+$")){
                    CustomDialogs.createErrorDialog("Erreur", "Le Nom ou prénom n'entre pas dans les conventions donnée", root, JFXDialog.DialogTransition.TOP);
                    return;
                }

                if(OtherIdentitiesList.getItems().contains(Result))exist = true;

                if(RemoveOtherIdentities.contains(Result)){
                    RemoveOtherIdentities.remove(Result);
                    OtherIdentitiesList.getItems().add(Result);
                    return;
                }
                if(!exist) {
                    Charactere charactere = new Charactere("(" + filename + ")" + Result);
                    charactere.CreateFile();
                    CustomDialogs.createValideDialog("Identité crée !", "l'identité "+ Result + " à bien été crée", root, JFXDialog.DialogTransition.TOP);
                    if(OtherIdentitiesList.getItems().contains("Aucun"))OtherIdentitiesList.getItems().remove("Aucun");
                    OtherIdentitiesList.getItems().add(Result);
                    AddOtherIdentities.add(Result);

                    try {
                        createOtherIdentitieeditedonglet(Result);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }else {

                    CustomDialogs.createErrorDialog("Erreur", "l'identité" + Result + " existe déjà ", root, JFXDialog.DialogTransition.TOP);
                }
            }

        });
        RemoveOtherIdentitiesButton.setOnAction(e -> {
            String name = OtherIdentitiesList.getSelectionModel().getSelectedItem();
            if(OtherIdentitiesList.getSelectionModel().getSelectedItem() != null){
                OtherIdentitiesList.getItems().remove(OtherIdentitiesList.getSelectionModel().getSelectedItem());
                if(OtherIdentitiesList.getItems().size() <= 0)OtherIdentitiesList.getItems().add("Aucun");
                if(AddOtherIdentities.contains(name)){
                    AddOtherIdentities.remove(name);
                    File file = new File(appdata+"/PersoIdentitie/fiches/("+filename+")"+name+".yml");
                    file.delete();
                }else{
                    RemoveOtherIdentities.add(name);
                }

            }else{
                CustomDialogs.createErrorDialog("Error", "Aucune autre identité n'a été sélectionné", root, JFXDialog.DialogTransition.CENTER);
            }
        });
        editOtherIdentitie.setOnAction(e -> {
            if(OtherIdentitiesList.getSelectionModel().getSelectedItem() != null && !OtherIdentitiesList.getSelectionModel().getSelectedItem().equalsIgnoreCase("Aucun")){
                try {
                    createOtherIdentitieeditedonglet(OtherIdentitiesList.getSelectionModel().getSelectedItem());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }else{
                CustomDialogs.createErrorDialog("Error", "Aucune identité n'a été sélectionnée", root, JFXDialog.DialogTransition.CENTER);
            }
        });

        //TODO#############OTHERACTION##########################

        TagsView.getChips().addListener((ListChangeListener.Change<? extends String> change) -> {
            while (change.next()){
                if(change.wasAdded()){
                    if(TagslistSuggestion.contains(TagsView.getChips().get(change.getFrom()))){
                        TagslistSuggestion.remove(TagsView.getChips().get(change.getFrom()));
                        Tagslist.add(TagsView.getChips().get(change.getFrom()));
                        TagsView.getSuggestions().setAll(TagslistSuggestion);
                        changedEvent(TagsView);
                    }else{
                        TagsView.getChips().remove(change.getFrom());
                    }

                } else if(change.wasRemoved()){
                    List<String> removed = new ArrayList<>(Tagslist);
                    removed.removeAll(TagsView.getChips());
                    TagslistSuggestion.add(removed.get(0));
                    Tagslist.remove(removed.get(0));
                    TagsView.getSuggestions().setAll(TagslistSuggestion);
                    changedEvent(TagsView);
                }
            }
        });

        for (Node node:componentsList) {
            if(node instanceof JFXTextField)((JFXTextField) node).textProperty().addListener(e -> changedEvent(node));
            if(node instanceof JFXListView)((JFXListView) node).getItems().addListener((InvalidationListener) e -> changedEvent(node));
            if(node instanceof TextArea)((TextArea) node).textProperty().addListener(e -> changedEvent(node));
            if(node instanceof JFXTextArea)((JFXTextArea) node).textProperty().addListener(e -> changedEvent(node));
            if(node instanceof JFXComboBox)((JFXComboBox<?>) node).valueProperty().addListener(e -> changedEvent(node));
        }

        mainController.button_Save.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if(!mainController.tabPane.getSelectionModel().getSelectedItem().getText().equalsIgnoreCase(filename + " [Edit]"))return;
            Save();
            setEditingProperty(false);
            System.out.println("called");
            mainController.button_Save.setDisable(true);
        });


    }

    private void createOtherIdentitieeditedonglet(String fileprofil) throws IOException {
        FXMLLoader panel = new FXMLLoader(FileClassPath.load("OtherIdentitieTabEdit.fxml", FileClassPath.Type.FXML));
        String realFileProfil = "(" + filename + ")" + fileprofil;
        BorderPane panelOnglet = panel.load();
        OtherIdentitieTabEditController tabController = panel.getController();
        tabController.initData(realFileProfil, root, mainController);
        Tab tab = new Tab();
        tab.setText(fileprofil+" [Edit]");
        tab.setContent(panelOnglet);
        tab.selectedProperty().addListener(
                (newValue) -> {
                    if(tab.isSelected())mainController.button_Save.setDisable(!tabController.getEditingProperty());
                });
        mainController.tabPane.getTabs().add(tab);
        mainController.tabPane.setTabClosingPolicy(javafx.scene.control.TabPane.TabClosingPolicy.ALL_TABS);
        tab.setOnCloseRequest(e -> tabController.closeRequest(e));
        mainController.tabPane.autosize();
        SingleSelectionModel<Tab> singleSelectionModel = mainController.tabPane.getSelectionModel();
        singleSelectionModel.select(mainController.tabPane.getTabs().size()-1);
    }

    private void setImageDeleteIcon(){
        for (int i = 0; i < ImageFlowPane.getChildren().size() ; i++) {
            if(ImageFlowPane.getChildren().get(i) instanceof StackPane) {
                StackPane pane = (StackPane) ImageFlowPane.getChildren().get(i);

                FontAwesomeIconView iconview = new FontAwesomeIconView(FontAwesomeIcon.USER_TIMES);
                iconview.setFill(Color.rgb(173, 0, 0, 0.75));
                iconview.setSize("40");

                FontAwesomeIconView iconview2 = new FontAwesomeIconView(FontAwesomeIcon.USER_PLUS);
                iconview2.setFill(Color.rgb(0, 147, 14, 1));
                iconview2.setSize("40");

                pane.setOnMouseEntered(e -> {
                    if(pane.getChildren().get(0).getOpacity() == 0.25)pane.getChildren().add(iconview2);
                    if(pane.getChildren().get(0).getOpacity() == 1)pane.getChildren().add(iconview);
                });
                pane.setOnMouseExited(e -> {
                    if(pane.getChildren().get(0).getOpacity() == 0.25)pane.getChildren().removeAll(iconview2);
                    if(pane.getChildren().get(0).getOpacity() == 1)pane.getChildren().removeAll(iconview);
                });

                //TODO########ACTION#################

                pane.setOnMouseClicked(e -> {
                    if(pane.getChildren().get(0).getOpacity() == 1) {
                        for (int j = 0; j < ImageFlowPane.getChildren().size(); j++) {
                            if (ImageFlowPane.getChildren().get(j).equals(pane)){
                                RemoveImages.add(filename + "_" + (j+1) + ".png");
                                SaveImageRemove.add(filename + "_" + (j+1) + ".png");
                            }
                        }

                        if(pane.getChildren().size() >= 2)pane.getChildren().get(1).setOpacity(0.25);
                        if(pane.getChildren().size() >= 1)pane.getChildren().get(0).setOpacity(0.25);
                        pane.getChildren().add(iconview2);
                    }else{
                        for (int j = 0; j < ImageFlowPane.getChildren().size(); j++) {
                            if (ImageFlowPane.getChildren().get(j).equals(pane)) {
                                RemoveImages.remove(filename + "_" + (j + 1) + ".png");
                                SaveImageRemove.remove(filename + "_" + (j + 1) + ".png");
                            }
                        }

                        if(pane.getChildren().size() >= 3)pane.getChildren().remove(2);
                        if(pane.getChildren().size() >= 2)pane.getChildren().get(1).setOpacity(1);
                        if(pane.getChildren().size() >= 2)pane.getChildren().remove(1);
                        if(pane.getChildren().size() >= 1)pane.getChildren().get(0).setOpacity(1);
                        pane.getChildren().add(iconview);
                    }
                    changedEvent(ImageFlowPane);
                });
            }
        }
    }

    private List getTagList(){

        List<String> list = new ArrayList<>();
        YamlFile file = new YamlFile(appdata+"/PersoIdentitie/Stockage/etiquette.yml");
        try {
            file.load();
            list = file.getStringList("listettiquette");
        }catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private void saveAllComponent() {
        componentsList.add(NameLabel);
        componentsList.add(PrenameLabel);
        componentsList.add(MarriedNameLabel);
        componentsList.add(BornCityLabel);
        componentsList.add(DeathCityLabel);
        componentsList.add(RaceLabel);
        componentsList.add(AffiliateElementLabel);
        componentsList.add(AstrologicalSignLabel);
        componentsList.add(LunarSignLabel);
        componentsList.add(AscendingLabel);
        componentsList.add(ChineseSignLabel);
        componentsList.add(AnimalTotemLabel);
        componentsList.add(PlanetLabel);
        componentsList.add(MainResidencePlaceLabel);
        componentsList.add(DeathDateLabel);
        componentsList.add(BornDateLabel);

        componentsList.add(FamiliesList);
        componentsList.add(OtherNameList);
        componentsList.add(DefaultList);
        componentsList.add(QualityList);
        componentsList.add(PowersList);
        componentsList.add(OtherIdentitiesList);

        componentsList.add(CaractereArena);
        componentsList.add(StoryArena);
        componentsList.add(PhysicalCaracteristicsArena);
        componentsList.add(StoryRoleArena);
        componentsList.add(OtherThingsArena);
        componentsList.add(PhysicalAbilityArena);
        componentsList.add(TypeDetailsArena);

        componentsList.add(GenreComboBox);
        componentsList.add(LevelComboBox);
        componentsList.add(DecanComboBox);

    }

    public void closeRequest(Event e) {
        if(getEditingProperty()){
            String[] content = new String[]{"Fermer sans enregistrer", "Fermer et enregistrer", "Annuler"};
           int result = CustomDialogs.createQuestionStage("Voulez vous fermer cet Onglet ?", "Les changement n'ont pas été sauvegarder voulez vous enregistrer ?", content);
           switch (result){
               case 0:
                   closeNoSave();
                   if(mainController.tabPane.getTabs().size() <= 1)mainController.button_Save.setDisable(true);
               break;
               case 1:
                   Save();
                   if(mainController.tabPane.getTabs().size() <= 1)mainController.button_Save.setDisable(true);
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
        charactere.saveProperties(this);
        String oldFileName = filename;

        /*################When Name Changed#################*/
        boolean changename = !filename.equalsIgnoreCase(PrenameLabel.getText()+" "+NameLabel.getText());
        if(changename){
            filename = PrenameLabel.getText() +" "+ NameLabel.getText();
            if(filename.matches("^[a-zA-Z]+[ \\-']?[[a-z]+[ \\-']?]*[a-zA-Z \\-]+$")) {
                charactere.FileName = filename;
                mainController.tabPane.getTabs().get(mainController.tabPane.getAccessibleText().indexOf(oldFileName + "[Edit]")).setText(filename + "[Edit]");
                File file = new File(appdata + "/PersoIdentitie/fiches/" + oldFileName + ".yml");
                File rename = new File(appdata + "/PersoIdentitie/fiches/" + filename + ".yml");
                file.renameTo(rename);
                YamlFile yamlFile = new YamlFile(appdata+"/PersoIdentitie/Stockage/etiquette.yml");
                try {
                    yamlFile.load();
                    for (String str : yamlFile.getStringList("listettiquette")) {
                        ArrayList array = new ArrayList(yamlFile.getStringList(str));
                        if (array.contains(oldFileName)) {
                            array.remove(oldFileName);
                            array.add(filename);
                        }
                        yamlFile.set(str, array);
                    }
                    yamlFile.save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /*################Image#############*/
        File file = new File(appdata+"/PersoIdentitie/fiches/images/"+oldFileName+"1.png");
        File rename = new File(appdata+"/PersoIdentitie/fiches/images/"+filename+".png");
        if(file.exists()){
            rename.delete();
            file.renameTo(rename);
        }
        /*#############GALERIE##############*/
        ArrayList<String> array = new ArrayList();
        File[] files = new File(appdata+"/PersoIdentitie/fiches/images/").listFiles();
        for (File arg : files) {
            if(arg.isFile()) {
                if(arg.getName().startsWith(oldFileName) && arg.getName().contains("_"))array.add(arg.getName());
            }
        }
        int number = 1;
        for (String str:array) {
            if(!RemoveImages.contains(str)){
                File oldname = new File(appdata+"/PersoIdentitie/fiches/images/"+str);
                File newname = new File(appdata+"/PersoIdentitie/fiches/images/"+oldFileName+"_"+number+".png");
                if(changename)newname = new File(appdata+"/PersoIdentitie/fiches/images/"+filename+"_"+number+".png");
                oldname.renameTo(newname);
                number++;
            }else{
                File removingfile = new File(appdata+"/PersoIdentitie/fiches/images/"+str);
                removingfile.delete();
            }
        }


    }

    private void closeNoSave() {
        //#####################IMAGE#####################
        File file = new File(appdata+"/PersoIdentitie/fiches/images/"+filename+"1.png");
        if(file.exists()){
            file.delete();
        }
        //####################OTHERIDENTITIE#############
        if(!AddOtherIdentities.isEmpty()){
            for (String str:AddOtherIdentities){
                file = new File(appdata+"/PersoIdentitie/fiches/("+filename+")"+str+".yml");
                file.delete();
            }
        }
        //##################GALERIE##################
        if(!AddImages.isEmpty()){
            for (String str:AddImages){
                file = new File(appdata+"/PersoIdentitie/fiches/images/"+str);
                file.delete();
            }
        }

    }

    public boolean ifRealChange(){
        boolean changed = false;
        if(!NameLabel.getText().equalsIgnoreCase(charactere.StringVar.get("Name")))changed = true;
        if(!PrenameLabel.getText().equalsIgnoreCase(charactere.StringVar.get("Prename")))changed = true;
        if(!MarriedNameLabel.getText().equalsIgnoreCase(charactere.StringVar.get("MarriedName")))changed = true;
        if(!BornCityLabel.getText().equalsIgnoreCase(charactere.StringVar.get("BornCity")))changed = true;
        if(!DeathCityLabel.getText().equalsIgnoreCase(charactere.StringVar.get("DeathCity")))changed = true;
        if(!RaceLabel.getText().equalsIgnoreCase(charactere.StringVar.get("Race")))changed = true;
        if(!AffiliateElementLabel.getText().equalsIgnoreCase(charactere.StringVar.get("AffiliateElement")))changed = true;
        if(!AstrologicalSignLabel.getText().equalsIgnoreCase(charactere.StringVar.get("AstrologicalSign")))changed = true;
        if(!LunarSignLabel.getText().equalsIgnoreCase(charactere.StringVar.get("LunarSign")))changed = true;
        if(!AscendingLabel.getText().equalsIgnoreCase(charactere.StringVar.get("Ascanding")))changed = true;

        if(!ChineseSignLabel.getText().equalsIgnoreCase(charactere.StringVar.get("ChineseSign")))changed = true;
        if(!AnimalTotemLabel.getText().equalsIgnoreCase(charactere.StringVar.get("AnimalTotem")))changed = true;
        if(!PlanetLabel.getText().equalsIgnoreCase(charactere.StringVar.get("Planet")))changed = true;
        if(!MainResidencePlaceLabel.getText().equalsIgnoreCase(charactere.StringVar.get("MainResidencePlace")))changed = true;
        if(!DeathDateLabel.getText().equalsIgnoreCase(charactere.StringVar.get("DeathDate")))changed = true;
        if(!BornDateLabel.getText().equalsIgnoreCase(charactere.StringVar.get("BornDate")))changed = true;

        if(!FamiliesList.getItems().equals(charactere.ListVar.get("Families")))changed = true;
        if(!OtherNameList.getItems().equals(charactere.ListVar.get("OthersNames")))changed = true;
        if(!DefaultList.getItems().equals(charactere.ListVar.get("Default")))changed = true;
        if(!QualityList.getItems().equals(charactere.ListVar.get("Quality")))changed = true;
        if(!PowersList.getItems().equals(charactere.ListVar.get("Powers")))changed = true;
        if(!OtherIdentitiesList.getItems().equals(charactere.ListVar.get("OtherIdentities")))changed = true;

        if(!CaractereArena.getText().equalsIgnoreCase(charactere.StringVar.get("Caractere")))changed = true;
        if(!StoryArena.getText().equalsIgnoreCase(charactere.StringVar.get("Story")))changed = true;
        if(!PhysicalCaracteristicsArena.getText().equalsIgnoreCase(charactere.StringVar.get("PhysicalCaracteristics")))changed = true;
        if(!StoryRoleArena.getText().equalsIgnoreCase(charactere.StringVar.get("StoryRole")))changed = true;
        if(!OtherThingsArena.getText().equalsIgnoreCase(charactere.StringVar.get("OtherThings")))changed = true;
        if(!PhysicalAbilityArena.getText().equalsIgnoreCase(charactere.StringVar.get("PhysicalAbility")))changed = true;
        if(!TypeDetailsArena.getText().equalsIgnoreCase(charactere.StringVar.get("TypeDetails")))changed = true;

        if(GenreComboBox.getValue() != null)if (!GenreComboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase(charactere.StringVar.get("Gender"))) changed = true;
        if(LevelComboBox.getValue() != null)if (!LevelComboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase(charactere.StringVar.get("Level"))) changed = true;
        if(DecanComboBox.getValue() != null)if (!DecanComboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase(charactere.StringVar.get("Decan"))) changed = true;


        if(!TagsView.getChips().equals(charactere.ListVar.get("Tags")))changed = true;
        if(!SaveImageRemove.isEmpty() && !SaveImageAdd.containsAll(SaveImageRemove))changed = true;
        if(!SaveImageRemove.containsAll(SaveImageAdd) && !SaveImageAdd.isEmpty())changed = true;
        if (new File(appdata+"/PersoIdentitie/fiches/images/"+filename+"1.png").exists())changed = true;

        if(!charactere.Preferences.equals(charactere.NewPreferences))changed = true;
        return changed;
    }

    public void changedEvent(Node node){
        log.add(node.getId());
        setEditingProperty(ifRealChange());
        mainController.button_Save.setDisable(!ifRealChange());
    }

    public void setEditingProperty(boolean bool){
        edited = bool;
    }

    public boolean getEditingProperty(){
        return edited;
    }

    public boolean isValidFormat(String date){
        boolean valid;
        try {
            LocalDate.parse(date, formatter);
            valid = true;
        }catch (DateTimeParseException e){
            e.printStackTrace();
            valid = false;
        }
        return valid;
    }
}
