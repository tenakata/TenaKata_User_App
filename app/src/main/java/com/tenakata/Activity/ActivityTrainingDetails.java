package com.tenakata.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.tenakata.R;
import com.tenakata.databinding.ActivityTrainingDetailsBinding;

import java.io.IOException;

public class ActivityTrainingDetails extends AppCompatActivity implements View.OnClickListener {
    ActivityTrainingDetailsBinding binding;
    private Context context;
    private FullscreenVideoLayout videoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_training_details);
        context = this;
        binding.tvHeadLeft.setOnClickListener(this);

        videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
        videoLayout.setActivity(this);
        Uri videoUri = Uri.parse("https://www.radiantmediaplayer.com/media/bbb-360p.mp4");
        try {

            videoLayout.setVideoURI(videoUri);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_head_left : finish();
            break;

        }
    }
}
