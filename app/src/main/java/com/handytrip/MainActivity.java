package com.handytrip;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.handytrip.Structures.MissionData;
import com.handytrip.Structures.MissionRecordItem;
import com.handytrip.Utils.AutoLayout;
import com.handytrip.Utils.BaseActivity;
import com.handytrip.Utils.BusEvents;
import com.handytrip.Utils.CustomCalloutBalloonAdapter;
import com.handytrip.Utils.GlobalBus;
import com.handytrip.Utils.MissionRecordListAdapter;
import com.handytrip.Utils.StaticData;
import com.squareup.otto.Subscribe;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener, MapView.POIItemEventListener {
    MapView mapView;
    RelativeLayout mapViewContainer;
    ImageView mission_ready_img;
    Location current;
    long backKeyPressedTime = 0;
    Toast toast;
    MissionData currentMission;
    boolean isMissionIng = false;
    boolean isCurrentMissionCorrect = false;


    boolean isFilterScreenOn = false;
    boolean isMissionFoundScreenOn = false;
    boolean isMissionReadyScreenOn = false;
    boolean isMissionQuestionScreenOn = false;
    boolean isMissionResultScreenOn = false;
    boolean isMissionTipScreenOn = false;

    boolean isInMap = true;


    boolean isMapHeading = true;

    @BindView(R.id.s1)
    TextView s1;
    @BindView(R.id.s2)
    TextView s2;
    @BindView(R.id.s3)
    TextView s3;
    @BindView(R.id.s4)
    TextView s4;
    @BindView(R.id.correct_answer_bg)
    ImageView correctAnswerBg;
    @BindView(R.id.mission_result_text)
    TextView missionResultText;
    @BindView(R.id.mission_result2_text)
    TextView missionResult2Text;
    @BindView(R.id.res1)
    TextView res1;
    @BindView(R.id.res2)
    TextView res2;
    @BindView(R.id.res3)
    TextView res3;
    @BindView(R.id.res4)
    TextView res4;
    @BindView(R.id.mission_result_get_tip)
    TextView missionResultGetTip;
    @BindView(R.id.mission_result_screen)
    RelativeLayout missionResultScreen;
    @BindView(R.id.mission_result_close)
    ImageView missionResultClose;
    @BindView(R.id.mission_finish_hint_close)
    ImageView missionFinishHintClose;
    @BindView(R.id.mission_finish_hint_title)
    TextView missionFinishHintTitle;
    @BindView(R.id.mission_finish_hint_img)
    ImageView missionFinishHintImg;
    @BindView(R.id.mission_finish_confirm)
    TextView missionFinishConfirm;
    @BindView(R.id.mission_finish_hint)
    TextView missionFinishHint;
    @BindView(R.id.mission_finish_tip_screen)
    RelativeLayout missionFinishTipScreen;
    @BindView(R.id.menu_map)
    LinearLayout menuMap;
    @BindView(R.id.menu_record)
    LinearLayout menuRecord;
    @BindView(R.id.menu_my_page)
    LinearLayout menuMyPage;
    @BindView(R.id.menu_map_img)
    ImageView menuMapImg;
    @BindView(R.id.menu_map_text)
    TextView menuMapText;
    @BindView(R.id.menu_record_img)
    ImageView menuRecordImg;
    @BindView(R.id.menu_record_text)
    TextView menuRecordText;
    @BindView(R.id.menu_my_page_img)
    ImageView menuMyPageImg;
    @BindView(R.id.menu_my_page_text)
    TextView menuMyPageText;
    @BindView(R.id.open_menu)
    ImageView openMenu;
    @BindView(R.id.mission_found_distance)
    TextView missionFoundDistance;
    @BindView(R.id.mission_record_back)
    ImageView missionRecordBack;
    @BindView(R.id.mission_record_list)
    RecyclerView missionRecordList;
    @BindView(R.id.mission_record_screen)
    LinearLayout missionRecordScreen;
    @BindView(R.id.input_answer_subject)
    EditText inputAnswerSubject;
    @BindView(R.id.confirm_answer_subject)
    TextView confirmAnswerSubject;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if (isMissionFoundScreenOn) {
            setMissionFoundScreen(false);
            setMainScreen(true);
        } else if (isMissionReadyScreenOn) {
            setMissionReadyScreen(false);
            setMainScreen(true);
        } else if (isMissionQuestionScreenOn) {
            setMissionQuestionScreen(false);
            setMainScreen(true);
        } else if (isMissionResultScreenOn) {
            setMissionResultScreen(false);
            setMainScreen(true);
        } else if (isMissionTipScreenOn) {
            setMissionFinishScreen(false);
            setMainScreen(true);
        } else if (isFilterScreenOn) {
            setFilterScreen(false);
            setMainScreen(true);
        } else if (!isInMap) {
            menuMap.performClick();
        } else {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                toast = Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
                toast.show();
            } else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                toast.cancel();
                finish();
            }
        }
    }

    StaticData staticData = StaticData.getInstance();
    @BindView(R.id.set_filter)
    ImageView setFilter;
    @BindView(R.id.close_filter)
    TextView closeFilter;
    @BindView(R.id.checkbox_all)
    CheckBox checkboxAll;
    @BindView(R.id.filter_select_all)
    LinearLayout filterSelectAll;
    @BindView(R.id.checkbox_done)
    CheckBox checkboxDone;
    @BindView(R.id.filter_select_done)
    LinearLayout filterSelectDone;
    @BindView(R.id.checkbox_history)
    CheckBox checkboxHistory;
    @BindView(R.id.filter_select_history)
    LinearLayout filterSelectHistory;
    @BindView(R.id.checkbox_experience)
    CheckBox checkboxExperience;
    @BindView(R.id.filter_select_experience)
    LinearLayout filterSelectExperience;
    @BindView(R.id.checkbox_sight)
    CheckBox checkboxSight;
    @BindView(R.id.filter_select_sight)
    LinearLayout filterSelectSight;
    @BindView(R.id.filter_confirm)
    TextView filterConfirm;
    @BindView(R.id.filter_screen)
    ConstraintLayout filterScreen;
    @BindView(R.id.zoom_tool)
    LinearLayout zoomTool;
    @BindView(R.id.navi_bar)
    LinearLayout naviBar;
    @BindView(R.id.go_my_location)
    TextView goMyLocation;
    @BindView(R.id.zoom_in)
    ImageView zoomIn;
    @BindView(R.id.zoom_out)
    ImageView zoomOut;
    @BindView(R.id.mission_found_theme)
    TextView missionFoundTheme;
    @BindView(R.id.rating)
    RatingBar rating;
    @BindView(R.id.mission_found_screen)
    ConstraintLayout missionFoundScreen;
    @BindView(R.id.mission_found_later)
    TextView missionFoundLater;
    @BindView(R.id.mission_found_join)
    TextView missionFoundJoin;
    @BindView(R.id.mission_ready_close)
    ImageView missionReadyClose;
    @BindView(R.id.mission_ready_img)
    ImageView missionReadyImg;
    @BindView(R.id.mission_ready_title)
    TextView missionReadyTitle;
    @BindView(R.id.mission_ready_rating)
    RatingBar missionReadyRating;
    @BindView(R.id.mission_ready_theme)
    TextView missionReadyTheme;
    @BindView(R.id.mission_ready_text)
    TextView missionReadyText;
    @BindView(R.id.mission_ready_next)
    TextView missionReadyNext;
    @BindView(R.id.mission_ready_paging)
    TextView missionReadyPaging;
    @BindView(R.id.mission_ready_screen)
    LinearLayout missionReadyScreen;
    @BindView(R.id.compass)
    ImageView compass;
    @BindView(R.id.top_bar)
    RelativeLayout topBar;
    @BindView(R.id.give_up_mission)
    TextView giveUpMission;
    @BindView(R.id.mission_question_text)
    TextView missionQuestionText;
    @BindView(R.id.mission_question_image)
    RelativeLayout missionQuestionImage;
    @BindView(R.id.mission_question_screen)
    LinearLayout missionQuestionScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mission_ready_img = findViewById(R.id.mission_ready_img);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.the_o_twin_three));
        roundedBitmapDrawable.setCornerRadius(5);
        roundedBitmapDrawable.setAntiAlias(true);
        mission_ready_img.setImageDrawable(roundedBitmapDrawable);
        mapView = new MapView(this);
        mapViewContainer = findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);
        mapView.setCurrentLocationEventListener(this);
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
        mapView.setPOIItemEventListener(this);

        setFilterScreen();

        AutoLayout.setResizeView(this);
    }

    public int getDistance(Location current, Location dest) {
        return Math.round(current.distanceTo(dest));
    }

    public void popMissionFound(int theme, int rate) {
        missionFoundScreen.setVisibility(View.VISIBLE);
        switch (theme) {
            case 1:
                missionFoundTheme.setText("역사");
                break;
            case 2:
                missionFoundTheme.setText("체험");
                break;
            case 3:
                missionFoundTheme.setText("풍경");
                break;
        }
        rating.setRating(rate);
    }

    private void getSelectedMissionInfo(MapPOIItem mapPOIItem) {
        if (current == null) {
            Toast.makeText(this, "아직 GPS정보를 수신하지 못했습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            boolean isDone = ((MissionData) mapPOIItem.getUserObject()).isDone();
            if (!isDone) {
                Location selectedMission = new Location("missionPoint");
                selectedMission.setLongitude(((MissionData) mapPOIItem.getUserObject()).getmLng());
                selectedMission.setLatitude(((MissionData) mapPOIItem.getUserObject()).getmLat());
                if (getDistance(current, selectedMission) <= 60) {
                    currentMission = (MissionData) mapPOIItem.getUserObject();
                    setMainScreen(false);
                    setMissionReadyScreen(true);
                } else {
                    Toast.makeText(this, "미션 스팟 50미터 이내에서 진행해주세요.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "이미 완료한 미션입니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "미션 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
//        missionFoundScreen.setVisibility(View.GONE);
        current = new Location("currentPoint");
        current.setLatitude(mapPoint.getMapPointGeoCoord().latitude);
        current.setLongitude(mapPoint.getMapPointGeoCoord().longitude);
        if (!isMissionIng) {
            Location dest = new Location("missionPoint");
            for (int i = 0; i < staticData.missionData.size(); i++) {
                dest.setLatitude(staticData.missionData.get(i).getmLat());
                dest.setLongitude(staticData.missionData.get(i).getmLng());
                if (getDistance(current, dest) <= 60) {
                    isMissionIng = true;
                    currentMission = staticData.missionData.get(i);
                    Call<JsonObject> getUserMissions = api.getUserMissions(
                            pref.getUserId(),
                            currentMission.getmName(),
                            String.valueOf(currentMission.getmLat()),
                            String.valueOf(currentMission.getmLng())
                    );
                    getUserMissions.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            boolean isGiveUp = false;
                            boolean isCorrectBefore = false;
                            boolean isFirstTime = true;
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                            Date date = new Date();
                            Date missionDate = new Date();

                            JsonObject resObject = response.body();
                            JsonArray resArray = resObject.getAsJsonArray("result");
                            if (resArray.size() == 0) {
                                popMissionFound(currentMission.getmTheme(), currentMission.getmRate());
                            } else {
                                JsonObject uMission = resArray.get(0).getAsJsonObject();
                                try {
                                    missionDate = dateFormat.parse(uMission.get("M_ANS_TIME").getAsString());
                                    isGiveUp = false;
                                } catch (Exception e) {
                                    try {
                                        missionDate = dateFormat.parse(uMission.get("M_GIVE_UP_TIME").getAsString());
                                        isGiveUp = true;
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                if (uMission.get("M_IS_CORRECT").getAsInt() == 1) {
                                    isCorrectBefore = true;
                                } else {
                                    isCorrectBefore = false;
                                }

                                if (uMission.get("IS_FIRST_TIME").getAsInt() == 1) {
                                    isFirstTime = true;
                                } else {
                                    isFirstTime = false;
                                }


                                try {
                                    date = dateFormat.parse(dateFormat.format(new Date()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (!isCorrectBefore || isGiveUp) {
                                    if (date.after(missionDate)) {
                                        if (isFirstTime) {
                                            popMissionFound(currentMission.getmTheme(), currentMission.getmRate());
                                        }
                                    } else {
                                        isMissionIng = false;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });


                }
            }
            Log.d("CurrentLocationChanged", "popupMissionFound");
        }
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Subscribe
    public void setPins(BusEvents.setPins pins) {
        setMissionPins();
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        mapView.setCurrentLocationRadius(50);
        mapView.setCurrentLocationRadiusStrokeColor(Color.argb(Integer.parseInt("4c", 16), Integer.parseInt("67", 16), Integer.parseInt("5d", 16), Integer.parseInt("c6", 16)));
        mapView.setCurrentLocationRadiusFillColor(Color.argb(Integer.parseInt("4c", 16), Integer.parseInt("67", 16), Integer.parseInt("5d", 16), Integer.parseInt("c6", 16)));
        getDoneMissions();
//        setMissionPins();
//        MapPOIItem[] mapPOIItems = new MapPOIItem[staticData.missionData.size()];
//        for (int i = 0; i < staticData.missionData.size(); i++) {
//            MapPOIItem marker = new MapPOIItem();
//            marker.setItemName(staticData.missionData.get(i).getmName());
//            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(staticData.missionData.get(i).getmLat(), staticData.missionData.get(i).getmLng()));
//            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
//            switch (staticData.missionData.get(i).getmTheme()) {
//                case 0:
//                    marker.setCustomImageResourceId(R.drawable.pin_compelete);
//                    break;
//                case 1:
//                    marker.setCustomImageResourceId(R.drawable.pin_navy_history);
//                    break;
//                case 2:
//                    marker.setCustomImageResourceId(R.drawable.pin_experience);
//                    break;
//                case 3:
//                    marker.setCustomImageResourceId(R.drawable.pin_landscape);
//                    break;
//            }
//            marker.setUserObject(staticData.missionData.get(i));
//            marker.setCustomImageAutoscale(true);
//            marker.setCustomImageAnchor(0.5f, 1.0f);
//            mapPOIItems[i] = marker;
//        }
//        mapView.addPOIItems(mapPOIItems);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @OnClick({R.id.set_filter, R.id.close_filter, R.id.filter_select_all, R.id.filter_select_done, R.id.filter_select_history, R.id.filter_select_experience, R.id.filter_select_sight, R.id.filter_confirm,
            R.id.mission_found_join, R.id.mission_found_later,
            R.id.mission_ready_close, R.id.mission_ready_next,
            R.id.go_my_location, R.id.zoom_in, R.id.zoom_out, R.id.compass,
            R.id.s1, R.id.s2, R.id.s3, R.id.s4, R.id.give_up_mission,
            R.id.mission_result_get_tip,
            R.id.mission_finish_confirm,
            R.id.mission_result_close,
            R.id.mission_finish_hint_close,
            R.id.menu_map, R.id.menu_record, R.id.menu_my_page,
            R.id.mission_record_back,
            R.id.confirm_answer_subject})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.set_filter:
                setFilterScreen(true);
                setMainScreen(false);
                isMissionIng = true;
                setMissionFoundScreen(false);
                break;
            case R.id.close_filter:
                setFilterScreen(false);
                setMainScreen(true);
                isMissionIng = false;
                break;
            case R.id.filter_select_all:
                if (staticData.filter.isAll()) {
                    staticData.filter.setAll(false);
                } else {
                    staticData.filter.setAll(true);
                }
                setFilterScreen();
                break;
            case R.id.filter_select_done:
                if (staticData.filter.isDone()) {
                    staticData.filter.setDone(false);
                } else {
                    staticData.filter.setDone(true);
                }
                isMissionIng = false;
                setFilterScreen();
                break;
            case R.id.filter_select_history:
                if (staticData.filter.isHistory()) {
                    staticData.filter.setHistory(false);
                } else {
                    staticData.filter.setHistory(true);
                }
                setFilterScreen();
                break;
            case R.id.filter_select_experience:
                if (staticData.filter.isExperience()) {
                    staticData.filter.setExperience(false);
                } else {
                    staticData.filter.setExperience(true);
                }
                setFilterScreen();
                break;
            case R.id.filter_select_sight:
                if (staticData.filter.isSight()) {
                    staticData.filter.setSight(false);
                } else {
                    staticData.filter.setSight(true);
                }
                setFilterScreen();
                break;
            case R.id.filter_confirm:
                setMissionPins();
                setFilterScreen(false);
                setMainScreen(true);
                isMissionIng = false;
                break;

            case R.id.mission_found_later:
                setMissionFoundScreen(false);
                sendGiveUp();
                isMissionIng = false;
                break;
            case R.id.mission_found_join:
                isMissionIng = true;
                Log.d("mission join", "saldfj");
                setMissionFoundScreen(false);
                setMainScreen(false);
                setMissionReadyScreen(true);
                //mission start
                break;

            case R.id.mission_ready_close:
                isMissionIng = false;
                setMissionReadyScreen(false);
                setMainScreen(true);
                sendGiveUp();
                break;

            case R.id.mission_ready_next:
                setMissionReadyScreen(false);
                setMissionQuestionScreen(true);
                break;

            case R.id.go_my_location:
                if (current != null) {
                    mapView.moveCamera(CameraUpdateFactory.newMapPoint(MapPoint.mapPointWithGeoCoord(current.getLatitude(), current.getLongitude())));
                } else {
                    Toast.makeText(MainActivity.this, "아직 GPS 정보를 수신하지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.zoom_in:
                mapView.zoomIn(true);
                break;

            case R.id.zoom_out:
                mapView.zoomOut(true);
                break;

            case R.id.compass:
                isMapHeading = !isMapHeading;
                if (isMapHeading) {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                } else {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                }
                break;

            case R.id.s1:
                sendUserAnswer(currentMission.getS1(), 1);
                break;
            case R.id.s2:
                sendUserAnswer(currentMission.getS2(), 2);
                break;
            case R.id.s3:
                sendUserAnswer(currentMission.getS3(), 3);
                break;
            case R.id.s4:
                sendUserAnswer(currentMission.getS4(), 4);
                break;
            case R.id.give_up_mission:
                sendGiveUp();
                break;

            case R.id.mission_result_get_tip:
                setMissionResultScreen(false);
                setMissionQuestionScreen(false);
                setMissionFinish(isCurrentMissionCorrect ? 1 : 0);
                setMissionFinishScreen(true);
                break;

            case R.id.mission_finish_confirm:
                isMissionIng = false;
                setMissionFinishScreen(false);
                setMainScreen(true);
                break;

            case R.id.mission_result_close:
                isMissionIng = false;
                setMissionResultScreen(false);
                setMainScreen(true);
                break;
            case R.id.mission_finish_hint_close:
                isMissionIng = false;
                setMissionFinishScreen(false);
                setMainScreen(true);
                break;


            case R.id.menu_map:
                isInMap = true;
                isMissionIng = false;
                setMainScreen(true);
                setMissionFoundScreen(false);
                setMissionReadyScreen(false);
                setMissionQuestionScreen(false);
                setMissionResultScreen(false);
                setMissionFinishScreen(false);
                setMissionRecordScreen(false);
                menuMapText.setTextColor(getResources().getColor(R.color.mainThemeColor));
                menuMapImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_map_act));

                menuRecordText.setTextColor(Color.parseColor("#80000000"));
                menuRecordImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_history));

                menuMyPageText.setTextColor(Color.parseColor("#80000000"));
                menuMyPageImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_mypage));
                break;

            case R.id.menu_record:
                isInMap = false;
                isMissionIng = true;
                setMainScreen(false);
                setMissionFoundScreen(false);
                setMissionReadyScreen(false);
                setMissionQuestionScreen(false);
                setMissionResultScreen(false);
                setMissionFinishScreen(false);
                setMissionRecordScreen(true);
                naviBar.setVisibility(View.VISIBLE);
                menuRecordText.setTextColor(getResources().getColor(R.color.mainThemeColor));
                menuRecordImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_history_act));

                menuMapText.setTextColor(Color.parseColor("#80000000"));
                menuMapImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_map));

                menuMyPageText.setTextColor(Color.parseColor("#80000000"));
                menuMyPageImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_mypage));
                break;

            case R.id.menu_my_page:
                isInMap = false;
                isMissionIng = true;
                setMainScreen(false);
                setMissionFoundScreen(false);
                setMissionReadyScreen(false);
                setMissionQuestionScreen(false);
                setMissionResultScreen(false);
                setMissionFinishScreen(false);
                menuMyPageText.setTextColor(getResources().getColor(R.color.mainThemeColor));
                menuMyPageImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_mypage_act));

                menuMapText.setTextColor(Color.parseColor("#80000000"));
                menuMapImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_map));

                menuRecordText.setTextColor(Color.parseColor("#80000000"));
                menuRecordImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_history));
                break;

            case R.id.mission_record_back:
                menuMap.performClick();
                break;

            case R.id.confirm_answer_subject:
                break;
        }
    }

    private void sendNotificationThatWeMetBefore() {
        Call<String> sendNotificationThatWeMetBefore = api.isFirstMeet(
                pref.getUserId(),
                currentMission.getmName(),
                String.valueOf(currentMission.getmLat()),
                String.valueOf(currentMission.getmLng())
        );
        sendNotificationThatWeMetBefore.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("updateFirstMeetCondition", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void setDoneMissionPins() {

        Call<JsonObject> getUserDoneMissions = api.getUserDoneMissions(pref.getUserId());
        getUserDoneMissions.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                staticData.doneData.clear();
                JsonObject object = response.body().getAsJsonObject();
                JsonArray array = object.getAsJsonArray("result");
                for (int i = 0; i < array.size(); i++) {
                    JsonObject res = array.get(i).getAsJsonObject();
                    staticData.doneData.add(new MissionData(
                            res.get("M_NAME").getAsString(),
                            res.get("M_LAT").getAsDouble(),
                            res.get("M_LNG").getAsDouble()
                    ));
                }

                MapPOIItem[] doneItems = new MapPOIItem[staticData.doneData.size()];

                for (int i = 0; i < staticData.doneData.size(); i++) {
                    MapPOIItem marker = new MapPOIItem();
                    marker.setItemName(staticData.doneData.get(i).getmName());
                    marker.setMapPoint(MapPoint.mapPointWithGeoCoord(staticData.doneData.get(i).getmLat(), staticData.doneData.get(i).getmLng()));
                    marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    marker.setCustomImageResourceId(R.drawable.pin_completed);
                    marker.setCustomImageAutoscale(true);
                    marker.setCustomImageAnchor(0.5f, 1.0f);
                    marker.setUserObject(staticData.doneData.get(i));
//                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                    doneItems[i] = marker;
                }
                mapView.addPOIItems(doneItems);

                BusEvents.setPins pins = new BusEvents.setPins();
                GlobalBus.getBus().post(pins);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }

    private void getDoneMissions() {
        Call<JsonObject> getUserDoneMissions = api.getUserDoneMissions(pref.getUserId());
        getUserDoneMissions.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                staticData.doneData.clear();
                JsonObject object = response.body().getAsJsonObject();
                JsonArray array = object.getAsJsonArray("result");
                for (int i = 0; i < array.size(); i++) {
                    JsonObject res = array.get(i).getAsJsonObject();
                    staticData.doneData.add(new MissionData(
                            res.get("M_NAME").getAsString(),
                            res.get("M_LAT").getAsDouble(),
                            res.get("M_LNG").getAsDouble()
                    ));
                }
                BusEvents.setPins pins = new BusEvents.setPins();
                GlobalBus.getBus().post(pins);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void setMissionRecordScreen(boolean on) {
        ArrayList<MissionRecordItem> datas = new ArrayList<>();
        Call<JsonObject> getAllUserMissions = api.getAllUserMissions(pref.getUserId());
        MissionRecordListAdapter adapter = new MissionRecordListAdapter(datas, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        missionRecordList.setAdapter(adapter);
        missionRecordList.setLayoutManager(gridLayoutManager);
        getAllUserMissions.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject obj = response.body();
                JsonArray ary = obj.getAsJsonArray("result");
                for (int i = 0; i < ary.size(); i++) {
                    JsonObject resObj = ary.get(i).getAsJsonObject();
                    datas.add(new MissionRecordItem(
                            resObj.get("M_IMG").getAsString(),
                            resObj.get("M_NAME").getAsString(),
                            TextUtils.isEmpty(resObj.get("M_ANS_TIME").getAsString()) ?
                                    resObj.get("M_GIVE_UP_TIME").getAsString() :
                                    resObj.get("M_ANS_TIME").getAsString()
                    ));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, "미션 기록을 불러오는데 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                menuMap.performClick();
            }
        });

        if (on) {
            missionRecordScreen.setVisibility(View.VISIBLE);
        } else {
            missionRecordScreen.setVisibility(View.GONE);
        }
    }

    private void setMissionFinishScreen(boolean on) {
        isMissionTipScreenOn = on;
        if (on) {
            missionFinishTipScreen.setVisibility(View.VISIBLE);
        } else {
            missionFinishTipScreen.setVisibility(View.GONE);
        }
    }

    private void setMissionFinish(int isCorrect) {
        boolean isC = isCorrect == 1;
        if (isC) {
            missionFinishHintTitle.setText("여행 꿀Tip");
            Glide.with(MainActivity.this)
                    .load(currentMission.getmTipImgUrl())
                    .into(missionFinishHintImg);
            missionFinishHint.setText(currentMission.getmTipText());
            missionFinishConfirm.setText("미션 완료");
        } else {
            missionFinishHintTitle.setText("힌트를 보고\n재시도 해보세요!");
            Glide.with(MainActivity.this)
                    .load(currentMission.getmHintImgUrl())
                    .into(missionFinishHintImg);
            missionFinishHint.setText(currentMission.getmHintText());
            missionFinishConfirm.setText("10분 후에 재시도");
        }
    }

    public String getTimeAfter10minutes() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Log.d("timeNow : ", sdf.format(calendar.getTime()));
        calendar.add(Calendar.MINUTE, 10);
        Log.d("reMatchTime : ", sdf.format(calendar.getTime()));
        return sdf.format(calendar.getTime());
    }

    public String getTime(String currentDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        try {
            // Get calendar set to current date and time with Singapore time zone
            Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Calcutta"));
            calendar.setTime(format.parse(currentDate));
            //Formatter
            DateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
            return formatter.format(calendar.getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private void sendGiveUp() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        String currentDateandTime = sdf.format(new Date());
        String timeNow = getTimeAfter10minutes();
        Call<String> putUserGiveUp = api.putUserAnswer(
                pref.getUserId(),
                currentMission.getmName(),
                String.valueOf(currentMission.getmLat()),
                String.valueOf(currentMission.getmLng()),
                0,
                "",
                timeNow,
                currentMission.getmReadyImgUrl()
        );
        putUserGiveUp.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                isMissionIng = false;
                setMissionFoundScreen(false);
                setMissionReadyScreen(false);
                setMissionQuestionScreen(false);
                setMissionResultScreen(false);
                setMainScreen(true);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isMissionIng = false;
                setMissionFoundScreen(false);
                setMissionReadyScreen(false);
                setMissionQuestionScreen(false);
                setMissionResultScreen(false);
                setMainScreen(true);
            }
        });

        sendNotificationThatWeMetBefore();
    }

    private void sendUserAnswer(String ans, int selection) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        String currentDateandTime = sdf.format(new Date());
        String timeNow = getTimeAfter10minutes();
        Call<String> putUserAnswer = api.putUserAnswer(
                pref.getUserId(),
                currentMission.getmName(),
                String.valueOf(currentMission.getmLat()),
                String.valueOf(currentMission.getmLng()),
                currentMission.getmAns().equals(ans) ? 1 : 0,
                timeNow,
                "",
                currentMission.getmReadyImgUrl());

        putUserAnswer.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("putAnswerReponse", response.body());
                if (currentMission.getmAns().equals(ans)) {
                    setMissionResultCorrect(selection);
                    isCurrentMissionCorrect = true;
                } else {
                    setMissionResultWrong(selection);
                    isCurrentMissionCorrect = false;
                }
                setMissionResultScreen(true);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });

        sendNotificationThatWeMetBefore();
    }

    private void setMissionResultScreen(boolean on) {
        isMissionResultScreenOn = on;
        if (on) {
            missionResultScreen.setVisibility(View.VISIBLE);
        } else {
            missionResultScreen.setVisibility(View.GONE);
        }
    }

    private void setMissionResultWrong(int userSelected) {
        missionResultText.setText("안타깝게도\n오답이에요!");
        missionResult2Text.setText("괜찮아요!\n10분 만 기다리면\n다시 시도할 수 있어요^^");
        correctAnswerBg.setVisibility(View.GONE);
        missionResultGetTip.setText("힌트 보러가기");
        switch (userSelected) {
            case 1:
                res1.setBackground(getResources().getDrawable(R.drawable.wrong_btn_normal));
                res2.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                res3.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                res4.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                break;
            case 2:
                res2.setBackground(getResources().getDrawable(R.drawable.wrong_btn_normal));
                res1.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                res3.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                res4.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                break;
            case 3:
                res3.setBackground(getResources().getDrawable(R.drawable.wrong_btn_normal));
                res1.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                res2.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                res4.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                break;
            case 4:
                res4.setBackground(getResources().getDrawable(R.drawable.wrong_btn_normal));
                res1.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                res2.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                res3.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                break;
        }
    }

    private void setMissionResultCorrect(int userSelected) {
        missionResultText.setText("정답!\n축하해요");
        missionResult2Text.setText("여행 꿀팁을 알려줄게요!");
        correctAnswerBg.setVisibility(View.VISIBLE);
        correctAnswerBg.setImageResource(R.drawable.petal);
        missionResultGetTip.setText("여행 꿀팁 보러가기");
        switch (userSelected) {
            case 1:
                res1.setBackground(getResources().getDrawable(R.drawable.correct_btn_normal));
                res2.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                res3.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                res4.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                break;
            case 2:
                res2.setBackground(getResources().getDrawable(R.drawable.correct_btn_normal));
                res1.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                res3.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                res4.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                break;
            case 3:
                res3.setBackground(getResources().getDrawable(R.drawable.correct_btn_normal));
                res1.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                res2.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                res4.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                break;
            case 4:
                res4.setBackground(getResources().getDrawable(R.drawable.correct_btn_normal));
                res1.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                res2.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                res3.setBackground(getResources().getDrawable(R.drawable.transparent_rounded_box_normal));
                break;
        }
    }

    private void setMainScreen(boolean on) {
        if (on) {
            topBar.setVisibility(View.VISIBLE);
            compass.setVisibility(View.VISIBLE);
            setFilter.setVisibility(View.VISIBLE);
            closeFilter.setVisibility(View.GONE);
            zoomTool.setVisibility(View.VISIBLE);
            naviBar.setVisibility(View.VISIBLE);
            goMyLocation.setVisibility(View.VISIBLE);
        } else {
            topBar.setVisibility(View.GONE);
            compass.setVisibility(View.GONE);
            setFilter.setVisibility(View.GONE);
            closeFilter.setVisibility(View.VISIBLE);
            zoomTool.setVisibility(View.GONE);
            naviBar.setVisibility(View.GONE);
            goMyLocation.setVisibility(View.GONE);
        }
    }

    private void setMissionQuestionScreen(boolean on) {
        isMissionQuestionScreenOn = on;
        if (on) {
            missionQuestionScreen.setVisibility(View.VISIBLE);
            Glide.with(MainActivity.this)
                    .load(currentMission.getmReadyImgUrl())
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            missionQuestionImage.setBackground(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
            missionQuestionText.setText(currentMission.getmQuest());
            if (currentMission.isEssay()) {
                s1.setVisibility(View.GONE);
                s2.setVisibility(View.GONE);
                s3.setVisibility(View.GONE);
                s4.setVisibility(View.GONE);

                res2.setVisibility(View.GONE);
                res3.setVisibility(View.GONE);
                res4.setVisibility(View.GONE);

                inputAnswerSubject.setVisibility(View.VISIBLE);
                confirmAnswerSubject.setVisibility(View.VISIBLE);
            } else {
                s1.setVisibility(View.VISIBLE);
                s2.setVisibility(View.VISIBLE);
                s3.setVisibility(View.VISIBLE);
                s4.setVisibility(View.VISIBLE);

                res2.setVisibility(View.VISIBLE);
                res3.setVisibility(View.VISIBLE);
                res4.setVisibility(View.VISIBLE);

                s1.setText(currentMission.getS1());
                s2.setText(currentMission.getS2());
                s3.setText(currentMission.getS3());
                s4.setText(currentMission.getS4());

                res1.setText(currentMission.getS1());
                res2.setText(currentMission.getS2());
                res3.setText(currentMission.getS3());
                res4.setText(currentMission.getS4());

                inputAnswerSubject.setVisibility(View.GONE);
                confirmAnswerSubject.setVisibility(View.GONE);
            }
        } else {
            missionQuestionScreen.setVisibility(View.GONE);
        }
    }

    private void setMissionFoundScreen(boolean on) {
        isMissionFoundScreenOn = on;
        if (on) {
            missionFoundScreen.setVisibility(View.VISIBLE);
        } else {
            missionFoundScreen.setVisibility(View.GONE);
        }
    }

    private void setMissionReadyScreen(boolean on) {
        isMissionReadyScreenOn = on;
        if (on) {
            missionReadyScreen.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(currentMission.getmReadyImgUrl())
                    .into(missionReadyImg);
            missionReadyTitle.setText(currentMission.getmName());
            switch (currentMission.getmTheme()) {
                case 1:
                    missionReadyTheme.setText("역사");
                    break;
                case 2:
                    missionReadyTheme.setText("체험");
                    break;
                case 3:
                    missionReadyTheme.setText("풍경");
                    break;
            }
            missionReadyText.setText(currentMission.getmReadyText());
            missionReadyRating.setRating(currentMission.getmRate());
        } else {
            missionReadyScreen.setVisibility(View.GONE);
        }
    }

    private MapPOIItem[] removeNull(MapPOIItem[] origin) {
        ArrayList<MapPOIItem> arrayList = new ArrayList<>();
        for (int i = 0; i < origin.length; i++) {
            if (origin[i] != null) {
                arrayList.add(origin[i]);
            }
        }
        MapPOIItem[] resArray = arrayList.toArray(new MapPOIItem[arrayList.size()]);
        return resArray;
    }

    private void setMissionPins() {
        mapView.removeAllPOIItems();
        ArrayList<MissionData> historyData = new ArrayList<>();
        ArrayList<MissionData> experienceData = new ArrayList<>();
        ArrayList<MissionData> sightData = new ArrayList<>();
        ArrayList<MissionData> doneData = new ArrayList<>();
        for (int i = 0; i < staticData.missionData.size(); i++) {
            switch (staticData.missionData.get(i).getmTheme()) {
//                case 0:
//                    doneData.add(staticData.missionData.get(i));
//                    break;
                case 1:
                    if (!staticData.missionData.get(i).isDone())
                        historyData.add(staticData.missionData.get(i));
                    else
                        doneData.add(staticData.missionData.get(i));
                    break;
                case 2:
                    if (!staticData.missionData.get(i).isDone())
                        experienceData.add(staticData.missionData.get(i));
                    else
                        doneData.add(staticData.missionData.get(i));
                    break;
                case 3:
                    if (!staticData.missionData.get(i).isDone())
                        sightData.add(staticData.missionData.get(i));
                    else
                        doneData.add(staticData.missionData.get(i));
                    break;
            }
        }

        MapPOIItem[] historyItems = new MapPOIItem[historyData.size()];
        MapPOIItem[] experienceItems = new MapPOIItem[experienceData.size()];
        MapPOIItem[] sightItems = new MapPOIItem[sightData.size()];
        MapPOIItem[] doneItems = new MapPOIItem[doneData.size()];

        if (staticData.filter.isDone()) {
//            setDoneMissionPins();
            for (int i = 0; i < doneData.size(); i++) {
                MapPOIItem marker = new MapPOIItem();
                marker.setItemName(doneData.get(i).getmName());
                marker.setMapPoint(MapPoint.mapPointWithGeoCoord(doneData.get(i).getmLat(), doneData.get(i).getmLng()));
                marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                marker.setCustomImageResourceId(R.drawable.pin_completed);
                marker.setCustomImageAutoscale(true);
                marker.setCustomImageAnchor(0.5f, 1.0f);
                marker.setUserObject(doneData.get(i));
                doneItems[i] = marker;
            }
            if (doneItems.length > 0)
                mapView.addPOIItems(doneItems);
        }

        if (staticData.filter.isHistory()) {
            for (int i = 0; i < historyData.size(); i++) {
                MapPOIItem marker = new MapPOIItem();
                marker.setItemName(historyData.get(i).getmName());
                marker.setMapPoint(MapPoint.mapPointWithGeoCoord(historyData.get(i).getmLat(), historyData.get(i).getmLng()));
                marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                marker.setCustomImageResourceId(R.drawable.pin_navy_history);
                marker.setCustomImageAutoscale(true);
                marker.setCustomImageAnchor(0.5f, 1.0f);
                marker.setUserObject(historyData.get(i));
                historyItems[i] = marker;
            }
            if (historyItems.length > 0)
                mapView.addPOIItems(historyItems);
        }
        if (staticData.filter.isExperience()) {
            for (int i = 0; i < experienceData.size(); i++) {
                MapPOIItem marker = new MapPOIItem();
                marker.setItemName(experienceData.get(i).getmName());
                marker.setMapPoint(MapPoint.mapPointWithGeoCoord(experienceData.get(i).getmLat(), experienceData.get(i).getmLng()));
                marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                marker.setCustomImageResourceId(R.drawable.pin_experience);
                marker.setCustomImageAutoscale(true);
                marker.setCustomImageAnchor(0.5f, 1.0f);
                marker.setUserObject(experienceData.get(i));
                experienceItems[i] = marker;
            }
            if (experienceItems.length > 0)
                mapView.addPOIItems(experienceItems);
        }


        if (staticData.filter.isSight()) {
            for (int i = 0; i < sightData.size(); i++) {
                MapPOIItem marker = new MapPOIItem();
                marker.setItemName(sightData.get(i).getmName());
                marker.setMapPoint(MapPoint.mapPointWithGeoCoord(sightData.get(i).getmLat(), sightData.get(i).getmLng()));
                marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                marker.setCustomImageResourceId(R.drawable.pin_landscape);
                marker.setCustomImageAutoscale(true);
                marker.setCustomImageAnchor(0.5f, 1.0f);
                marker.setUserObject(sightData.get(i));
                sightItems[i] = marker;
            }
            if (sightItems.length > 0)
                mapView.addPOIItems(sightItems);
        }
    }

    private void setFilterScreen(boolean on) {
        isFilterScreenOn = on;
        if (on) {
            filterScreen.setVisibility(View.VISIBLE);
        } else {
            filterScreen.setVisibility(View.GONE);
        }
    }

    private void setFilterScreen() {
        if (staticData.filter.isAll()) {
            checkboxAll.setChecked(true);
            checkboxDone.setChecked(true);
            checkboxHistory.setChecked(true);
            checkboxExperience.setChecked(true);
            checkboxSight.setChecked(true);
        } else {
            checkboxAll.setChecked(false);
            if (staticData.filter.isDone()) {
                checkboxDone.setChecked(true);
            } else {
                checkboxDone.setChecked(false);
            }

            if (staticData.filter.isHistory()) {
                checkboxHistory.setChecked(true);
            } else {
                checkboxHistory.setChecked(false);
            }

            if (staticData.filter.isExperience()) {
                checkboxExperience.setChecked(true);
            } else {
                checkboxExperience.setChecked(false);
            }

            if (staticData.filter.isSight()) {
                checkboxSight.setChecked(true);
            } else {
                checkboxSight.setChecked(false);
            }
        }
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        getSelectedMissionInfo(mapPOIItem);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem
            mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint
            mapPoint) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalBus.getBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalBus.getBus().unregister(this);
    }

    @OnClick(R.id.confirm_answer_subject)
    public void onViewClicked() {
    }
}
