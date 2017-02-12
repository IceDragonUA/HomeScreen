package com.example.icedr.homescreendemo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssetsWrapper {

    @SerializedName("assets")
    private List<Asset> assetsList;

    public AssetsWrapper() {
    }

    public List<Asset> getAssetsList() {
        return assetsList;
    }
}
