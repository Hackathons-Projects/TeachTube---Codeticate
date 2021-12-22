package com.hackathon.teachtube.OtherActivities.Study.StudyFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hackathon.teachtube.Adapter.VideoRecyclerViewAdapter;
import com.hackathon.teachtube.Classes.MainLoader;
import com.hackathon.teachtube.Models.ChapterSuitcase;
import com.hackathon.teachtube.Models.StudyMaterialsModel;
import com.hackathon.teachtube.OtherActivities.Study.MainStudyActivity;
import com.hackathon.teachtube.OtherActivities.Study.YoutubeVideoActivity;
import com.hackathon.teachtube.R;
import com.hackathon.teachtube.Utils.DataSharedManager;
import com.hackathon.teachtube.Utils.TinyDB;

import java.util.ArrayList;
import java.util.Objects;

public class VideoFragment extends Fragment {

    public static final String TAG = "VideoFragment";
    private Context context;
    private TinyDB tinyDB;

    private ArrayList<StudyMaterialsModel.Study> studyArrayList = new ArrayList<>();

    private RecyclerView video_recyclerView;
    private VideoRecyclerViewAdapter videoRecyclerViewAdapter;
    private TextView chapterTitle;
    private ChapterSuitcase chepter;
    private FloatingActionButton add_video;

    public VideoFragment(ArrayList<StudyMaterialsModel.Study> studyArrayList) {
        this.studyArrayList = studyArrayList;
    }

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        context = getActivity();
        tinyDB = new TinyDB(context);

        chepter = tinyDB.getObject(DataSharedManager.SELECT_SUBJECT_DEFAULT_CHAPTER_DATA , ChapterSuitcase.class);

        video_recyclerView = view.findViewById(R.id.video_recyclerView);
        chapterTitle = view.findViewById(R.id.chapterTitle);
        add_video = view.findViewById(R.id.add_video);

        chapterTitle.setText(chepter.getSubject());

        if (studyArrayList.size() == 0) {
            MainLoader.Loader(true, view.findViewById(R.id.notFound));
        } else {
            MainLoader.Loader(false, view.findViewById(R.id.notFound));
            videoRecyclerViewAdapter = new VideoRecyclerViewAdapter(studyArrayList, context, new VideoRecyclerViewAdapter.onPlayerClickListener() {
                @Override
                public void onPlayerClick(int position, View v) {
                    Intent intent = new Intent(context, YoutubeVideoActivity.class);
                    intent.putExtra("id", studyArrayList.get(position).getUrl());
                    startActivity(intent);
                }
            });
            video_recyclerView.setAdapter(videoRecyclerViewAdapter);
        }

        ((MainStudyActivity) Objects.requireNonNull(requireActivity())).setOnVideoChangeListener(new MainStudyActivity.OnVideoChangeListener() {
            @Override
            public void OnVideoChange(ArrayList<StudyMaterialsModel.Study> studyArrayList) {
                chepter = tinyDB.getObject(DataSharedManager.SELECT_SUBJECT_DEFAULT_CHAPTER_DATA, ChapterSuitcase.class);
                chapterTitle.setText(chepter.getSubject());
                if (studyArrayList.size() == 0) {
                    MainLoader.Loader(true, view.findViewById(R.id.notFound));
                } else {
                    MainLoader.Loader(false, view.findViewById(R.id.notFound));
                    videoRecyclerViewAdapter = new VideoRecyclerViewAdapter(studyArrayList, context, new VideoRecyclerViewAdapter.onPlayerClickListener() {
                        @Override
                        public void onPlayerClick(int position, View v) {
                            Intent intent = new Intent(context, YoutubeVideoActivity.class);
                            intent.putExtra("id", studyArrayList.get(position).getUrl());
                            startActivity(intent);
                        }
                    });
                    video_recyclerView.setAdapter(videoRecyclerViewAdapter);
                }
            }
        });

        /*videoRecyclerViewAdapter.setOnMoreClickListener(new VideoRecyclerViewAdapter.OnMoreClickListener() {
            @Override
            public void OnEditClick(StudyMaterialsModel.Study study, int position) {

            }

            @Override
            public void OnDeleteClick(StudyMaterialsModel.Study study, int position) {

            }
        });*/

        add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(context, AddVideoActivity.class);
                startActivity(intent);
                getActivity().finish();*/
            }
        });

        return view;
    }

}