package com.handytrip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.handytrip.Utils.AutoLayout;
import com.handytrip.Utils.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.shadow)
    View shadow;
    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.confirm_password)
    EditText confirmPassword;

    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.input_phone)
    EditText inputPhone;
    @BindView(R.id.input_certi)
    EditText inputCerti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        AutoLayout.setResizeView(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick({R.id.back, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.confirm:
                if(! isEmailValid(inputEmail.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "이메일 형식을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(! inputPassword.getText().toString().equals(confirmPassword.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Call<String> signUp = api.signUp(
                        inputEmail.getText().toString(),
                        confirmPassword.getText().toString(),
                        inputPhone.getText().toString());
                signUp.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String res = response.body();
                        if(res.contains("ERR")){
                            Toast.makeText(SignUpActivity.this, "서버와 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                        } else if("EXIST".equals(res)){
                            Toast.makeText(SignUpActivity.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                        } else if("ERR_WHILE_INSERT".equals(res)){
                            Toast.makeText(SignUpActivity.this, "서버와 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(SignUpActivity.this, "회원 가입이 완료되었습니다. 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            intent.putExtra("u_id", inputEmail.getText().toString());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(SignUpActivity.this, "서버와 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
