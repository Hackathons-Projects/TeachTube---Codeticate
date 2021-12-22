package com.hackathon.teachtube.OtherActivities.Study;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.hackathon.teachtube.Adapter.CustomTabAdapter;
import com.hackathon.teachtube.Models.DashboardModel;
import com.hackathon.teachtube.Models.LiveClassModel;
import com.hackathon.teachtube.OtherActivities.Study.LiveClassFragments.TodayFragment;
import com.hackathon.teachtube.OtherActivities.Study.LiveClassFragments.UpcomingFragment;
import com.hackathon.teachtube.R;
import com.hackathon.teachtube.Utils.DataSharedManager;
import com.hackathon.teachtube.Utils.ImpMethods;
import com.hackathon.teachtube.Utils.TinyDB;

import java.util.ArrayList;

public class LiveClassActivity extends AppCompatActivity {
    public static final String TAG = "LiveClassActivity";
    private Context context;
    private Toolbar toolbar;
    private TinyDB tinyDB;
    private DashboardModel dashboardModel;

    private ViewPager viewPager_liveClass;
    private TabLayout tab_layout_liveClass;

    private FloatingActionButton add_liveClass;

    private ArrayList<LiveClassModel> today_liveClassModels = new ArrayList<>();
    private ArrayList<LiveClassModel> upcoming_liveClassModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_class);
        context = LiveClassActivity.this;
        tinyDB = new TinyDB(context);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Live Class");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        dashboardModel = tinyDB.getObject(DataSharedManager.DASHBOARD_BACKUP_DATA, DashboardModel.class);

        tab_layout_liveClass = findViewById(R.id.tab_layout_liveClass);
        viewPager_liveClass = findViewById(R.id.viewPager_liveClass);

        add_liveClass = findViewById(R.id.add_liveClass);

        long currentTimeInMillies = System.currentTimeMillis();

        for (int i = 0; dashboardModel.getLiveClassModels().size() > i; i++)
        {
            LiveClassModel liveClassModel = dashboardModel.getLiveClassModels().get(i);
            long startTimeInMillies = Long.parseLong(liveClassModel.getStartTime());
            if (liveClassModel.getDate().equals(ImpMethods.getDateFromMillies(System.currentTimeMillis() , "yyyy-MM-dd"))) {
                today_liveClassModels.add(liveClassModel);
            } else if (currentTimeInMillies < startTimeInMillies){
                upcoming_liveClassModels.add(liveClassModel);
            }
        }

        CustomTabAdapter customTabAdapter = new CustomTabAdapter(getSupportFragmentManager());
        customTabAdapter.addFragment(new TodayFragment(today_liveClassModels), "Today");
        customTabAdapter.addFragment(new UpcomingFragment(upcoming_liveClassModels), "Upcoming");

        viewPager_liveClass.setOffscreenPageLimit(1);

        viewPager_liveClass.setAdapter(customTabAdapter);
        tab_layout_liveClass.setupWithViewPager(viewPager_liveClass);
        customTabAdapter.notifyDataSetChanged();

        add_liveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(context , AddLiveClassActivity.class);
                startActivity(intent);
                finish();*/
            }
        });

    }
}