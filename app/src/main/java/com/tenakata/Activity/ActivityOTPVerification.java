package com.tenakata.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.tenakata.CallBacks.AuthenticationCallBacks;
import com.tenakata.Dialog.ProgressDialog;
import com.tenakata.R;
import com.tenakata.Utilities.IntentHelper;
import com.tenakata.databinding.ActivityOtpverificationBinding;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;

public class ActivityOTPVerification extends AppCompatActivity implements View.OnClickListener {

    private ActivityOtpverificationBinding binding;
    private String verificationID;
    private Context context;
    private ProgressDialog loader;
    private String contact, countryCode;


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewClickBack:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otpverification);

        context = this;
        binding.viewClickBack.setOnClickListener(this);
        loader = new ProgressDialog(context);
        FirebaseApp.initializeApp(this);

        if (getIntent()!=null){
            countryCode = getIntent().getStringExtra("countryCode");
            contact = getIntent().getStringExtra("contact");
            if (contact!=null && !contact.equals("")) {
                sendOtp();
            }
        }


        binding.viewEnterCode.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                Log.d("OTP listener", "====> ");
            }

            @Override
            public void onOTPComplete(String code) {

                if (verificationID != null && code != null) {
                    verifyCode(verificationID, code);
                } else {
                    Toast.makeText(context, getString(R.string.txt_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.viewTapHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { if (contact != null) {
                    sendOtp();
                }
            }
        });
    }


    private void startCountDownTimer() {
        try {
            new CountDownTimer(60000, 1000) {
                public void onTick(long millisUntilFinished) {
                    binding.viewTapHere.setEnabled(false);
                    binding.viewTapHere.setTextColor(getResources().getColor(R.color.colorGray));
                }

                public void onFinish() {
                    binding.viewTapHere.setEnabled(true);
                    binding.viewTapHere.setTextColor(getResources().getColor(R.color.colorBlue));
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void sendOtp() {

        final int Timeout = 60;

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+" + countryCode.concat(contact).trim(),  // Phone number to verify
                Timeout,                 // Timeout duration
                TimeUnit.SECONDS,       // Unit of timeout
                this,           // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        binding.viewEnterCode.setOTP(phoneAuthCredential.getSmsCode());
                        FirebaseAuth.getInstance().signOut();
                        startActivity(IntentHelper.getForgotPassword(context)
                                .putExtra("countryCode",countryCode)
                                .putExtra("contact",contact)
                        );
                        finish();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        binding.viewEnterCode.setOTP(null);
                        Toast.makeText(context, getString(R.string.txt_went_wrong), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verID, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verID, forceResendingToken);
                        verificationID = verID;
                        startCountDownTimer();
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                    }
                });
    }

    private void verifyCode(String verificationID, String otp) {
        PhoneAuthCredential authCredential = PhoneAuthProvider.getCredential(verificationID, otp);
        FirebaseAuth.getInstance().signInWithCredential(authCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            startActivity(IntentHelper.getForgotPassword(context)
                                    .putExtra("countryCode",countryCode)
                                    .putExtra("contact",contact)
                            );
                            finish();

                        }
                        Log.w("failed message", "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            //Toast.makeText(context,"Invalid OTP",Toast.LENGTH_SHORT).show();

                            try {
                                if (task.getException().toString().contains("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The sms verification code used to create the phone auth credential is invalid. Please resend the verification code sms and be sure use the verification code provided by the user.")) {
                                    new AlertDialog.Builder(context).setTitle(getResources().getString(R.string.app_name))
                                            .setMessage(R.string.txt_wrong_otp)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();

                                } else if (task.getException().toString().contains("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The sms code has expired. Please re-send the verification code to try again.")) {

                                    new AlertDialog.Builder(context).setTitle(getResources().getString(R.string.app_name))
                                            .setMessage(R.string.txt_code_expired)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
