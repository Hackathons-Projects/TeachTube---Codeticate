package com.hackathon.teachtube.OtherActivities.Study.LiveClassFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hackathon.teachtube.Adapter.LiveClassAdapter;
import com.hackathon.teachtube.Classes.MainLoader;
import com.hackathon.teachtube.Models.DashboardModel;
import com.hackathon.teachtube.Models.LiveClassModel;
import com.hackathon.teachtube.R;
import com.hackathon.teachtube.Utils.DataSharedManager;
import com.hackathon.teachtube.Utils.TinyDB;

import java.util.ArrayList;

public class UpcomingFragment extends Fragment {
    public static final String TAG = "UpcomingFragment";
    private Context context;
    private TinyDB tinyDB;
    private DashboardModel dashboardModel;

    private RecyclerView recyclerView_liveClass_upcoming;
    private ArrayList<LiveClassModel> liveClassModel = new ArrayList<>();
    private LiveClassAdapter liveClassAdapter;

    public UpcomingFragment(ArrayList<LiveClassModel> liveClassModel) {
        this.liveClassModel = liveClassModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        context = getActivity();
        tinyDB = new TinyDB(context);

        dashboardModel = tinyDB.getObject(DataSharedManager.DASHBOARD_BACKUP_DATA, DashboardModel.class);

        recyclerView_liveClass_upcoming = view.findViewById(R.id.recyclerView_liveClass_upcoming);

        liveClassAdapter = new LiveClassAdapter(context , liveClassModel);
        if (liveClassAdapter.getItemCount() == 0) {
            MainLoader.Loader(true, view.findViewById(R.id.notFound));
        } else {
            MainLoader.Loader(false, view.findViewById(R.id.notFound));
            recyclerView_liveClass_upcoming.setAdapter(liveClassAdapter);
        }
        return view;
    }
}