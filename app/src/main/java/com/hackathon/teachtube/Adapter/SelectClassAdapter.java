package com.hackathon.teachtube.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.hackathon.teachtube.Models.ClassSubjectModel;
import com.hackathon.teachtube.R;


import java.util.ArrayList;

public class SelectClassAdapter extends RecyclerView.Adapter<SelectClassAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ClassSubjectModel> topicArrayList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public SelectClassAdapter(Context context, ArrayList<ClassSubjectModel> topicArrayList) {
        this.context = context;
        this.topicArrayList = topicArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.select_topic_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_title.setText(topicArrayList.get(position).get_class());

    }

    @Override
    public int getItemCount() {
        return topicArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        MaterialCardView cardView;
       // LinearLayout select_subject_chipsLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            cardView = itemView.findViewById(R.id.cardView);
            //select_subject_chipsLayout = itemView.findViewById(R.id.select_subject_chipsLayout);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.OnItemClick(getAdapterPosition());
                }
            });

        }
    }

    public interface OnItemClickListener
    {
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

}
