package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.boardSetup;
import com.example.myapplication.recordingSetup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void playButtonClicked(View view) {
        Intent intent = new Intent(this, boardSetup.class);
        startActivity(intent);
    }

    public void recordingsButtonClicked(View view) {
        Intent intent = new Intent(this, recordingSetup.class);
        startActivity(intent);
    }
}