package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class recordingSetup extends AppCompatActivity{

    private Button sortByDateButton;
    private Button sortByTitleButton;

    private Button deleteButton;
    private ListView listView;

    public static List<chessGameItem> item = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_setup);
        sortByTitleButton = (Button) findViewById(R.id.button15);
        sortByDateButton = (Button) findViewById(R.id.button16);
        listView = (ListView) findViewById(R.id.listView);

        LoadSaveData.context = getApplicationContext();
        LoadSaveData.loadGameData();


        if(LoadSaveData.list != null) {
            String[] games = new String[LoadSaveData.list.size()];

            for(int i = 0; i < LoadSaveData.list.size(); i++) {
                games[i] = LoadSaveData.list.get(i).toString();
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, games);
            listView.setAdapter(arrayAdapter);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chessGameItem gameData = LoadSaveData.list.get(i);
                Intent intent = new Intent(recordingSetup.this, gamePlayBack.class);
                intent.putExtra("moves", gameData.getMovesList());
                startActivity(intent);
            }
        });

        sortByDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(LoadSaveData.list != null) {
                    Collections.sort(LoadSaveData.list, new sortByDate());
                    String[] games1 = new String[LoadSaveData.list.size()];
                    for(int i = 0; i < LoadSaveData.list.size(); i++) {
                        games1[i] = LoadSaveData.list.get(i).getName();
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(recordingSetup.this, android.R.layout.simple_list_item_1, games1);
                    listView.setAdapter(arrayAdapter);
                } else {
                    Toast.makeText(recordingSetup.this, "No saved games to sort!", Toast.LENGTH_LONG).show();
                }
            }
        });

        sortByTitleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(LoadSaveData.list != null) {
                    Collections.sort(LoadSaveData.list, new sortByName());
                    String[] games1 = new String[LoadSaveData.list.size()];
                    for (int i = 0; i<LoadSaveData.list.size(); i++) {
                        games1[i] = LoadSaveData.list.get(i).getName();
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(recordingSetup.this, android.R.layout.simple_list_item_1, games1);
                    listView.setAdapter(arrayAdapter);
                } else {
                    Toast.makeText(recordingSetup.this,"No saved games to sort!",Toast.LENGTH_LONG).show();
                }
            }
        });


    }





    class sortByDate implements Comparator<chessGameItem> {
        public int compare(chessGameItem o1, chessGameItem o2) {
            if(o1 != null && o2 != null)
            {
                return o2.date.compareTo(o1.date);
            }
            return 0;
        }
    }

    class sortByName implements Comparator<chessGameItem> {
        public int compare(chessGameItem o1, chessGameItem o2) {
            if(o1 != null && o2 != null)
            {
                return o1.name.compareToIgnoreCase(o2.name);
            }
            return 0;

        }
    }

}