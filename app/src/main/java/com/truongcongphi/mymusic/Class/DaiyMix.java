package com.truongcongphi.mymusic.Class;

import java.io.Serializable;

public class DaiyMix implements Serializable {
    private String mixId;
    private String mixName;
    private String url;

    public DaiyMix(String mixId, String mixName, String url) {
        this.mixId = mixId;
        this.mixName = mixName;
        this.url = url;
    }

    public DaiyMix() {
    }

    public String getMixId() {
        return mixId;
    }

    public void setMixId(String mixId) {
        this.mixId = mixId;
    }

    public String getMixName() {
        return mixName;
    }

    public void setMixixName(String mixixName) {
        this.mixName = mixixName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
