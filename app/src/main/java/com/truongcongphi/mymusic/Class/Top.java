package com.truongcongphi.mymusic.Class;

import java.io.Serializable;

public class Top implements Serializable {
    private String topId, topName, topUrl;

    public Top(String topId, String topName, String topUrl) {
        this.topId = topId;
        this.topName = topName;
        this.topUrl = topUrl;
    }

    public Top() {
    }

    public String getTopId() {
        return topId;
    }

    public void setTopId(String topId) {
        this.topId = topId;
    }

    public String getTopName() {
        return topName;
    }

    public void setTopName(String topName) {
        this.topName = topName;
    }

    public String getTopUrl() {
        return topUrl;
    }

    public void setTopUrl(String topUrl) {
        this.topUrl = topUrl;
    }
}
