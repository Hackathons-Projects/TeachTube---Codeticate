package com.hackathon.teachtube.OtherActivities.Study;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.hackathon.teachtube.Adapter.SelectSubjectAdapter;
import com.hackathon.teachtube.Adapter.SelectClassAdapter;
import com.hackathon.teachtube.Classes.MainLoader;
import com.hackathon.teachtube.Models.ChapterSuitcase;
import com.hackathon.teachtube.Models.DashboardModel;
import com.hackathon.teachtube.Models.ClassSubjectModel;
import com.hackathon.teachtube.Models.UserSuitcase;
import com.hackathon.teachtube.R;
import com.hackathon.teachtube.Utils.DataSharedManager;
import com.hackathon.teachtube.Utils.TinyDB;

import java.util.ArrayList;

public class SelectSubjectActivity extends AppCompatActivity {

    public static final String TAG = "SelectSubjectActivity";
    private Context context;
    private Toolbar toolbar;
    private TinyDB tinyDB;
    private UserSuitcase userSuitcase;

    private DashboardModel dashboardModel;

    private RecyclerView select_class_recyclerView, select_subject_recyclerView;//, select_chapter_recyclerView;
    //private SelectChapterAdapter selectChapterAdapter;
    private SelectSubjectAdapter selectSubjectAdapter;
    private SelectClassAdapter selectClassAdapter;

    private String subject_id , subject , class_id , class_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject);
        context = SelectSubjectActivity.this;
        tinyDB = new TinyDB(context);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Subject");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userSuitcase = tinyDB.getObject(DataSharedManager.USER_SUITCASE, UserSuitcase.class);

        dashboardModel = tinyDB.getObject(DataSharedManager.DASHBOARD_BACKUP_DATA, DashboardModel.class);

        select_class_recyclerView = findViewById(R.id.select_class_recyclerView);
        select_subject_recyclerView = findViewById(R.id.select_subject_recyclerView);
        //select_chapter_recyclerView = findViewById(R.id.select_chapter_recyclerView);

        ArrayList<ClassSubjectModel> classSubjectModel = dashboardModel.getSubjectModel();

        toggleSubjectLayout(true);
        toggleClassLayout(false);

        selectClassAdapter = new SelectClassAdapter(context, classSubjectModel);
        MainLoader.Loader(selectClassAdapter.getItemCount() == 0, findViewById(R.id.notFound));
        selectClassAdapter.setOnItemClickListener(new SelectClassAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int class_position) {
                //toggleChapterLayout(false);
                toggleSubjectLayout(false);
                toggleClassLayout(true);

                class_id = classSubjectModel.get(class_position).getClassId();
                class_name = classSubjectModel.get(class_position).get_class();

                toolbar.setTitle(classSubjectModel.get(class_position).get_class());
                selectSubjectAdapter = new SelectSubjectAdapter(context, classSubjectModel.get(class_position).getSubjects());
                MainLoader.Loader(selectSubjectAdapter.getItemCount() == 0, findViewById(R.id.notFound));
                selectSubjectAdapter.setOnItemClickListener(new SelectSubjectAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(int subject_position) {
                        //toggleChapterLayout(true);
                        toggleSubjectLayout(false);
                        toggleClassLayout(false);

                        subject = classSubjectModel.get(class_position).getSubjects().get(subject_position).getSubject();
                        subject_id = classSubjectModel.get(class_position).getSubjects().get(subject_position).getSubjectId();

                        ChapterSuitcase chapterSuitcase = new ChapterSuitcase(subject_id , subject , class_id , class_name);
                        tinyDB.putObject(DataSharedManager.SELECT_SUBJECT_DEFAULT_CHAPTER_DATA, chapterSuitcase);
                        Intent intent = new Intent(context, MainStudyActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                select_subject_recyclerView.setAdapter(selectSubjectAdapter);
            }
        });
        select_class_recyclerView.setAdapter(selectClassAdapter);

        /*new getData(userSuitcase.branch_id , userSuitcase.session_id , userSuitcase.teacher_id).execute();

        findViewById(R.id.refreshLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getData(userSuitcase.branch_id , userSuitcase.session_id , userSuitcase.teacher_id).execute();
            }
        });*/

    }

    @Override
    public void onBackPressed() {
        MainLoader.Loader(false, findViewById(R.id.notFound));
        if (((LinearLayout) select_class_recyclerView.getParent()).getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        } else if (((LinearLayout) select_subject_recyclerView.getParent()).getVisibility() == View.VISIBLE) {
            String[] title = toolbar.getTitle().toString().split("-");
            toolbar.setTitle(title[0].trim());
            //toggleChapterLayout(false);
            toggleSubjectLayout(false);
            toggleClassLayout(true);
        }
    }

    private void toggleSubjectLayout(boolean b) {
        ((LinearLayout) select_subject_recyclerView.getParent()).setVisibility(b ? View.VISIBLE : View.GONE);
        ((LinearLayout) select_subject_recyclerView.getParent()).setEnabled(b);
    }

    private void toggleClassLayout(boolean b) {
        ((LinearLayout) select_class_recyclerView.getParent()).setVisibility(b ? View.VISIBLE : View.GONE);
        ((LinearLayout) select_class_recyclerView.getParent()).setEnabled(b);
    }

}