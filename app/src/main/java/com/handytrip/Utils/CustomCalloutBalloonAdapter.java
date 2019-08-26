package com.handytrip.Utils;

import android.util.Log;
import android.view.View;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;

public class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {

    private final View mCalloutBalloon = null;

    public CustomCalloutBalloonAdapter() {
    }

    @Override
    public View getCalloutBalloon(MapPOIItem mapPOIItem) {
        Log.d("getCalloutBalloon", "pressed??");
        return null;
    }

    @Override
    public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
        Log.d("pressed item : ", mapPOIItem.getItemName());
        return null;
    }
}
