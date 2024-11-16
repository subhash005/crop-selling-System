package com.example.cropmanagmentsystem;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomProgressBar extends LinearLayout {

    private ProgressBar progressBar;
    private TextView progressText;

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomProgressBar(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        // Inflate the custom layout
        LayoutInflater.from(context).inflate(R.layout.custom_progressbar_layout, this, true);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);
    }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
        progressText.setText(progress + "%");
    }

    public int getProgress() {
        return progressBar.getProgress();
    }

    public void setMax(int max) {
        progressBar.setMax(max);
    }
}
