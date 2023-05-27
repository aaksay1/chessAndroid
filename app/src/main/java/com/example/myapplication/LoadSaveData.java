package com.example.myapplication;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoadSaveData implements Serializable {

    //private static final long serialVersionUID = 8880762940869156565L;

    public static List<chessGameItem> list;

    public static Context context;

    public static void loadGameData() {


        File f = new File(context.getFilesDir(), "data.dat");
        if (f.exists()) {
            try {
                FileInputStream fileInputStream = context.openFileInput("data.dat");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                list = (ArrayList<chessGameItem>) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
            } catch(Exception e){
                e.printStackTrace();
            }
        } else {
            list = new ArrayList<chessGameItem>();
        }

    }

    public static void saveGameData() {
        try {
            FileOutputStream fileOutputStream=context.openFileOutput("data.dat",0);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(list);
            objectOutputStream.close();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
