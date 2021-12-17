package com.hackathon.teachtube.Security;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.hackathon.teachtube.Classes.MobileVerification;
import com.hackathon.teachtube.R;
import com.hackathon.teachtube.Utils.AllKeyUrls;
import com.hackathon.teachtube.Utils.AuthEncrypter;
import com.hackathon.teachtube.Utils.Constants;
import com.hackathon.teachtube.Utils.ImpMethods;
import com.hackathon.teachtube.Utils.TinyDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    private Context context;
    private static final String TAG = "RegisterActivity";

    private TextView login_link;
    private TextInputEditText et_email, et_mobileNo, et_contact_person, et_CaCode; //et_gst_no
    private MaterialCardView btn_submit;

    private Toolbar toolbar;

    private ProgressDialog verifyingOtpDialog;

    private MobileVerification mobileVerification;

    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = RegisterActivity.this;
        mobileVerification = new MobileVerification(context, findViewById(R.id.rootView));

        firebaseDatabase = FirebaseDatabase.getInstance();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitle("Register");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        login_link = findViewById(R.id.login_link);
        //et_gst_no = findViewById(R.id.et_gst_no);
        et_email = findViewById(R.id.et_email);
        et_mobileNo = findViewById(R.id.et_mobileNo);
        et_contact_person = findViewById(R.id.et_contactPerson);
        //btn_verify_mobile = findViewById(R.id.btn_verify_mobile);
        btn_submit = findViewById(R.id.btn_submit);

        verifyingOtpDialog = new ProgressDialog(context);
        verifyingOtpDialog.setMessage("Verifying...");
        verifyingOtpDialog.setCancelable(false);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String mGSTNo = et_gst_no.getText().toString().trim();
                String mEmail = et_email.getText().toString().trim();
                String mMobileNo = et_mobileNo.getText().toString().trim();
                String mContactPerson = et_contact_person.getText().toString().trim();
                String mCACode = et_CaCode.getText().toString().trim();

                String gstNoRegex = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$";

                int check = 0;

                ((TextInputLayout) et_mobileNo.getParent().getParent()).setErrorEnabled(false);

                ((TextInputLayout) et_email.getParent().getParent()).setErrorEnabled(false);

                ((TextInputLayout) et_contact_person.getParent().getParent()).setErrorEnabled(false);

                ((TextInputLayout) et_CaCode.getParent().getParent()).setErrorEnabled(false);

                if (mContactPerson.equals("")) {
                    ((TextInputLayout) et_contact_person.getParent().getParent()).setError("Field Required");
                    ((TextInputLayout) et_contact_person.getParent().getParent()).setErrorEnabled(true);
                    check++;
                }

                /*if (mCACode.equals("")) {
                    ((TextInputLayout) et_CaCode.getParent().getParent()).setError("Field Required");
                    ((TextInputLayout) et_CaCode.getParent().getParent()).setErrorEnabled(true);
                    check++;
                }*/

                if (!ImpMethods.isMobileNoValid(mMobileNo)) {
                    Log.d(TAG, "onClick: if");
                    ((TextInputLayout) et_mobileNo.getParent().getParent()).setError("Invalid Mobile No.");
                    ((TextInputLayout) et_mobileNo.getParent().getParent()).setErrorEnabled(true);
                    check++;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                    Log.d(TAG, "where: 3");
                    ((TextInputLayout) et_email.getParent().getParent()).setError("Invalid Email Address");
                    ((TextInputLayout) et_email.getParent().getParent()).setErrorEnabled(true);
                    check++;
                }

                if (check == 0) {
                    Log.d(TAG, "where: 6");

                    verifyMobileNo(mMobileNo , mEmail, mContactPerson, mCACode , findViewById(R.id.rootView));
                }
            }
        });

        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void registerUser(String mobileNo, String email, String contactPerson, String caCode, View view) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Registering...");
        dialog.show();

        String encryptedData = AuthEncrypter.Encrypt(mobileNo);
        Map<String, Object> data = new HashMap<>();
        data.put(Constants.USER_NAME, contactPerson);
        data.put(Constants.USER_EMAIL, email);
        data.put(Constants.USER_MOBILE_NO, encryptedData);

        firebaseDatabase.getReference("Users").child(encryptedData).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
                dialog.dismiss();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Snackbar.make(ImpMethods.getViewFromContext(context), "Network Problem! Please try again.", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private int otpVerifiedFlag = 0;
    private String previousSuccessfulNo = "";

    private void verifyMobileNo(String phone_no, String email, String contactPerson, String caCode, View view) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Verifying...");
        dialog.show();
        /*RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AllKeyUrls.getGetSigninMobileNo(phone_no),

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("0")) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                String userStatus = dataObject.getString("user_status");


                                TinyDB tinyDB = new TinyDB(context);
                                tinyDB.putString(DataSharedManager.USER_STATUS, userStatus);

                                String mMobileNo = phone_no;

                                dialog.dismiss();

                                mobileVerification.verifyOtpDialog("+91" + mMobileNo);
                                mobileVerification.addOnMobileVerificationFinished(new MobileVerification.OnMobileVerificationFinished() {
                                    @Override
                                    public void MobileVerificationFinished(int errorCode, boolean success, String mobileNo) {
                                        previousSuccessfulNo = mobileNo;
                                        if (success) {
                                            ((TextInputLayout) et_mobileNo.getParent().getParent()).setErrorEnabled(false);

                                            ((TextInputLayout) et_mobileNo.getParent().getParent()).setEndIconDrawable(R.drawable.correct);
                                            ((TextInputLayout) et_mobileNo.getParent().getParent()).setEndIconVisible(true);

                                            registerUser(mMobileNo, email, contactPerson, caCode, view);

                                            //btn_verify_mobile.setEnabled(false);
                                            //et_mobileNo.setEnabled(false);
                                            //btn_verify_mobile.setImageResource(R.drawable.ic_edit);
                                            //btn_verify_mobile.setTag(R.drawable.ic_edit);
                                            otpVerifiedFlag = 1;
                                        } else {
                                            otpVerifiedFlag = 0;
                                            switch (errorCode) {
                                                case MobileVerification.OTP_DIALOG_CLOSED:
                                                    ((TextInputLayout) et_mobileNo.getParent().getParent()).setErrorEnabled(true);
                                                    ((TextInputLayout) et_mobileNo.getParent().getParent()).setError("Verification Required");
                                                    break;
                                                case MobileVerification.OTP_SENT_LIMIT_END:
                                                    //tooManyRequestFlag = 1;
                                                    ((TextInputLayout) et_mobileNo.getParent().getParent()).setErrorEnabled(true);
                                                    ((TextInputLayout) et_mobileNo.getParent().getParent()).setError("Too Many Failed Attempts.\nTry again Later!");
                                                    //btn_verify_mobile.setEnabled(false);
                                                    break;
                                                case MobileVerification.OTP_VERIFICATION_FAILED:
                                                    ((TextInputLayout) et_mobileNo.getParent().getParent()).setEndIconActivated(false);
                                                    break;
                                                case MobileVerification.INVALID_MOBILE_NO:
                                                    ((TextInputLayout) et_mobileNo.getParent().getParent()).setErrorEnabled(true);
                                                    ((TextInputLayout) et_mobileNo.getParent().getParent()).setError("Invalid Mobile No.");
                                                    break;
                                                case MobileVerification.OTP_VERIFICATION_FAILURE:
                                                    break;
                                            }
                                        }
                                    }
                                });

                                MainLoader.Loader(false, findViewById(R.id.LL_loader));
                            } else {
                                MainLoader.Loader(false, findViewById(R.id.LL_loader));
                                dialog.dismiss();
                                ImpMethods.showErrorSnackbar(context, view, "Mobile No. Already Registered");
                            }

                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                            MainLoader.Loader(false, findViewById(R.id.LL_loader));
                            Log.d(TAG, e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MainLoader.Loader(false, findViewById(R.id.LL_loader));
                        dialog.dismiss();
                        Log.d(TAG, "onErrorResponse: " + error.toString());
                        ImpMethods.showErrorSnackbar(context, view, "Network Problem !!\nPlease try again later");
                    }
                }
        );
        requestQueue.add(stringRequest);*/

        String decryptedData = AuthEncrypter.Encrypt(phone_no);
        firebaseDatabase.getReference("Users").child(decryptedData).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    mobileVerification.verifyOtpDialog(phone_no);
                    mobileVerification.addOnMobileVerificationFinished(new MobileVerification.OnMobileVerificationFinished() {
                        @Override
                        public void MobileVerificationFinished(int errorCode, boolean success, String mobileNo) {
                            previousSuccessfulNo = mobileNo;
                            if (success) {
                                ((TextInputLayout) et_mobileNo.getParent().getParent()).setErrorEnabled(false);

                                ((TextInputLayout) et_mobileNo.getParent().getParent()).setEndIconDrawable(R.drawable.correct);
                                ((TextInputLayout) et_mobileNo.getParent().getParent()).setEndIconVisible(true);

                                registerUser(mobileNo, email, contactPerson, caCode, view);

                                //btn_verify_mobile.setEnabled(false);
                                //et_mobileNo.setEnabled(false);
                                //btn_verify_mobile.setImageResource(R.drawable.ic_edit);
                                //btn_verify_mobile.setTag(R.drawable.ic_edit);
                                otpVerifiedFlag = 1;
                            } else {
                                otpVerifiedFlag = 0;
                                switch (errorCode) {
                                    case MobileVerification.OTP_DIALOG_CLOSED:
                                        ((TextInputLayout) et_mobileNo.getParent().getParent()).setErrorEnabled(true);
                                        ((TextInputLayout) et_mobileNo.getParent().getParent()).setError("Verification Required");
                                        break;
                                    case MobileVerification.OTP_SENT_LIMIT_END:
                                        //tooManyRequestFlag = 1;
                                        ((TextInputLayout) et_mobileNo.getParent().getParent()).setErrorEnabled(true);
                                        ((TextInputLayout) et_mobileNo.getParent().getParent()).setError("Too Many Failed Attempts.\nTry again Later!");
                                        //btn_verify_mobile.setEnabled(false);
                                        break;
                                    case MobileVerification.OTP_VERIFICATION_FAILED:
                                        ((TextInputLayout) et_mobileNo.getParent().getParent()).setEndIconActivated(false);
                                        break;
                                    case MobileVerification.INVALID_MOBILE_NO:
                                        ((TextInputLayout) et_mobileNo.getParent().getParent()).setErrorEnabled(true);
                                        ((TextInputLayout) et_mobileNo.getParent().getParent()).setError("Invalid Mobile No.");
                                        break;
                                    case MobileVerification.OTP_VERIFICATION_FAILURE:
                                        break;
                                }
                            }
                        }
                    });
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

}
