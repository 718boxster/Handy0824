package com.handytrip.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    Context context;

    private static String USER_ID = "USER_ID";
    private static String USER_PW = "USER_PW";

    public Preferences(Context context) {
        this.context = context;
        pref = context.getSharedPreferences("HANDY.pref", Context.MODE_PRIVATE);
        editor = pref.edit();
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
}
