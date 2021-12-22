package com.hackathon.teachtube.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.hackathon.teachtube.Models.ClassSubjectModel;
import com.hackathon.teachtube.R;

import java.util.ArrayList;

public class SelectSubjectAdapter extends RecyclerView.Adapter<SelectSubjectAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ClassSubjectModel.Subject> datumArrayList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public SelectSubjectAdapter(Context context, ArrayList<ClassSubjectModel.Subject> datumArrayList) {
        this.context = context;
        this.datumArrayList = datumArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.select_subject_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.select_subject_title.setText(datumArrayList.get(position).getSubject());
        //holder.chip.setText(datumArrayList.get(position).getSubject());
    }

    @Override
    public int getItemCount() {
        return datumArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView select_subject_title;
        MaterialCardView cardView;
        //Chip chip;
       // LinearLayout select_subject_chipsLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            select_subject_title = itemView.findViewById(R.id.select_subject_title);
            cardView = itemView.findViewById(R.id.cardView);
            //chip = itemView.findViewById(R.id.chip);
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
