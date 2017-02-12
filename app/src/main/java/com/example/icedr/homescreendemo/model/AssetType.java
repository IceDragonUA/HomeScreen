package com.example.icedr.homescreendemo.model;


public enum AssetType {

    IMAGE("image"), MOVIE("movie"), NOT_DEFINED("not_defined");

    private String asString;

    AssetType(String asString) {
        this.asString = asString;
    }

    public static AssetType fromString(String stringRepresentation) {
        for (AssetType assetType : AssetType.values()) {
            if (assetType.asString.equalsIgnoreCase(stringRepresentation)) {
                return assetType;
            }
        }
        return NOT_DEFINED;
    }
}
