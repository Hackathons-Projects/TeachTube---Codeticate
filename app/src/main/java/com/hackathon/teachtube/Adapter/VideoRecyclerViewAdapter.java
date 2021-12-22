package com.hackathon.teachtube.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.hackathon.teachtube.Models.ChapterSuitcase;
import com.hackathon.teachtube.Models.StudyMaterialsModel;
import com.hackathon.teachtube.R;
import com.hackathon.teachtube.Utils.DataSharedManager;
import com.hackathon.teachtube.Utils.TinyDB;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

@SuppressLint("LongLogTag")
public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder>
{

    private final String TAG = "VideoRecyclerViewAdapter";

    private ArrayList<StudyMaterialsModel.Study> studyArrayList = new ArrayList<>();
    private Context context;
    onPlayerClickListener onPlayerClickListener;
    private int row_index = -1;
    private TinyDB tinyDB;


    private ChapterSuitcase chepter;
    //private OnMoreClickListener onMoreClickListener;

    public VideoRecyclerViewAdapter(ArrayList<StudyMaterialsModel.Study> studyArrayList, Context context, VideoRecyclerViewAdapter.onPlayerClickListener onPlayerClickListener) {
        this.studyArrayList = studyArrayList;
        this.context = context;
        this.onPlayerClickListener = onPlayerClickListener;
        tinyDB = new TinyDB(context);
        chepter = tinyDB.getObject(DataSharedManager.SELECT_SUBJECT_DEFAULT_CHAPTER_DATA , ChapterSuitcase.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.study_video_items , parent , false);
        return new ViewHolder(view , onPlayerClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        holder.video_thumbnail.initialize(studyArrayList.get(position).getUrl(),
                new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                        youTubeThumbnailLoader.setVideo(studyArrayList.get(position).getUrl());

                        youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                            @Override
                            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                                youTubeThumbnailLoader.release();
                                //FlagThumbnailLoaded = 1;
                            }

                            @Override
                            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                                Log.d(TAG, "youtube thumbnail loader initialize failed");
                            }
                        });
                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                }
        );

        holder.video_title.setText(getTitleQuietly(studyArrayList.get(position).getUrl()));

        /*holder.youtube_recyclerViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index=position;
                notifyDataSetChanged();
            }
        });
        if(row_index==position){
            holder.customViewCircle.setVisibility(View.VISIBLE);
            holder.itemLayout.setBackgroundColor(context.getResources().getColor(R.color.LightGreen));
        }
        else
        {
            holder.customViewCircle.setVisibility(View.GONE);
            holder.itemLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }*/

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
                                /*Intent intent = new Intent(context , EditVideoActivity.class);
                                intent.putExtra("data" , tinyDB.getJsonFromObject(studyArrayList.get(position)));
                                intent.putExtra("id" , studyArrayList.get(position).getId());
                                intent.putExtra("subject_id" , chepter.getSubject_id());
                                intent.putExtra("class_id" , chepter.getClass_id());
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

    public static String getTitleQuietly(String youtubeUrl) {
        try {
            if (youtubeUrl != null) {

                URL embededURL = new URL("http://www.youtube.com/oembed?url=" +
                        youtubeUrl + "&format=json"
                );

                return new JSONObject(IOUtils.toString(embededURL)).getString("title");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return studyArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        YouTubeThumbnailView video_thumbnail;
        TextView video_title;
        ImageButton ib_more;
        onPlayerClickListener onPlayerClickListener;

        public ViewHolder(@NonNull View itemView , onPlayerClickListener onPlayerClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            video_thumbnail = itemView.findViewById(R.id.video_thumbnail);
            video_title = itemView.findViewById(R.id.video_title);
            ib_more = itemView.findViewById(R.id.ib_more);
            this.onPlayerClickListener = onPlayerClickListener;
        }

        @Override
        public void onClick(View v)
        {
            onPlayerClickListener.onPlayerClick(getAdapterPosition() , v);
        }
    }

    public interface onPlayerClickListener
    {
        void onPlayerClick(int position , View v);
    }

    public interface OnMoreClickListener
    {
        void OnEditClick(StudyMaterialsModel.Study study , int position);

        void OnDeleteClick(StudyMaterialsModel.Study study , int position);
    }

    public void setOnMoreClickListener(OnMoreClickListener onMoreClickListener)
    {
        //this.onMoreClickListener = onMoreClickListener;
    }

}
