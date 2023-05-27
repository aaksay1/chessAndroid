package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.pieces.piece;
import com.example.myapplication.pieces.queen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class board extends View {
    private final int boardColor;
    private final Paint paint = new Paint();
    private int cellSize = getWidth()/8;
    static piece[][] chessB = new piece[8][8];
    Bitmap myBitmap = null;
    piece selectedPiece = null;
    boardSetup bs;

    boolean canUndo = true;

    static boolean white = true;
    static int oldX;
    static int oldY;

    static int newX;
    static int newY;

    static piece prevPiece = null;

    int numberOfMoves = 1;

    boolean illegalMove = false;

    boolean gameIsPlayed = true;

    ArrayList<String> movesList = new ArrayList<>();

    piece justCaptured = null;


    public board(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        numberOfMoves = 1;
        white = true;

        oldX = -1;
        oldY = -1;
        newX = -1;
        newY = -1;

        chessBoard b = new chessBoard();
        b.createBoard(chessB);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.board, 0, 0);

        try{
            boardColor = a.getInteger(R.styleable.board_boardColor, 0);

        }finally{
            a.recycle();
        }

    }

    public void setBS(boardSetup bs) {
        this.bs = bs;
    }

    public void draw() { // have to add an option for saving this game
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Draw");
        builder.setMessage("This game is a draw, nobody won");
        movesList.add("draw");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Dismiss the alert when the "OK" button is clicked
                gameOver(bs);

                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void resign() { // had to add an option to save this game

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Resign");

        movesList.add("resign");

        if(white) {
            builder.setMessage("White resigned, Black Wins!");
            movesList.add("black");
        } else {
            builder.setMessage("Black resigned, White Wins!");
            movesList.add("white");
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Dismiss the alert when the "OK" button is clicked
                gameOver(bs);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void ai() {
        oldX = -1;
        oldY = -1;
        newX = -1;
        newY = -1;
        invalidate();

        canUndo = true;

        List<piece> list = new ArrayList<>();

        char c;

        if(white) {
            c = 'w';
        } else {
            c = 'b';
        }

        for(int i = 0; i < chessB.length; i++) {
            for(int j = 0; j < chessB[i].length; j++) {

                if((chessB[i][j] != null) && (chessB[i][j].getSign().charAt(0) == c)) { // not null and same color
                    list.add(chessB[i][j]);
                }
            }
        }

        List<String> moveList = new ArrayList<>();


        for(int i = 0; i < list.size(); i++) {
            for(int j = 0; j < chessB.length; j++) {
                for(int k = 0; k < chessB[j].length; k++) {

                    piece p = list.get(i);

                    if(p.isValidMove(p.getX(), p.getY(), j, k, chessB, prevPiece) && !putsKingInCheck(p, p.getX(), p.getY(), j, k)) {
                        String s = Integer.toString(p.getX())+Integer.toString(p.getY()) + Integer.toString(j)+Integer.toString(k);
                        moveList.add(s);

                    }
                }
            }
        }

        Random random = new Random();

        if(moveList.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle("There are no possible moves!");
            builder.setMessage("Game Over");
            movesList.add("end");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    gameOver(bs);
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            return;
        }

        int randomNum = random.nextInt(moveList.size());

        String m = moveList.get(randomNum);
        int oX = Character.getNumericValue(m.charAt(0));
        int oY = Character.getNumericValue(m.charAt(1));

        int nX = Character.getNumericValue(m.charAt(2));
        int nY = Character.getNumericValue(m.charAt(3));

        piece p = chessB[oX][oY];

        if(p == null) {
            ai();
            return;
        }

        if(!p.isValidMove(oX, oY, nX, nY, chessB, prevPiece) || putsKingInCheck(p, oX, oY, nX, nY)) {
            ai();
            return;
        }

        if(chessB[nX][nY] != null) {
            justCaptured = chessB[nX][nY];
            justCaptured.setX(nX);
            justCaptured.setY(nY);
        }
        chessB[nX][nY] = p;
        chessB[oX][oY] = null;

        p.setX(nX);
        p.setY(nY);

        p.setHasMoved(true);


        if(p.getSign().charAt(1) == 'p' && (nX == 0 || nY == 7)) { // check for pawn promotion
            char ch = p.getSign().charAt(0); // get color

            p = new queen(ch+"Q", true, nX, nY, false);

        }

        if(checkMate(p, chessB)) {

            gameIsPlayed = false;

            String s ="";

            if(white) {
                s = "White";
            } else {
                s = "Black";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle("Checkmate");
            builder.setMessage("Game over " + s +" won!");
            movesList.add(s.toLowerCase());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    gameOver(bs);
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }

        if(gameIsPlayed && check(p, chessB)) {

            String s = "";

            if(white) {
                s = "Black";
            } else {
                s = "White";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle("Check");
            builder.setMessage(s+" King is in Check!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Dismiss the alert when the "OK" button is clicked
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }

        movesList.add(m);
        white = !(white);
        numberOfMoves++;
        prevPiece = p;
        invalidate();

    }

    public void undo() {

        if(!canUndo) { // no moves to undo
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle("You can't undo");
            builder.setMessage("You can't undo, you can only undo once before making another move!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Dismiss the alert when the "OK" button is clicked
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        if(movesList.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle("You can't undo");
            builder.setMessage("You haven't made a move yet");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Dismiss the alert when the "OK" button is clicked
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;

        }

        white = !(white);

        piece pToRemove = chessB[Character.getNumericValue(movesList.get(movesList.size()-1).charAt(2))][Character.getNumericValue(movesList.get(movesList.size()-1).charAt(3))];
        pToRemove.setHasMoved(false);


        String moveToRepeat = movesList.get(movesList.size()-1);

        piece p = chessB[Character.getNumericValue(moveToRepeat.charAt(2))][Character.getNumericValue(moveToRepeat.charAt(3))];
        chessB[Character.getNumericValue(moveToRepeat.charAt(0))][Character.getNumericValue(moveToRepeat.charAt(1))] = p;
        chessB[Character.getNumericValue(moveToRepeat.charAt(2))][Character.getNumericValue(moveToRepeat.charAt(3))] = null;

        if(justCaptured != null) {
            if(justCaptured.getX() == Character.getNumericValue(moveToRepeat.charAt(2)) && justCaptured.getY() == Character.getNumericValue(moveToRepeat.charAt(3))) {
                chessB[Character.getNumericValue(moveToRepeat.charAt(2))][Character.getNumericValue(moveToRepeat.charAt(3))] = justCaptured;
            }
        }

        p.setX(Character.getNumericValue(moveToRepeat.charAt(0)));
        p.setY(Character.getNumericValue(moveToRepeat.charAt(1)));

        movesList.remove(movesList.get(movesList.size()-1));
        numberOfMoves--;
        prevPiece = p;
        canUndo = false;
        oldX = -1;
        oldY = -1;
        newX = -1;
        newY = -1;
        invalidate();

    }


    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        int dimension = Math.min(getMeasuredWidth(), getMeasuredHeight());
        cellSize = dimension/8;
        setMeasuredDimension(dimension, dimension);
    }

   @Override
    public void onDraw(Canvas canvas){
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        drawGameBoard(canvas);
        drawGamePieces(canvas);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();

        illegalMove = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                if(selectedPiece == null) {
                    oldX = touchX/cellSize;
                    oldY = touchY/cellSize;
                    newX = -1;
                    newY = -1;

                    if(chessB[oldX][oldY] != null) {
                        invalidate();
                        selectedPiece = chessB[oldX][oldY];

                    } else {
                        //error handling for empty chess piece
                        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                        builder.setTitle("Empty Square");
                        builder.setMessage("You clicked on an empty square!");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Dismiss the alert when the "OK" button is clicked
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        oldX = -1;
                        oldY = -1;
                        newX = -1;
                        newY = -1;
                        invalidate();
                    }

                } else {
                    newX = touchX/cellSize;
                    newY = touchY/cellSize;

                    if (!selectedPiece.isValidMove(oldX, oldY, newX, newY, chessB, prevPiece) || putsKingInCheck(selectedPiece, oldX, oldY, newX, newY) /*|| !isRightColor(white, selectedPiece)*/) {
                        illegalMove = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                        builder.setTitle("Illegal Move");
                        builder.setMessage("Please do another move");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Dismiss the alert when the "OK" button is clicked
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    if(!illegalMove) {
                        if(selectedPiece.getSign().charAt(1) == 'p' && (newY == 0 || newY == 7)) { // check for pawn promotion
                            char c = selectedPiece.getSign().charAt(0); // get color

                            selectedPiece = new queen(c+"Q", true, newX, newY, false);

                        }

                        numberOfMoves++;
                        selectedPiece.setHasMoved(true);

                        if(chessB[newX][newY] != null) {
                            justCaptured = chessB[newX][newY];
                            justCaptured.setX(newX);
                            justCaptured.setY(newY);
                        }

                        chessB[newX][newY] = selectedPiece;
                        chessB[oldX][oldY] = null;

                        selectedPiece.setX(newX);
                        selectedPiece.setY(newY);
                        movesList.add(new String(Integer.toString(oldX) + Integer.toString(oldY)+Integer.toString(newX) + Integer.toString(newY))); // for undo and to record

                        invalidate();


                        if(checkMate(selectedPiece, chessB)) {
                            gameIsPlayed = false;

                            String s ="";

                            if(white) {
                                s = "White";
                            } else {
                                s = "Black";
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                            builder.setTitle("Checkmate");
                            builder.setMessage("Game over " + s +" won!");
                            movesList.add(s.toLowerCase());
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    gameOver(bs);
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }

                        if(gameIsPlayed && check(selectedPiece, chessB)) {

                            String s = "";

                            if(white) {
                                s = "Black";
                            } else {
                                s = "White";
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                            builder.setTitle("Check");
                            builder.setMessage(s+" King is in Check!");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Dismiss the alert when the "OK" button is clicked
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }

                        if(numberOfMoves % 2 == 0) {
                            white = false;
                        } else {
                            white = true;
                        }

                        prevPiece = selectedPiece;
                        canUndo = true;
                        invalidate();
                    }

                    if(illegalMove) {
                        oldX = -1;
                        oldY = -1;
                        newX = -1;
                        newY = -1;
                    }
                    invalidate();

                    selectedPiece = null;
                }
        }

        return true;
    }

    public void drawGamePieces(Canvas canvas) {

        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                if(chessB[x][y] != null) {
                    int row = chessB[x][y].getX();
                    int col = chessB[x][y].getY();
                    Rect dstRect = new Rect(cellSize * row, cellSize * col, cellSize * (row + 1), cellSize * (col + 1));

                    if(chessB[x][y].getSign().equals("bR")) { // black rook
                        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.black_rook);

                    } else if(chessB[x][y].getSign().equals("bB")) { // black bishop
                        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.black_bishop);

                    } else if(chessB[x][y].getSign().equals("bN")) { // black knight
                        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.black_knight);

                    } else if(chessB[x][y].getSign().equals("bQ")) { // black queen
                        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.black_queen);

                    } else if (chessB[x][y].getSign().equals("bK")) { // black king
                        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.black_king);

                    } else if(chessB[x][y].getSign().equals("bp")) { // black pawn
                        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.black_pawn);

                    } else if(chessB[x][y].getSign().equals("wR")) {
                        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_rook);

                    } else if(chessB[x][y].getSign().equals("wB")) {
                        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_bishop);

                    } else if(chessB[x][y].getSign().equals("wN")) {
                        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_knight);

                    } else if(chessB[x][y].getSign().equals("wQ")) {
                        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_queen);

                    } else if(chessB[x][y].getSign().equals("wK")) {
                        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_king);

                    } else if(chessB[x][y].getSign().equals("wp")) {
                        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_pawn);
                    }

                    if(myBitmap != null) {
                        canvas.drawBitmap(myBitmap, null, dstRect, null);
                    }


                }

            }
        }

    }


    public void drawGameBoard(Canvas canvas) {

        paint.setColor(boardColor);
        boolean whitee = true;

        int wColor = Color.parseColor("#ebecd0");
        int bColor = Color.parseColor("#779656");

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {

                int x = j * cellSize;
                int y = i * cellSize;

                if(whitee) {

                    if((j == oldX && i == oldY) || (j == newX && i == newY)) {

                        paint.setColor(Color.YELLOW);
                    } else {
                        paint.setColor(wColor);
                    }

                    canvas.drawRect(x, y, x + cellSize, y + cellSize, paint);
                    whitee = false;
                } else {

                    if((j == oldX && i == oldY) || (j == newX && i == newY)) {
                        paint.setColor(Color.YELLOW);
                    } else {
                        paint.setColor(bColor);
                    }
                    canvas.drawRect(x, y, x + cellSize, y + cellSize, paint);
                    whitee = true;
                }
            }
            whitee = !(whitee);
        }

    }

    // this method checks if a checkmate is made
    public static boolean checkMate(piece p, piece[][] board) {

        if(!check(p, board)) {
            return false;
        }

        int kX = 0;
        int kY = 0;

        // gets opposite colored kings location
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] != null) {
                    piece piece = board[i][j];
                    if(piece.getSign().charAt(1) == 'K' && p.getSign().charAt(0) != piece.getSign().charAt(0)) {
                        kX = i;
                        kY = j;
                        break;
                    }
                }
            }
        }


        piece[][] hypotheticalBoard = new piece[8][8];

        // gets the hypothetical board
        for(int i = 0; i < chessB.length; i++){
            for(int j = 0; j < chessB[i].length; j++) {
                hypotheticalBoard[i][j] = chessB[i][j];
            }
        }

        if(canDefend(hypotheticalBoard[kX][kY], hypotheticalBoard)) {
            return false;
        }

        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {

                if((board[kX][kY]).isValidMove(kX, kY, x, y, board, p)) {
                    hypotheticalBoard[x][y] = board[kX][kY];
                    hypotheticalBoard[kX][kY] = null;

                    if(!check(hypotheticalBoard[x][y], hypotheticalBoard) || canDefend(hypotheticalBoard[x][y], hypotheticalBoard)) {
                        return false;
                    }
                }
            }
        }

        return true;

    }
    public static boolean check(piece p, piece[][] board) {

        int kX = 0;
        int kY = 0;

        // Find the king of the opposite color as the piece p
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] != null) {
                    piece piece = board[i][j];
                    if(piece.getSign().charAt(1) == 'K' && p.getSign().charAt(0) != piece.getSign().charAt(0)) {
                        kX = i;
                        kY = j;
                        break;
                    }
                }
            }
        }

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] != null){
                    piece piece = board[i][j];
                    if((piece.getSign().charAt(0) == p.getSign().charAt(0)) && piece.isValidMove(i, j, kX, kY, board, prevPiece)){

                        return true;
                    }
                }

            }
        }
        return false;

    }

    public static boolean canDefend(piece king, piece[][] board) {
        // checks if the king that is in check can be defended by another piece

        int kX = king.getX();
        int kY = king.getY();

        char color = king.getSign().charAt(0);

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] != null && board[i][j].getSign().charAt(0) != color){
                    piece piece = board[i][j];
                    if(piece.isValidMove(piece.getX(), piece.getY(), kX, kY, board, prevPiece)){

                        for(int x = 0; x < 8; x++) {
                            for(int y = 0; y < 8; y++) {

                                if(board[x][y] != null && board[x][y].getSign().charAt(0) == color) {
                                    piece defender = board[x][y];
                                    if(defender.isValidMove(x, y, i, j, board, prevPiece)) {
                                        return true;
                                    }
                                }
                            }
                        }

                    }
                }

            }
        }

        return false;

    }

    // this method checks if the piece is the right color meaning that it is the current color
    public static boolean isRightColor(boolean white, piece piece) {
        return ((white && piece.getSign().charAt(0) == 'w') || (!white && piece.getSign().charAt(0) == 'b'));

    }

    public static boolean putsKingInCheck(piece p, int oldX, int oldY, int newX, int newY) {

        piece[][] hypotheticalBoard = new piece[8][8];

        for(int i = 0; i < chessB.length; i++){
            for(int j = 0; j < chessB[i].length; j++) {
                hypotheticalBoard[i][j] = chessB[i][j];
            }
        }

        // Update the board with the hypothetical move
        hypotheticalBoard[newX][newY] = hypotheticalBoard[oldX][oldY];
        hypotheticalBoard[oldX][oldY] = null;

        int kX = 0;
        int kY = 0;

        // Find the king of the same color as the piece p
        for(int i = 0; i < hypotheticalBoard.length; i++) {
            for(int j = 0; j < hypotheticalBoard[i].length; j++) {
                if(hypotheticalBoard[i][j] != null) {
                    if(hypotheticalBoard[i][j].getSign().charAt(1) == 'K' && p.getSign().charAt(0) == hypotheticalBoard[i][j].getSign().charAt(0)) {
                        kX = i;
                        kY = j;
                        break;
                    }
                }
            }
        }

        // Check if the king is under attack
        for(int i = 0; i < hypotheticalBoard.length; i++) {
            for(int j = 0; j < hypotheticalBoard[i].length; j++) {
                if(hypotheticalBoard[i][j] != null && hypotheticalBoard[i][j].getSign().charAt(0) != p.getSign().charAt(0)){

                    if(hypotheticalBoard[i][j].isValidMove(i, j, kX, kY, hypotheticalBoard, prevPiece)){

                        return true;
                    }
                }

            }
        }

        return false;

    }

    public void endGame(boardSetup bs) {
        movesList.add("end");
        gameOver(bs);
    }

    public void gameOver(boardSetup bs) {

        invalidate();

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage("Would you like to record this game?")
                .setTitle("GAME OVER");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if(movesList.isEmpty()) {
                    bs.goToHomePage();
                } else {
                    bs.goToSaveGameScreen(movesList);
                }

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                bs.goToHomePage();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
