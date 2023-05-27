package com.example.myapplication.pieces;

/**
 * The Knight class extends Piece to create an instance of the Knight Piece. 
 * @author Akin Aksay, Zachary Goldring
 *
 */

public class knight extends piece {

    public knight(String sign, boolean hasMoved, int x, int y, boolean enpassantable) {
        super(sign, hasMoved, x, y, enpassantable);
        //TODO Auto-generated constructor stub
    }

    @Override
    public boolean isValidMove(int oldX, int oldY, int newX, int newY, piece[][] board, piece prev) {

        if (oldX < 0 || oldX > 7 || oldY < 0 || oldY > 7 || newX < 0 || newX > 7 || newY < 0 || newY > 7) {
            return false; // out of bounds
        }
    
        int dX = Math.abs(newX - oldX);
        int dY = Math.abs(newY - oldY);

        if((dX == 1 && dY == 2) || (dX == 2 && dY == 1)) {

            return ((board[newX][newY] == null) ||(isOpponentPiece(oldX, oldY, newX, newY, board)));
        } else {
           
            return false;
            
        }
    }

    public static boolean isOpponentPiece(int oldX, int oldY, int x, int y, piece[][] board) {
        
        piece p = board[x][y];
       
        return p != null && p.getSign().charAt(0) != board[oldX][oldY].getSign().charAt(0);
    }
    
}
