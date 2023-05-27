package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class boardSetup extends AppCompatActivity {

    private board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_setup);

        board = findViewById(R.id.board);
        board.setBS(this);

    }

    public void endGameButton(View view){
        board.endGame(this);
    }

    public void drawButton(View view) {
        board.draw();
    }

    public void resignButton(View view) {
        board.resign();
    }

    public void aiButton(View view) {

        board.ai();
    }

    public void undoButton(View view) {

        board.undo();
    }

    public void goToHomePage() {
        Intent intent = new Intent(boardSetup.this, MainActivity.class);
        startActivity(intent);
    }

    public void goToSaveGameScreen(ArrayList<String> list) {
        Intent intent = new Intent(boardSetup.this, saveGameScreen.class);
        intent.putExtra("moves", list);
        startActivity(intent);
    }


}