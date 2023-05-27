package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class gamePlayBack extends AppCompatActivity {

    public ArrayList<String> movesList;

    private com.example.myapplication.gamePlaybackBoard gamePlaybackBoard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay_back);

        gamePlaybackBoard = findViewById(R.id.gamePlaybackBoard);
        Intent intent = getIntent();
        movesList = intent.getStringArrayListExtra("moves");
        gamePlaybackBoard.setMoves(movesList);
        gamePlaybackBoard.setGPB(this);
    }

    public void nextMoveButton(View view) {
        gamePlaybackBoard.nextMove();
    }

    public void getHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}