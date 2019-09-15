package com.handytrip;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.handytrip.Structures.MissionData;
import com.handytrip.Utils.AutoLayout;
import com.handytrip.Utils.BaseActivity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {


    long backKeyPressedTime = 0;
    Toast toast;
    boolean isFindingPassword = false;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.signup)
    TextView signup;
    @BindView(R.id.find_id)
    TextView findId;
    @BindView(R.id.confirm)
    TextView confirm;

    boolean isAllPermissionGranted = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            toast.cancel();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        AutoLayout.setResizeView(this);


        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                isAllPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                isAllPermissionGranted = false;
            }
        };
        TedPermission.with(this).setPermissionListener(permissionListener)
                .setPermissions(
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).check();


        inputEmail.requestFocus();
        inputEmail.performClick();
    }


    @OnClick({R.id.back, R.id.signup, R.id.find_id, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.signup:
                Intent signUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivityForResult(signUp, 1);
                break;
            case R.id.find_id:
                Intent intent = new Intent(LoginActivity.this, FindAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.confirm:
                if (!isAllPermissionGranted) {
                    Toast.makeText(this, "설정->애플리케이션->권한 에서 앱에서 요구하는 권한을 부여해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isNetworkConnected(this)) {
                    Toast.makeText(this, "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isEmailValid(inputEmail.getText().toString())) {
                    Toast.makeText(this, "이메일 형식을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String uId = inputEmail.getText().toString();
                String uPw = inputPassword.getText().toString();
                if (TextUtils.isEmpty(uId)) {
                    Toast.makeText(LoginActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(uPw)) {
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("로그인");
                progressDialog.setMessage("로그인 하는 중...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

                Call<String> login = api.login(uId, uPw);
                login.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body().toString().equals("F")) {
                            Toast.makeText(LoginActivity.this, "통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        } else if (response.body().toString().equals("LOGIN_FAILED")) {
                            Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        } else if (response.body().toString().equals("LOGIN_SUCCESS")) {
                            pref.setUserId(uId);
                            pref.setUserPw(uPw);
                            Call<JsonObject> getUserDoneMissions = api.getUserDoneMissions(pref.getUserId());
                            getUserDoneMissions.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                                    JsonObject obj = response.body();
                                    JsonArray array = obj.getAsJsonArray("result");
                                    staticData.doneData.clear();
                                    for (int i = 0; i < array.size(); i++) {
                                        JsonObject res = array.get(i).getAsJsonObject();
                                        staticData.doneData.add(new MissionData(
                                                res.get("M_NAME").getAsString(),
                                                res.get("M_LAT").getAsDouble(),
                                                res.get("M_LNG").getAsDouble()
                                        ));
                                    }

                                    for (int i = 0; i < staticData.missionData.size(); i++) {
                                        for (int j = 0; j < staticData.doneData.size(); j++) {
                                            if (staticData.doneData.get(j).getmName().equals(staticData.missionData.get(i).getmName())
                                                    && staticData.doneData.get(j).getmLat() == staticData.missionData.get(i).getmLat()
                                                    && staticData.doneData.get(j).getmLng() == staticData.missionData.get(i).getmLng()) {
                                                staticData.missionData.get(i).setDone(true);
                                            }
                                        }
                                    }

                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }

                            });

                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    String u_id = data.getStringExtra("u_id");
                    inputEmail.setText(u_id);
                    inputPassword.requestFocus();
                    break;
            }
        }
    }
}
