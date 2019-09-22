package com.handytrip.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.handytrip.R;
import com.handytrip.Structures.MissionRecordItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MissionRecordListAdapter extends RecyclerView.Adapter<MissionRecordListAdapter.Holder> {
    ArrayList<MissionRecordItem> datas = new ArrayList<>();
    Context context;
    View.OnClickListener onClickListener;

    public MissionRecordListAdapter(ArrayList<MissionRecordItem> datas, View.OnClickListener onclickListener, Context context) {
        this.datas = datas;
        this.context = context;
        this.onClickListener = onclickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mission_record_item, null);
        AutoLayout.setView(v);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        MissionRecordItem data = datas.get(position);
        Glide.with(context).load(data.getImgUrl()).into(holder.img);
        holder.mName.setText(data.getmName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        try {
            Date date = sdf.parse(data.getmDate());
            SimpleDateFormat parseDate = new SimpleDateFormat("yyyy. MM. dd a HH:mm", Locale.US);
            String resDate = parseDate.format(date);
//            if(resDate.contains("오후")){
//                resDate.replace("오후", "PM");
//            } else if(resDate.contains("오전")){
//                resDate.replace("오전", "AM");
//            }
            holder.mDate.setText(resDate);
        } catch (Exception e){
            e.printStackTrace();
        }
//        holder.mDate.setText(data.getmDate());
        holder.recordBody.setTag(data);
        holder.recordBody.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView mName;
        TextView mDate;
        LinearLayout recordBody;
        public Holder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.mission_img);
            mName = itemView.findViewById(R.id.mission_name);
            mDate = itemView.findViewById(R.id.mission_date);
            recordBody = itemView.findViewById(R.id.record_body);
        }
    }
}
