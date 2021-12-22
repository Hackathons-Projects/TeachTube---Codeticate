package com.hackathon.teachtube.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackathon.teachtube.Adapter.LiveClassAdapter;
import com.hackathon.teachtube.BuildConfig;
import com.hackathon.teachtube.Classes.MainLoader;
import com.hackathon.teachtube.Models.ChapterSuitcase;
import com.hackathon.teachtube.Models.ClassSubjectModel;
import com.hackathon.teachtube.Models.DashboardModel;
import com.hackathon.teachtube.Models.LiveClassModel;
import com.hackathon.teachtube.OtherActivities.Study.LiveClassActivity;
import com.hackathon.teachtube.OtherActivities.Study.MainStudyActivity;
import com.hackathon.teachtube.OtherActivities.Study.SelectSubjectActivity;
import com.hackathon.teachtube.R;
import com.hackathon.teachtube.Security.LoginActivity;
import com.hackathon.teachtube.Utils.AllKeyUrls;
import com.hackathon.teachtube.Utils.Constants;
import com.hackathon.teachtube.Utils.DataSharedManager;
import com.hackathon.teachtube.Utils.TinyDB;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "MainActivity";
    private Context context;
    private Toolbar toolbar;
    private TinyDB tinyDB;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    private RecyclerView recyclerView_liveClasses;
    private LiveClassAdapter liveClassAdapter;

    private MaterialCardView record_video, study_materials, live_classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        tinyDB = new TinyDB(context);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.mainNavigation_slider);
        navigationView.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Human Helper");
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerToggle.setDrawerIndicatorEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_list);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT | GravityCompat.START);
            }
        });

        ((TextView) findViewById(R.id.tv_version)).setText(BuildConfig.VERSION_NAME);

        recyclerView_liveClasses = findViewById(R.id.recyclerView_liveClasses);
        record_video = findViewById(R.id.record_video);
        study_materials = findViewById(R.id.study_materials);
        live_classes = findViewById(R.id.live_classes);

        record_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        study_materials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tinyDB.getObject(DataSharedManager.SELECT_SUBJECT_DEFAULT_CHAPTER_DATA, ChapterSuitcase.class) == null) {
                    Intent intent = new Intent(context, SelectSubjectActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, MainStudyActivity.class);
                    startActivity(intent);
                }
            }
        });

        live_classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LiveClassActivity.class);
                startActivity(intent);
            }
        });

        getData();

    }

    DashboardModel dashboardModel = new DashboardModel();

    private void getData() {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        FirebaseDatabase.getInstance(Constants.FIREBASE_REFERENCE).getReference().child("Class").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<ClassSubjectModel> subjectModels = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String mClass = dataSnapshot.child(Constants.CLASS).getValue().toString();
                        DataSnapshot subjects = dataSnapshot.child(Constants.SUBJECT);
                        ArrayList<ClassSubjectModel.Subject> subjectList = new ArrayList<>();
                        for (DataSnapshot d : subjects.getChildren()) {
                            String subject = d.getValue().toString();
                            subjectList.add(new ClassSubjectModel.Subject(d.getKey(), subject));
                        }
                        subjectModels.add(new ClassSubjectModel(dataSnapshot.getKey(), mClass, subjectList));
                    }
                    dashboardModel.setSubjectModel(subjectModels);
                    tinyDB.putObject(DataSharedManager.DASHBOARD_BACKUP_DATA, dashboardModel);

                    FirebaseDatabase.getInstance(Constants.FIREBASE_REFERENCE).getReference().child("Live_Class").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            dialog.dismiss();
                            if (snapshot.exists()) {
                                ArrayList<LiveClassModel> liveClassModels = new ArrayList<>();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String mClass = dataSnapshot.child(Constants.CLASS).getValue().toString();
                                    String mDate = dataSnapshot.child(Constants.DATE).getValue().toString();
                                    String mEndTime = dataSnapshot.child(Constants.END_TIME).getValue().toString();
                                    String mStartTime = dataSnapshot.child(Constants.START_TIME).getValue().toString();
                                    String mMeetingId = dataSnapshot.child(Constants.MEETING_ID).getValue().toString();
                                    String mMeetingPwd = dataSnapshot.child(Constants.MEETING_PWD).getValue().toString();
                                    String mMeetingURL = dataSnapshot.child(Constants.MEETING_URL).getValue().toString();
                                    String mSubject = dataSnapshot.child(Constants.SUBJECT).getValue().toString();
                                    String mTitle = dataSnapshot.child(Constants.TITLE).getValue().toString();
                                    String mPlatform = dataSnapshot.child(Constants.PLATFORM).getValue().toString();

                                    liveClassModels.add(new LiveClassModel(dataSnapshot.getKey(), mPlatform, mTitle, mMeetingURL, mDate,
                                            mStartTime, mEndTime, mMeetingPwd, mMeetingId, mMeetingId, mClass, mSubject));
                                }
                                dashboardModel.setLiveClassModels(liveClassModels);
                                tinyDB.putObject(DataSharedManager.DASHBOARD_BACKUP_DATA, dashboardModel);
                                liveClassAdapter = new LiveClassAdapter(context, liveClassModels);
                                if (liveClassAdapter.getItemCount() == 0) {
                                    findViewById(R.id.notFound).setVisibility(View.VISIBLE);
                                } else {
                                    findViewById(R.id.notFound).setVisibility(View.GONE);
                                    recyclerView_liveClasses.setAdapter(liveClassAdapter);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            dialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_feedback:
                sendMessage("Feedback: ", getResources().getString(R.string.app_name));
                return true;

            case R.id.user_contactUs:
                sendMessage("Contact Us: ", getResources().getString(R.string.app_name));
                return true;

            case R.id.user_aboutUs:
                Intent user_aboutUs = new Intent(context, InfoActivity.class);
                startActivity(user_aboutUs);
                return true;

            case R.id.logout_user: {
                new AlertDialog.Builder(context).setTitle("Logout")
                        .setMessage("Are you sure want to Logout from this Account?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tinyDB.clear();
                                context.getSharedPreferences(DataSharedManager.LOCAl_FLAGS, MODE_PRIVATE).edit().clear().apply();
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                dialog.dismiss();
                                dialog.cancel();
                            }
                        })
                        .show();
            }
            return true;
            default:
                return false;
        }
    }

    private void sendMessage(String etContent, String etHeader) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.EMAIL_ID});
        intent.putExtra(Intent.EXTRA_SUBJECT, etHeader);
        intent.putExtra(Intent.EXTRA_TEXT, etContent);
        intent.setType("text/plain");
        intent.setPackage("com.google.android.gm");
        try {
            startActivity(Intent.createChooser(intent, "Email via"));
            Log.i(TAG, "Finished sending email...");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}