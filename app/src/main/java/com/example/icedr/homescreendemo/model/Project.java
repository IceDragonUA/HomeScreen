package com.example.icedr.homescreendemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Project implements Parcelable {

    @SerializedName("id")
    private int projectId;

    @SerializedName("name")
    private String projectName;

    @SerializedName("description")
    private String projectDescription;

    @SerializedName("clientId")
    private int clientId;

    @SerializedName("image")
    private Image projectImage;

    @SerializedName("technologies")
    private String[] projectTechnologies;

    @SerializedName("supportedScreens")
    private String[] projectSupportedScreens;

    @SerializedName("solutionTypes")
    private String[] projectSolutionTypes;

    public Project() {
    }

    private Project(Parcel parcel) {
        projectId = parcel.readInt();
        projectName = parcel.readString();
        projectDescription = parcel.readString();
        clientId = parcel.readInt();
        projectImage = parcel.readParcelable(Project.class.getClassLoader());
        projectTechnologies = parcel.createStringArray();
        projectSupportedScreens = parcel.createStringArray();
        projectSolutionTypes = parcel.createStringArray();
    }

    public int getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public int getClientId() {
        return clientId;
    }

    public Image getProjectImage() {
        return projectImage;
    }

    public String[] getProjectTechnologies() {
        return projectTechnologies;
    }

    public String[] getProjectSupportedScreens() {
        return projectSupportedScreens;
    }

    public String[] getProjectSolutionTypes() {
        return projectSolutionTypes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(projectId);
        dest.writeString(projectName);
        dest.writeString(projectDescription);
        dest.writeInt(clientId);
        dest.writeParcelable(projectImage, flags);
        dest.writeStringArray(projectTechnologies);
        dest.writeStringArray(projectSupportedScreens);
        dest.writeStringArray(projectSolutionTypes);
    }

    public static final Parcelable.Creator<Project> CREATOR = new Parcelable.Creator<Project>() {

        @Override
        public Project createFromParcel(Parcel source) {
            return new Project(source);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

}