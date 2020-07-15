package com.tenakata.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.tenakata.Base.BaseActivity;
import com.tenakata.CallBacks.AuthenticationCallBacks;
import com.tenakata.CallBacks.BaseCallBacks;
import com.tenakata.Dialog.ProgressDialog;
import com.tenakata.Models.ModelOtp;
import com.tenakata.Models.ModelSuccess;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.Utilities.IntentHelper;
import com.tenakata.databinding.ActivityOtpverificationBinding;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;

import static com.tenakata.Dialog.ProgressDialog.progressDialog;

public class ActivityOTPVerification extends BaseActivity  {

    private ActivityOtpverificationBinding binding;
    private String verificationID;
    private Context context;
    private ProgressDialog loader;
    private String contact, countryCode;
    String resendOtp;


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewClickBack:
                finish();
                break;
        }
    }

    @Override
    public void onClick(int viewId, View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otpverification);
        resendOtp=getIntent().getStringExtra("otp");
        context = this;
        binding.viewClickBack.setOnClickListener(this);
        loader = new ProgressDialog(context);
        FirebaseApp.initializeApp(this);

        if (getIntent()!=null){
            countryCode = getIntent().getStringExtra("countryCode");
            contact = getIntent().getStringExtra("contact");
           /* if (contact!=null && !contact.equals("")) {
                sendOtp();
            }*/

        }


        binding.firstPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length()==4){
                  //  apiLoginWithmPin(charSequence.toString());
                    showLoader();
                   apiCheckPin(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.viewTapHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { if (contact != null) {
                showLoader();
                   resendOtp();
                }
            }
        });
    }
    private void resendOtp(){

        final JSONObject params=new JSONObject();
        try {
            params.put("phone", getIntent().getStringExtra("contact"));
            params.put("country_code", getIntent().getStringExtra("countryCode"));
            params.put("role", "user");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Authentication.object(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_OTP),this,params);
    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        super.onTaskSuccess(responseObj);
        if (responseObj instanceof ModelSuccess){
            startActivity(IntentHelper.getForgotPassword(this).putExtra("countryCode",getIntent().getStringExtra("countryCode")).putExtra("contact",getIntent().getStringExtra("contact")));
            finish();
        }
        if (responseObj instanceof ModelOtp) {
            ModelOtp modelSuccess =(ModelOtp)responseObj;
            resendOtp =String.valueOf(modelSuccess.getOtp());
        }

    }

    @Override
    public void onTaskError(String errorMsg) {
        super.onTaskError(errorMsg);
        Toast.makeText(this,"Invalid Otp",Toast.LENGTH_SHORT).show();
    }

    private void apiCheckPin(String otp) {
        final JSONObject params=new JSONObject();
        try {
            params.put("otp", otp);
            params.put("role", "user");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Authentication.object(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_CHECK_OTP),this,params);
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


   /* private void sendOtp() {

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
    }*/

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

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
