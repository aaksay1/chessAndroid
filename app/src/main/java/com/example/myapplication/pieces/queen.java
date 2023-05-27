package com.example.myapplication.pieces;

/**
 * The Queen class extends Piece to create an instance of the Queen Piece. 
 * @author Akin Aksay, Zachary Goldring
 *
 */

public class queen extends piece {

    public queen(String sign, boolean hasMoved, int x, int y, boolean enpassantable) {
        super(sign, hasMoved, x, y, enpassantable);
        //TODO Auto-generated constructor stub
    }

    @Override
    public boolean isValidMove(int oldX, int oldY, int newX, int newY, piece[][] board, piece prev) {

        if (oldX < 0 || oldX > 7 || oldY < 0 || oldY > 7 || newX < 0 || newX > 7 || newY < 0 || newY > 7) {
            return false; // out of bounds
        }
  
        int deltaX = Math.abs(newX - oldX);
        int deltaY = Math.abs(newY - oldY);

        if (deltaX != 0 && deltaY != 0 && deltaX != deltaY) {
            return false;
        }

        // Then, we need to check if there is any piece in the new position that belongs to the same color
        if (board[newX][newY] != null && board[newX][newY].sign.charAt(0) == this.sign.charAt(0)) {
            return false;
        }

        // Finally, we need to check if there are any obstacles in the path of the queen
        int xDir = 0;
        int yDir = 0;

        if (deltaX != 0 && deltaY == 0) {
            xDir = (newX - oldX) > 0 ? 1 : -1;
        } else if (deltaX == 0 && deltaY != 0) {
            yDir = (newY - oldY) > 0 ? 1 : -1;
        } else {
            xDir = (newX - oldX) > 0 ? 1 : -1;
            yDir = (newY - oldY) > 0 ? 1 : -1;
        }

        int currX = oldX + xDir;
        int currY = oldY + yDir;

        while (currX != newX || currY != newY) {
            if (board[currX][currY] != null) {
                return false;
            }

        currX += xDir;
        currY += yDir;
        }

        // If we have reached here, it means the move is valid
        return true;

        
    }

}

    

