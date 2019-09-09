package com.handytrip;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.handytrip.Utils.AutoLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindAccountActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);
        ButterKnife.bind(this);

        tabs.addTab(tabs.newTab().setText("비밀번호 찾기"));
        tabs.addTab(tabs.newTab().setText("가입한 이메일 찾기"));
        tabs.setTabTextColors(ContextCompat.getColor(this, R.color.unSelectedTabColor), ContextCompat.getColor(this, R.color.white));
        AutoLayout.setResizeView(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
    }
}
