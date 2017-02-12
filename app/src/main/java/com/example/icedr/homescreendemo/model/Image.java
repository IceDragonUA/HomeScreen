package com.example.icedr.homescreendemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Image implements Parcelable {

    @SerializedName("url")
    private String projectImageUrl;

    public Image() {
    }

    private Image(Parcel parcel) {
        projectImageUrl = parcel.readString();
    }

    public String getProjectImageUrl() {
        return projectImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(projectImageUrl);
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {

        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };




}
