package com.handytrip;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.handytrip.Structures.MissionRecordItem;
import com.handytrip.Utils.AutoLayout;
import com.handytrip.Utils.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionTip extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.mission_name)
    TextView missionName;
    @BindView(R.id.mission_rate)
    RatingBar missionRate;
    @BindView(R.id.mission_theme)
    TextView missionTheme;
    @BindView(R.id.mission_date)
    TextView missionDate;
    @BindView(R.id.mission_text)
    TextView missionText;

    String mName;
    String mLat;
    String mLng;
    String mPlace;
    String mDate;
    String mTip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_tip);
        ButterKnife.bind(this);
        AutoLayout.setResizeView(this);
//        if (getIntent().hasExtra("data")) {
            MissionRecordItem data = ((MissionRecordItem) getIntent().getSerializableExtra("data"));
            mName = data.getmName();
            mLat = data.getmLat();
            mLng = data.getmLng();
            mPlace = data.getmPlace();
            mDate = data.getmDate();

            Call<JsonObject> getMission = api.getSpecificMission(mName, mPlace, mLat, mLng);
            getMission.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject resObj = response.body();
                    JsonArray resArray = resObj.getAsJsonArray("result");
                    Log.d("getMissionTip", resObj.toString());
                    try {
                        JsonObject res = resArray.get(0).getAsJsonObject();
                        Glide.with(MissionTip.this).load(staticData.IMG_BASE_URL + res.get("M_READY_IMG").getAsString()).into(img);
                        missionName.setText(res.get("M_NAME").getAsString());
                        switch (res.get("M_THEME").getAsInt()) {
                            case 1:
                                missionTheme.setText("역사");
                                break;
                            case 2:
                                missionTheme.setText("체험");
                                break;
                            case 3:
                                missionTheme.setText("풍경");
                                break;
                            default:
                                missionTheme.setText("");
                                break;
                        }
                        missionRate.setRating(res.get("M_RATE").getAsInt());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        Date date = sdf.parse(mDate);
                        SimpleDateFormat parseDate = new SimpleDateFormat("yyyy. MM. dd a HH:mm", Locale.US);
                        String resDate = parseDate.format(date);
                        missionDate.setText(resDate);
                        missionText.setText(res.get("M_TIP_T").getAsString());

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    t.printStackTrace();
                }
            });
//        }


    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
