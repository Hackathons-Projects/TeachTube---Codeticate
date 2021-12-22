package com.hackathon.teachtube.OtherActivities.Study;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.hackathon.teachtube.R;

import java.util.Objects;

public class YoutubeVideoActivity extends YouTubeBaseActivity {

    private Context context;

    private YouTubePlayerView youtubePlayer;
    private String videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_vedio);
        context = YoutubeVideoActivity.this;

        youtubePlayer = findViewById(R.id.youtubePlayer);

        videoId = getIntent().getStringExtra("id");

        //addFullScreenListenerToPlayer(true);

        youtubePlayer.initialize("AIzaSyCGvrT5aq8WFRoAqVWAkykDvgrMeaxYKko",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.loadVideo(videoId);
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //addFullScreenListenerToPlayer(false);
    }

    private void addFullScreenListenerToPlayer(boolean b) {
        if (b)
        {
            ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            Objects.requireNonNull(((AppCompatActivity) context).getSupportActionBar()).hide();
            ((Activity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            Objects.requireNonNull(((AppCompatActivity) context).getSupportActionBar()).show();
            ((AppCompatActivity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

}