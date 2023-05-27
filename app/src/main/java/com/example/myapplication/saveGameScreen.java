package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class saveGameScreen extends AppCompatActivity {

    private Button save;
    private Button cancel;
    private EditText name;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_game_screen);
        list = new ArrayList<>();
        save = (Button)findViewById(R.id.saveButton);
        cancel = (Button)findViewById(R.id.cancelButton);
        name = (EditText) findViewById(R.id.gameName);

        Intent intent = getIntent();

        list=intent.getStringArrayListExtra("moves");

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(name.getText().toString() == null || name.getText().toString().length()==0){
                    Toast.makeText(saveGameScreen.this,"Name field can't be empty",Toast.LENGTH_LONG).show();
                    return;
                }

                chessGameItem newGame = new chessGameItem(name.getText().toString(), list);

                if(LoadSaveData.list == null || LoadSaveData.list.size()==0) {
                    LoadSaveData.list = new ArrayList<chessGameItem>();
                }

                LoadSaveData.list.add(newGame);

                try {
                    LoadSaveData.saveGameData();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(saveGameScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(saveGameScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}