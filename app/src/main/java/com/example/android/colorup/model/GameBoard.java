package com.example.android.colorup.model;

import com.example.android.colorup.model.Coordinates;
import com.example.android.colorup.model.UpdateSquare;

import java.util.ArrayList;
import java.util.Random;

public class GameBoard {

    public static int DIRECTION_LEFT = 1;
    public static int DIRECTION_RIGHT = 2;
    public static int DIRECTION_UP = 3;
    public static int DIRECTION_DOWN = 4;
    private int[][] board;
    private int[][] previousBoard;
    private int mLastDirection = 0;
    private int mScore = 0;
    private int mPreviousScore = 0;

    public GameBoard(){};

    public GameBoard(int rows,int columns) {
        startNewGame(rows, columns);
    }

    public GameBoard(int[][] board,int score) {
        loadGame(board, score);
    }

    public void startNewGame(int rows,int columns) {
        board = new int[rows][columns];
        previousBoard = new int[rows][columns];
    }

    public void loadGame(int[][] board,int score) {
        this.board = board;
        mScore = score;
        previousBoard = new int[board.length][board[0].length];
    }

    public Coordinates addRandomSquare(){
        ArrayList<Coordinates> freeSquares = new ArrayList<>();

        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[i].length;j++){
                if(board[i][j] == 0) {
                    freeSquares.add( new Coordinates(i,j));
                }
            }
        }

        if(freeSquares.size() != 0) {
            Coordinates  coord = getRandomSquare(freeSquares);
            board[coord.i][coord.j] = 1;

            for (int i = 0; i < board.length; i++) {
                String values = " ";
                for (int j = 0; j < board[i].length; j++) {
                    values += Integer.toString(board[i][j]) + "  ";
                }
                //Log.v("Row " + i,values);
            }
            //Log.v( " " ," ");

            return coord;
        }
        else{
            return null;
        }

    }

    public boolean gameOver(){
        // if there are any free squares return false
        for(int i=0;i<board.length;i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] == 0) return false;
            }
        }

        // if there are adjacent squares equal return false
        for(int i=0;i<board.length;i++) {
            for (int j = 0; j < board[i].length-1; j++) {
                if(board[i][j]==board[i][j+1]) return false;
            }
        }

        for (int j = 0; j < board[0].length; j++) {
            for(int i=0;i<board.length-1;i++) {
                if(board[i][j]==board[i+1][j]) return false;
            }
        }

        return true;
    }

    private Coordinates getRandomSquare(ArrayList<Coordinates> freeSquares){
        Random rand = new Random();

        return freeSquares.get(rand.nextInt(freeSquares.size()));
    }

    public int[][] getBoard() {
        return board;
    }

    public ArrayList<UpdateSquare> getUpdates(int direction){

//        setPrevioudBoard();
//        mPreviousScore = mScore;

        ArrayList<UpdateSquare> allUpdates = new ArrayList<>();

        if (direction == DIRECTION_LEFT) {
            for (int i = 0; i < board.length; i++) {
                ArrayList<Coordinates> nonZeroCoords = new ArrayList<>();
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] > 0)
                        nonZeroCoords.add(new Coordinates(i, j));
                }
                addUpdatesFromCoords(allUpdates,nonZeroCoords,direction);
            }
        }
        else if (direction == DIRECTION_RIGHT) {
            for (int i = 0; i < board.length; i++) {
                ArrayList<Coordinates> nonZeroCoords = new ArrayList<>();
                for (int j = board[i].length-1; j>=0; j--) {
                    if (board[i][j] > 0)
                        nonZeroCoords.add(new Coordinates(i, j));
                }
                addUpdatesFromCoords(allUpdates,nonZeroCoords,direction);
            }
        }
        else if (direction == DIRECTION_UP) {
            for (int j = 0; j < board[0].length; j++) {
                ArrayList<Coordinates> nonZeroCoords = new ArrayList<>();
                for (int i = 0; i < board.length; i++) {
                    if (board[i][j] > 0)
                        nonZeroCoords.add(new Coordinates(i, j));
                }
                addUpdatesFromCoords(allUpdates,nonZeroCoords, direction);
            }
        }
        else  {
            for (int j = 0; j < board[0].length; j++) {
                ArrayList<Coordinates> nonZeroCoords = new ArrayList<>();
                for (int i = board.length-1; i>=0; i--) {
                    if (board[i][j] > 0)
                        nonZeroCoords.add(new Coordinates(i, j));
                }
                addUpdatesFromCoords(allUpdates,nonZeroCoords, direction);
            }
        }

        if(allUpdates.size()>0){
            setPrevioudBoard();
            mPreviousScore = mScore;
            updateBoard(allUpdates,direction);
        }

        return allUpdates;
    }

    private void setPrevioudBoard(){
        for(int i=0;i<board.length;i++)
            for(int j=0;j<board[i].length;j++)
                previousBoard[i][j] = board[i][j];
    }

    public int getLastDirection() {
        return mLastDirection;
    }

    public Integer getScore() {
        return mScore;
    }

    private void updateBoard(ArrayList<UpdateSquare> allUpdates, int direction){

        mLastDirection = direction;

        int[][] originalBoard = board;
        // set true if the square has been changed and should not be set to zero
        boolean[][] squareChanged = new boolean[board.length][board[0].length];

        for(int i=0;i<allUpdates.size();i++){
            int squareValue = originalBoard[allUpdates.get(i).getStartRow()][allUpdates.get(i).getStartColumn()];
            if(allUpdates.get(i).getIncrease()){
                squareValue++;
                incrementScore(allUpdates.get(i).getStartRow(),allUpdates.get(i).getStartColumn());}

            if(!squareChanged[allUpdates.get(i).getStartRow()][allUpdates.get(i).getStartColumn()]) board[allUpdates.get(i).getStartRow()][allUpdates.get(i).getStartColumn()] = 0;

            if(direction == DIRECTION_LEFT){
                board[allUpdates.get(i).getStartRow()][allUpdates.get(i).getStartColumn()-allUpdates.get(i).getDistance()] = squareValue;
                squareChanged[allUpdates.get(i).getStartRow()][allUpdates.get(i).getStartColumn()-allUpdates.get(i).getDistance()] = true;
            }
            else if(direction == DIRECTION_RIGHT){
                board[allUpdates.get(i).getStartRow()][allUpdates.get(i).getStartColumn()+allUpdates.get(i).getDistance()] = squareValue;
                squareChanged[allUpdates.get(i).getStartRow()][allUpdates.get(i).getStartColumn()+allUpdates.get(i).getDistance()] = true;
            }
            else if(direction == DIRECTION_UP){
                board[allUpdates.get(i).getStartRow()-allUpdates.get(i).getDistance()][allUpdates.get(i).getStartColumn()] = squareValue;
                squareChanged[allUpdates.get(i).getStartRow()-allUpdates.get(i).getDistance()][allUpdates.get(i).getStartColumn()] = true;
            }
            else{
                board[allUpdates.get(i).getStartRow()+allUpdates.get(i).getDistance()][allUpdates.get(i).getStartColumn()] = squareValue;
                squareChanged[allUpdates.get(i).getStartRow()+allUpdates.get(i).getDistance()][allUpdates.get(i).getStartColumn()] = true;
            }
        }

    }

    private void incrementScore(int row,int column){
        mScore += board[row][column];
    }

    private void addUpdatesFromCoords(ArrayList<UpdateSquare> allUpdates, ArrayList<Coordinates> nonZeroCoords, int direction){
        int mergeCount = 0;
        boolean mergeNextEqual = true;
        for (int k = 0; k < nonZeroCoords.size(); k++) {
            boolean increaseNumber = false;
            if(k > 0) {
                if(mergeNextEqual){
                    increaseNumber = board[nonZeroCoords.get(k-1).i][nonZeroCoords.get(k-1).j] == board[nonZeroCoords.get(k).i][nonZeroCoords.get(k).j];
                    if (increaseNumber) {
                        mergeNextEqual = false;
                        mergeCount++;
                    }
                } else if (!mergeNextEqual) {
                    mergeNextEqual = true;
                }
            }

            int distance;
            if(direction == DIRECTION_LEFT )
                distance = nonZeroCoords.get(k).j - k + mergeCount;
            else if(direction == DIRECTION_RIGHT)
                distance = board[0].length - 1 - nonZeroCoords.get(k).j - k + mergeCount;
            else if(direction == DIRECTION_UP)
                distance = nonZeroCoords.get(k).i - k + mergeCount;
            else
                distance = board.length - 1 - nonZeroCoords.get(k).i - k + mergeCount;

            if(distance > 0)
                allUpdates.add(new UpdateSquare(nonZeroCoords.get(k),
                        distance,
                        direction,
                        increaseNumber));
        }



    }

    public void restart(){
        for(int i=0;i<board.length;i++)
            for(int j=0;j<board[i].length;j++)
                board[i][j] = 0;

        mLastDirection = 0;
        mPreviousScore = 0;
        mScore = 0;
    }

    public int[][] undo(){
        boolean changed = false;

        for(int i=0;i<board.length;i++)
            for(int j=0;j<board[i].length;j++) {
                if(board[i][j] != previousBoard[i][j])
                    changed = true;
                board[i][j] = previousBoard[i][j];
            }

        if (changed) mScore = mPreviousScore;



        return board;
    }


}
