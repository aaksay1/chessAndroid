package com.example.myapplication.pieces;

import android.util.Log;

/**
 * The Rook class extends Piece to create an instance of the Rook Piece. 
 * @author Akin Aksay, Zachary Goldring
 *
 */

public class rook extends piece {

    public rook(String sign, boolean hasMoved, int x, int y, boolean enpassantable) {
        super(sign, hasMoved, x, y, enpassantable);
        //TODO Auto-generated constructor stub
    }

    @Override
    public boolean isValidMove(int oldX, int oldY, int newX, int newY, piece[][] board, piece prev) {

        if (oldX < 0 || oldX > 7 || oldY < 0 || oldY > 7 || newX < 0 || newX > 7 || newY < 0 || newY > 7) {
            return false; // out of bounds
        }

        if (newX != oldX && newY != oldY) { // went diagonal
            return false;
        }

        if (board[newX][newY] != null && board[newX][newY].sign.charAt(0) == this.sign.charAt(0)) {
            return false; // tries to capture teammate
        }

        if(newX == oldX) { // went up or down
            int yDir = (newY - oldY) > 0 ? 1 : -1;
            int currY = oldY + yDir;

            while(currY != newY) {

                if(currY < 0) {
                    return false;
                }

                if(board[newX][currY] != null) {
                    return false;
                }
                currY += yDir;
            }
        } else { // went left or right
            int xDir = (newX - oldX) > 0 ? 1 : -1;
            int currX = oldX + xDir;

            while(currX != newX) {
                if(board[currX][newY] != null) {
                    return false;
                }
                currX += xDir;
            }
        }

        return true;

    }

}
