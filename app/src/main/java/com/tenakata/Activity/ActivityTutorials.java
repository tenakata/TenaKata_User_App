package com.tenakata.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.IntentHelper;
import com.tenakata.R;
import com.tenakata.Adapters.TutorialsAdapter;
import com.tenakata.databinding.ActivityTutorialsBinding;

import java.util.ArrayList;
import java.util.List;

public class ActivityTutorials extends AppCompatActivity implements View.OnClickListener {
    private ActivityTutorialsBinding binding;
    private Context context;
    private List<String> tutorialText = new ArrayList<>();
    private List<Integer> tutorialIcons = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_tutorials);
        context =this;

        binding.startBtn.setOnClickListener(this);
        tutorialText.add(getString(R.string.txt_tutorial_text_1));
        tutorialText.add(getString(R.string.txt_tutorial_text_2));
        tutorialText.add(getString(R.string.txt_tutorial_text_2));
        tutorialText.add(getString(R.string.txt_tutorial_text_2));
        tutorialIcons.add(R.drawable.screen_image);
        tutorialIcons.add(R.drawable.screen_image_four);
        tutorialIcons.add(R.drawable.screen_image_three);
        tutorialIcons.add(R.drawable.screen_image_two);

        binding.viewpager.setAdapter(new TutorialsAdapter(context,tutorialIcons, tutorialText));
        binding.indicator.setViewPager(binding.viewpager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startBtn:
                HRPrefManager.getInstance(context).setKeyIsStart(true);
                startActivity(IntentHelper.getBioMetric(context));
                finish();
                break;
        }
    }
}
