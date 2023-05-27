package com.example.myapplication;

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
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.pieces.piece;

import java.util.ArrayList;

public class gamePlaybackBoard extends View {

    boolean resign = false;

    private final int boardColor;
    private final Paint paint = new Paint();
    private int cellSize = getWidth()/8;
    static piece[][] chessB = new piece[8][8];

    gamePlayBack GPB;

    int numberOfMoves = 0;

    static int oldX = -1;
    static int oldY = -1;

    Bitmap myBitmap = null;

    ArrayList<String> moves = new ArrayList<>();

    public void setMoves(ArrayList<String> moves) {
        this.moves = moves;
    }


    public gamePlaybackBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

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
        boolean white = true;

        int wColor = Color.parseColor("#ebecd0");
        int bColor = Color.parseColor("#779656");

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {

                int x = j * cellSize;
                int y = i * cellSize;

                if(white) {

                    if(j == oldX && i == oldY) {

                        paint.setColor(Color.YELLOW);
                    } else {
                        paint.setColor(wColor);
                    }

                    canvas.drawRect(x, y, x + cellSize, y + cellSize, paint);
                    white = false;
                } else {

                    if(j == oldX && i == oldY) {
                        paint.setColor(Color.YELLOW);
                    } else {
                        paint.setColor(bColor);
                    }
                    canvas.drawRect(x, y, x + cellSize, y + cellSize, paint);
                    white = true;
                }
            }
            white = !(white);
        }

    }

    public void nextMove() {

        if(numberOfMoves >= moves.size()) {
            homePage();
        } else {
            String move = moves.get(numberOfMoves);

            if(move.equals("draw")) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setTitle("The Game is Over");
                builder.setMessage("The Game ended in a Draw");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        GPB.getHome();
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            } else if(move.equals("resign")) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setTitle("The Game is Over");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        GPB.getHome();
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                resign = true;

            } else if(move.equals("end")) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setTitle("The Game Ended Abruptly");
                builder.setMessage("Nobody Won!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        GPB.getHome();
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            } else if(move.equals("white")) {
                if(resign) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                    builder.setTitle("The Game is Over");
                    builder.setMessage("Black Resigned, White Won");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            GPB.getHome();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                    builder.setTitle("The Game is Over");
                    builder.setMessage("White Won");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            GPB.getHome();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

            } else if(move.equals("black")) {
                if(resign) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                    builder.setTitle("The Game is Over");
                    builder.setMessage("White Resigned, Black Won!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            GPB.getHome();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                    builder.setTitle("The Game is Over");
                    builder.setMessage("Black Won!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            GPB.getHome();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

            } else {
                int oldX = Character.getNumericValue(move.charAt(0));
                int oldY = Character.getNumericValue(move.charAt(1));
                int newX = Character.getNumericValue(move.charAt(2));
                int newY = Character.getNumericValue(move.charAt(3));

                piece p = chessB[oldX][oldY];
                chessB[newX][newY] = p;
                chessB[oldX][oldY] = null;

                p.setX(newX);
                p.setY(newY);

                invalidate();

            }
            numberOfMoves++;
        }

    }


    public void setGPB(gamePlayBack GPB) {
        this.GPB = GPB;
    }

    public void homePage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("The Game is Over");
        builder.setMessage("You are going to be directed to Home Page");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                GPB.getHome();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
