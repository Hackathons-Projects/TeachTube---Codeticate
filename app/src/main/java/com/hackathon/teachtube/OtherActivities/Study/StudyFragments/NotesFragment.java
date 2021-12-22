package com.hackathon.teachtube.OtherActivities.Study.StudyFragments;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hackathon.teachtube.Adapter.NotesExerciseRecyclerAdapter;
import com.hackathon.teachtube.Classes.MainLoader;
import com.hackathon.teachtube.Models.ChapterSuitcase;
import com.hackathon.teachtube.Models.StudyMaterialsModel;
import com.hackathon.teachtube.Models.ClassSubjectModel;
import com.hackathon.teachtube.OtherActivities.Study.MainStudyActivity;
import com.hackathon.teachtube.R;
import com.hackathon.teachtube.Utils.AllKeyUrls;
import com.hackathon.teachtube.Utils.DataSharedManager;
import com.hackathon.teachtube.Utils.FileUtils;
import com.hackathon.teachtube.Utils.ImpMethods;
import com.hackathon.teachtube.Utils.TinyDB;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class NotesFragment extends Fragment {

    public static final String TAG = "NotesFragment";
    private Context context;
    private TinyDB tinyDB;

    private ArrayList<StudyMaterialsModel.Note> noteArrayList = new ArrayList<>();

    private RecyclerView notes_recyclerView;
    private NotesExerciseRecyclerAdapter notesExerciseRecyclerAdapter;

    private TextView chapterTitle;
    private ChapterSuitcase chepter;

    private FloatingActionButton add_notes;
    private long dowloadId = 0;
    private BroadcastReceiver onDownloadComplete;
    private DownloadManager downloadManager;

    public NotesFragment(ArrayList<StudyMaterialsModel.Note> noteArrayList) {
        this.noteArrayList = noteArrayList;
    }

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        context = getActivity();
        tinyDB = new TinyDB(context);

        chepter = tinyDB.getObject(DataSharedManager.SELECT_SUBJECT_DEFAULT_CHAPTER_DATA, ChapterSuitcase.class);

        notes_recyclerView = view.findViewById(R.id.notes_recyclerView);
        add_notes = view.findViewById(R.id.add_notes);

        chapterTitle = view.findViewById(R.id.chapterTitle);

        chapterTitle.setText(chepter.getSubject());

        if (noteArrayList.size() == 0) {
            MainLoader.Loader(true, view.findViewById(R.id.notFound));
        } else {
            MainLoader.Loader(false, view.findViewById(R.id.notFound));
            setNotesAdapter(noteArrayList);
        }

        ((MainStudyActivity) Objects.requireNonNull(requireActivity())).setOnNotesChangeListener(new MainStudyActivity.OnNotesChangeListener() {
            @Override
            public void OnNotesChange(ArrayList<StudyMaterialsModel.Note> notes) {
                chepter = tinyDB.getObject(DataSharedManager.SELECT_SUBJECT_DEFAULT_CHAPTER_DATA, ChapterSuitcase.class);
                chapterTitle.setText(chepter.getSubject());
                if (notes.size() == 0) {
                    MainLoader.Loader(true, view.findViewById(R.id.notFound));
                } else {
                    MainLoader.Loader(false, view.findViewById(R.id.notFound));
                    setNotesAdapter(notes);
                }
            }
        });

        add_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(context, AddNotesActivity.class);
                startActivity(intent);
                getActivity().finish();*/
            }
        });
        return view;
    }

    private void setNotesAdapter(ArrayList<StudyMaterialsModel.Note> noteArrayList)
    {
        notesExerciseRecyclerAdapter = new NotesExerciseRecyclerAdapter(null, noteArrayList, context);
        notesExerciseRecyclerAdapter.setOnDownloadClickListener(new NotesExerciseRecyclerAdapter.OnDownloadClickListener() {
            @Override
            public void onDownloadClick(ProgressBar progressBar, RelativeLayout cancel_download, ImageView imgDocument, ImageView imgDownloadIcon, String docName, String docId, String docType, int position) {
                String directory = docType.equals("NOTE") ? "/Notes/" : "/Exercise/";
                File applictionFile = new File(Environment
                        .getExternalStorageDirectory().getAbsolutePath()
                        + "/" + getResources().getString(R.string.real_app_name)
                        + directory + docName);
                if (applictionFile.exists()) {
                    Log.d(TAG, "onDownloadClick: 1");
                    OpenPDF(applictionFile);
                } else {
                    if (cancel_download.getVisibility() != View.VISIBLE)
                    {
                        Log.d(TAG, "onDownloadClick: 2");
                        if (onDownloadComplete != null)
                            Objects.requireNonNull(requireActivity()).unregisterReceiver(onDownloadComplete);
                        dowloadId = 0;
                        downloadManager = null;
                        long Id = PermissionRequest(docType, progressBar, cancel_download, imgDownloadIcon, docName, docId, context);
                        Log.d("download_id", "" + Id);

                        cancel_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (downloadManager != null) {
                                    if (Id != 0) {
                                        Log.d(TAG, "onDownloadClick: 4");
                                        downloadManager.remove(Id);
                                        Toast.makeText(context, "Download Cancelled!", Toast.LENGTH_SHORT).show();
                                        cancel_download.setVisibility(View.GONE);
                                        cancel_download.setEnabled(false);

                                        imgDownloadIcon.setVisibility(View.VISIBLE);
                                        imgDownloadIcon.setEnabled(true);

                                        imgDocument.setVisibility(View.GONE);
                                        imgDocument.setEnabled(false);
                                    }
                                }
                            }
                        });

                        if (Id != 0) {
                            Log.d(TAG, "onDownloadClick: 5");
                            onDownloadComplete = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    //Fetching the download id received with the broadcast
                                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                                    //Checking if the received broadcast is for our enqueued download by matching download id
                                    Log.d(TAG, "onDownloadClick: 7");
                                    if (Id == id && intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                                        Log.d(TAG, "onDownloadClick: 6");
                                        if (applictionFile.exists())
                                        {
                                            Log.d(TAG, "onDownloadClick: 10");
                                            Toast.makeText(getActivity(), "Download Completed", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            cancel_download.setVisibility(View.GONE);
                                            cancel_download.setEnabled(false);
                                            imgDownloadIcon.setVisibility(View.GONE);
                                            imgDocument.setVisibility(View.VISIBLE);

                                            if (FileUtils.checkFileExtension("pdf" , docName))
                                            {
                                                imgDocument.setImageBitmap(FileUtils.getPdfThumbnail(context , Uri.fromFile(applictionFile)));
                                            }else{
                                                imgDocument.setImageBitmap(FileUtils.getBitmap(applictionFile));
                                            }
                                        }

                                    }
                                }
                            };
                            Objects.requireNonNull(requireActivity()).registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        }
                    }else{
                        Log.d(TAG, "onDownloadClick: 3");
                    }
                }
            }
        });
        notes_recyclerView.setAdapter(notesExerciseRecyclerAdapter);
    }

    public long PermissionRequest(String type, ProgressBar progressBar, RelativeLayout cancel_download, ImageView imgDownloadIcon, String filename, String docId, Context context) {
        Dexter.withActivity((Activity) context)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Log.d(TAG, "onDownloadClick: 8");
                        progressBar.setVisibility(View.VISIBLE);
                        imgDownloadIcon.setVisibility(View.GONE);
                        cancel_download.setEnabled(true);
                        cancel_download.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Downloading file... ", Toast.LENGTH_SHORT).show();
                        dowloadId = DownloadFiles(AllKeyUrls.getUploadNoteDocumentPath(docId), filename, context, type);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(context, "Permission Denied!! :(\n Please Try again to download file", Toast.LENGTH_SHORT).show();
                        dowloadId = 0;
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
                                        dowloadId = 0;
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
                                        dowloadId = 0;
                                    }
                                })
                                .show();
                    }
                }).check();
        return dowloadId;
    }

    private long DownloadFiles(String url, String filename, Context context, String type) {
        Log.d(TAG, "onDownloadClick: 9");
        ImpMethods.createAppFolder(context);
        url = url + FileUtils.getExtension(filename);
        Uri uri = Uri.parse(url);
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        Log.d("download_URL", url);

        request.setTitle(getResources().getString(R.string.real_app_name));
        request.setDescription("Downloading file...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        String directory = type.equals("NOTE") ? "/Notes/" : "/Exercise/";

        Log.d(TAG, "DownloadFiles: " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getResources().getString(R.string.app_name) + directory + filename);
        Log.d(TAG, "public dir DownloadFiles: " + Environment.DIRECTORY_DOCUMENTS + "/" + context.getResources().getString(R.string.app_name) + directory + filename);

        Uri fileUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getResources().getString(R.string.app_name) + directory + filename));
        Log.d(TAG, "DownloadFiles uri: " + fileUri.toString());
        //request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getResources().getString(R.string.app_name),  directory + filename);
        request.setDestinationUri(fileUri);
        request.setMimeType("*/*");

        return downloadManager.enqueue(request);
    }

    public void OpenPDF(File file) {
        try {
            Intent pdfIntent = new Intent();
            //pdfIntent.setType("application/pdf");
            pdfIntent.setAction(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfIntent.setData(uri);
            startActivity(pdfIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "OpenPDF: " + e.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (onDownloadComplete != null)
            Objects.requireNonNull(requireActivity()).unregisterReceiver(onDownloadComplete);
    }

}