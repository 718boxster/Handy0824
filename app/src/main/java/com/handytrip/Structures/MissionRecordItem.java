package com.handytrip.Structures;

import java.io.Serializable;

public class MissionRecordItem implements Serializable {
    String imgUrl;
    String mName;
    String mDate;
    String mPlace;
    String mLat;
    String mLng;

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

    public String getmPlace() {
        return mPlace;
    }

    public void setmPlace(String mPlace) {
        this.mPlace = mPlace;
    }

    public String getmLat() {
        return mLat;
    }

    public void setmLat(String mLat) {
        this.mLat = mLat;
    }

    public String getmLng() {
        return mLng;
    }

    public void setmLng(String mLng) {
        this.mLng = mLng;
    }

    public MissionRecordItem(String imgUrl, String mName, String mDate, String mPlace, String mLat, String mLng) {
        this.imgUrl = imgUrl;
        this.mName = mName;
        this.mDate = mDate;
        this.mPlace = mPlace;
        this.mLat = mLat;
        this.mLng = mLng;
    }
}
