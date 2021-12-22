package com.hackathon.teachtube.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.hackathon.teachtube.Models.LiveClassModel;
import com.hackathon.teachtube.R;
import com.hackathon.teachtube.Utils.ImpMethods;
import com.hackathon.teachtube.Utils.TinyDB;

import java.util.ArrayList;

public class LiveClassAdapter extends RecyclerView.Adapter<LiveClassAdapter.ViewHolder> {

    private Context context;
    private ArrayList<LiveClassModel> liveClassModels;
    private OnItemClickListener onItemClickListener;
    private TinyDB tinyDB;

    public LiveClassAdapter(Context context, ArrayList<LiveClassModel> liveClassModels) {
        this.context = context;
        this.liveClassModels = liveClassModels;
        tinyDB = new TinyDB(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.live_class_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        LiveClassModel liveClassModel = liveClassModels.get(position);

        long currentTimeInMillies = System.currentTimeMillis();
        long endTimeInMillies = Long.parseLong(liveClassModel.getEndTime());
        long startTimeInMillies = Long.parseLong(liveClassModel.getEndTime());

        if (currentTimeInMillies > endTimeInMillies) {
            holder.tv_liveClass_status.setText("End");
        } else if (startTimeInMillies < currentTimeInMillies && currentTimeInMillies < endTimeInMillies) {
            holder.tv_liveClass_status.setText("Live");
        } else if (currentTimeInMillies < startTimeInMillies) {
            holder.tv_liveClass_status.setText("Upcoming");
        }

        //holder.tv_liveClass_class.setText(liveClassModel.getExamName());
        //holder.tv_liveClass_subject.setText(liveClassModel.getExamName());
        //holder.tv_liveClass_topic.setText(liveClassModel.getExamName());
        holder.tv_liveClass_name.setText(liveClassModel.getTitle());
        holder.tv_liveClass_date.setText(ImpMethods.ChangeDateFormat("yyyy-MM-dd" , "dd MMM, yyyy" , liveClassModel.getDate()));
        holder.tv_liveClass_time.setText(ImpMethods.getDateFromMillies(Long.parseLong(liveClassModel.getStartTime()) , "hh:mm a") + " - " + ImpMethods.getDateFromMillies(Long.parseLong(liveClassModel.getEndTime()) , "hh:mm a"));

        holder.ib_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context , view);
                popupMenu.getMenu().add("Edit");
                popupMenu.getMenu().add("Delete");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        switch (menuItem.getTitle().toString())
                        {
                            case "Edit":
                                /*Intent intent = new Intent(context , EditLiveClassActivity.class);
                                intent.putExtra("data" , tinyDB.getJsonFromObject(liveClassModel));
                                intent.putExtra("id" , liveClassModel.getId());
                                intent.putExtra("position" , position);
                                context.startActivity(intent);
                                ((Activity)context).finish();*/
                                return true;
                            case "Delete":
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return liveClassModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_liveClass_status, tv_liveClass_class, tv_liveClass_subject, tv_liveClass_topic, tv_liveClass_name, tv_liveClass_date, tv_liveClass_time;
        MaterialCardView cardView;
        ImageButton ib_more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_liveClass_status = itemView.findViewById(R.id.tv_liveClass_status);
            tv_liveClass_class = itemView.findViewById(R.id.tv_liveClass_class);
            tv_liveClass_subject = itemView.findViewById(R.id.tv_liveClass_subject);
            tv_liveClass_topic = itemView.findViewById(R.id.tv_liveClass_topic);
            tv_liveClass_name = itemView.findViewById(R.id.tv_liveClass_name);
            tv_liveClass_date = itemView.findViewById(R.id.tv_liveClass_date);
            tv_liveClass_time = itemView.findViewById(R.id.tv_liveClass_time);
            cardView = itemView.findViewById(R.id.cardView);
            ib_more = itemView.findViewById(R.id.ib_more);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.OnItemClick(liveClassModels.get(getAdapterPosition()), getAdapterPosition());
                }
            });

        }
    }

    public interface OnItemClickListener {
        void OnItemClick(LiveClassModel liveClassModel, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
