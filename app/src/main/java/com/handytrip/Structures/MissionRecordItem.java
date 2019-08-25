package com.handytrip.Structures;

public class MissionRecordItem {
    String imgUrl;
    String mName;
    String mDate;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public MissionRecordItem(String imgUrl, String mName, String mDate) {
        this.imgUrl = imgUrl;
        this.mName = mName;
        this.mDate = mDate;
    }
}
