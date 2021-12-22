package com.hackathon.teachtube.OtherActivities.Study;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.hackathon.teachtube.Adapter.CustomTabAdapter;
import com.hackathon.teachtube.Adapter.SelectSubjectAdapter;
import com.hackathon.teachtube.Adapter.SelectClassAdapter;
import com.hackathon.teachtube.Classes.MainLoader;
import com.hackathon.teachtube.Models.ChapterSuitcase;
import com.hackathon.teachtube.Models.DashboardModel;
import com.hackathon.teachtube.Models.StudyMaterialsModel;
import com.hackathon.teachtube.Models.ClassSubjectModel;
import com.hackathon.teachtube.Models.UserSuitcase;
import com.hackathon.teachtube.OtherActivities.Study.StudyFragments.ExerciseFragment;
import com.hackathon.teachtube.OtherActivities.Study.StudyFragments.NotesFragment;
import com.hackathon.teachtube.OtherActivities.Study.StudyFragments.VideoFragment;
import com.hackathon.teachtube.R;
import com.hackathon.teachtube.Utils.Constants;
import com.hackathon.teachtube.Utils.DataSharedManager;
import com.hackathon.teachtube.Utils.TinyDB;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainStudyActivity extends AppCompatActivity {

    public static final String TAG = "MainStudyActivity";
    private Context context;
    private Toolbar toolbar;
    private TinyDB tinyDB;
    private UserSuitcase userSuitcase;

    private TabLayout tab_mainStudy;
    private ViewPager viewPager_mainStudy;
    private int mIndicator_Tab_width;
    private View mIndicator;

    private LinearLayout changeSubjectTopicChapter;

    private Call<StudyMaterialsModel> studyMaterialsModelCall;

    private Call<ClassSubjectModel> subjectTopicChapterModelCall;

    private RecyclerView select_class_recyclerView, select_subject_recyclerView;//, select_chapter_recyclerView;
    //private SelectChapterAdapter selectChapterAdapter;
    private SelectSubjectAdapter selectSubjectAdapter;
    private SelectClassAdapter selectClassAdapter;

    private ProgressDialog progressDialog;

    private ChapterSuitcase chapterSuitcase;

    private TextView currentSubject_image;
    private TextView currentSubject_title, currentSubject_class;
    private OnExerciseChangeListener onExerciseChangeListener;
    private OnNotesChangeListener onNotesChangeListener;
    private OnVideoChangeListener onVideoChangeListener;
    private DashboardModel dashboardModel;

    private String subject_id, subject, class_id, class_name, section_id, section, topic_id, topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_study);
        context = MainStudyActivity.this;
        tinyDB = new TinyDB(context);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //userSuitcase = tinyDB.getObject(DataSharedManager.USER_SUITCASE, UserSuitcase.class);

        dashboardModel = tinyDB.getObject(DataSharedManager.DASHBOARD_BACKUP_DATA, DashboardModel.class);

        chapterSuitcase = tinyDB.getObject(DataSharedManager.SELECT_SUBJECT_DEFAULT_CHAPTER_DATA, ChapterSuitcase.class);

        currentSubject_image = toolbar.findViewById(R.id.currentSubject_image);
        currentSubject_title = toolbar.findViewById(R.id.currentSubject_title);
        currentSubject_class = toolbar.findViewById(R.id.currentSubject_class);
        refreshToolbarData();

        tab_mainStudy = toolbar.findViewById(R.id.tab_mainStudy);
        viewPager_mainStudy = findViewById(R.id.viewPager_mainStudy);
        mIndicator = findViewById(R.id.indicator);

        changeSubjectTopicChapter = findViewById(R.id.changeSubjectTopicChapter);

        changeSubjectTopicChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSubject();
            }
        });

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        Dexter.withActivity((Activity) context)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        getData(chapterSuitcase.getClass_id(),
                                chapterSuitcase.getSubject_id(),
                                false);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(context, "Permission Denied!! :(\n Please Try again to upload file", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, final PermissionToken token) {
                        new AlertDialog.Builder(context).setTitle("Permission Required")
                                .setMessage("Permission is required to proceed")
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        token.cancelPermissionRequest();
                                    }
                                })
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        token.continuePermissionRequest();
                                    }
                                })
                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        token.cancelPermissionRequest();
                                    }
                                })
                                .show();
                    }
                }).check();

        findViewById(R.id.refreshLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chapterSuitcase = tinyDB.getObject(DataSharedManager.SELECT_SUBJECT_DEFAULT_CHAPTER_DATA, ChapterSuitcase.class);
                getData(chapterSuitcase.getClass_id(),
                        chapterSuitcase.getSubject_id(),
                        false);
            }
        });

    }

    private void refreshToolbarData() {
        currentSubject_title.setText(chapterSuitcase.getSubject());
        currentSubject_class.setText(chapterSuitcase.getClass_name());
        currentSubject_image.setText(String.valueOf(chapterSuitcase.getSubject().charAt(0)).toUpperCase() + String.valueOf(chapterSuitcase.getSubject().charAt(1)).toLowerCase());
    }

    private void changeSubject() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_select_subject);

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);

        toolbar.setTitle("Subject");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainLoader.Loader(false, dialog.findViewById(R.id.notFound));
                if (((LinearLayout) select_class_recyclerView.getParent()).getVisibility() == View.VISIBLE) {
                    dialog.dismiss();
                    dialog.cancel();
                } else if (((LinearLayout) select_subject_recyclerView.getParent()).getVisibility() == View.VISIBLE) {
                    String[] title = toolbar.getTitle().toString().split("-");
                    toolbar.setTitle(title[0].trim());
                    //toggleChapterLayout(false);
                    toggleSubjectLayout(false);
                    toggleClassLayout(true);
                }
            }
        });

        select_class_recyclerView = dialog.findViewById(R.id.select_class_recyclerView);
        select_subject_recyclerView = dialog.findViewById(R.id.select_subject_recyclerView);
        //select_chapter_recyclerView = dialog.findViewById(R.id.select_chapter_recyclerView);

        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        ArrayList<ClassSubjectModel> classSubjectModel = dashboardModel.getSubjectModel();

        //toggleChapterLayout(false);
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

                        ChapterSuitcase chapterSuitcase = new ChapterSuitcase(subject_id, subject, class_id, class_name);
                        tinyDB.putObject(DataSharedManager.SELECT_SUBJECT_DEFAULT_CHAPTER_DATA, chapterSuitcase);
                        refreshToolbarData();
                        dialog.dismiss();
                        dialog.cancel();
                        getData(chapterSuitcase.getClass_id(),
                                chapterSuitcase.getSubject_id(),
                                true);
                    }
                });
                select_subject_recyclerView.setAdapter(selectSubjectAdapter);
            }
        });

        //new getData(userSuitcase.branch_id, userSuitcase.session_id, userSuitcase.teacher_id, dialog, toolbar).execute();

        dialog.show();

    }

    private void toggleSubjectLayout(boolean b) {
        ((LinearLayout) select_subject_recyclerView.getParent()).setVisibility(b ? View.VISIBLE : View.GONE);
        ((LinearLayout) select_subject_recyclerView.getParent()).setEnabled(b);
    }

    private void toggleClassLayout(boolean b) {
        ((LinearLayout) select_class_recyclerView.getParent()).setVisibility(b ? View.VISIBLE : View.GONE);
        ((LinearLayout) select_class_recyclerView.getParent()).setEnabled(b);
    }

    public interface OnExerciseChangeListener {
        void OnExerciseChange(ArrayList<StudyMaterialsModel.Exercise> exerciseArrayList);
    }

    public interface OnNotesChangeListener {
        void OnNotesChange(ArrayList<StudyMaterialsModel.Note> noteArrayList);
    }

    public interface OnVideoChangeListener {
        void OnVideoChange(ArrayList<StudyMaterialsModel.Study> studyArrayList);
    }

    public void setOnExerciseChangeListener(OnExerciseChangeListener onExerciseChangeListener) {
        this.onExerciseChangeListener = onExerciseChangeListener;
    }

    public void setOnNotesChangeListener(OnNotesChangeListener onNotesChangeListener) {
        this.onNotesChangeListener = onNotesChangeListener;
    }

    public void setOnVideoChangeListener(OnVideoChangeListener onVideoChangeListener) {
        this.onVideoChangeListener = onVideoChangeListener;
    }

    public void getData(String class_id, String subject_id, boolean refresh) {
        ArrayList<StudyMaterialsModel.Exercise> exerciseArrayList = new ArrayList<>();
        ArrayList<StudyMaterialsModel.Study> studyArrayList = new ArrayList<>();
        ArrayList<StudyMaterialsModel.Note> noteArrayList = new ArrayList<>();

        MainLoader.Loader(true, findViewById(R.id.LL_loader));
        FirebaseDatabase.getInstance(Constants.FIREBASE_REFERENCE).getReference().child("Lecture").equalTo(Constants.CLASS, class_id).equalTo(Constants.SUBJECT, subject_id).get().
                addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        MainLoader.Loader(false, findViewById(R.id.LL_loader));
                        //Toast.makeText(context, ""+task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        if (task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            //Toast.makeText(context, "" + snapshot.exists(), Toast.LENGTH_SHORT).show();
                            if (snapshot.exists()) {

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    StudyMaterialsModel.Study study = dataSnapshot.getValue(StudyMaterialsModel.Study.class);
                                    study.setId(dataSnapshot.getKey());
                                    studyArrayList.add(study);
                                }
                                if (refresh)
                                    onVideoChangeListener.OnVideoChange(studyArrayList);
                            }
                            FirebaseDatabase.getInstance(Constants.FIREBASE_REFERENCE).getReference().child("Notes").equalTo(Constants.CLASS, class_id).equalTo(Constants.SUBJECT, subject_id).get().
                                    addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            MainLoader.Loader(false, findViewById(R.id.LL_loader));
                                            //Toast.makeText(context, ""+task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                            if (task.isSuccessful()) {
                                                DataSnapshot snapshot = task.getResult();
                                                //Toast.makeText(context, "" + snapshot.exists(), Toast.LENGTH_SHORT).show();
                                                if (snapshot.exists()) {

                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        StudyMaterialsModel.Note note = dataSnapshot.getValue(StudyMaterialsModel.Note.class);
                                                        note.setId(dataSnapshot.getKey());
                                                        noteArrayList.add(note);
                                                    }
                                                    if (refresh)
                                                        onNotesChangeListener.OnNotesChange(noteArrayList);
                                                }
                                                FirebaseDatabase.getInstance(Constants.FIREBASE_REFERENCE).getReference().child("Excercise").equalTo(Constants.CLASS, class_id).equalTo(Constants.SUBJECT, subject_id).get().
                                                        addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                                MainLoader.Loader(false, findViewById(R.id.LL_loader));
                                                                //Toast.makeText(context, ""+task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                                                if (task.isSuccessful()) {
                                                                    DataSnapshot snapshot = task.getResult();
                                                                    //Toast.makeText(context, "" + snapshot.exists(), Toast.LENGTH_SHORT).show();
                                                                    if (snapshot.exists()) {

                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                            StudyMaterialsModel.Exercise exercise = dataSnapshot.getValue(StudyMaterialsModel.Exercise.class);
                                                                            exercise.setId(dataSnapshot.getKey());
                                                                            exerciseArrayList.add(exercise);
                                                                        }
                                                                        if (refresh)
                                                                            onExerciseChangeListener.OnExerciseChange(exerciseArrayList);
                                                                        else {
                                                                            CustomTabAdapter mTabAdapter = new CustomTabAdapter(getSupportFragmentManager());
                                                                            mTabAdapter.addFragment(new VideoFragment(studyArrayList), "");
                                                                            mTabAdapter.addFragment(new NotesFragment(noteArrayList), "");
                                                                            mTabAdapter.addFragment(new ExerciseFragment(exerciseArrayList), "");

                                                                            viewPager_mainStudy.setOffscreenPageLimit(2);

                                                                            viewPager_mainStudy.setAdapter(mTabAdapter);
                                                                            tab_mainStudy.setupWithViewPager(viewPager_mainStudy);
                                                                            tab_mainStudy.getTabAt(0).setIcon(R.drawable.youtube_selected);
                                                                            tab_mainStudy.getTabAt(1).setIcon(R.drawable.ic_notes);
                                                                            tab_mainStudy.getTabAt(2).setIcon(R.drawable.ic_excersice);

                                                                            //Determine indicator width at runtime
                                                                            tab_mainStudy.post(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    mIndicator_Tab_width = (tab_mainStudy.getWidth() / tab_mainStudy.getTabCount());
                                                                                    //Assign new width
                                                                                    RelativeLayout.LayoutParams indicatorParams = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
                                                                                    indicatorParams.width = mIndicator_Tab_width;
                                                                                    mIndicator.setLayoutParams(indicatorParams);
                                                                                }
                                                                            });

                                                                            viewPager_mainStudy.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                                                                @Override
                                                                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                                                                    //FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIndicator_Tab.getLayoutParams();

                                                                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();

                                                                                    //Multiply positionOffset with indicatorWidth to get translation
                                                                                    float translationOffset = (positionOffset + position) * mIndicator_Tab_width;
                                                                                    params.leftMargin = (int) translationOffset;
                                                                                    mIndicator.setLayoutParams(params);

                                                                                    Log.d("positionOffset", String.valueOf(positionOffset));
                                                                                    Log.d("params", String.valueOf(params));
                                                                                    Log.d("translationOffset", String.valueOf(translationOffset));
                                                                                    Log.d("position", String.valueOf(positionOffset + position));
                                                                                    Log.d("positionOffsetPixels", String.valueOf(positionOffsetPixels));
                                                                                }

                                                                                @Override
                                                                                public void onPageSelected(int position) {

                                                                                }

                                                                                @Override
                                                                                public void onPageScrollStateChanged(int state) {

                                                                                }
                                                                            });
                                                                        }
                                                                    }

                                                                }
                                                            }
                                                        })
                                                        .addOnCanceledListener(new OnCanceledListener() {
                                                            @Override
                                                            public void onCanceled() {
                                                                MainLoader.Loader(false, findViewById(R.id.LL_loader));
                                                                MainLoader.Loader(true, findViewById(R.id.notFound));
                                                                Log.d(TAG, "onCanceled: ");
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        MainLoader.Loader(false, findViewById(R.id.LL_loader));
                                                        MainLoader.Loader(true, findViewById(R.id.somethingWrong));
                                                        Log.d(TAG, "onFailure: " + e.toString());
                                                    }
                                                });
                                            }
                                        }
                                    })
                                    .addOnCanceledListener(new OnCanceledListener() {
                                        @Override
                                        public void onCanceled() {
                                            MainLoader.Loader(false, findViewById(R.id.LL_loader));
                                            MainLoader.Loader(true, findViewById(R.id.notFound));
                                            Log.d(TAG, "onCanceled: ");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    MainLoader.Loader(false, findViewById(R.id.LL_loader));
                                    MainLoader.Loader(true, findViewById(R.id.somethingWrong));
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        MainLoader.Loader(false, findViewById(R.id.LL_loader));
                        MainLoader.Loader(true, findViewById(R.id.notFound));
                        Log.d(TAG, "onCanceled: ");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                MainLoader.Loader(false, findViewById(R.id.LL_loader));
                MainLoader.Loader(true, findViewById(R.id.somethingWrong));
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }

    /*private class getStudyMaterials extends AsyncTask<Void, Void, Void> {

        String branch_id, class_id, subject_id, topic_id, chepter_id;
        boolean refresh;

        public getStudyMaterials(String branch_id, String class_id, String subject_id, String topic_id, String chepter_id, boolean refresh) {
            this.branch_id = branch_id;
            this.class_id = class_id;
            this.subject_id = subject_id;
            this.topic_id = topic_id;
            this.chepter_id = chepter_id;
            this.refresh = refresh;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (refresh)
                        progressDialog.show();
                    else
                        MainLoader.Loader(true, findViewById(R.id.LL_loader));
                    MainLoader.Loader(false, findViewById(R.id.somethingWrong));
                    MainLoader.Loader(false, findViewById(R.id.notFound));
                }
            });
            studyMaterialsModelCall = RetrofitInstance.getInstance(AllKeyUrls.BASE_URL)
                    .create(StudyMaterialsInterface.class)
                    .getStudyMaterials(
                            branch_id,
                            class_id,
                            subject_id,
                            topic_id,
                            chepter_id
                    );
            studyMaterialsModelCall.enqueue(new Callback<StudyMaterialsModel>() {
                @Override
                public void onResponse(Call<StudyMaterialsModel> call, Response<StudyMaterialsModel> response) {
                    if (response.isSuccessful()) {

                        StudyMaterialsModel studyMaterialsModel = response.body();

                        if (refresh) {
                            onExerciseChangeListener.OnExerciseChange(studyMaterialsModel.getData().getExercise());
                            onNotesChangeListener.OnNotesChange(studyMaterialsModel.getData().getNotes());
                            onVideoChangeListener.OnVideoChange(studyMaterialsModel.getData().getStudy());
                        } else {
                            CustomTabAdapter mTabAdapter = new CustomTabAdapter(getSupportFragmentManager());
                            mTabAdapter.addFragment(new VideoFragment(studyMaterialsModel.getData().getStudy()), "");
                            mTabAdapter.addFragment(new NotesFragment(studyMaterialsModel.getData().getNotes()), "");
                            mTabAdapter.addFragment(new ExerciseFragment(studyMaterialsModel.getData().getExercise()), "");

                            viewPager_mainStudy.setOffscreenPageLimit(2);

                            viewPager_mainStudy.setAdapter(mTabAdapter);
                            tab_mainStudy.setupWithViewPager(viewPager_mainStudy);
                            tab_mainStudy.getTabAt(0).setIcon(R.drawable.youtube_selected);
                            tab_mainStudy.getTabAt(1).setIcon(R.drawable.ic_notes);
                            tab_mainStudy.getTabAt(2).setIcon(R.drawable.ic_excersice);

                            //Determine indicator width at runtime
                            tab_mainStudy.post(new Runnable() {
                                @Override
                                public void run() {
                                    mIndicator_Tab_width = (tab_mainStudy.getWidth() / tab_mainStudy.getTabCount());
                                    //Assign new width
                                    RelativeLayout.LayoutParams indicatorParams = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
                                    indicatorParams.width = mIndicator_Tab_width;
                                    mIndicator.setLayoutParams(indicatorParams);
                                }
                            });

                            viewPager_mainStudy.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                    //FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIndicator_Tab.getLayoutParams();

                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();

                                    //Multiply positionOffset with indicatorWidth to get translation
                                    float translationOffset = (positionOffset + position) * mIndicator_Tab_width;
                                    params.leftMargin = (int) translationOffset;
                                    mIndicator.setLayoutParams(params);

                                    Log.d("positionOffset", String.valueOf(positionOffset));
                                    Log.d("params", String.valueOf(params));
                                    Log.d("translationOffset", String.valueOf(translationOffset));
                                    Log.d("position", String.valueOf(positionOffset + position));
                                    Log.d("positionOffsetPixels", String.valueOf(positionOffsetPixels));
                                }

                                @Override
                                public void onPageSelected(int position) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });
                        }
                    } else {
                        try {
                            String errorString = response.errorBody().string();
                            Log.d(TAG, "onResponse error: " + errorString);
                            switch (response.code()) {
                                case 400: {
                                    assert response.errorBody() != null;

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            MainLoader.Loader(true, findViewById(R.id.notFound));
                                        }
                                    });

                                }
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainLoader.Loader(false, findViewById(R.id.LL_loader));
                            progressDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onFailure(Call<StudyMaterialsModel> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.toString());
                    //MainLoader.Loader(false, findViewById(R.id.LL_loader));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainLoader.Loader(false, findViewById(R.id.LL_loader));
                            MainLoader.Loader(true, findViewById(R.id.somethingWrong));
                            progressDialog.dismiss();
                        }
                    });
                }
            });
            return null;
        }
    }*/

    /*private class getData extends AsyncTask<Void, Void, Void> {

        String branch_id, session_id, teacher_id;
        Dialog dialog;
        Toolbar toolbar;

        public getData(String branch_id, String session_id, String teacher_id, Dialog dialog, Toolbar toolbar) {
            this.branch_id = branch_id;
            this.session_id = session_id;
            this.teacher_id = teacher_id;
            this.dialog = dialog;
            this.toolbar = toolbar;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.show();
                }
            });
            subjectTopicChapterModelCall = RetrofitInstance.getInstance(AllKeyUrls.BASE_URL)
                    .create(SubjectTopicChapterInterface.class)
                    .getData(branch_id,
                            session_id,
                            teacher_id
                    );
            subjectTopicChapterModelCall.enqueue(new Callback<SubjectTopicChapterModel>() {
                @Override
                public void onResponse(Call<SubjectTopicChapterModel> call, Response<SubjectTopicChapterModel> response) {
                    if (response.isSuccessful()) {

                        SubjectTopicChapterModel subjectTopicChapterModel = response.body();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                                toggleChapterLayout(false);
                                toggleSubjectLayout(true);
                                toggleTopicLayout(false);
                            }
                        });

                        selectSubjectAdapter = new SelectSubjectAdapter(context, subjectTopicChapterModel.getData());
                        selectSubjectAdapter.setOnItemClickListener(new SelectSubjectAdapter.OnItemClickListener() {
                            @Override
                            public void OnItemClick(int subject_position) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        toggleChapterLayout(false);
                                        toggleSubjectLayout(false);
                                        toggleTopicLayout(true);
                                    }
                                });

                                toolbar.setTitle(subjectTopicChapterModel.getData().get(subject_position).getSubject());
                                selectTopicAdapter = new SelectTopicAdapter(context, subjectTopicChapterModel.getData().get(subject_position).getTopic());
                                selectTopicAdapter.setOnItemClickListener(new SelectTopicAdapter.OnItemClickListener() {
                                    @Override
                                    public void OnItemClick(int topic_position) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                toggleChapterLayout(true);
                                                toggleSubjectLayout(false);
                                                toggleTopicLayout(false);
                                                toolbar.setTitle(subjectTopicChapterModel.getData().get(subject_position).getSubject() + " - " + subjectTopicChapterModel.getData().get(subject_position).getTopic().get(topic_position).getTopicName());
                                            }
                                        });
                                        selectChapterAdapter = new SelectChapterAdapter(context, subjectTopicChapterModel.getData().get(subject_position).getTopic().get(topic_position).getChepter());
                                        selectChapterAdapter.setOnItemClickListener(new SelectChapterAdapter.OnItemClickListener() {
                                            @Override
                                            public void OnItemClick(int position) {
                                                SubjectTopicChapterModel.Chepter chepter = subjectTopicChapterModel.getData().get(subject_position).getTopic().get(topic_position).getChepter().get(position);
                                                tinyDB.putObject(DataSharedManager.SELECT_SUBJECT_DEFAULT_CHAPTER_DATA, chepter);

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        currentSubject_title.setText(chepter.getSubject());
                                                        currentSubject_class.setText(chepter.getClass_());
                                                        currentSubject_image.setText(String.valueOf(chepter.getSubject().charAt(0)).toUpperCase() + String.valueOf(chepter.getSubject().charAt(1)).toLowerCase());
                                                        dialog.dismiss();
                                                    }
                                                });
                                                new getStudyMaterials(userSuitcase.branch_id,
                                                        chepter.getClassId(),
                                                        chepter.getSubjectId(),
                                                        chepter.getTopicId(),
                                                        chepter.getId(),
                                                        true
                                                ).execute();
                                            }
                                        });
                                        select_chapter_recyclerView.setAdapter(selectChapterAdapter);
                                    }
                                });
                                select_topic_recyclerView.setAdapter(selectTopicAdapter);
                            }
                        });
                        select_subject_recyclerView.setAdapter(selectSubjectAdapter);

                    } else {
                        try {
                            String errorString = response.errorBody().string();
                            Log.d(TAG, "onResponse error: " + errorString);
                            switch (response.code()) {
                                case 400: {
                                    assert response.errorBody() != null;

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            MainLoader.Loader(true, findViewById(R.id.notFound));
                                            dialog.dismiss();
                                        }
                                    });

                                }
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onFailure(Call<SubjectTopicChapterModel> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.toString());
                    //MainLoader.Loader(false, findViewById(R.id.LL_loader));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ImpMethods.showNetworkProblem(findViewById(R.id.rootView));
                            progressDialog.dismiss();
                            dialog.dismiss();
                        }
                    });
                }
            });
            return null;
        }
    }*/

}