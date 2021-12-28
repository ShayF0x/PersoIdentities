package fr.shayfox.persoidentitie.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class DialogGalerieController implements Initializable {

    @FXML
    public JFXButton Left, Right, Close;
    @FXML
    public ImageView IconView;
    @FXML
    public StackPane Layout;
    @FXML
    public Pane paddingpanel;
    private int count;
    private String filename;
    private File testpath;
    private String appdata = System.getenv("APPDATA");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    public void init(String filename){
        this.filename = filename;
        count = 1;
        updateImage();
        Right.setOnAction(e -> {
          testpath = new File(appdata+"/PersoIdentitie/fiches/images/"+filename+"_"+(count+1)+".png");
          if(testpath.exists()){
              count++;
              updateImage();
          }
        });
        Left.setOnAction(e -> {
            if(count == 1)return;
            testpath = new File(appdata+"/PersoIdentitie/fiches/images/"+filename+"_"+(count-1)+".png");
            if(testpath.exists()){
                count--;
                updateImage();
            }
        });

    }

    private void updateImage(){
        File path = new File(appdata+"/PersoIdentitie/fiches/images/"+filename+"_"+count+".png");
        Image image = new Image(path.toURI().toString());
        if(image.getWidth() < IconView.getFitWidth() || image.getHeight() < IconView.getFitHeight()){
            image = new Image(path.toURI().toString(), IconView.getFitWidth(), IconView.getFitHeight(), true, true);
        }else if(image.getWidth() > 1500 || image.getHeight() > 900){
            image = new Image(path.toURI().toString(), 1500, 900, true, true);
        }
        IconView.setPreserveRatio(true);
        IconView.setFitHeight(image.getHeight());
        IconView.setFitWidth(image.getWidth());
        IconView.setImage(image);
        paddingpanel.setMinWidth(IconView.getFitWidth());
    }
}
