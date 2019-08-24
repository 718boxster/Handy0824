package com.handytrip.Utils;

import com.squareup.otto.Bus;

public class GlobalBus {
    private static Bus gBus;
    public static Bus getBus(){
        if(gBus == null){
            gBus = new Bus();
        }
        return gBus;
    }
}
