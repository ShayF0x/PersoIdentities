package fr.shayfox.persoidentitie;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import fr.shayfox.persoidentitie.controllers.MainController;
import fr.shayfox.persoidentitie.updater.Updater;
import fr.shayfox.persoidentitie.utils.FileClassPath;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main extends Application {
    public static Stage primaryStage;
    private static final String APPDATA = System.getenv("APPDATA");

    private static final String defaultTheme = "DarkTheme";


    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
        LoadFiles();

        new Updater(null);

        System.out.println("All File has been load !");
        FXMLLoader fxmlLoader = new FXMLLoader(FileClassPath.load("Main.fxml", FileClassPath.Type.FXML));
        @NotNull
        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        root.getStyleClass().add("borderPane");
        primaryStage.setTitle("PersoIdentities");

        Image icon = new Image(FileClassPath.load("23924.png", FileClassPath.Type.IMAGE).toString(), 26, 26, true, true);
        primaryStage.getIcons().add(icon);

        primaryStage.setMinHeight(Screen.getPrimary().getBounds().getHeight()/1080*967);
        primaryStage.setMinWidth(Screen.getPrimary().getBounds().getWidth()/1920*1686);
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal.intValue()<=1871){
                if(mainController.checkbox_selectionlist.isSelected()) {
                    mainController.checkbox_selectionlist.setSelected(false);
                    mainController.checkbox_selectionlist.fire();
                }
            }
        });

        JFXDecorator decorator = new JFXDecorator(primaryStage, root, true, true, true);
        setDecoratorStyle(decorator, icon);

        Scene scene = new Scene(decorator, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
        primaryStage.setScene(scene);
        setTheme();

        primaryStage.show();

    }

    public static void setTheme() {
        String applyTheme = defaultTheme;
        List<String> themeList = listDirectory(APPDATA +"/PersoIdentitie/Stockage/themes", ".css");
        themeList.remove("Font");

        Properties prop = null;
        try {
            prop = MainController.load(APPDATA +"/PersoIdentitie/Stockage/config.properties");

            if(prop.getProperty("theme") != null && themeList.contains(prop.getProperty("theme"))) {
                applyTheme = prop.getProperty("theme");
            }else{
                prop.setProperty("theme", applyTheme);
                prop.store(new FileOutputStream(APPDATA +"/PersoIdentitie/Stockage/config.properties"), "config");
            }
         } catch (IOException e) {
            e.printStackTrace();
        }

        if(primaryStage.getScene().getStylesheets().size() > 0){
            primaryStage.getScene().getStylesheets().clear();
        }

        File cssFile = new File(APPDATA +"/PersoIdentitie/Stockage/themes/"+applyTheme+".css");
        File fontCssFile = new File(APPDATA +"/PersoIdentitie/Stockage/themes/Font.css");
        primaryStage.getScene().getStylesheets().add("file:///"+fontCssFile.getAbsolutePath().replace("\\", "/"));
        primaryStage.getScene().getStylesheets().add("file:///"+cssFile.getAbsolutePath().replace("\\", "/"));
    }

    private void setDecoratorStyle(JFXDecorator decorator, Image icon) {
        decorator.setMaximized(true);
        decorator.getStylesheets().add(FileClassPath.load("Decorator.css", FileClassPath.Type.CSS).toString());
        HBox child = (HBox) decorator.getChildren().get(0);
        child.getChildren().remove(1);
        for (int i = 0; i < child.getChildren().size(); i++) {
            if(child.getChildren().get(i) instanceof JFXButton){
                JFXButton button = (JFXButton) child.getChildren().get(i);
                SVGGlyph glyph = (SVGGlyph) button.getGraphic();
                glyph.setFill(Color.rgb(159, 156, 174));
            }
        }

        JFXButton button = (JFXButton) child.getChildren().get(2);
        button.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> new Thread(() -> {
            try {
                Thread.sleep(1);
                Platform.runLater(() -> {
                    HBox child1 = (HBox) decorator.getChildren().get(0);
                    JFXButton button1 = (JFXButton) child1.getChildren().get(2);
                    SVGGlyph glyph = (SVGGlyph) button1.getGraphic();
                    glyph.setFill(Color.rgb(159, 156, 174));
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start());

        decorator.setGraphic(new ImageView(icon));

    }

    public static void close(){
        primaryStage.close();
    }

    private void LoadFiles() {
        createFile("/PersoIdentitie");
        createFile("/PersoIdentitie/fiches");
        createFile("/PersoIdentitie/Stockage");
        createFile("/PersoIdentitie/fiches/images");
        createFile("/PersoIdentitie/Stockage/themes");
        createFileTheme();
        createFileJar();
        createEtiquetteConfig();
        createConfig();
    }

    private void createFileJar() {
        try {
            File updaterFile = new File(APPDATA +"/PersoIdentitie/Updater.jar");

            copyResource("apps/Updater.jar", updaterFile.getAbsolutePath(), Main.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*#########################################
	  #MÉTHODE POUR CRÉE LES FICHIER DE THEMES#
	  #########################################*/
    private static void createFileTheme() {
        try {
            File darkFile = new File(APPDATA +"/PersoIdentitie/Stockage/themes/DarkTheme.css");
            File lightFile = new File(APPDATA +"/PersoIdentitie/Stockage/themes/LightTheme.css");
            File fontFile = new File(APPDATA +"/PersoIdentitie/Stockage/themes/Font.css");
            //Main.class.getClassLoader().getResource("resources/images/23924.png").toString()
            copyResource("themes/DarkTheme.css", darkFile.getAbsolutePath(), Main.class);
            copyResource("themes/LightTheme.css", lightFile.getAbsolutePath(), Main.class);
            copyResource("themes/Font.css", fontFile.getAbsolutePath(), Main.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void copyResource(String res, String dest, Class c) throws IOException{
        InputStream src = Main.class.getClassLoader().getResourceAsStream(res);
        Files.copy(src, Paths.get(dest), StandardCopyOption.REPLACE_EXISTING);
    }

    /*##########################################
      #MÉTHODE POUR CRÉE LE FICHIER D'ETIQUETTE#
      ##########################################*/
    private static void createEtiquetteConfig() {
        File dir = new File(APPDATA +"/PersoIdentitie/Stockage/etiquette.yml");
        String content = "listettiquette:";
        if(dir.exists())return;
        try {
            dir.createNewFile();
            FileWriter fw = new FileWriter(dir.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
	/*##########################################
	  ##########################################
	  ##########################################*/



    /*##########################################
      ##M�THODE POUR CR�E LE FICHIER DE CONFIG##
      ##########################################*/
    private static void createConfig() {
        File dir = new File(APPDATA +"/PersoIdentitie/Stockage/config.properties");
        String content = "affichagefichier=true\ntheme=DarkTheme";
        if(dir.exists())return;
        try {
            dir.createNewFile();
            FileWriter fw = new FileWriter(dir.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
	/*##########################################
	  ##########################################
	  ##########################################*/


    /*##########################################
     M�THODE POUR CR�E LE FICHIER PERSOIDENTITIES ET FICHES
      ##########################################*/
    private void createFile(String file) {
        File dir = new File(APPDATA +file);
        if (dir.exists())return;
        dir.mkdirs();
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

    public static String getCurrentTheme(){
        String applyTheme = defaultTheme;
        List<String> themeList = listDirectory(APPDATA +"/PersoIdentitie/Stockage/themes", ".css");
        themeList.remove("Font");


        try {
            Properties prop = MainController.load(APPDATA +"/PersoIdentitie/Stockage/config.properties");

            if(prop.getProperty("theme") != null && themeList.contains(prop.getProperty("theme"))) {
                applyTheme = prop.getProperty("theme");
            }else{
                prop.setProperty("theme", applyTheme);
                prop.store(new FileOutputStream(APPDATA +"/PersoIdentitie/Stockage/config.properties"), "config");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return applyTheme;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
