package fr.shayfox.persoidentitie.updater;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GitHubReleaseInfo {
    @SerializedName("tag_name")
    String tagName;

    boolean prerelease;

    @Nullable
    GitHubAssetInfo platformAsset;

    List<GitHubAssetInfo> assets;

    Version Version;
}
