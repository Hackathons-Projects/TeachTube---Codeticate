package com.hackathon.teachtube.Classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hackathon.teachtube.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class MobileVerification
{
    public static final int OTP_DIALOG_CLOSED = 100;
    public static final int OTP_VERIFICATION_FAILED = 200;
    public static final int OTP_SENT_LIMIT_END = 300;
    public static final int OTP_VERIFICATION_FAILURE = 400;
    public static final int INVALID_MOBILE_NO = 500;

    private static final boolean SUCCESS = true;
    private static final boolean FAILED = false;
    private static final String TAG = "MobileVerification";

    private String Verification_ID;
    private FirebaseAuth mAuth;

    private Context context;
    private View view;
    private OnMobileVerificationFinished onMobileVerificationFinished;

    private BottomSheetDialog dialogVerifyOtp;
    //private MaterialButton btn_continue;
    private CountDownTimer mCountDownTimer;
    private OtpTextView otp_pinView;
    private TextView resend_otp_link, check_otp_status, code_sended_mobileNo;
    private ImageButton btn_close;
    private ProgressDialog verifyingOtpDialog;

    private String phoneNo;

    public MobileVerification(Context context , View view) {
        this.context = context;
        this.view = view;
        mAuth = FirebaseAuth.getInstance();
    }

    {
        /**
         * Otp dialog closed = 100
         */
    }

    public void verifyOtpDialog(String mobileNo) {
        dialogVerifyOtp = new BottomSheetDialog(Objects.requireNonNull(context), R.style.BottomSheetDialogTheme);
        dialogVerifyOtp.setContentView(R.layout.mobile_otp_verify);
        dialogVerifyOtp.setCancelable(false);

        resend_otp_link = dialogVerifyOtp.findViewById(R.id.resend_otp_link);
        otp_pinView = dialogVerifyOtp.findViewById(R.id.otp_pinView);
        //btn_continue = dialogVerifyOtp.findViewById(R.id.btn_continue);
        check_otp_status = dialogVerifyOtp.findViewById(R.id.check_otp_status);
        code_sended_mobileNo = dialogVerifyOtp.findViewById(R.id.code_sended_mobileNo);
        btn_close = dialogVerifyOtp.findViewById(R.id.btn_close);

        //phoneNo = mobileNo.replace("+91", "");

        verifyingOtpDialog = new ProgressDialog(context);
        verifyingOtpDialog.setMessage("Verifying...");
        verifyingOtpDialog.setCancelable(false);

        /*
          There have to get the all error codes of the firebaseAuth and handle th exception.
          And When code is sent to the device, the otpVerifyingDialog dismiss();
          So, have to solve that.
         */


        code_sended_mobileNo.setText(phoneNo);

        sendVerificationCode(mobileNo);
        countDownTimer();

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogVerifyOtp.dismiss();
                dialogVerifyOtp.cancel();
                onMobileVerificationFinished.MobileVerificationFinished(OTP_DIALOG_CLOSED , FAILED , phoneNo);
                mCountDownTimer.cancel();
            }
        });

        resend_otp_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode(mobileNo);
                countDownTimer();
            }
        });

        /*btn_continue.setOnClickListener(v -> {
            String code = otp_pinView.getOTP();
            if (otp_pinView.getOTP().equals("")) {
                check_otp_status.setVisibility(View.VISIBLE);
                check_otp_status.setText("Field Required");
            } else {
                verifyingOtpDialog.show();
                verifyCode(code, phoneNo);
            }
        });*/

        otp_pinView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(@NotNull String otp) {
                verifyingOtpDialog.show();
                verifyCode(otp, phoneNo);
            }
        });

        dialogVerifyOtp.show();
    }

    private void countDownTimer() {
        mCountDownTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                resend_otp_link.setText(String.valueOf(millisUntilFinished / 1000));
                resend_otp_link.setEnabled(false);
            }

            @Override
            public void onFinish() {
                resend_otp_link.setText("Resend OTP");
                resend_otp_link.setEnabled(true);
            }
        }.start();
    }

    private void verifyCode(String code, String mMobileNo) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(Verification_ID, code);
        signinWithCredintials(credential, mMobileNo);
    }

    private void sendVerificationCode(String credentials) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(credentials)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity((Activity) context)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)// OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signinWithCredintials(PhoneAuthCredential credential, String mMobileNo) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            onMobileVerificationFinished.MobileVerificationFinished(0 , SUCCESS , mMobileNo);

                            dialogVerifyOtp.dismiss();
                            dialogVerifyOtp.cancel();
                            verifyingOtpDialog.dismiss();
                            mCountDownTimer.cancel();
                        } else {
                            Log.d(TAG, "onComplete: ");
                            otp_pinView.setOTP("");
                            onMobileVerificationFinished.MobileVerificationFinished(OTP_VERIFICATION_FAILED , FAILED , mMobileNo);
                            Log.d("firebase_error", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                            //Toast.makeText(context, "firebase_error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            dialogVerifyOtp.dismiss();
                            dialogVerifyOtp.cancel();
                            mCountDownTimer.cancel();
                            verifyingOtpDialog.dismiss();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                        onMobileVerificationFinished.MobileVerificationFinished(OTP_VERIFICATION_FAILURE , FAILED , mMobileNo);
                        Snackbar.make(view.findViewById(R.id.rootView), "Network Problem !!\nPlease try again later", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    Log.d(TAG, "onCodeSent: ");
                    super.onCodeSent(s, forceResendingToken);
                    Verification_ID = s;
                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    Log.d(TAG, "onVerificationCompleted: ");
                    String code = phoneAuthCredential.getSmsCode();
                    Log.d(TAG, "onVerificationCompleted: " + code);
                    if (code != null) {
                        otp_pinView.setOTP(code);
                        verifyingOtpDialog.show();
                        verifyCode(code, phoneNo);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Log.d(TAG, "onVerificationFailed: ");
                    Log.d("FirebaseException_error", e.toString());
                    dialogVerifyOtp.dismiss();
                    dialogVerifyOtp.cancel();
                    mCountDownTimer.cancel();
                    if (e instanceof FirebaseTooManyRequestsException) {
                        onMobileVerificationFinished.MobileVerificationFinished(OTP_SENT_LIMIT_END , FAILED , phoneNo);
                    }
                    if (e instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        onMobileVerificationFinished.MobileVerificationFinished(INVALID_MOBILE_NO , FAILED , phoneNo);
                    }

                }

            };

    public interface OnMobileVerificationFinished
    {
        void MobileVerificationFinished(int errorCode , boolean success , String mobileNo);
    }

    public void addOnMobileVerificationFinished(OnMobileVerificationFinished onMobileVerificationFinished)
    {
        this.onMobileVerificationFinished = onMobileVerificationFinished;
    }

}
