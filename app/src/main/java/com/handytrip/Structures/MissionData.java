package com.handytrip.Structures;

public class MissionData {
    private String mName;
    private String mPlace;
    private double mLat;
    private double mLng;
    private int mTheme; // 1 역사, 2 체험, 3 풍경
    private int mRate; // 1 주관식, 2 객관식
    private String mReadyText;
    private String mReadyImgUrl;
    private String mHintText;
    private String mHintImgUrl;
    private String mTipText;
    private String mTipImgUrl;
    private String mQuest;
    private String mAns;
    private String s1;
    private String s2;
    private String s3;
    private String s4;
    private String ansTime;
    private boolean isDone;
    private boolean isEssay;

    public void setmLat(double mLat) {
        this.mLat = mLat;
    }

    public void setmLng(double mLng) {
        this.mLng = mLng;
    }

    public boolean isEssay() {
        return isEssay;
    }

    public void setEssay(boolean essay) {
        isEssay = essay;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPlace() {
        return mPlace;
    }

    public void setmPlace(String mPlace) {
        this.mPlace = mPlace;
    }

    public double getmLat() {
        return mLat;
    }

    public void setmLat(long mLat) {
        this.mLat = mLat;
    }

    public double getmLng() {
        return mLng;
    }

    public void setmLng(long mLng) {
        this.mLng = mLng;
    }

    public int getmTheme() {
        return mTheme;
    }

    public void setmTheme(int mTheme) {
        this.mTheme = mTheme;
    }

    public int getmRate() {
        return mRate;
    }

    public void setmRate(int mRate) {
        this.mRate = mRate;
    }

    public String getmReadyText() {
        return mReadyText;
    }

    public void setmReadyText(String mReadyText) {
        this.mReadyText = mReadyText;
    }

    public String getmReadyImgUrl() {
        return mReadyImgUrl;
    }

    public void setmReadyImgUrl(String mReadyImgUrl) {
        this.mReadyImgUrl = mReadyImgUrl;
    }

    public String getmHintText() {
        return mHintText;
    }

    public void setmHintText(String mHintText) {
        this.mHintText = mHintText;
    }

    public String getmHintImgUrl() {
        return mHintImgUrl;
    }

    public void setmHintImgUrl(String mHintImgUrl) {
        this.mHintImgUrl = mHintImgUrl;
    }

    public String getmTipText() {
        return mTipText;
    }

    public void setmTipText(String mTipText) {
        this.mTipText = mTipText;
    }

    public String getmTipImgUrl() {
        return mTipImgUrl;
    }

    public void setmTipImgUrl(String mTipImgUrl) {
        this.mTipImgUrl = mTipImgUrl;
    }

    public String getmQuest() {
        return mQuest;
    }

    public void setmQuest(String mQuest) {
        this.mQuest = mQuest;
    }

    public String getmAns() {
        return mAns;
    }

    public void setmAns(String mAns) {
        this.mAns = mAns;
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public String getS3() {
        return s3;
    }

    public void setS3(String s3) {
        this.s3 = s3;
    }

    public String getS4() {
        return s4;
    }

    public void setS4(String s4) {
        this.s4 = s4;
    }

    public String getAnsTime() {
        return ansTime;
    }

    public void setAnsTime(String ansTime) {
        this.ansTime = ansTime;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public MissionData(String mName, String mPlace, double mLat, double mLng, int mTheme, int mRate, String mReadyText, String mReadyImgUrl, String mHintText, String mHintImgUrl, String mTipText, String mTipImgUrl, String mQuest, String mAns, String s1, String s2, String s3, String s4, String ansTime, boolean isDone) {
        this.mName = mName;
        this.mPlace = mPlace;
        this.mLat = mLat;
        this.mLng = mLng;
        this.mTheme = mTheme;
        this.mRate = mRate;
        this.mReadyText = mReadyText;
        this.mReadyImgUrl = mReadyImgUrl;
        this.mHintText = mHintText;
        this.mHintImgUrl = mHintImgUrl;
        this.mTipText = mTipText;
        this.mTipImgUrl = mTipImgUrl;
        this.mQuest = mQuest;
        this.mAns = mAns;
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.s4 = s4;
        this.ansTime = ansTime;
        this.isDone = false;
    }

    public MissionData(String mName, String mPlace, double mLat, double mLng, int mTheme, int mRate, String mReadyText, String mReadyImgUrl, String mHintText, String mHintImgUrl, String mTipText, String mTipImgUrl, String mQuest, String mAns, String s1, String s2, String s3, String s4) {
        this.mName = mName;
        this.mPlace = mPlace;
        this.mLat = mLat;
        this.mLng = mLng;
        this.mTheme = mTheme;
        this.mRate = mRate;
        this.mReadyText = mReadyText;
        this.mReadyImgUrl = mReadyImgUrl;
        this.mHintText = mHintText;
        this.mHintImgUrl = mHintImgUrl;
        this.mTipText = mTipText;
        this.mTipImgUrl = mTipImgUrl;
        this.mQuest = mQuest;
        this.mAns = mAns;
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.s4 = s4;
        this.isDone = false;
    }

    public MissionData(String mName, double mLat, double mLng){
        this.mName = mName;
        this.mLat = mLat;
        this.mLng = mLng;
        this.isDone = true;
    }

    public MissionData() {
    }

}
