package com.hackathon.teachtube.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.hackathon.teachtube.Models.ChapterSuitcase;
import com.hackathon.teachtube.Models.StudyMaterialsModel;
import com.hackathon.teachtube.R;
import com.hackathon.teachtube.Utils.DataSharedManager;
import com.hackathon.teachtube.Utils.FileUtils;
import com.hackathon.teachtube.Utils.TinyDB;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//import id.zelory.compressor.Compressor;

public class NotesExerciseRecyclerAdapter extends RecyclerView.Adapter<NotesExerciseRecyclerAdapter.ViewHolder> {

    private ArrayList<StudyMaterialsModel.Exercise> exerciseArrayList;
    private ArrayList<StudyMaterialsModel.Note> noteArrayList;
    private Context context;
    private OnDownloadClickListener onDownloadClickListener;
    private TinyDB tinyDB;
    private ChapterSuitcase chepter;

    public NotesExerciseRecyclerAdapter(ArrayList<StudyMaterialsModel.Exercise> exerciseArrayList, ArrayList<StudyMaterialsModel.Note> noteArrayList, Context context) {
        this.exerciseArrayList = exerciseArrayList;
        this.noteArrayList = noteArrayList;
        this.context = context;
        tinyDB = new TinyDB(context);
        chepter = tinyDB.getObject(DataSharedManager.SELECT_SUBJECT_DEFAULT_CHAPTER_DATA, ChapterSuitcase.class);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_notes_exercise_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (exerciseArrayList == null) {
            holder.tv_title.setText(noteArrayList.get(position).getName());
        } else {
            holder.tv_title.setText(exerciseArrayList.get(position).getName());
        }

        String docName = exerciseArrayList == null ? noteArrayList.get(position).getUrl() : exerciseArrayList.get(position).getUrl();
        String docId = exerciseArrayList == null ? noteArrayList.get(position).getId() : exerciseArrayList.get(position).getId();

        String directory = (exerciseArrayList == null ? "NOTE" : "EXERCISE").equals("NOTE") ? "/Notes/" : "/Exercise/";
        File applicationFile = new File(Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + "/" + context.getResources().getString(R.string.real_app_name)
                + directory + docName);

        if (applicationFile.exists()) {
            holder.cancel_download.setVisibility(View.GONE);
            holder.cancel_download.setEnabled(false);

            holder.img_download.setVisibility(View.GONE);
            holder.img_download.setEnabled(false);

            holder.img_document.setVisibility(View.VISIBLE);
            holder.img_document.setEnabled(true);

            if (FileUtils.checkFileExtension("pdf", docName)) {
                holder.img_document.setImageBitmap(FileUtils.getPdfThumbnail(context, Uri.fromFile(applicationFile)));
            } else {

                Bitmap bitmap = BitmapFactory.decodeFile(applicationFile.getPath());
                holder.img_document.setImageBitmap(bitmap);

                //holder.img_document.setImageBitmap(FileUtils.getBitmap(applicationFile));
            }

        } else {
            holder.cancel_download.setVisibility(View.GONE);
            holder.cancel_download.setEnabled(false);

            holder.img_download.setVisibility(View.VISIBLE);
            holder.img_download.setEnabled(true);

            holder.img_document.setVisibility(View.GONE);
            holder.img_document.setEnabled(false);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*holder.cancel_download.setVisibility(View.GONE);
                holder.cancel_download.setEnabled(false);

                holder.img_download.setVisibility(View.VISIBLE);
                holder.img_download.setEnabled(true);

                holder.img_document.setVisibility(View.GONE);
                holder.img_document.setEnabled(false);*/

                onDownloadClickListener.onDownloadClick(holder.download_loader, holder.cancel_download, holder.img_document, holder.img_download, docName, docId, exerciseArrayList == null ? "NOTE" : "EXERCISE", position);
            }
        });

        holder.more_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenu().add("Edit");
                popupMenu.getMenu().add("Delete");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getTitle().toString()) {
                            case "Edit":
                                if (exerciseArrayList == null) {
                                    /*Intent intent = new Intent(context , EditNoteActivity.class);
                                    intent.putExtra("data" , tinyDB.getJsonFromObject(noteArrayList.get(position)));
                                    intent.putExtra("id" , noteArrayList.get(position).getId());
                                    intent.putExtra("subject_id" , chepter.getSubject_id());
                                    intent.putExtra("class_id" , chepter.getClass_id());
                                    intent.putExtra("position" , position);
                                    context.startActivity(intent);
                                    ((Activity)context).finish();*/
                                } else {
                                    /*Intent intent = new Intent(context , EditExerciseActivity.class);
                                    intent.putExtra("data" , tinyDB.getJsonFromObject(exerciseArrayList.get(position)));
                                    intent.putExtra("id" , exerciseArrayList.get(position).getId());
                                    intent.putExtra("subject_id" , chepter.getSubject_id());
                                    intent.putExtra("class_id" , chepter.getClass_id());
                                    intent.putExtra("position" , position);
                                    context.startActivity(intent);
                                    ((Activity)context).finish();*/
                                }
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

    public interface OnDownloadClickListener {
        void onDownloadClick(ProgressBar progressBar, RelativeLayout cancel_download, ImageView imgDocument, ImageView imgDownloadIcon, String docName, String docId, String docType, int position);
    }

    public void setOnDownloadClickListener(OnDownloadClickListener onDownloadClickListener) {
        this.onDownloadClickListener = onDownloadClickListener;
    }

    @Override
    public int getItemCount() {
        if (exerciseArrayList == null)
            return noteArrayList.size();
        else
            return exerciseArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView tv_title;
        ImageButton more_options;
        ImageView img_document, img_download;
        ProgressBar download_loader;
        RelativeLayout cancel_download;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            cardView = itemView.findViewById(R.id.cardView);
            img_document = itemView.findViewById(R.id.img_document);
            img_download = itemView.findViewById(R.id.img_download);
            download_loader = itemView.findViewById(R.id.download_loader);
            more_options = itemView.findViewById(R.id.more_options);
            cancel_download = itemView.findViewById(R.id.cancel_download);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
    }
}
