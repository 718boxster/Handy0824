package com.handytrip;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.handytrip.Utils.Preferences;

public class ProfileDialog extends Dialog {

    String title;
    Context context;
    Preferences pref;
    OnDialogResult result;

    public ProfileDialog(@NonNull Context context, String title) {
        super(context);
        this.title = title;
        this.context = context;
        pref = new Preferences(context);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if("NICKNAME".equals(title)){
            setContentView(R.layout.nickname);
            TextView titleText = findViewById(R.id.title);
            EditText nick = findViewById(R.id.input_nick_name);
            TextView cancel = findViewById(R.id.cancel);
            TextView save = findViewById(R.id.save);

            titleText.setText(title);
            if(! TextUtils.isEmpty(pref.getUserNick())){
                nick.setHint(pref.getUserNick());
            }
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProfileDialog.this.dismiss();
                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!TextUtils.isEmpty(nick.getText().toString())){
                        pref.setUserNick(nick.getText().toString());
                    } else{
                        pref.setUserNick(" ");
                    }
                    Toast.makeText(context, "닉네임이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    result.done("change");
                    ProfileDialog.this.dismiss();
                }
            });
        } else if("NAME".equals(title)){
            setContentView(R.layout.nickname);
            TextView titleText = findViewById(R.id.title);
            EditText nick = findViewById(R.id.input_nick_name);
            TextView cancel = findViewById(R.id.cancel);
            TextView save = findViewById(R.id.save);

            titleText.setText(title);
            if(! TextUtils.isEmpty(pref.getUserName())){
                nick.setHint(pref.getUserName());
            }
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProfileDialog.this.dismiss();
                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!TextUtils.isEmpty(nick.getText().toString())){
                        pref.setUserName(nick.getText().toString());
                        Toast.makeText(context, "사용자 이름이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        result.done("change");
                        ProfileDialog.this.dismiss();
                    } else{
                        Toast.makeText(context, "사용자 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else if("ACCOUNT".equals(title)){
            setContentView(R.layout.show_email);
            TextView save = findViewById(R.id.save);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProfileDialog.this.dismiss();
                }
            });
        }
    }

    public void setOnDialogResult(OnDialogResult onDialogResult) {
        result = onDialogResult;
    }

    public interface OnDialogResult {
        void done(String done);
    }
}
