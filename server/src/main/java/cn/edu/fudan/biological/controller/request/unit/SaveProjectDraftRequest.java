package cn.edu.fudan.biological.controller.request.unit;

import java.util.HashMap;
import java.util.HashSet;

public class SaveProjectDraftRequest {
    public String unitname;
    public String projectId;
    public String projectName;
    public String projectGoal;
    public String projectDuration;
    public HashSet<HashMap<String, Object>> projectItems;
    public HashSet<HashMap<String, Object>> agreeItems;
    public boolean isPublished;

    public SaveProjectDraftRequest(String unitname, String projectId, String projectName, String projectGoal,
                                   String projectDuration, HashSet<HashMap<String, Object>> projectItems,
                                   HashSet<HashMap<String, Object>> agreeItems, boolean isPublished) {
        this.unitname = unitname;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectGoal = projectGoal;
        this.projectDuration = projectDuration;
        this.projectItems = projectItems;
        this.agreeItems = agreeItems;
        this.isPublished = isPublished;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectGoal() {
        return projectGoal;
    }

    public void setProjectGoal(String projectGoal) {
        this.projectGoal = projectGoal;
    }

    public String getProjectDuration() {
        return projectDuration;
    }

    public void setProjectDuration(String projectDuration) {
        this.projectDuration = projectDuration;
    }

    public HashSet<HashMap<String, Object>> getProjectItems() {
        return projectItems;
    }

    public void setProjectItems(HashSet<HashMap<String, Object>> projectItems) {
        this.projectItems = projectItems;
    }

    public HashSet<HashMap<String, Object>> getAgreeItems() {
        return agreeItems;
    }

    public void setAgreeItems(HashSet<HashMap<String, Object>> agreeItems) {
        this.agreeItems = agreeItems;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }
}
