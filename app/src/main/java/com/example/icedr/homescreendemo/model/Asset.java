package com.example.icedr.homescreendemo.model;

import com.google.gson.annotations.SerializedName;

public class Asset {

    @SerializedName("url")
    private String assetUrl;

    @SerializedName("type")
    private String assetType;

    public Asset() {
    }

    public String getAssetUrl() {
        return assetUrl;
    }

    public String getAssetType() {
        return assetType;
    }

}
