package com.handytrip.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.handytrip.R;
import com.handytrip.Structures.MissionRecordItem;

import java.util.ArrayList;

public class MissionRecordListAdapter extends RecyclerView.Adapter<MissionRecordListAdapter.Holder> {
    ArrayList<MissionRecordItem> datas = new ArrayList<>();
    Context context;

    public MissionRecordListAdapter(ArrayList<MissionRecordItem> datas, Context context) {
        this.datas = datas;
        this.context = context;
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
        holder.mDate.setText(data.getmDate());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView mName;
        TextView mDate;
        public Holder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.mission_img);
            mName = itemView.findViewById(R.id.mission_name);
            mDate = itemView.findViewById(R.id.mission_date);
        }
    }
}
