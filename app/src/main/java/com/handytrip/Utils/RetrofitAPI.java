package com.handytrip.Utils;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

//esp 서버로 전송하기 위한 인터페이스 ( Retrofit 라이브러리 사용 )
public interface RetrofitAPI {
    @GET("login_test.php")
    Call<String> login(@Query("_id") String _id, @Query("pw") String pw);
    //@Path 는 baseURL 뒤에 / 가 붙고, @Query는 ? 가 붙음
    @GET("get_user_info.php")
    Call<String> getUserInfo(@Query("_id") String _id);

    @GET("get_all_missions.php")
    Call<JsonObject> getAllMissions();

    @GET("get_specific_mission.php")
    Call<JsonObject> getSpecificMission(@Query("m_name") String m_name, @Query("m_place") String m_place, @Query("m_lat") String m_lat, @Query("m_lng") String m_lng);

    @GET("put_user_answer.php")
    Call<String> putUserAnswer(@Query("u_id") String _id, @Query("m_name") String m_name, @Query("m_lat") String m_lat, @Query("m_lng") String m_lng, @Query("m_is_correct") int isCorrect, @Query("m_ans_time") String m_ans_time, @Query("m_give_up_time") String m_give_up_time, @Query("m_img") String m_img, @Query("m_is_first") int m_is_first, @Query("m_place") String m_place);

    @GET("get_user_missions.php")
    Call<JsonObject> getUserMissions(@Query("u_id") String _id, @Query("m_name") String m_name, @Query("m_lat") String m_lat, @Query("m_lng") String m_lng);

    @GET("sign_up.php")
    Call<String> signUp(@Query("u_id") String _id, @Query("pw") String pw, @Query("phone") String phone);

    @GET("get_all_user_missions.php")
    Call<JsonObject> getAllUserMissions(@Query("u_id") String u_id);

    @GET("detect_is_first_meet.php")
    Call<String> isFirstMeet(@Query("u_id") String u_id, @Query("m_name") String m_name, @Query("m_lat") String m_lat, @Query("m_lng") String m_lng);

    @GET("get_user_done_missions.php")
    Call<JsonObject> getUserDoneMissions(@Query("u_id") String u_id);

    @GET("send_fcm_token.php")
    Call<String> sendFcmToken(@Query("fcmToken") String token);
}
