package com.tenakata.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.tenakata.Base.BaseActivity;
import com.tenakata.Models.ModelSuccess;
import com.tenakata.Models.TrainingViewModel;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.databinding.ActivityTrainingDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ActivityTrainingDetails extends BaseActivity implements View.OnClickListener {
    ActivityTrainingDetailsBinding binding;
    Intent intent;
    private Context context;
    private FullscreenVideoLayout videoLayout;

    @Override
    public void onClick(int viewId, View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_training_details);
        context = this;
        intent=getIntent();
       // Toast.makeText(this,intent.getStringExtra("id"),Toast.LENGTH_LONG).show();
        hitApi();
        binding.tvHeadLeft.setOnClickListener(this);
        binding.trainingNextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitApiRating();
            }
        });

        videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
        videoLayout.setActivity(this);



    }

    private void hitApi() {

        final JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("id", intent.getStringExtra("id"));
            jsonObject.put("role", "user");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Authentication.object(this, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_TRAINING_VIEW),this,jsonObject);
    }

    private void hitApiRating() {

        final JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("id", intent.getStringExtra("id"));
            jsonObject.put("role", "user");
            jsonObject.put("rating",binding.ratingBar6.getRating());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Authentication.object(this, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_TRAINING_RATING),this,jsonObject);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_head_left : finish();
            break;

        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onTaskError(String errorMsg) {
        super.onTaskError(errorMsg);

    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        super.onTaskSuccess(responseObj);
        if (responseObj instanceof TrainingViewModel){
            TrainingViewModel model=(TrainingViewModel) responseObj;
            binding.tvTrainingDetailsDescription.setText(model.getResult().get(0).getDescription());
            binding.tvTrainingDetailsTitle.setText(model.getResult().get(0).getTitle());
            Uri videoUri1 = Uri.parse("https://www.radiantmediaplayer.com/media/bbb-360p.mp4");
            Uri videoUri = Uri.parse(model.getResult().get(0).getVideo());
            try {

                videoLayout.setVideoURI(videoUri1);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (responseObj instanceof ModelSuccess){
            Toast.makeText(this,"Thank You for Rating...",Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
