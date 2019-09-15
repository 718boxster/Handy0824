package com.handytrip;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.handytrip.Structures.MissionData;
import com.handytrip.Utils.AutoLayout;
import com.handytrip.Utils.BaseActivity;
import com.handytrip.Utils.StaticData;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.appTitle)
    TextView appTitle;
    @BindView(R.id.progress)
    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        AutoLayout.setResizeView(this);


        Call<JsonObject> getMissions = api.getAllMissions();
        getMissions.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JsonObject missionObj = response.body();
                    JsonArray allMissionsArray = missionObj.getAsJsonArray("result");
                    StaticData staticData = StaticData.getInstance();
                    staticData.missionData.clear();
                    for (int i = 0; i < allMissionsArray.size(); i++) {
                        JsonObject mission = allMissionsArray.get(i).getAsJsonObject();
                        try {
                            MissionData addData = new MissionData();
                            addData.setmName(mission.get("M_NAME").getAsString().replace("\"", ""));
                            addData.setmPlace(mission.get("M_PLACE").getAsString().replace("\"", ""));
                            addData.setmLat(mission.get("M_LAT").getAsDouble());
                            addData.setmLng(mission.get("M_LNG").getAsDouble());
                            addData.setmTheme(mission.get("M_THEME").getAsInt());
                            addData.setmRate(mission.get("M_RATE").getAsInt());
                            addData.setmReadyText(mission.get("M_READY_T").getAsString().replace("\"", ""));
                            addData.setmReadyImgUrl(staticData.IMG_BASE_URL + mission.get("M_READY_IMG").getAsString().replace("\"", ""));
                            addData.setmHintText(mission.get("M_HINT_T").toString().replace("\"", ""));
                            addData.setmHintImgUrl(staticData.IMG_BASE_URL + mission.get("M_HINT_IMG").getAsString().replace("\"", ""));
                            addData.setmTipText(mission.get("M_TIP_T").toString().replace("\"", ""));
                            addData.setmTipImgUrl( staticData.IMG_BASE_URL + mission.get("M_TIP_IMG").getAsString().replace("\"", ""));
                            addData.setmQuest(mission.get("M_QUEST_T").getAsString().replace("\"", ""));
                            addData.setmAns(mission.get("ANS").getAsString().replace("\"", ""));
                            if( (TextUtils.isEmpty(mission.get("S_1").toString()) || "null".equals(mission.get("S_1").toString()) )
                            || (TextUtils.isEmpty(mission.get("S_2").toString()) || "null".equals(mission.get("S_2").toString()) )
                            || (TextUtils.isEmpty(mission.get("S_3").toString()) || "null".equals(mission.get("S_3").toString()) )
                            || (TextUtils.isEmpty(mission.get("S_4").toString()) || "null".equals(mission.get("S_4").toString()) )){
                                addData.setEssay(true);
                            } else{
                                addData.setS1(mission.get("S_1").getAsString().replace("\"", ""));
                                addData.setS2(mission.get("S_2").getAsString().replace("\"", ""));
                                addData.setS3(mission.get("S_3").getAsString().replace("\"", ""));
                                addData.setS4(mission.get("S_4").getAsString().replace("\"", ""));
                                addData.setEssay(false);
                            }
                            Log.d("missionName", mission.get("M_NAME").toString());
                            staticData.missionData.add(addData);
                        } catch (Exception e){
                            e.printStackTrace();
//                            Toast.makeText(SplashActivity.this, "미션 정보를 받아오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
//                            MissionData addData = new MissionData();
//                            addData.setmName(mission.get("M_NAME").getAsString());
//                            addData.setmPlace(mission.get("M_PLACE").getAsString());
//                            addData.setmLat(mission.get("M_LAT").getAsDouble());
//                            addData.setmLng(mission.get("M_LNG").getAsDouble());
//                            addData.setmTheme(mission.get("M_THEME").getAsInt());
//                            addData.setmRate(mission.get("M_RATE").getAsInt());
//                            addData.setmReadyText(mission.get("M_READY_T").getAsString());
//                            addData.setmReadyImgUrl(staticData.IMG_BASE_URL + mission.get("M_READY_IMG").getAsString());
//                            addData.setmHintText(mission.get("M_HINT_T").getAsString());
//                            addData.setmHintImgUrl(staticData.IMG_BASE_URL + mission.get("M_HINT_IMG").getAsString());
//                            addData.setmTipText(mission.get("M_TIP_T").getAsString());
//                            addData.setmTipImgUrl( staticData.IMG_BASE_URL + mission.get("M_TIP_IMG").getAsString());
//                            addData.setmQuest(mission.get("M_QUEST_T").getAsString());
//                            addData.setmAns(mission.get("ANS").getAsString());
//                            addData.setS1(mission.get("S_1").getAsString());
//                            addData.setS2(mission.get("S_2").getAsString());
//                            addData.setS3(mission.get("S_3").getAsString());
//                            addData.setS4(mission.get("S_4").getAsString());
//                            addData.setEssay(true);
//                            staticData.missionData.add(addData);
                        }
                    }

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SplashActivity.this, "데이터를 받아오는데 실패했습니다. 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("failed to get missions", call.toString() + "  /  " + t.toString());
                Toast.makeText(SplashActivity.this, "서버와의 통신이 원활하지 않습니다. 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

//        GetMission getMission = new GetMission();
//        getMission.execute();
    }

    class GetMission extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
}
