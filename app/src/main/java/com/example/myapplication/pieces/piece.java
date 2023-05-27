package com.example.myapplication.pieces;

/**
 * The Piece class that every chess piece extends to
 * @author Akin Aksay, Zachary Goldring
 *
 */

public abstract class piece {

    protected String sign;
    protected boolean hasMoved;
    protected int x;
    protected int y;
    protected boolean enpassantable;

    public piece(String sign, boolean hasMoved, int x, int y, boolean enpassantable) {
        this.sign = sign;
        this.hasMoved = false;
        this.x = x;
        this.y = y;
        this.enpassantable = enpassantable;
    }

    public boolean getEnpassantable() {
        return enpassantable;
    }

    public int getX(){
        return x;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getY(){
        return y;
    }
    public void setY(int y){
        this.y = y;
    }

    public void setHasMoved(boolean t) {
        this.hasMoved = t;
    }

    public String getSign(){
        return sign;
    }


    public abstract boolean isValidMove(int oldX, int oldY, int newX, int newY, piece[][] board, piece prev);
    
}
