package com.handytrip.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
    public StaticData staticData;
    public RetrofitAPI api;
    public Preferences pref;
    public boolean isSuccess = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        staticData = new StaticData();
        api = RetrofitInit.getRetrofit();
        pref = new Preferences(this);
    }


    public boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo wimax = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
        boolean bwimax = false;

        if (wimax != null) {
            bwimax = wimax.isConnected();
        }
        if (mobile != null) {
            if (mobile.isConnected() || wifi.isConnected() || bwimax) {
                return true;
            }
        } else {
            if (wifi.isConnected() || bwimax) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public boolean sendFcmToken(String token){
//        Log.d("sendedToken-Base", token);
        Call<String> sendFcmToken = api.sendFcmToken(pref.getUserId(), token);
        sendFcmToken.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                isSuccess = true;
                Toast.makeText(BaseActivity.this, "푸시 서버에 등록되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isSuccess = false;
            }
        });
        return isSuccess;
    }
}
