package com.handytrip;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
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
import com.handytrip.Utils.RecyclerViewMargin;
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

public class MainActivity extends BaseActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener, MapView.POIItemEventListener, NavigationView.OnNavigationItemSelectedListener {
    MapView mapView;
    RelativeLayout mapViewContainer;
    ImageView mission_ready_img;
    Location current;
    long backKeyPressedTime = 0;
    Toast toast;
    MissionData currentMission;
    boolean isMissionIng = false;
    boolean isCurrentMissionCorrect = false;

    //현재 표시중인 화면 제어용 변수
    boolean isFilterScreenOn = false;  //필터
    boolean isMissionFoundScreenOn = false; //미션발견
    boolean isMissionReadyScreenOn = false; //미션준비
    boolean isMissionQuestionScreenOn = false; //미션문제
    boolean isMissionResultScreenOn = false; //미션결과
    boolean isMissionTipScreenOn = false; //미션팁
    boolean isMyPageScreenOn = false; //마이페이지
    boolean isMyInfoScreenOn = false; //내정보
    boolean isSettingsScreenOn = false; //설정
    boolean isMissionRecordScreenOn = false; //미션기록
    boolean isInMap = true; //현재 지도가 표시되어지고 있는지
    boolean isPop = false; //미션발견 팝업이 실행중인지
    boolean isMapHeading = true; //지도 방향을 고정할것인지

    private static final int MISSION_DISTANCE = 60; //미션발견 최소 거리 (60미터)

    DrawerLayout drawerLayout; //왼쪽의 네비게이션바


    //화면에 보여지는 뷰들을 정의한 부분
    //ButterKnife를 사용했기때문에 onCreate에서 뷰를 찾아주는 작업을 하지 않아도 됨.
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
    @BindView(R.id.mypage_myinfo)
    LinearLayout mypageMyinfo;
    @BindView(R.id.mypage_reward)
    LinearLayout mypageReward;
    @BindView(R.id.mypage_mission_history)
    LinearLayout mypageMissionHistory;
    @BindView(R.id.mypageScreen)
    LinearLayout mypageScreen;
    @BindView(R.id.myinfo_mission_history)
    LinearLayout myinfoMissionHistory;
    @BindView(R.id.myinfoScreen)
    LinearLayout myinfoScreen;
    @BindView(R.id.myinfo_back)
    ImageView myinfoBack;
    @BindView(R.id.settings_back)
    ImageView settingsBack;
    @BindView(R.id.settingsScreen)
    ScrollView settingsScreen;
    @BindView(R.id.set_alarm)
    Switch setAlarm;
    @BindView(R.id.set_location)
    Switch setLocation;
    @BindView(R.id.set_keep_screen_on)
    Switch setKeepScreenOn;
    @BindView(R.id.mypage_email)
    LinearLayout mypageEmail;
    @BindView(R.id.mypage_logout)
    LinearLayout mypageLogout;
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

    NavigationView navigationView;

    MissionRecordListAdapter adapter;

    ArrayList<MissionRecordItem> datas = new ArrayList<>(); //서버에서 받아온 미션 기록을 저장할 어레이리스트
    GridLayoutManager gridLayoutManager;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() { //뒤로가기 버튼을 눌렀을때
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {  //제일 먼저 왼쪽의 네비게이션바가 실행되어지고있는지 확인 후 닫음
            drawerLayout.closeDrawers();
        } else if (isMyPageScreenOn) { //이하의 코드는 각 화면이 실행되어지고 있는지 확인 후 최종적으로 지도 화면이 남게되면 앱을 종료시킴
            setMyPageScreen(false);
            setMainScreen(true);
            setMenuMap();
        } else if (isMyInfoScreenOn) {
            setMyInfoScreen(false);
            setMainScreen(true);
            setMenuMap();
        } else if (isMissionRecordScreenOn) {
            setMissionRecordScreen(false);
            setMainScreen(true);
            setMenuMap();
        } else if (isSettingsScreenOn) {
            setSettingsScreen(false);
            setMainScreen(true);
            setMenuMap();
        } else if (isMissionFoundScreenOn) {
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
            naviBar.setVisibility(View.VISIBLE);
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

    //하단 메뉴의 지도 탭 터치시
    private void setMenuMap() {
        //지도 메뉴를 활성화 색으로 바꾸고 나머지 메뉴 비활성화
        menuMapText.setTextColor(getResources().getColor(R.color.mainThemeColor));
        menuMapImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_map_act));

        menuRecordText.setTextColor(Color.parseColor("#80000000"));
        menuRecordImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_history));

        menuMyPageText.setTextColor(Color.parseColor("#80000000"));
        menuMyPageImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_mypage));
    }

    //StaticData.java 파일에 정의되어있는 static 변수들을 가져옴
    StaticData staticData = StaticData.getInstance();


    @Override //네비게이션바에서 메뉴 클릭시 메뉴 실행 후 네비게이션바 닫음
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu click event

//        switch (item.getItemId()) {
//            case R.id.settings:
//                setSettingsScreen(true);
//                break;
//        }
        drawerLayout.closeDrawers();
        return super.onOptionsItemSelected(item);
    }

    public static MainActivity mainActivity; //메인액티비티를 싱글톤으로 실행하기 위해 스태틱 변수로 불러올 수 있게 함

    public static MainActivity getInstance() {
        if (mainActivity == null) {
            mainActivity = new MainActivity();
        }
        return mainActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        drawerLayout = findViewById(R.id.drawerLayout);
        mission_ready_img = findViewById(R.id.mission_ready_img);
        //맵뷰는 다음맵을 사용했음
        mapView = new MapView(this);
        mapViewContainer = findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);
        mapView.setCurrentLocationEventListener(this); //위치 변화가 있을때 실행되는 이벤트 리스너
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading); //맵 방향 회전을 할것인지
        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
        mapView.setPOIItemEventListener(this); //맵뷰에서 핀터치시 실행되는 이벤트 리스너

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        //네비게이션바에 있는 프로필 이미지 뷰
        ImageView profileImg = navigationView.getHeaderView(0).findViewById(R.id.profile_img);
        try{ //Glide 라이브러리를 사용해 동그랗게 편집해서 넣어줌
            profileImg.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this).load(Uri.parse(pref.getProfileImg())).apply(new RequestOptions().circleCrop()).into(profileImg);
        } catch (Exception e){
            e.printStackTrace();
        }
        //알림을 받기위한 FCM 토큰을 서버에 저장하는 함수
        sendFcmToken(FirebaseInstanceId.getInstance().getToken());

        //저장되어있는 FCM토큰이 없는 경우 파이어베이스에 토큰을 요청해서 저장
        if (TextUtils.isEmpty(pref.getFcmToken())) {
            pref.setFcmToken(FirebaseInstanceId.getInstance().getToken());
            if (!sendFcmToken(pref.getFcmToken())) {
//                Toast.makeText(MainActivity.this, "푸시 메세지 서버와의 통신이 불안정합니다.\n알림을 받지 못할 수 있습니다.", Toast.LENGTH_SHORT).show();
            }
        } else if (!pref.getFcmToken().equals(FirebaseInstanceId.getInstance().getToken())) {
            if (!sendFcmToken(pref.getFcmToken())) {
//                Toast.makeText(MainActivity.this, "푸시 메세지 서버와의 통신이 불안정합니다.\n알림을 받지 못할 수 있습니다.", Toast.LENGTH_SHORT).show();
            }
        }

//        Log.d("FCMFCMFCM", pref.getFcmToken());

        //필터 체크박스의 옵션값 저장장
        checkboxAll.setOnCheckedChangeListener((compoundButton, b) -> staticData.filter.setAll(b));
        checkboxDone.setOnCheckedChangeListener((compoundButton, b) -> staticData.filter.setDone(b));
        checkboxHistory.setOnCheckedChangeListener((compoundButton, b) -> staticData.filter.setHistory(b));
        checkboxExperience.setOnCheckedChangeListener((compoundButton, b) -> staticData.filter.setExperience(b));
        checkboxSight.setOnCheckedChangeListener((compoundButton, b) -> staticData.filter.setSight(b));


        adapter = new MissionRecordListAdapter(datas, new View.OnClickListener() {
            @Override
            public void onClick(View view) { //미션 기록 리스트 아이템 클릭 리스너
                MissionRecordItem posData = ((MissionRecordItem) view.getTag()); //해당 아이템의 정보를 불러와서
                Intent intent = new Intent(MainActivity.this, MissionTip.class); //MissionTip 액티비티에 전송하며 MissionTip 액티비티 실행
                intent.putExtra("data", posData);
                startActivity(intent);
            }
        }, this);
        gridLayoutManager = new GridLayoutManager(this, 2);
        missionRecordList.setAdapter(adapter);
        missionRecordList.setLayoutManager(gridLayoutManager);
        missionRecordList.addItemDecoration(new RecyclerViewMargin(20, 2));

        //알림설정 온/오프
        setAlarm.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                pref.setGetNotification(true);
            } else {
                //do not get Notification
                pref.setGetNotification(false);
            }
        });
        setAlarm.setChecked(false);

        //위치설정 온/오프 : 기능없음
        setLocation.setOnCheckedChangeListener((compoundButton, b) -> {
        });

        //디스플레이 항상 켜짐 온/오프
        setKeepScreenOn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });
        setKeepScreenOn.setChecked(true);

        //저장된 필터 설정을 체크해둠
        setFilterScreen();

        //기기에 따라 디스플레이 픽셀 수를 계산하여 레이아웃 크기 조정하는 클래스 : Utils -> AutoLayout.java
        AutoLayout.setResizeView(this);
    }

    //현재 위치와 목적지까지의 직선거리를 계산해주는 함수
    public int getDistance(Location current, Location dest) {
        return Math.round(current.distanceTo(dest));
    }

    //근처에 미션이 발견되면 미션발견 팝업 뷰를 표시해주는 함수
    public void popMissionFound(int theme, int rate) {
        missionFoundScreen.setVisibility(View.VISIBLE);
        switch (theme) { //해당 미션의 테마에 따라 텍스트가 바뀜
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
        //해당 미션의 난이도에 따라 난이도 표시가 달라짐
        rating.setRating(rate);
    }

    //처음 발견된 미션이 아닌 미션 중, 지도에서 직접 선택한 미션의 정보를 보여주는 뷰를 표시해주는 함수
    private void getSelectedMissionInfo(MapPOIItem mapPOIItem) {
        if (current == null) { //current 변수는 전역변수로 현재 위치가 변동될때마다 값이 바뀜
            Toast.makeText(this, "아직 GPS정보를 수신하지 못했습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        try { //선택한 미션이 이미 완료된 미션인지 확인하는 변수
            boolean isDone = ((MissionData) mapPOIItem.getUserObject()).isDone();
            //선택한 미션의 정보를 저장하는 변수
            currentMission = (MissionData) mapPOIItem.getUserObject();
            //현재 미션이 진행중이라는걸 알려주는 변수 : 이 변수가 true 값일때, 주변의 미션을 발견해도 미션 발견 팝업을 표시하지 않는다.
            isMissionIng = true;
            if (!isDone) { //완료된 미션이 아닐 경우
                Location selectedMission = new Location("missionPoint"); //현재 미션의 좌표값을 받아온다.
                selectedMission.setLongitude(((MissionData) mapPOIItem.getUserObject()).getmLng());
                selectedMission.setLatitude(((MissionData) mapPOIItem.getUserObject()).getmLat());
                if (getDistance(current, selectedMission) <= MISSION_DISTANCE) { //기준거리 내의 미션일 경우 미션 진행
                    Call<JsonObject> getUserMissions = api.getUserMissions( //유저 아이디, 미션이름, 미션좌표를 파라미터로 해당 미션을 데이터베이스에서 검색한다.
                            pref.getUserId(),
                            currentMission.getmName(),
                            String.valueOf(currentMission.getmLat()),
                            String.valueOf(currentMission.getmLng())
                    );
                    getUserMissions.enqueue(new Callback<JsonObject>() { //검색 결과
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            boolean isGiveUp = false; //포기했던 미션인지
                            boolean isCorrectBefore = false; //오답을 제출했던 미션인지
                            boolean isFirstTime = false; //처음 만나는 미션인지
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss"); //날짜형식은 데이터베이스에 있는 형식과 맞춰주고, 이를 활용해 10분이 지났는지 계산한다.
                            Date date = new Date(); //현재시간
                            Date missionDate = new Date(); //미션 포기/오답/정답 시간

                            JsonObject resObject = response.body();
                            JsonArray resArray = resObject.getAsJsonArray("result");
//                            if (resArray.size() == 0) {
////                                popMissionFound(currentMission.getmTheme(), currentMission.getmRate());
//                            } else {
                            try {
                                JsonObject uMission = resArray.get(0).getAsJsonObject();
                                try {
                                    missionDate = dateFormat.parse(uMission.get("M_ANS_TIME").getAsString()); //오답/정답 제출 시간이 존재한다면
                                    isGiveUp = false; //포기하지 않은 미션
                                } catch (Exception e) { //오답/정답 제출 시간이 없다면
                                    try {
                                        missionDate = dateFormat.parse(uMission.get("M_GIVE_UP_TIME").getAsString()); //포기한 시간 저장
                                        isGiveUp = true;
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                if (uMission.get("M_IS_CORRECT").getAsInt() == 1) { //정답을 제출했었는지   1:정답  2:오답
                                    isCorrectBefore = true;
                                } else {
                                    isCorrectBefore = false;
                                }

                                if (uMission.get("IS_FIRST_TIME").getAsInt() == 1) { //처음 만나는 미션인지  1:처음  2:처음아님
                                    isFirstTime = true;
                                } else {
                                    isFirstTime = false;
                                }
                                try {
                                    date = dateFormat.parse(dateFormat.format(new Date())); //현재 시간을 받아옴
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (!isCorrectBefore || isGiveUp) { //오답이었거나 포기한 미션인 경우
                                    if (date.after(missionDate)) { //오답제출/포기 시간이 현재 시간보다 10분 전인지 계산해서
                                        setMainScreen(false); //맞다면 미션 화면을 보여준다.
                                        setMissionFoundScreen(false);
                                        setMissionReadyScreen(true);
                                    } else { //10분이 지나기 전인 경우, 메세지를 출력해준다.
                                        Toast.makeText(MainActivity.this, "아직 10분이 지나지 않았어요!", Toast.LENGTH_SHORT).show();
                                        isMissionIng = false; //현재 미션이 진행중이지 않다 라고 알려준다.
                                    }
                                } else { //정답을 제출했던 미션인 경우
                                    //이미 완료한 미션임을 알려준다.
                                    Toast.makeText(MainActivity.this, "이미 완료한 미션입니다.", Toast.LENGTH_SHORT).show();
                                    isMissionIng = false; //현재 미션이 진행중이지 않다 라고 알려준다.
                                }
                            } catch (Exception e) { //유저 아이디, 미션이름, 미션좌표를 파라미터로 검색했을 때, 데이터베이스에 미션이 존재하지 않는다면, 처음 만난 미션으로 간주하여 미션을 진행시킨다.
                                e.printStackTrace();
                                isMissionIng = true; //현재 미션이 진행중이라고 알림
                                setMainScreen(false); //지도 화면을 끄고
                                setMissionFoundScreen(false); //주변 미션 감지 팝업을 끄고
                                setMissionReadyScreen(true); //미션 준비 화면을 보여준다.
                            }

                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });
                } else { //선택한 미션이 기준거리 내에 있지 않은 경우, 메세지 출력
                    Toast.makeText(this, "미션 스팟 50미터 이내에서 진행해주세요.", Toast.LENGTH_SHORT).show();
                }
            } else { //이미 완료한 미션인 경우 메세지 출력
                Toast.makeText(MainActivity.this, "이미 완료한 미션입니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) { //해당 미션의 정보가 정상적으로 저장되어있지 않은 경우, 메세지 출력
            e.printStackTrace();
            Toast.makeText(this, "미션 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override //현재 위치가 바뀔때마다 호출되는 함수로, 다음지도 API의 메서드이다.
    //바로 위에 있는, 맵에서 직접 미션을 선택했을 때와 동일한 로직으로 동작한다.
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
                if (getDistance(current, dest) <= MISSION_DISTANCE) {
                    isMissionIng = true;
                    currentMission = staticData.missionData.get(i);
                    Call<JsonObject> getUserMissions = api.getUserMissions(
                            pref.getUserId(),
                            currentMission.getmName(),
                            String.valueOf(currentMission.getmLat()),
                            String.valueOf(currentMission.getmLng())
                    );
                    Log.d("currentMissionName", currentMission.getmName());
                    Log.d("currentMissionLat", currentMission.getmLat() + "");
                    Log.d("currentMissionLng", currentMission.getmLng() + "");
                    getUserMissions.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            boolean isGiveUp = false;
                            boolean isCorrectBefore = false;
                            boolean isFirstTime = false;
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                            Date date = new Date();
                            Date missionDate = new Date();

                            JsonObject resObject = response.body();
                            JsonArray resArray = resObject.getAsJsonArray("result");
//                            if (resArray.size() == 0) {
////                                popMissionFound(currentMission.getmTheme(), currentMission.getmRate());
//                            } else {
                            try {
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
                                }

                                if (uMission.get("IS_FIRST_TIME").getAsInt() == 1) {
                                    isFirstTime = true;
                                }


                                try {
                                    date = dateFormat.parse(dateFormat.format(new Date()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (isGiveUp) {
                                    if (date.after(missionDate)) {
//                                        if (isFirstTime) {
                                        isPop = true;
                                        popMissionFound(currentMission.getmTheme(), currentMission.getmRate());
//                                        }
                                    } else {
                                        isMissionIng = false;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                isPop = true;
                                popMissionFound(currentMission.getmTheme(), currentMission.getmRate());
                            }

                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });
                    if (isPop) {
                        isPop = false;
                        break;
                    }
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

    @Override //다음 지도가 정상적으로 호출되었을 때 호출되는 함수
    public void onMapViewInitialized(MapView mapView) {
        mapView.setCurrentLocationRadius(50); //현재위치 반경 50미터 크기의 원 생성
        //원의 테두리 색 설정
        mapView.setCurrentLocationRadiusStrokeColor(Color.argb(Integer.parseInt("4c", 16), Integer.parseInt("67", 16), Integer.parseInt("5d", 16), Integer.parseInt("c6", 16)));
        //원의 배경 색 설정
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

    //각 뷰들을 터치했을 때 동작하는 이벤트 리스너
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
            R.id.confirm_answer_subject,
            R.id.open_menu}) //위와 같은 뷰들에 모두 이벤트 리스너를 연결시킨다. ( ButterKnife 플러그인)
    //이 이벤트 리스너에서는 뷰를 보여주고, 숨기는 동작이 주를 이룬다.
    //예를 들어, setMainScreen(true); 라면, 메인화면(지도)을 표시해준다.
    //반대로 setMainScreen(false); 라면, 메인화면(지도)을 표시하지 않는다.
    //상황에 따라 몇개의 뷰를 숨기고, 표시해주는 동작을 한다.
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

            case R.id.go_my_location: //내 위치로 이동하기 버튼을 눌렀을 때.
                if (current != null) { //위치가 변동될때마다 내 위치를 저장하는 current 변수로부터 경도, 위도를 받아와서 맵 중앙에 위치시킨다.
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

            case R.id.compass: //지도 보기 모드를 방향 고정으로, 또는 방향회전으로 설정한다.
                isMapHeading = !isMapHeading;
                if (isMapHeading) {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                } else {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                }
                break;

            case R.id.s1: //문제 정답을 선택하면, 번호에 따라 sendUserAnswer 함수를 호출하여 사용자가 선택한 정답을 서버로 전송한다.
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
                sendGiveUp(); //미션 포기를 누르면, 지금 진행중인 미션을 포기했음을 서버로 전송한다.
                break;

            case R.id.mission_result_get_tip:
                inputAnswerSubject.setText("");
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
                setMyPageScreen(false);
                setMyInfoScreen(false);
                setSettingsScreen(false);
                setMenuMap();
                break;

            case R.id.menu_record: //하단의 미션 기록 탭
                isInMap = false;
                isMissionIng = true;
                setMainScreen(false);
                setMissionFoundScreen(false);
                setMissionReadyScreen(false);
                setMissionQuestionScreen(false);
                setMissionResultScreen(false);
                setMissionFinishScreen(false);
                setMissionRecordScreen(true);
                setMyPageScreen(false);
                setMyInfoScreen(false);
                setSettingsScreen(false);
                naviBar.setVisibility(View.VISIBLE);
                menuRecordText.setTextColor(getResources().getColor(R.color.mainThemeColor));
                menuRecordImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_history_act));

                menuMapText.setTextColor(Color.parseColor("#80000000"));
                menuMapImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_map));

                menuMyPageText.setTextColor(Color.parseColor("#80000000"));
                menuMyPageImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_mypage));
                break;

            case R.id.menu_my_page: //하단의 마이페이지 탭
                isInMap = false;
                isMissionIng = true;
                setMainScreen(false);
                setMissionFoundScreen(false);
                setMissionReadyScreen(false);
                setMissionQuestionScreen(false);
                setMissionResultScreen(false);
                setMissionFinishScreen(false);
                setMissionRecordScreen(false);
                setMyInfoScreen(false);
                setMyPageScreen(true);
                setSettingsScreen(false);
                naviBar.setVisibility(View.VISIBLE);
                menuMyPageText.setTextColor(getResources().getColor(R.color.mainThemeColor));
                menuMyPageImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_mypage_act));

                menuMapText.setTextColor(Color.parseColor("#80000000"));
                menuMapImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_map));

                menuRecordText.setTextColor(Color.parseColor("#80000000"));
                menuRecordImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_history));
                break;

            case R.id.mission_record_back:
                isInMap = true;
                isMissionIng = false;
                setMainScreen(true);
                setMissionFoundScreen(false);
                setMissionReadyScreen(false);
                setMissionQuestionScreen(false);
                setMissionResultScreen(false);
                setMissionFinishScreen(false);
                setMissionRecordScreen(false);
                setMyPageScreen(false);
                setMyInfoScreen(false);
                setSettingsScreen(false);
                menuMapText.setTextColor(getResources().getColor(R.color.mainThemeColor));
                menuMapImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_map_act));

                menuRecordText.setTextColor(Color.parseColor("#80000000"));
                menuRecordImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_history));

                menuMyPageText.setTextColor(Color.parseColor("#80000000"));
                menuMyPageImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_mypage));
                break;

            case R.id.confirm_answer_subject:
                sendUserAnswer(inputAnswerSubject.getText().toString());
                hideKeyboard(this);
                break;

            case R.id.open_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
    }

    private void setMyPageScreen(boolean on) {
        isMyPageScreenOn = on;
        if (on) {
            mypageScreen.setVisibility(View.VISIBLE);
        } else {
            mypageScreen.setVisibility(View.GONE);
        }
    }

    public static void hideKeyboard(Activity activity) { //주관식 정답을 입력하고 제출버튼을 눌렀을때, 키보드를 숨겨주는 함수
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void sendNotificationThatWeMetBefore() { //해당 미션을 한번 마주쳤음을 서버로 전송해준다.
        Call<String> sendNotificationThatWeMetBefore = api.isFirstMeet(
                pref.getUserId(),
                currentMission.getmName(),
                String.valueOf(currentMission.getmLat()),
                String.valueOf(currentMission.getmLng())
        );
        sendNotificationThatWeMetBefore.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("updateFirstMeet", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    private void getDoneMissions() { //서버로부터 완료된 미션을 받아오는 함수
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
                            res.get("M_LNG").getAsDouble(),
                            res.get("M_PLACE").getAsString()
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
        isMissionRecordScreenOn = on;
        if (on) {
            naviBar.setVisibility(View.VISIBLE);
            missionRecordScreen.setVisibility(View.VISIBLE);
            Call<JsonObject> getAllUserMissions = api.getAllUserMissions(pref.getUserId());
            getAllUserMissions.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject obj = response.body();
                    JsonArray ary = obj.getAsJsonArray("result");
                    datas.clear();
                    for (int i = 0; i < ary.size(); i++) {
                        JsonObject resObj = ary.get(i).getAsJsonObject();
                        datas.add(new MissionRecordItem(
                                resObj.get("M_IMG").getAsString(),
                                resObj.get("M_NAME").getAsString(),
                                TextUtils.isEmpty(resObj.get("M_ANS_TIME").getAsString()) ?
                                        resObj.get("M_GIVE_UP_TIME").getAsString() :
                                        resObj.get("M_ANS_TIME").getAsString(),
                                resObj.get("M_PLACE").getAsString(),
                                resObj.get("M_LAT").getAsString(),
                                resObj.get("M_LNG").getAsString()
                        ));
                        Log.d("recordImgUrl", resObj.get("M_IMG").getAsString());
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "미션 기록을 불러오는데 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    menuMap.performClick();
                }
            });

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

    private void setMissionFinish(int isCorrect) { //정답 제출 후, 오답인지 정답인지를 판단하여 서로 다른 화면을 출력해준다.
        boolean isC = isCorrect == 1;
        if (isC) {
            missionFinishHintTitle.setText("여행 꿀Tip");
            Glide.with(MainActivity.this)
                    .load(currentMission.getmTipImgUrl())
                    .override(500, 200)
                    .into(missionFinishHintImg);
            missionFinishHint.setText(currentMission.getmTipText());
            missionFinishConfirm.setText("미션 완료");
        } else {
            missionFinishHintTitle.setText("힌트를 보고\n재시도 해보세요!");
            Glide.with(MainActivity.this)
                    .load(currentMission.getmHintImgUrl())
                    .override(500, 200)
                    .into(missionFinishHintImg);
            missionFinishHint.setText(currentMission.getmHintText());
            missionFinishConfirm.setText("10분 후에 재시도");
        }
    }

    //현재 시간으로부터 10분이 지난 시간을 return해주는 함수
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

    //미션을 포기함을 서버로 전송하는 함수
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
                currentMission.getmReadyImgUrl(),
                0,
                currentMission.getmPlace()
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

    //사용자가 입력한 답을 서버로 전송하는 함수 (객관식)
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
                currentMission.getmReadyImgUrl(),
                0,
                currentMission.getmPlace());

        putUserAnswer.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("putAnswerReponse", response.body());
                if (currentMission.getmAns().equals(ans)) {
                    setMissionResultCorrect(selection);
                    isCurrentMissionCorrect = true;
                    currentMission.setmTheme(4);
                } else {
                    setMissionResultWrong(selection);
                    isCurrentMissionCorrect = false;
                }
                setMissionResultScreen(true);
                setMissionPins();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });

        sendNotificationThatWeMetBefore();
    }
    //사용자가 입력한 답을 서버로 전송하는 함수 (주관식)
    private void sendUserAnswer(String ans) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        String currentDateandTime = sdf.format(new Date());
        String timeNow = getTimeAfter10minutes();
        Log.d("isCorrect??", ans.equals(currentMission.getmAns()) + "");
        Call<String> putUserAnswer = api.putUserAnswer(
                pref.getUserId(),
                currentMission.getmName(),
                String.valueOf(currentMission.getmLat()),
                String.valueOf(currentMission.getmLng()),
                currentMission.getmAns().equals(ans) ? 1 : 0,
                timeNow,
                "",
                currentMission.getmReadyImgUrl(),
                0,
                currentMission.getmPlace());

        putUserAnswer.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("putAnswerReponse", response.body());
                if (currentMission.getmAns().equals(ans)) {
                    setMissionResultCorrect();
                    isCurrentMissionCorrect = true;
                    currentMission.setmTheme(4);
                } else {
                    setMissionResultWrong();
                    isCurrentMissionCorrect = false;
                }
                setMissionResultScreen(true);
                setMissionPins();
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

    //오답 제출시 결과 화면 (객관식)
    private void setMissionResultWrong(int userSelected) {
        missionResultText.setText("안타깝게도\n오답이에요!");
        missionResult2Text.setText("괜찮아요!\n10분 만 기다리면\n다시 시도할 수 있어요^^");
        correctAnswerBg.setVisibility(View.GONE);
        missionResultGetTip.setText("힌트 보러가기");
        res2.setVisibility(View.VISIBLE);
        res3.setVisibility(View.VISIBLE);
        res4.setVisibility(View.VISIBLE);
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
    //오답 제출시 결과 화면 (주관식)
    private void setMissionResultWrong() {
        missionResultText.setText("안타깝게도\n오답이에요!");
        missionResult2Text.setText("괜찮아요!\n10분만 기다리면\n다시 시도할 수 있어요^^");
        correctAnswerBg.setVisibility(View.GONE);
        missionResultGetTip.setText("힌트 보러가기");
        res1.setText(inputAnswerSubject.getText().toString());
        res1.setBackground(getResources().getDrawable(R.drawable.wrong_btn_normal));
        res2.setVisibility(View.GONE);
        res3.setVisibility(View.GONE);
        res4.setVisibility(View.GONE);
    }
    //정답 제출시 결과 화면(주관식)
    private void setMissionResultCorrect() {
        missionResultText.setText("정답!\n축하해요");
        missionResult2Text.setText("여행 꿀팁을 알려줄게요!");
        correctAnswerBg.setVisibility(View.VISIBLE);
        correctAnswerBg.setImageResource(R.drawable.petal);
        missionResultGetTip.setText("여행 꿀팁 보러가기");
        res1.setText(inputAnswerSubject.getText().toString());
        res1.setBackground(getResources().getDrawable(R.drawable.correct_btn_normal));
        res2.setVisibility(View.GONE);
        res3.setVisibility(View.GONE);
        res4.setVisibility(View.GONE);
    }
    //정답 제출시 결과 화면(객관식)
    private void setMissionResultCorrect(int userSelected) {
        missionResultText.setText("정답!\n축하해요");
        missionResult2Text.setText("여행 꿀팁을 알려줄게요!");
        correctAnswerBg.setVisibility(View.VISIBLE);
        correctAnswerBg.setImageResource(R.drawable.petal);
        missionResultGetTip.setText("여행 꿀팁 보러가기");
        res2.setVisibility(View.VISIBLE);
        res3.setVisibility(View.VISIBLE);
        res4.setVisibility(View.VISIBLE);
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
                            Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                            bitmap = Bitmap.createScaledBitmap(bitmap, 1080, 1440, false);
                            missionQuestionImage.setBackground(new BitmapDrawable(bitmap));
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
            Log.d("missionReadyImgUrl", currentMission.getmReadyImgUrl());
            Glide.with(MainActivity.this)
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


    //데이터베이스에 있는 미션 좌표를 읽어와서 지도에 표시해주는 함수
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
                case 1: //각 테마별로 구분하여 저장한 후
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
                default:
                    doneData.add(staticData.missionData.get(i));
                    break;
            }
        }

        MapPOIItem[] historyItems = new MapPOIItem[historyData.size()];
        MapPOIItem[] experienceItems = new MapPOIItem[experienceData.size()];
        MapPOIItem[] sightItems = new MapPOIItem[sightData.size()];
        MapPOIItem[] doneItems = new MapPOIItem[doneData.size()];

        if (staticData.filter.isDone()) { //필터에서 선택되어있는 테마만 표시함
            Log.d("setDonePin", "adding");
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
            if (doneItems.length > 0) {
                if (staticData.filter.isDone()) {
                    mapView.addPOIItems(doneItems);
                }
            }
        }

        if (staticData.filter.isHistory()) {
            Log.d("setHistoryPin", "adding");
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
            if (historyItems.length > 0) {
                if (staticData.filter.isHistory()) {
                    mapView.addPOIItems(historyItems);
                }
            }

        }

        if (staticData.filter.isExperience()) {
            Log.d("setExperiencePin", "adding");
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
            if (experienceItems.length > 0) {
                if (staticData.filter.isExperience()) {
                    mapView.addPOIItems(experienceItems);
                }
            }

        }


        if (staticData.filter.isSight()) {
            Log.d("setSightPin", "adding");
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
            if (sightItems.length > 0) {
                if (staticData.filter.isSight()) {
                    mapView.addPOIItems(sightItems);
                }
            }

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

    private void setMyInfoScreen(boolean on) {
        isMyInfoScreenOn = on;
        if (on) {
            myinfoScreen.setVisibility(View.VISIBLE);
        } else {
            myinfoScreen.setVisibility(View.GONE);
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


    //네비게이션에서 메뉴를 선택했을때
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.settings) { //설정을 누르면
            setSettingsScreen(true); //설정 화면 켬
            setMainScreen(false); //메인화면 끔
            setMissionFoundScreen(false); //미션팝업 끔
            setMissionReadyScreen(false); //미션준비화면 끔
            setMissionQuestionScreen(false); //미션문제화면 끔
            setMissionResultScreen(false); //미션결과화면 끔
            setMissionFinishScreen(false); //미션마무리화면 끔
            setMyPageScreen(false); //마이페이지 끔
            setMyInfoScreen(false); //내 정보 끔
            isInMap = false; //현재 지도를 표시하고있지 않음 ( 메인화면이 아님 )
            isMissionIng = true; //미션이 진행중이라고 설정하여 미션 팝업이 생성되지 않도록 한다.
        } else if (menuItem.getItemId() == R.id.notice) { //공지사항 클릭시
            Intent intent = new Intent(MainActivity.this, Notifications.class); //공지사항 액티비티로 이동한다.
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START); //네비게이션 닫음
        return true;
    }

    @OnClick(R.id.mypage_myinfo)
    public void onMypageMyinfoClicked() {
//        setMainScreen(false);
//        setMissionFoundScreen(false);
//        setMissionReadyScreen(false);
//        setMissionQuestionScreen(false);
//        setMissionResultScreen(false);
//        setMissionFinishScreen(false);
//        setMyPageScreen(false);
//        setMyInfoScreen(true);
//        setSettingsScreen(false);
        Intent intent = new Intent(MainActivity.this, Profile.class);
        startActivityForResult(intent, 7);
    }

    @OnClick(R.id.mypage_reward)
    public void onMypageRewardClicked() {
    }

    @OnClick(R.id.mypage_mission_history)
    public void onMypageMissionHistoryClicked() {
        isInMap = false;
        isMissionIng = true;
        setMainScreen(false);
        setMissionFoundScreen(false);
        setMissionReadyScreen(false);
        setMissionQuestionScreen(false);
        setMissionResultScreen(false);
        setMissionFinishScreen(false);
        setMissionRecordScreen(true);
        setMyPageScreen(false);
        setMyInfoScreen(false);
        setSettingsScreen(false);
        naviBar.setVisibility(View.VISIBLE);
        menuRecordText.setTextColor(getResources().getColor(R.color.mainThemeColor));
        menuRecordImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_history_act));

        menuMapText.setTextColor(Color.parseColor("#80000000"));
        menuMapImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_map));

        menuMyPageText.setTextColor(Color.parseColor("#80000000"));
        menuMyPageImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_mypage));
    }

    @OnClick(R.id.myinfo_mission_history)
    public void onViewClicked() {
        isInMap = false;
        isMissionIng = true;
        setMainScreen(false);
        setMissionFoundScreen(false);
        setMissionReadyScreen(false);
        setMissionQuestionScreen(false);
        setMissionResultScreen(false);
        setMissionFinishScreen(false);
        setMissionRecordScreen(true);
        setMyPageScreen(false);
        setMyInfoScreen(false);
        setSettingsScreen(false);
        naviBar.setVisibility(View.VISIBLE);
        menuRecordText.setTextColor(getResources().getColor(R.color.mainThemeColor));
        menuRecordImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_history_act));

        menuMapText.setTextColor(Color.parseColor("#80000000"));
        menuMapImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_map));

        menuMyPageText.setTextColor(Color.parseColor("#80000000"));
        menuMyPageImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_mypage));
    }

    @OnClick(R.id.myinfo_back)
    public void onMyinfoBackClicked() {
        isInMap = true;
        isMissionIng = false;
        setMainScreen(true);
        setMissionFoundScreen(false);
        setMissionReadyScreen(false);
        setMissionQuestionScreen(false);
        setMissionResultScreen(false);
        setMissionFinishScreen(false);
        setMissionRecordScreen(false);
        setMyPageScreen(false);
        setMyInfoScreen(false);
        setSettingsScreen(false);
        menuMapText.setTextColor(getResources().getColor(R.color.mainThemeColor));
        menuMapImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_map_act));

        menuRecordText.setTextColor(Color.parseColor("#80000000"));
        menuRecordImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_history));

        menuMyPageText.setTextColor(Color.parseColor("#80000000"));
        menuMyPageImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_mypage));
    }

    @OnClick(R.id.settings_back)
    public void onSettingsBackClicked() {
        isInMap = true;
        isMissionIng = false;
        setMainScreen(true);
        setMissionFoundScreen(false);
        setMissionReadyScreen(false);
        setMissionQuestionScreen(false);
        setMissionResultScreen(false);
        setMissionFinishScreen(false);
        setMissionRecordScreen(false);
        setMyPageScreen(false);
        setMyInfoScreen(false);
        setSettingsScreen(false);
    }

    private void setSettingsScreen(boolean on) {
        isSettingsScreenOn = on;
        if (on) {
            settingsScreen.setVisibility(View.VISIBLE);
        } else {
            settingsScreen.setVisibility(View.GONE);
        }
    }

//프로필 사진을 불러올때, 선택한 사진을 받아와서 프로필 이미지에 적용시키는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 7) {
                try {
                    String imgUri = data.getStringExtra("imgUri");
                    Uri img = Uri.parse(imgUri);
                    ImageView profileImg = navigationView.findViewById(R.id.profile_img);
                    profileImg.setScaleType(ImageView.ScaleType.FIT_XY);
                    Glide.with(this).load(img).apply(new RequestOptions().circleCrop()).into(profileImg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClick(R.id.mypage_email)
    public void onMypageEmailClicked() {
        ProfileDialog accountDialog = new ProfileDialog(this, "ACCOUNT");
        accountDialog.show();
        accountDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @OnClick(R.id.mypage_logout)
    public void onMypageLogoutClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그아웃");
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setPositiveButton("로그아웃", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }).setNegativeButton("취소", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        Dialog dialog = builder.create();
        dialog.show();
    }
}
