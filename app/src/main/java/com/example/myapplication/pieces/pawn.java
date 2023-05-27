package com.example.myapplication.pieces;

/**
 * The Pawn class extends Piece to create an instance of the Pawn Piece. 
 * @author Akin Aksay, Zachary Goldring
 *
 */
public class pawn extends piece {

    public pawn(String sign, boolean hasMoved, int x, int y, boolean enpassantable) {
        super(sign, hasMoved, x, y, enpassantable);
        
    }

    @Override
    public boolean isValidMove(int oldX, int oldY, int newX, int newY, piece[][] board, piece prev) {
        
		if (oldX < 0 || oldX > 7 || oldY < 0 || oldY > 7 || newX < 0 || newX > 7 || newY < 0 || newY > 7) {
            return false; // out of bounds
        }

		int deltaX;
		int deltaY;
		boolean legal = false;

        String s = this.sign;
		
		if (s.charAt(0) == 'w'){
			deltaY = -(newY-oldY);
		}else{
			deltaY = newY - oldY;
		}
		
		deltaX = Math.abs(newX-oldX);

		if (deltaY == 1 && deltaX == 0 && board[newX][newY] == null){
			legal = true;
			this.enpassantable = false;
		} else if ((deltaX == 1 && deltaY == 1)){  //pawn capture

			if(((board[newX][newY] != null) && (board[newX][newY].sign.charAt(0) != this.sign.charAt(0)))) {
				legal  = true;
				this.enpassantable = false;
			} else if(board[newX][newY] == null) {

				if(prev != null) {
					if(this.getSign().charAt(0) == 'w') {
						
						piece p = board[newX][newY+1];
						if(p == null) {
							return false;
						}
						if(p.getEnpassantable() && (p == prev)) {
							board[newX][newY+1] = null;
							legal  = true;
							this.enpassantable = false;
						}
					}else {
						piece p = board[newX][newY-1];
						if(p == null) {
							return false;
						}
						if(p.getEnpassantable() && (p == prev)) {
							board[newX][newY-1] = null;
							legal  = true;
							this.enpassantable = false;
						}
					}

				}
			}

		} else if (!hasMoved && deltaY == 2 && deltaX == 0 && board[newX][newY] == null){
			legal = true;
			this.enpassantable = true;
		
		}

        return legal;
      
    }
    
}
