package com.handytrip.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.handytrip.R;
import com.handytrip.Structures.NotificationData;

import java.util.ArrayList;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.Holder> {
    ArrayList<NotificationData> datas = new ArrayList<>();
    View.OnClickListener onClickListener;

    public NotificationListAdapter(ArrayList<NotificationData> datas, View.OnClickListener onClickListener) {
        this.datas = datas;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_item, null);
        AutoLayout.setView(v);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        NotificationData  data = datas.get(position);
        holder.date.setText(data.getDate());
        holder.text.setText(data.getText());
        holder.delete.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView date;
        TextView text;
        ImageView delete;
        public Holder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.noti_date);
            text = itemView.findViewById(R.id.noti_text);
            delete = itemView.findViewById(R.id.delete_noti);
        }
    }
}
