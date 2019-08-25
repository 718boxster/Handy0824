package com.handytrip;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
                            staticData.missionData.add(new MissionData(
                                    mission.get("M_NAME").getAsString(),
                                    mission.get("M_PLACE").getAsString(),
                                    mission.get("M_LAT").getAsDouble(),
                                    mission.get("M_LNG").getAsDouble(),
                                    mission.get("M_THEME").getAsInt(),
                                    mission.get("M_RATE").getAsInt(),
                                    mission.get("M_READY_T").getAsString(),
                                    staticData.IMG_BASE_URL + mission.get("M_READY_IMG").getAsString(),
                                    mission.get("M_HINT_T").getAsString(),
                                    staticData.IMG_BASE_URL + mission.get("M_HINT_IMG").getAsString(),
                                    mission.get("M_TIP_T").getAsString(),
                                    staticData.IMG_BASE_URL + mission.get("M_TIP_IMG").getAsString(),
                                    mission.get("M_QUEST_T").getAsString(),
                                    mission.get("ANS").getAsString(),
                                    mission.get("S_1").getAsString(),
                                    mission.get("S_2").getAsString(),
                                    mission.get("S_3").getAsString(),
                                    mission.get("S_4").getAsString()
                            ));
                        } catch (Exception e){
                            e.printStackTrace();
//                            Toast.makeText(SplashActivity.this, "미션 정보를 받아오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                            staticData.missionData.add(new MissionData(
                                    mission.get("M_NAME").getAsString(),
                                    mission.get("M_PLACE").getAsString(),
                                    mission.get("M_LAT").getAsDouble(),
                                    mission.get("M_LNG").getAsDouble(),
                                    mission.get("M_THEME").getAsInt(),
                                    mission.get("M_RATE").getAsInt(),
                                    mission.get("M_READY_T").getAsString(),
                                    staticData.IMG_BASE_URL + mission.get("M_READY_IMG").getAsString(),
                                    mission.get("M_HINT_T").getAsString(),
                                    staticData.IMG_BASE_URL + mission.get("M_HINT_IMG").getAsString(),
                                    mission.get("M_TIP_T").getAsString(),
                                    staticData.IMG_BASE_URL + mission.get("M_TIP_IMG").getAsString(),
                                    mission.get("M_QUEST_T").getAsString(),
                                    mission.get("ANS").getAsString(),
                                    "",
                                    "",
                                    "",
                                    ""
                            ));
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
