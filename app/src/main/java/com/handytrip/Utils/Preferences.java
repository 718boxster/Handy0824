package com.handytrip.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    Context context;

    private static String USER_ID = "USER_ID";
    private static String USER_PW = "USER_PW";
    private static String PROFILE_IMG = "PROFILE_IMG";
    private static String USER_NICK = "USER_NICK";
    private static String USER_NAME = "USER_NAME";

    private static String FCM_TOKEN = "FCM_TOKEN";

    private static String GET_NOTIFICATION = "GET_NOTIFICATION";

    public Preferences(Context context) {
        this.context = context;
        pref = context.getSharedPreferences("HANDY.pref", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setFcmToken(String fcmToken){
        editor.putString(FCM_TOKEN, fcmToken);
        editor.commit();
    }
    public String getFcmToken(){
        return pref.getString(FCM_TOKEN, null);
    }

    public void setUserId(String id){
        editor.putString(USER_ID, id);
        editor.commit();
    }
    public void setUserPw(String pw){
        editor.putString(USER_PW, pw);
        editor.commit();
    }

    public String getUserId(){
        return pref.getString(USER_ID, "");
    }
    public String getUserPw(){
        return pref.getString(USER_PW, "");
    }

    public void setGetNotification(boolean getNotification){
        editor.putBoolean(GET_NOTIFICATION, getNotification);
        editor.commit();
    }
    public boolean isGetNotification(){
        return pref.getBoolean(GET_NOTIFICATION, true);
    }

    public void setProfileImg(String uri){
        editor.putString(PROFILE_IMG, uri);
        editor.commit();
    }
    public String getProfileImg(){
        return pref.getString(PROFILE_IMG, "");
    }

    public void setUserNick(String nickname){
        editor.putString(USER_NICK, nickname);
        editor.commit();
    }
    public String getUserNick(){
        return pref.getString(USER_NICK, "");
    }

    public void setUserName(String name){
        editor.putString(USER_NAME, name);
        editor.commit();
    }
    public String getUserName(){
        return pref.getString(USER_NAME, "");
    }
}
