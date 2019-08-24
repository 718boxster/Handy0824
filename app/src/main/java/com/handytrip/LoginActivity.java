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

import androidx.annotation.RequiresApi;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.handytrip.Utils.AutoLayout;
import com.handytrip.Utils.BaseActivity;
import com.handytrip.Utils.Preferences;

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


    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @OnClick({R.id.back, R.id.signup, R.id.find_id, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.signup:
            case R.id.find_id:
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.confirm:
                if(!isAllPermissionGranted){
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
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();

                        if(response.body().toString().equals("F")){
                            Toast.makeText(LoginActivity.this, "통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                        } else if(response.body().toString().equals("LOGIN_FAILED")){
                            Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                        } else if(response.body().toString().equals("LOGIN_SUCCESS")){
                            pref.setUserId(uId);
                            pref.setUserPw(uPw);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
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

    private class Login extends AsyncTask<Void, Void, String> {

        Context context;
        String id;
        String pw;
        String result;
        ProgressDialog progressDialog;

        public Login(Context context, String id, String pw) {
            this.context = context;
            this.id = id;
            this.pw = pw;
            this.result = "";
            this.progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {

            Log.d("login result", result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);



        }
    }
}
