package com.handytrip;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.handytrip.Structures.NotificationData;
import com.handytrip.Utils.AutoLayout;
import com.handytrip.Utils.BusEvents;
import com.handytrip.Utils.GlobalBus;
import com.handytrip.Utils.NotificationDB;
import com.handytrip.Utils.NotificationListAdapter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Notifications extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.settings_back)
    ImageView settingsBack;
    @BindView(R.id.noti_list)
    RecyclerView notiList;

    LinearLayoutManager linearLayoutManager;
    NotificationListAdapter adapter;

    ArrayList<NotificationData> datas = new ArrayList<>();

    NotificationDB db;
    @BindView(R.id.no_noti)
    TextView noNoti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        AutoLayout.setResizeView(this);
        ButterKnife.bind(this);

        db = NotificationDB.getInstance(this);

        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapter = new NotificationListAdapter(datas, this);
        notiList.setLayoutManager(linearLayoutManager);
        notiList.setAdapter(adapter);


        refresh();


    }

    private void refresh() {
        int notiCnt = 0;

        Cursor c = db.selectNotification();
        c.moveToFirst();
        datas.clear();
        if (c.getCount() != 0) {
            do {
                datas.add(new NotificationData(c.getString(c.getColumnIndex("DATE")),
                        c.getString(c.getColumnIndex("NOTIFICATION"))));
                notiCnt++;
            } while (c.moveToNext() && notiCnt < 5);
            adapter.notifyDataSetChanged();
        } else{
            notiList.setVisibility(View.GONE);
            noNoti.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.settings_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onClick(View view) {
        int pos = notiList.getChildLayoutPosition(((View) view.getParent().getParent()));
        db.deleteNotification(datas.get(pos).getDate(), datas.get(pos).getText());
        datas.remove(pos);
        refresh();
    }


    @Subscribe
    public void refresh(BusEvents.refreshList refreshList){
        refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalBus.getBus().register(this);
    }
}
