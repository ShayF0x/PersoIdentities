package fr.shayfox.persoidentitie.updater;

import com.google.gson.annotations.SerializedName;
import com.sun.istack.internal.Nullable;

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
