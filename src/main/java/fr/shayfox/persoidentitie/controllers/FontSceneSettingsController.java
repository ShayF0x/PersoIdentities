package fr.shayfox.persoidentitie.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import fr.shayfox.persoidentitie.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FontSceneSettingsController implements Initializable {

    @FXML
    public Label HightText, MediumText, NormalText, SubTitleText, lettersText, lettersCapitalText, numberText, caractersText;
    @FXML
    public JFXTextArea arenaText;
    @FXML
    public JFXComboBox chooseFont;
    @FXML
    public JFXTextField TextField;
    @FXML
    public JFXCheckBox fontBoldCheckBox, fontItalicCheckBox;
    @FXML
    public VBox VboxContenant;

    private List<String> fontList, stylesList;
    private String font, defaultFont;
    private boolean isBold, isItalic, isDefaultBold, isDefaultItalic;

    private static final String appdata = System.getenv("APPDATA");
    private File fontCssFile = new File(appdata+"/PersoIdentitie/Stockage/themes/Font.css");


    private ThemeManagerTabController managerTabController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void initData(ThemeManagerTabController managerTabController){
        this.managerTabController = managerTabController;
        initVar();
        initComponent();
    }

    public void initVar(){
        fontList = new ArrayList<>(Font.getFamilies());
        stylesList = new ArrayList<>();

        try {

            /*################################
            TODO#########FileReader###########
            ##################################
             */
            List<String> list = Files.readAllLines(Paths.get(fontCssFile.getAbsolutePath().replace("\\", "/")), StandardCharsets.UTF_8);
            list.remove(0);
            list.remove(list.size()-1);
            String text = String.join("\n", list);


            /*################################
            TODO#########Family###########
            ##################################
             */
            Pattern p = Pattern.compile("-fx-font-family: \".+\";", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(list.get(0));
            if(m.find()){
                defaultFont = m.group().replaceAll("-fx-font-family: \"", "").replaceAll("\";", "");
                font = defaultFont;
                stylesList.add(m.group());
            }

            /*##########################
            TODO#########BOLD###########
            ############################
             */
            p = Pattern.compile("-fx-font-weight: bold;", Pattern.CASE_INSENSITIVE);
            m = p.matcher(text);
            if(m.find()){
                isDefaultBold = true;
                stylesList.add(m.group());
            }else{
                isDefaultBold = false;
            }
            isBold = isDefaultBold;


            /*############################
            TODO#########ITALIC###########
            ##############################
             */
            p = Pattern.compile("-fx-font-style: italic;", Pattern.CASE_INSENSITIVE);
            m = p.matcher(text);
            if(m.find()){
                isDefaultItalic = true;
                stylesList.add(m.group());
            }else{
                isDefaultItalic = false;
            }
            isItalic = isDefaultItalic;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initComponent(){
        actualizedAll();

        fontBoldCheckBox.setSelected(isBold);
        fontItalicCheckBox.setSelected(isItalic);

        chooseFont.getItems().addAll(fontList);
        if(font != null){
            chooseFont.getSelectionModel().select(font);
        }

        chooseFont.setOnAction(action -> {
            stylesList.remove("-fx-font-family: \""+font+"\";");
            font = chooseFont.getSelectionModel().getSelectedItem().toString();
            stylesList.add("-fx-font-family: \""+font+"\";");
            actualizedAll();
            managerTabController.setEditingProperty(valueChange());
        });

        fontBoldCheckBox.setOnAction(action -> {
            isBold = fontBoldCheckBox.isSelected();
            if(fontBoldCheckBox.isSelected()){
                stylesList.add("-fx-font-weight: bold;");
            }else{
                stylesList.remove("-fx-font-weight: bold;");
            }
            actualizedAll();
            managerTabController.setEditingProperty(valueChange());
        });
        fontItalicCheckBox.setOnAction(action -> {
            isItalic = fontItalicCheckBox.isSelected();
            if(fontItalicCheckBox.isSelected()){
                stylesList.add("-fx-font-style: italic;");
            }else{
                stylesList.remove("-fx-font-style: italic;");
            }
            actualizedAll();
            managerTabController.setEditingProperty(valueChange());
        });
    }

    public void actualizedAll(){
        String mainstyle = String.join("", stylesList);
        String fontStyle = "-fx-font-family: \""+font+"\";";

        VboxContenant.setStyle(mainstyle);
        HightText.setStyle(fontStyle);
        MediumText.setStyle(fontStyle);
        NormalText.setStyle(fontStyle);
        SubTitleText.setStyle(fontStyle);
        lettersText.setStyle(fontStyle);
        lettersCapitalText.setStyle(fontStyle);
        numberText.setStyle(fontStyle);
        caractersText.setStyle(fontStyle);
        arenaText.setStyle(fontStyle);
        TextField.setStyle(fontStyle);
    }

    public boolean valueChange(){

        if(isBold!=isDefaultBold)return true;
        if(isItalic!=isDefaultItalic)return true;
        if(!font.equalsIgnoreCase(defaultFont))return true;

        return false;
    }

    public void save(){
        try {
        FileWriter write = new FileWriter(fontCssFile.getAbsolutePath().replace("\\", "/"), false);

        PrintWriter print_line = new PrintWriter(write);
        print_line.print(".root .text {\n"+String.join("\n\t", stylesList)+"\n}");
        print_line.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Main.setTheme();
    }
}
