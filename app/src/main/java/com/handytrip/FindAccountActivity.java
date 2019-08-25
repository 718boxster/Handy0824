package com.handytrip;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.handytrip.Utils.AutoLayout;

import butterknife.ButterKnife;

public class FindAccountActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);
        ButterKnife.bind(this);
        AutoLayout.setResizeView(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
