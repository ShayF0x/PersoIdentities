package fr.shayfox.persoidentitie.utils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import fr.shayfox.persoidentitie.Main;
import fr.shayfox.persoidentitie.controllers.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CustomDialogs {

    private static String Result;
    private static String[] ResultList;
    private static  int intResult;
    private static DialogController dialogController;
    private static final String appdata = System.getenv("APPDATA");
    private static File fontCssFile = new File(appdata+"/PersoIdentitie/Stockage/themes/Font.css");

    public static String createInputStage(String Title, String question) {
        return createInputStage(Title, question, null);
    }

    public static String[] createPersoInputStage(String Title) {
        Stage stage;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FileClassPath.load("PersoInputDialog.fxml", FileClassPath.Type.FXML));

            StackPane parent = fxmlLoader.load();

            parent.getStylesheets().add(FileClassPath.load("CustomDialog.css", FileClassPath.Type.FXML).toString());
            PersoInputDialogController dialogController = fxmlLoader.getController();

            Image img = new Image(FileClassPath.load("23924.png", FileClassPath.Type.IMAGE).toString());
            dialogController.Icon.setImage(img);
            dialogController.Title.setText(Title);

            dialogController.mainroot.autosize();
            dialogController.root.autosize();

            double Height = dialogController.root.getHeight();
            double Width = dialogController.root.getWidth();

            parent.setBackground(new Background(new BackgroundFill(Color. rgb(0, 44, 175), null, new Insets((Main.primaryStage.getHeight() - Height) / 2 + 1, (Main.primaryStage.getWidth() - Width) / 2 + 1, (Main.primaryStage.getHeight() - Height) / 2 + 1, (Main.primaryStage.getWidth() - Width) / 2 + 1))));
            parent.setPadding(new Insets((Main.primaryStage.getHeight() - Height) / 2 + 1, (Main.primaryStage.getWidth() - Width) / 2 + 1, (Main.primaryStage.getHeight() - Height) / 2 + 1, (Main.primaryStage.getWidth() - Width) / 2 + 1));


            JFXDepthManager.setDepth(dialogController.mainroot, 4);

            Scene scene = new Scene(parent, Main.primaryStage.getWidth(), Main.primaryStage.getHeight());
            scene.setFill(Color.TRANSPARENT);

            stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);

            //TODO#CSS
            File cssFile = new File(appdata+"/PersoIdentitie/Stockage/themes/"+Main.getCurrentTheme()+".css");
            scene.getStylesheets().add("file:///"+cssFile.getAbsolutePath().replace("\\", "/"));
            scene.getStylesheets().add("file:///"+fontCssFile.getAbsolutePath().replace("\\", "/"));
            dialogController.Input.requestFocus();

            stage.showAndWait();

            ResultList = new String[]{dialogController.PreName,dialogController.Name,dialogController.World};
            if(dialogController.cancelled)ResultList = null;

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return ResultList;
    }

    public static String createInputStage(String Title, String question, String content) {
        Stage stage;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FileClassPath.load("TextInputDialog.fxml", FileClassPath.Type.FXML));

            StackPane parent = fxmlLoader.load();

            parent.getStylesheets().add(FileClassPath.load("CustomDialog.css", FileClassPath.Type.CSS).toString());
            TextInputDialogController dialogController = fxmlLoader.getController();

            Image img = new Image(FileClassPath.load("23924.png", FileClassPath.Type.IMAGE).toString());
            dialogController.Icon.setImage(img);
            dialogController.Title.setText(Title);
            dialogController.Question.setPromptText(question);
            if(content != null)dialogController.Input.setText(content);

            Text text = new Text(question);
            dialogController.Question.setMinWidth(text.getLayoutBounds().getWidth()+5);

            dialogController.mainroot.autosize();
            dialogController.root.autosize();

            double Height = dialogController.root.getHeight();
            double Width = dialogController.root.getWidth();

            parent.setBackground(new Background(new BackgroundFill(Color. rgb(0, 44, 175), null, new Insets((Main.primaryStage.getHeight() - Height) / 2 + 1, (Main.primaryStage.getWidth() - Width) / 2 + 1, (Main.primaryStage.getHeight() - Height) / 2 + 1, (Main.primaryStage.getWidth() - Width) / 2 + 1))));
            parent.setPadding(new Insets((Main.primaryStage.getHeight() - Height) / 2 + 1, (Main.primaryStage.getWidth() - Width) / 2 + 1, (Main.primaryStage.getHeight() - Height) / 2 + 1, (Main.primaryStage.getWidth() - Width) / 2 + 1));


            JFXDepthManager.setDepth(dialogController.mainroot, 4);

            Scene scene = new Scene(parent, Main.primaryStage.getWidth(), Main.primaryStage.getHeight());
            scene.setFill(Color.TRANSPARENT);

            stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);

            //TODO#CSS
            File cssFile = new File(appdata+"/PersoIdentitie/Stockage/themes/"+Main.getCurrentTheme()+".css");
            scene.getStylesheets().add("file:///"+cssFile.getAbsolutePath().replace("\\", "/"));
            scene.getStylesheets().add("file:///"+fontCssFile.getAbsolutePath().replace("\\", "/"));
            dialogController.Input.requestFocus();

            stage.showAndWait();

            Result = dialogController.text;

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return Result;
    }

    public static String createComboboxStage(String Title, String question, String[] item) {
        Stage stage;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FileClassPath.load("ComboBoxDialog.fxml", FileClassPath.Type.FXML));

            StackPane parent = fxmlLoader.load();
            parent.getStylesheets().add(FileClassPath.load("CustomDialog.css", FileClassPath.Type.CSS).toString());
            ComboBoxDialogController dialogController = fxmlLoader.getController();

            Image img = new Image(FileClassPath.load("23924.png", FileClassPath.Type.IMAGE).toString());
            dialogController.Input.getItems().setAll(item);
            dialogController.Icon.setImage(img);
            dialogController.Title.setText(Title);
            dialogController.Question.setText(question);
            dialogController.Input.getSelectionModel().select(0);
            dialogController.Question.setMinWidth(question.length()*6.33);

            dialogController.mainroot.setMinSize(Main.primaryStage.getWidth(), Main.primaryStage.getHeight());
            dialogController.root.setMinSize(487, 283);
            parent.setBackground(new Background(new BackgroundFill(Color. rgb(0, 44, 175), null, new Insets((Main.primaryStage.getHeight()-283)/2+1, (Main.primaryStage.getWidth()-487)/2+1,(Main.primaryStage.getHeight()-283)/2+1,(Main.primaryStage.getWidth()-487)/2+1))));

            JFXDepthManager.setDepth(dialogController.mainroot, 4);

            Scene scene = new Scene(parent, Main.primaryStage.getWidth(), Main.primaryStage.getHeight());
            scene.setFill(Color.TRANSPARENT);

            stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);

            dialogController.Input.requestFocus();

            //TODO#CSS
            File cssFile = new File(appdata+"/PersoIdentitie/Stockage/themes/"+Main.getCurrentTheme()+".css");
            stage.getScene().getStylesheets().add("file:///"+cssFile.getAbsolutePath().replace("\\", "/"));
            stage.getScene().getStylesheets().add("file:///"+fontCssFile.getAbsolutePath().replace("\\", "/"));

            stage.showAndWait();

            Result = dialogController.text;

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return Result;
    }

    public static String createFindPersoDialog(String Title) {
        Stage stage;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FileClassPath.load("FindPersoDialog.fxml", FileClassPath.Type.FXML));

            StackPane parent = fxmlLoader.load();
            parent.getStylesheets().add(FileClassPath.load("CustomDialog.css", FileClassPath.Type.CSS).toString());
            FindPersoDialogController dialogController = fxmlLoader.getController();

            Image img = new Image(FileClassPath.load("23924.png", FileClassPath.Type.IMAGE).toString());
            dialogController.Icon.setImage(img);
            dialogController.Title.setText(Title);

            dialogController.mainroot.setMinSize(Main.primaryStage.getWidth(), Main.primaryStage.getHeight());
            dialogController.root.setMinSize(487, 283);
            parent.setBackground(new Background(new BackgroundFill(Color. rgb(0, 44, 175), null, new Insets((Main.primaryStage.getHeight()-283)/2+1, (Main.primaryStage.getWidth()-487)/2+1,(Main.primaryStage.getHeight()-283)/2+1,(Main.primaryStage.getWidth()-487)/2+1))));

            JFXDepthManager.setDepth(dialogController.mainroot, 4);

            Scene scene = new Scene(parent, Main.primaryStage.getWidth(), Main.primaryStage.getHeight());
            scene.setFill(Color.TRANSPARENT);

            stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);

            dialogController.Input.requestFocus();

            //TODO#CSS
            File cssFile = new File(appdata+"/PersoIdentitie/Stockage/themes/"+Main.getCurrentTheme()+".css");
            stage.getScene().getStylesheets().add("file:///"+cssFile.getAbsolutePath().replace("\\", "/"));
            stage.getScene().getStylesheets().add("file:///"+fontCssFile.getAbsolutePath().replace("\\", "/"));

            stage.showAndWait();

            Result = dialogController.text;

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return Result;
    }

    public static void createEditingPreferenceDialog(Object object, String FileName, Charactere charactere, StackPane root){
        ArrayList<String> PrefList = new ArrayList<>(charactere.NewPreferences.keySet());
        ArrayList<Node> children = new ArrayList<>();

        Stage stage;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FileClassPath.load("PreferenceDialog.fxml", FileClassPath.Type.FXML));

            StackPane parent = fxmlLoader.load();
            parent.getStylesheets().add(FileClassPath.load("CustomDialog.css", FileClassPath.Type.CSS).toString());
            PreferenceDialogController dialogController = fxmlLoader.getController();

            dialogController.initdata(object, root, true);


            //Ajout des composant

            Image img = new Image(FileClassPath.load("23924.png", FileClassPath.Type.IMAGE).toString());
            dialogController.Icon.setImage(img);
            dialogController.Title.setText("Préférences de " + FileName);

            //ajout des préférences

            for (String value : PrefList) {
                children.add(dialogController.makePane(value, true, charactere, parent));
            }

            //ajout du composant ajouter un préférence

            StackPane iconpane = new StackPane();
            MaterialDesignIconView iconview = new MaterialDesignIconView(MaterialDesignIcon.PLAYLIST_PLUS);
            iconview.setSize("50");
            iconview.setFill(Color.rgb(0, 147, 14, 0.25));

            iconpane.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> iconview.setFill(Color.rgb(0, 147, 14, 1)));
            iconpane.addEventHandler(MouseEvent.MOUSE_EXITED, e -> iconview.setFill(Color.rgb(0, 147, 14, 0.25)));
            iconpane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> dialogController.pane.getChildren().add(dialogController.pane.getChildren().size()-1, dialogController.makePane("Préférence"+dialogController.pane.getChildren().size(), false, charactere, parent)));


            //ajout de touttt les composant
            iconpane.getChildren().add(iconview);
            children.add(children.size(), iconpane);
            dialogController.pane.getChildren().addAll(children);

            //reglage Fênetre et tt


            dialogController.mainroot.setPrefSize(Main.primaryStage.getWidth(), Main.primaryStage.getHeight());
            parent.setBackground(new Background(new BackgroundFill(Color. rgb(0, 44, 175), null, new Insets((Main.primaryStage.getHeight() - 350) / 2 + 1, (Main.primaryStage.getWidth() - 550) / 2 + 1, (Main.primaryStage.getHeight() - 350) / 2 + 1, (Main.primaryStage.getWidth() - 550) / 2 + 1))));
            parent.setPadding(new Insets((Main.primaryStage.getHeight() - 350) / 2 + 1, (Main.primaryStage.getWidth() - 550) / 2 + 1, (Main.primaryStage.getHeight() - 350) / 2 + 1, (Main.primaryStage.getWidth() - 550) / 2 + 1));

            JFXDepthManager.setDepth(dialogController.mainroot, 4);

            Scene scene = new Scene(parent, Main.primaryStage.getWidth(), Main.primaryStage.getHeight());
            scene.setFill(Color.TRANSPARENT);

            stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);

            //TODO#CSS
            File cssFile = new File(appdata+"/PersoIdentitie/Stockage/themes/"+Main.getCurrentTheme()+".css");
            stage.getScene().getStylesheets().add("file:///"+cssFile.getAbsolutePath().replace("\\", "/"));
            stage.getScene().getStylesheets().add("file:///"+fontCssFile.getAbsolutePath().replace("\\", "/"));

            dialogController.pane.requestFocus();

            stage.showAndWait();


        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static void createPreferenceDialog(String FileName, Charactere charactere, StackPane root){
        ArrayList<String> PrefList = new ArrayList<>(charactere.NewPreferences.keySet());
        ArrayList<Node> children = new ArrayList<>();

        Stage stage;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FileClassPath.load("PreferenceDialog.fxml", FileClassPath.Type.FXML));

            StackPane parent = fxmlLoader.load();
            parent.getStylesheets().add(FileClassPath.load("CustomDialog.css", FileClassPath.Type.CSS).toString());
            PreferenceDialogController dialogController = fxmlLoader.getController();

            //Ajout des composant

            Image img = new Image(FileClassPath.load("23924.png", FileClassPath.Type.IMAGE).toString());
            dialogController.Icon.setImage(img);
            dialogController.Title.setText("Préférences de " + FileName);

            //ajout des préférences

            for (String value : PrefList) {
                children.add(dialogController.makePane(value, true, charactere, parent));
            }

            //ajout de touttt les composant
            dialogController.pane.getChildren().addAll(children);

            //reglage Fênetre et tt


            dialogController.mainroot.setPrefSize(Main.primaryStage.getWidth(), Main.primaryStage.getHeight());
            parent.setBackground(new Background(new BackgroundFill(Color. rgb(0, 44, 175), null, new Insets((Main.primaryStage.getHeight() - 350) / 2 + 1, (Main.primaryStage.getWidth() - 550) / 2 + 1, (Main.primaryStage.getHeight() - 350) / 2 + 1, (Main.primaryStage.getWidth() - 550) / 2 + 1))));
            parent.setPadding(new Insets((Main.primaryStage.getHeight() - 350) / 2 + 1, (Main.primaryStage.getWidth() - 550) / 2 + 1, (Main.primaryStage.getHeight() - 350) / 2 + 1, (Main.primaryStage.getWidth() - 550) / 2 + 1));

            JFXDepthManager.setDepth(dialogController.mainroot, 4);

            Scene scene = new Scene(parent, Main.primaryStage.getWidth(), Main.primaryStage.getHeight());
            scene.setFill(Color.TRANSPARENT);

            stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);

            dialogController.pane.requestFocus();
            dialogController.initdata(null, root, false);

            //TODO#CSS
            File cssFile = new File(appdata+"/PersoIdentitie/Stockage/themes/"+Main.getCurrentTheme()+".css");
            stage.getScene().getStylesheets().add("file:///"+cssFile.getAbsolutePath().replace("\\", "/"));
            stage.getScene().getStylesheets().add("file:///"+fontCssFile.getAbsolutePath().replace("\\", "/"));

            stage.showAndWait();


        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static int createQuestionStage(String Title, String question, String[] content) {
        Stage stage;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FileClassPath.load("QuestionDialog.fxml", FileClassPath.Type.FXML));

            StackPane parent = fxmlLoader.load();
            parent.getStylesheets().add(FileClassPath.load("CustomDialog.css", FileClassPath.Type.CSS).toString());
            QuestionDialogController dialogController = fxmlLoader.getController();

            Image img = new Image(FileClassPath.load("23924.png", FileClassPath.Type.IMAGE).toString());
            dialogController.Icon.setImage(img);
            dialogController.Title.setText(Title);
            dialogController.Question.setText(question);

            //set button
            for (String value: content) {
                JFXButton button = new JFXButton(value);
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, dialogController::closeStage);
                dialogController.buttonlayout.getChildren().add( dialogController.buttonlayout.getChildren().size(), button);
            }

            dialogController.mainroot.autosize();
            dialogController.root.autosize();

            double Height = dialogController.root.getHeight();
            double Width = dialogController.root.getWidth();

            parent.setBackground(new Background(new BackgroundFill(Color.rgb(0, 44, 175), null, new Insets((Main.primaryStage.getHeight() - Height) / 2 + 1, (Main.primaryStage.getWidth() - Width) / 2 + 1, (Main.primaryStage.getHeight() - Height) / 2 + 1, (Main.primaryStage.getWidth() - Width) / 2 + 1))));
            parent.setPadding(new Insets((Main.primaryStage.getHeight() - Height) / 2 + 1, (Main.primaryStage.getWidth() - Width) / 2 + 1, (Main.primaryStage.getHeight() - Height) / 2 + 1, (Main.primaryStage.getWidth() - Width) / 2 + 1));

            JFXDepthManager.setDepth(dialogController.mainroot, 4);

            Scene scene = new Scene(parent, Main.primaryStage.getWidth(), Main.primaryStage.getHeight());
            scene.setFill(Color.TRANSPARENT);

            stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);

            //TODO#CSS
            File cssFile = new File(appdata+"/PersoIdentitie/Stockage/themes/"+Main.getCurrentTheme()+".css");
            stage.getScene().getStylesheets().add("file:///"+cssFile.getAbsolutePath().replace("\\", "/"));
            stage.getScene().getStylesheets().add("file:///"+fontCssFile.getAbsolutePath().replace("\\", "/"));

            stage.showAndWait();

            intResult = dialogController.buttonpress;

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return intResult;
    }


    public static void createErrorDialog(String Title, String arg, StackPane root, JFXDialog.DialogTransition Transition){

        JFXDialog dialog = null;
        try {
            FXMLLoader rootdialog = new FXMLLoader(FileClassPath.load("Dialog.fxml", FileClassPath.Type.FXML));
            JFXDialogLayout dialog_layout = rootdialog.load();
            dialogController = rootdialog.getController();
            dialog = new JFXDialog(root, dialog_layout, Transition);
            dialog.setOverlayClose(false);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        dialogController.Layout.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(255, 0, 0, 0.4), 255, 0, 0, 0));
        dialogController.Title.setText(Title);
        dialogController.Paragraph.setText(arg);

        assert dialog != null;

        //TODO#CSS
        File cssFile = new File(appdata+"/PersoIdentitie/Stockage/themes/"+Main.getCurrentTheme()+".css");
        dialog.getDialogContainer().getStylesheets().add("file:///"+cssFile.getAbsolutePath().replace("\\", "/"));
        dialog.getDialogContainer().getStylesheets().add("file:///"+fontCssFile.getAbsolutePath().replace("\\", "/"));

        dialog.show();

        JFXDialog finalDialog = dialog;
        dialogController.acceptButton.setOnAction(eb -> finalDialog.close());

    }

    public static void createValideDialog(String Title, String arg, StackPane root, JFXDialog.DialogTransition Transition){

        JFXDialog dialog = null;
        try {
            FXMLLoader rootdialog = new FXMLLoader(FileClassPath.load("Dialog.fxml", FileClassPath.Type.FXML));
            JFXDialogLayout dialog_layout = rootdialog.load();
            dialogController = rootdialog.getController();
            dialog = new JFXDialog(root, dialog_layout, Transition);
            dialog.setOverlayClose(false);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        dialogController.Layout.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 255, 0, 0.4), 255, 0, 0, 0));
        dialogController.Title.setText(Title);
        dialogController.Paragraph.setText(arg);

        assert dialog != null;

        //TODO#CSS
        File cssFile = new File(appdata+"/PersoIdentitie/Stockage/themes/"+Main.getCurrentTheme()+".css");
        dialog.getDialogContainer().getStylesheets().add("file:///"+cssFile.getAbsolutePath().replace("\\", "/"));
        dialog.getDialogContainer().getStylesheets().add("file:///"+fontCssFile.getAbsolutePath().replace("\\", "/"));

        dialog.show();

        JFXDialog finalDialog = dialog;
        dialogController.acceptButton.setOnAction(eb -> finalDialog.close());

    }

}
