package fr.shayfox.persoidentitie.updater;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.istack.internal.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class Updater {
    private @Nullable
    GitHubReleaseInfo latestRelease;
    private static final String LATEST_RELEASE_ENDPOINT = "https://api.github.com/repos/ShayF0x/PersoIdentities/releases/latest";
    private final Version currentVersion;


    public Updater(Version currentVersion) {
        this.currentVersion = currentVersion;
        try {
            final HttpURLConnection connection = (HttpURLConnection) new URL(LATEST_RELEASE_ENDPOINT).openConnection();
            //connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                return;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF8")))) {
                this.latestRelease = new Gson().fromJson(reader, GitHubReleaseInfo.class);
                this.latestRelease.Version = new Version(this.latestRelease.tagName);

                for (GitHubAssetInfo assetInfo : this.latestRelease.assets) {
                    this.latestRelease.platformAsset = assetInfo;
                }

                if (this.currentVersion.compareTo(this.latestRelease.Version) >= 0) {
                    return;
                }

                System.out.println("une nouvelle version est disponible: " + this.latestRelease.tagName + " ! (Version actuelle : " + currentVersion.toString() + ").");
                final GitHubAssetInfo asset = this.latestRelease.platformAsset;
                boolean done = false;
                if (asset != null) {
                    System.out.println("download");
                    //download done = this.plugin.updatePlugin(asset);
                }
//                if (done) {
//                    this.logger.info(Messages.CONSOLE_VERSION_DOWNLOADED.getMessage());
//                    this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin,
//                            () -> this.plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart")
//                    );
//                } else {
//                    this.logger.warning(Messages.CONSOLE_UPDATE_ERROR.getMessage());
//                    this.plugin.addOnConnectWarning(Messages.CONSOLE_NEW_VERSION_AVAILABLE.getMessage());
//                }
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
