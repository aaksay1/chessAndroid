package com.example.myapplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class chessGameItem implements Serializable {

    //private static final long serialVersionUID = -5381145134256449565L;

    public ArrayList<String> movesList;

    public String name;

    public Calendar date;

    public chessGameItem(String name,  ArrayList<String> moves){
        this.name=name;
        this.date=Calendar.getInstance();
        this.movesList=moves;

    }

    public ArrayList<String> getMovesList() {
        return this.movesList;
    }

    public void setMovesList(ArrayList<String> movesList) {
        this.movesList = movesList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.getName();

    }
}
