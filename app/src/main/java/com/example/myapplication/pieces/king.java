package com.example.myapplication.pieces;

/**
 * The King class extends Piece to create an instance of the King Piece. 
 * @author Akin Aksay, Zachary Goldring
 *
 */

public class king extends piece {

    public king(String sign, boolean hasMoved, int x, int y, boolean enpassantable) {
        super(sign, hasMoved, x, y, enpassantable);
    }

    public boolean getHasMoved() {
        return this.hasMoved;
    }

    @Override
    public boolean isValidMove(int oldX, int oldY, int newX, int newY, piece[][] board, piece prev) {

        if (oldX < 0 || oldX > 7 || oldY < 0 || oldY > 7 || newX < 0 || newX > 7 || newY < 0 || newY > 7) {
            return false; // out of bounds
        }

        int deltaX = Math.abs(newX - oldX);
        int deltaY = Math.abs(newY - oldY);

        if(deltaX == 2 && deltaY == 0) {
            return (castle(oldX, oldY, newX, newY, board, prev));
           
        }

        if (1 >= deltaX && 1 >= deltaY) {
            if(board[newX][newY] == null || board[newX][newY].getSign().charAt(0) != this.getSign().charAt(0)) {
                return !isAttacked(oldX, oldY, newX, newY, board, prev);
            }

        }

        return false;
    }

    public boolean castle(int oldX, int oldY, int newX, int newY, piece[][] board, piece prev) {

        if(this.hasMoved) { // king has moved
            return false;
        }

        // if the king is in check
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] != null){
                    piece piece = board[i][j];
                    if((piece.getSign().charAt(0) != this.getSign().charAt(0)) && piece.isValidMove(piece.getX(), piece.getY(), this.getX(), this.getY(), board, prev)){

                       return false;
                    }
                } 
                 
            }
        }


        if(this.getSign().charAt(0) == 'w') { // white 

            if((newX - oldX) > 0) { // going right

                if(board[7][7] == null) {
                    return false;
                }

                if((isAttacked(oldX, oldY, 5, 7, board, null) || isAttacked(oldX, oldY, 6, 7, board, null)) || board[7][7].hasMoved || board[5][7] != null || board[6][7] != null) {
                    return false;
                } else {
                    piece rook = board[7][7];

                    board[5][7] = rook;

                    rook.setX(5);
                    rook.setY(7);

                    board[7][7] = null;
                    return true;
                }


            } else { // going left

                if(board[0][7] == null) {
                    return false;
                }

                if((isAttacked(oldX, oldY, 3, 7, board, null) || isAttacked(oldX, oldY, 2, 7, board, null)) || board[0][7].hasMoved || board[1][7] != null || board[2][7] != null || board[3][7] != null) {
                    return false;
                } else {
                    piece rook = board[0][7];

                    board[3][7] = rook;
                    rook.setX(3);
                    rook.setY(7);

                    board[0][7] = null;
                    return true;
                }

            }

        } else { //black

            if(board[7][0] == null) {
                return false;
            }

            if((newX-oldX) > 0) { // going right

                if((isAttacked(oldX, oldY, 5, 0, board, null) && isAttacked(oldX, oldY, 6, 0, board, null)) || board[7][0].hasMoved || board[5][0] != null || board[6][0] != null) {
                    return false;
                } else {

                    piece rook = board[7][0];
                    board[5][0] = rook;
                    board[7][0] = null;
                    rook.setX(5);
                    rook.setY(0);
                    return true;
                }

            } else { // going left

                if(board[0][0] == null) {
                    return false;
                }

                if((isAttacked(oldX, oldY, 3, 0, board, null) || isAttacked(oldX, oldY, 2, 0, board, null)) || board[0][0].hasMoved || board[1][0] != null || board[2][0] != null || board[3][0] != null) {
                    return false;
                } else {
                    piece rook = board[0][0];
                    board[3][0] = rook;
                    rook.setX(3);
                    rook.setY(0);
                    board[0][0] = null;
                    return true;
                }

            }

        }

       
    }
    

    public boolean isAttacked(int oldX, int oldY, int newX, int newY, piece[][] board, piece prev) {

        piece[][] hypotheticalBoard = new piece[8][8];

        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++) {
                hypotheticalBoard[i][j] = board[i][j];
            }
        }
    
        // Update the board with the hypothetical move
        hypotheticalBoard[newX][newY] = board[oldX][oldY];
        hypotheticalBoard[oldX][oldY] = null;
    
        // Check if the king is under attack
        for(int i = 0; i < hypotheticalBoard.length; i++) {
            for(int j = 0; j < hypotheticalBoard[i].length; j++) {
                if(hypotheticalBoard[i][j] != null){
                    piece piece = hypotheticalBoard[i][j];
                    if((piece.getSign().charAt(0) != board[oldX][oldY].getSign().charAt(0)) && piece.isValidMove(piece.getX(), piece.getY(), newX, newY, hypotheticalBoard, prev)){
                        
                       return true;
                    }
                } 
                 
            }
        }

    
        return false;

    }
    

}
