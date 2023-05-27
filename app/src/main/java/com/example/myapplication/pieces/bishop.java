package com.example.myapplication.pieces;

/**
 * The Bishop class extends Piece to create an instance of the Bishop Piece. 
 * @author Akin Aksay, Zachary Goldring
 *
 */

public class bishop extends piece {

    public bishop(String sign, boolean hasMoved, int x, int y, boolean enpassantable) {
        super(sign, hasMoved, x, y, enpassantable);
        
    }

    @Override
    public boolean isValidMove(int oldX, int oldY, int newX, int newY, piece[][] board, piece prev) {

        if (oldX < 0 || oldX > 7 || oldY < 0 || oldY > 7 || newX < 0 || newX > 7 || newY < 0 || newY > 7) {
            return false; // out of bounds
        }
        if (oldX == newX || oldY == newY) {
            // Bishop can only move diagonally
            return false;
        }
        int dX;
        int dY;

        dX = Math.abs(newX - oldX);
        dY = Math.abs(newY - oldY);

        if(dX != dY) {
            return false;
        } 

        if(board[newX][newY] != null && board[newX][newY].sign.charAt(0) == this.sign.charAt(0)) { // there is a piece but its our piece
            return false;
        }

        int xDir = (newX - oldX) > 0 ? 1 : -1;
        int yDir = (newY - oldY) > 0 ? 1 : -1;
        int currX = oldX + xDir;
        int currY = oldY + yDir;

        while (currX != newX && currY != newY) {
            if (board[currX][currY] != null) {
                return false;
            }
    
            currX += xDir;
            currY += yDir;
        }

        if(board[newX][newY] == null || isOpponentPiece(oldX, oldY, newX, newY, board)) {
            return true;
        }

        return true;
        
    }

    public static boolean isOpponentPiece(int oldX, int oldY, int x, int y, piece[][] board) {
        
        piece p = board[x][y];
       
        return p != null && p.getSign().charAt(0) != board[oldX][oldY].getSign().charAt(0);
    }
}
