package com.example.myapplication;

import com.example.myapplication.pieces.bishop;
import com.example.myapplication.pieces.king;
import com.example.myapplication.pieces.knight;
import com.example.myapplication.pieces.pawn;
import com.example.myapplication.pieces.piece;
import com.example.myapplication.pieces.queen;
import com.example.myapplication.pieces.rook;

public class chessBoard {

    public void createBoard(piece[][] board) {

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }

        board[0][1] = new pawn("bp", false, 0, 1, false);
        board[1][1] = new pawn("bp", false, 1, 1, false);
        board[2][1] = new pawn("bp", false, 2, 1, false);
        board[3][1] = new pawn("bp", false, 3, 1, false);
        board[4][1] = new pawn("bp", false, 4, 1, false);
        board[5][1] = new pawn("bp", false, 5, 1, false);
        board[6][1] = new pawn("bp", false, 6, 1, false);
        board[7][1] = new pawn("bp", false, 7, 1, false);

        board[0][0] = new rook("bR", false, 0, 0, false);
        board[1][0] = new knight("bN", false, 1, 0, false);
        board[2][0] = new bishop("bB", false, 2, 0, false);

        board[3][0] = new queen("bQ", false, 3, 0, false);
        board[4][0] = new king("bK", false, 4, 0, false);

        board[5][0] = new bishop("bB", false, 5, 0, false);
        board[6][0] = new knight("bN", false, 6, 0, false);
        board[7][0] = new rook("bR", false, 7, 0, false);

        board[0][6] = new pawn("wp", false, 0, 6, false);
        board[1][6] = new pawn("wp", false, 1, 6, false);
        board[2][6] = new pawn("wp", false, 2, 6, false);
        board[3][6] = new pawn("wp", false, 3, 6, false);
        board[4][6] = new pawn("wp", false, 4, 6, false);
        board[5][6] = new pawn("wp", false, 5, 6, false);
        board[6][6] = new pawn("wp", false, 6, 6, false);
        board[7][6] = new pawn("wp", false, 7, 6, false);

        board[0][7] = new rook("wR", false, 0, 7, false);
        board[1][7] = new knight("wN", false, 1, 7, false);
        board[2][7] = new bishop("wB", false, 2, 7, false);
        board[3][7] = new queen("wQ", false, 3, 7, false);
        board[4][7] = new king("wK", false, 4, 7, false);
        board[5][7] = new bishop("wB", false, 5, 7, false);
        board[6][7] = new knight("wN", false, 6, 7, false);
        board[7][7] = new rook("wR", false, 7, 7, false);

    }
}
