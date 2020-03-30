package com.xs.middle.compent.rxjava;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaos
 * @date 26/03/2020 14:15
 */
public class Plan {

    private String time;
    private String content;
    private List<String> actionList = new ArrayList<>();

    public Plan(String time, String content) {
        this.time = time;
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getActionList() {
        return actionList;
    }

    public void setActionList(List<String> actionList) {
        this.actionList = actionList;
    }
}

