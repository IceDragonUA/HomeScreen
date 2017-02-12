package com.example.icedr.homescreendemo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProjectsWrapper {

    @SerializedName("projects")
    private List<Project> projectList;

    public ProjectsWrapper() {
    }

    public List<Project> getProjectList() {
        return projectList;
    }
}
