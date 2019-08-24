package com.handytrip.Utils;

import com.handytrip.Structures.FilterData;
import com.handytrip.Structures.MissionData;

import java.util.ArrayList;

public class StaticData {
    public static StaticData staticData;
    public static ArrayList<MissionData> missionData;
    public static FilterData filter;

    public static String IMG_BASE_URL = "https://goffhdn8342.cafe24.com/pages/images/";

    public static StaticData getInstance(){
        if(staticData == null){
            staticData = new StaticData();
            missionData = new ArrayList<MissionData>();
            filter = new FilterData();
        } return staticData;
    }



}
