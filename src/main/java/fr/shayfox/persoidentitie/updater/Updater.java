package fr.shayfox.persoidentitie.updater;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jfoenix.controls.JFXDialog;
import fr.shayfox.persoidentitie.Main;
import fr.shayfox.persoidentitie.controllers.MainController;
import fr.shayfox.persoidentitie.utils.CustomDialogs;
import javafx.scene.control.TabPane;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class Updater {
    private @Nullable
    GitHubReleaseInfo latestRelease;
    private static final String LATEST_RELEASE_ENDPOINT = "https://api.github.com/repos/ShayF0x/PersoIdentities/releases/latest";
    private Version currentVersion;
    private final String APPDATA = System.getenv("APPDATA");


    public Updater(MainController mainController) {
        String version = this.getClass().getPackage().getImplementationVersion();
        if(version!=null)version.replaceAll("-SNAPSHOT", "");
        else version = "1.3.0";
        System.out.println("Version: "+version);
        this.currentVersion = new Version(version);

        try {
            System.out.println("connection");
            final HttpURLConnection connection = (HttpURLConnection) new URL(LATEST_RELEASE_ENDPOINT).openConnection();
            //connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                System.out.println("error");
                return;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF8")))) {
                this.latestRelease = new Gson().fromJson(reader, GitHubReleaseInfo.class);
                this.latestRelease.Version = new Version(this.latestRelease.tagName);

                for (GitHubAssetInfo assetInfo : this.latestRelease.assets) {
                    this.latestRelease.platformAsset = assetInfo;
                }

                if (this.currentVersion.compareTo(this.latestRelease.Version) >= 0) {
                    if(mainController != null)CustomDialogs.createValideDialog("Application à jour", "il semblerait que l'application soit à jour", mainController.root, JFXDialog.DialogTransition.CENTER);
                    return;
                }

                System.out.println("une nouvelle version est disponible: " + this.latestRelease.tagName + " ! (Version actuelle : " + currentVersion.toString() + ").");
                final GitHubAssetInfo asset = this.latestRelease.platformAsset;
                boolean done = false;
                if (asset != null) {
                    System.out.println("can download");
                    int result = CustomDialogs.createQuestionStage("Mise à jour", "Une mise à jour est disponible, voulez-vous mettre à jour persoIdentites ?", new String[]{"oui", "non"});
                    done = result == 0;
                }
                if (done) {
                    System.out.println("update");
                    if(mainController != null && mainController.tabPane.getTabs().size() > 1){
                        CustomDialogs.createErrorDialog("Erreur", "merci de fermer tout les onglets avant d'effectuer la mise à jour", mainController.root, JFXDialog.DialogTransition.CENTER);
                        return;
                    }
                    File updaterApp = new File(APPDATA+"/PersoIdentitie/apps.apps");
                    Runtime.getRuntime().exec(" java -apps " + updaterApp.getAbsolutePath());
                    Main.primaryStage.close();
                }
            } catch (JsonSyntaxException | NumberFormatException ex) {
                System.out.println("Failed to parse the latest version info.");
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            System.out.println("LOG: Failed to get release info from api.github.com.");
            ex.printStackTrace();
        }
    }
}
