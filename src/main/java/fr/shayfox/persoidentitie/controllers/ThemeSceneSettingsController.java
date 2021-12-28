package fr.shayfox.persoidentitie.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import fr.shayfox.persoidentitie.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThemeSceneSettingsController implements Initializable {

    @FXML
    public JFXComboBox themeComboBox, testCombobox;
    @FXML
    public JFXListView testListView;
    @FXML
    public VBox vBox;

    private static final String appdata = System.getenv("APPDATA");
    private ThemeManagerTabController managerTabController;

    private List<String> themesList = new ArrayList<>();
    private String currentTheme = "DarkTheme";
    private String defaultTheme;
    private String oldTheme;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void initData(ThemeManagerTabController managerTabController){
        this.managerTabController = managerTabController;
        initVar();
        initComponent();
    }

    public void initVar(){
        themesList = listDirectory(appdata+"/PersoIdentitie/Stockage/themes", ".css");
        themesList.remove("Font");

        try {
            Properties prop =  new Properties();
            InputStream inputStream = new FileInputStream(appdata+"/PersoIdentitie/Stockage/config.properties");
            prop.load(inputStream);

            if(prop.getProperty("theme") != null && themesList.contains(prop.getProperty("theme"))) {
                currentTheme = prop.getProperty("theme");
            }else{
                prop.setProperty("theme", currentTheme);
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        defaultTheme = currentTheme;
        oldTheme = currentTheme;
    }

    public void initComponent(){

        themeComboBox.getItems().addAll(themesList);
        if(currentTheme != null){
            themeComboBox.getSelectionModel().select(currentTheme);
        }

        testCombobox.setPromptText("Lettres");
        testCombobox.getItems().addAll(new String[]{"S","h","a","y","F","o","x"});
        testCombobox.getSelectionModel().select(0);

        testListView.getItems().addAll(new String[]{"Valeur 1", "Valeur 2", "Valeur 3", "Valeur 18062021"});

        themeComboBox.setOnAction(action -> {
            oldTheme = currentTheme;
            currentTheme = themeComboBox.getSelectionModel().getSelectedItem().toString();
            changeValue();
        });
    }

    public void actualizedAll(){
        File cssFile = new File(appdata+"/PersoIdentitie/Stockage/themes/"+currentTheme+".css");
        vBox.getStylesheets().add("file:///"+cssFile.getAbsolutePath().replace("\\", "/"));
    }

    public void removedStyle(String value){
        File cssFile = new File(appdata+"/PersoIdentitie/Stockage/themes/"+value+".css");
        vBox.getStylesheets().remove("file:///"+cssFile.getAbsolutePath().replace("\\", "/"));
    }

    public void changeValue(){
        removedStyle(oldTheme);
        actualizedAll();
        managerTabController.setEditingProperty(valueAsChanged());
    }


    public boolean valueAsChanged(){

        if(!currentTheme.equalsIgnoreCase(defaultTheme))return true;

        return false;
    }

    public void save(){
        defaultTheme = currentTheme;

        try {
            Properties prop =  new Properties();
            InputStream inputStream = new FileInputStream(appdata+"/PersoIdentitie/Stockage/config.properties");
            prop.load(inputStream);

            prop.setProperty("theme", currentTheme);
            prop.store(new FileOutputStream(appdata+"/PersoIdentitie/Stockage/config.properties"), "config");

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Main.setTheme();
    }

    @SuppressWarnings("rawtypes")
    private static ArrayList listDirectory (String dir, String ext) {
        File file = new File(dir);
        File[] files = file.listFiles();
        ArrayList<String> tab = new ArrayList<>();
        if(files != null) {
            for (File value : files) {
                if (!value.isDirectory()) {
                    if(!value.getName().matches("^[\\(\\)].*$")) tab.add(value.getName().replaceFirst(ext, " ").trim());
                }
            }
        }

        return tab;


    }
}
