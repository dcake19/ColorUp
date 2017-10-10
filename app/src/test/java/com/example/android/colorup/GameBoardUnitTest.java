package com.example.android.colorup;


import com.example.android.colorup.model.Coordinates;
import com.example.android.colorup.model.GameBoard;
import com.example.android.colorup.model.UpdateSquare;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;
import static org.junit.Assert.assertTrue;


@RunWith(PowerMockRunner.class)
@PrepareForTest(GameBoard.class)
public class GameBoardUnitTest {

    GameBoard mGameBoard;
    private String METHOD_UPDATE_BOARD = "updateBoard";

    @Before
    public void init(){
//        mGameBoard = new GameBoard(3,3);
//        mGameBoard.addRandomSquare();
//        mGameBoard.addRandomSquare();
    }

    @Test
    public void boardSetup(){

        mGameBoard = new GameBoard(3,3);
        mGameBoard.addRandomSquare();
        mGameBoard.addRandomSquare();

        int[][] board = mGameBoard.getBoard();

        assertTrue(setup(board,2));
    }

    private boolean setup(int[][] board,int squares){
        int count = 0;

        for(int i=0;i<board.length;i++) {
            for (int j = 0;j < board[i].length; j++) {
                if (board[i][j] != 0) count++;
            }
        }

        return count == squares;
    }

    @Test
    public void getUpdates(){

        MockitoAnnotations.initMocks(this);
        suppress(method(GameBoard.class,METHOD_UPDATE_BOARD));

        int[][] board = {{0,1,2},{1,1,0},{0,2,2}};
        mGameBoard = new GameBoard(board,0);

        ArrayList<UpdateSquare> updates = new ArrayList<>();
        updates.add(new UpdateSquare(new Coordinates(0,1),1,GameBoard.DIRECTION_LEFT,false));
        updates.add(new UpdateSquare(new Coordinates(0,2),1,GameBoard.DIRECTION_LEFT,false));
        updates.add(new UpdateSquare(new Coordinates(1,1),1,GameBoard.DIRECTION_LEFT,true));
        updates.add(new UpdateSquare(new Coordinates(2,1),1,GameBoard.DIRECTION_LEFT,false));
        updates.add(new UpdateSquare(new Coordinates(2,2),2,GameBoard.DIRECTION_LEFT,true));

        assertTrue(mGameBoard.getUpdates(GameBoard.DIRECTION_LEFT).equals(updates));

        ArrayList<UpdateSquare> updatesR = new ArrayList<>();
        updatesR.add(new UpdateSquare(new Coordinates(1,1),1,GameBoard.DIRECTION_RIGHT,false));
        updatesR.add(new UpdateSquare(new Coordinates(1,0),2,GameBoard.DIRECTION_RIGHT,true));
        updatesR.add(new UpdateSquare(new Coordinates(2,1),1,GameBoard.DIRECTION_RIGHT,true));

        assertTrue(mGameBoard.getUpdates(GameBoard.DIRECTION_RIGHT).equals(updatesR));

        ArrayList<UpdateSquare> updatesU = new ArrayList<>();
        updatesU.add(new UpdateSquare(new Coordinates(1,0),1,GameBoard.DIRECTION_UP,false));
        updatesU.add(new UpdateSquare(new Coordinates(1,1),1,GameBoard.DIRECTION_UP,true));
        updatesU.add(new UpdateSquare(new Coordinates(2,1),1,GameBoard.DIRECTION_UP,false));
        updatesU.add(new UpdateSquare(new Coordinates(2,2),2,GameBoard.DIRECTION_UP,true));

        assertTrue(mGameBoard.getUpdates(GameBoard.DIRECTION_UP).equals(updatesU));

        ArrayList<UpdateSquare> updatesD = new ArrayList<>();
        updatesD.add(new UpdateSquare(new Coordinates(1,0),1,GameBoard.DIRECTION_DOWN,false));
        updatesD.add(new UpdateSquare(new Coordinates(0,1),1,GameBoard.DIRECTION_DOWN,true));
        updatesD.add(new UpdateSquare(new Coordinates(0,2),2,GameBoard.DIRECTION_DOWN,true));

        assertTrue(mGameBoard.getUpdates(GameBoard.DIRECTION_DOWN).equals(updatesD));

        int[][] board2 = {{2,1,2},{1,1,1},{2,0,2}};
        GameBoard gameBoard = new GameBoard(board2,0);
        ArrayList<UpdateSquare> updates2 = new ArrayList<>();
        updates2.add(new UpdateSquare(new Coordinates(1,1),1,GameBoard.DIRECTION_LEFT,true));
        updates2.add(new UpdateSquare(new Coordinates(1,2),1,GameBoard.DIRECTION_LEFT,false));
        updates2.add(new UpdateSquare(new Coordinates(2,2),2,GameBoard.DIRECTION_LEFT,true));

        assertTrue(gameBoard.getUpdates(GameBoard.DIRECTION_LEFT).equals(updates2));

        ArrayList<UpdateSquare> updatesR2 = new ArrayList<>();
        updatesR2.add(new UpdateSquare(new Coordinates(1,1),1,GameBoard.DIRECTION_RIGHT,true));
        updatesR2.add(new UpdateSquare(new Coordinates(1,0),1,GameBoard.DIRECTION_RIGHT,false));
        updatesR2.add(new UpdateSquare(new Coordinates(2,0),2,GameBoard.DIRECTION_RIGHT,true));

        assertTrue(gameBoard.getUpdates(GameBoard.DIRECTION_RIGHT).equals(updatesR2));

        ArrayList<UpdateSquare> updatesU2 = new ArrayList<>();
        updatesU2.add(new UpdateSquare(new Coordinates(1,1),1,GameBoard.DIRECTION_UP,true));

        assertTrue(gameBoard.getUpdates(GameBoard.DIRECTION_UP).equals(updatesU2));

        ArrayList<UpdateSquare> updatesD2 = new ArrayList<>();
        updatesD2.add(new UpdateSquare(new Coordinates(1,1),1,GameBoard.DIRECTION_DOWN,false));
        updatesD2.add(new UpdateSquare(new Coordinates(0,1),2,GameBoard.DIRECTION_DOWN,true));

        assertTrue(gameBoard.getUpdates(GameBoard.DIRECTION_DOWN).equals(updatesD2));
    }

    @Test
    public void getUpdates2(){
        MockitoAnnotations.initMocks(this);
        suppress(method(GameBoard.class,METHOD_UPDATE_BOARD));

        int[][] board = {{2,1,2},{0,0,0},{0,0,0}};
        GameBoard gameBoard = new GameBoard(board,0);
        assertTrue(gameBoard.getUpdates(GameBoard.DIRECTION_LEFT).size()==0);
        assertTrue(gameBoard.getUpdates(GameBoard.DIRECTION_RIGHT).size()==0);
        assertTrue(gameBoard.getUpdates(GameBoard.DIRECTION_UP).size()==0);

        int[][] board2 = {{2,1,0},{0,0,0},{0,0,0}};
        GameBoard gameBoard2 = new GameBoard(board2,0);
        assertTrue(gameBoard2.getUpdates(GameBoard.DIRECTION_LEFT).size()==0);
        assertTrue(gameBoard2.getUpdates(GameBoard.DIRECTION_UP).size()==0);

        int[][] board3 = {{2,1,0},{1,0,0},{2,0,0}};
        GameBoard gameBoard3 = new GameBoard(board3,0);
        assertTrue(gameBoard3.getUpdates(GameBoard.DIRECTION_LEFT).size()==0);

    }

    @Test
    public void updateBoard(){
        int[][] boardLeft = {{2,1,2},{1,1,1},{2,0,2}};
        GameBoard gameBoard = new GameBoard(boardLeft,0);
        gameBoard.getUpdates(GameBoard.DIRECTION_LEFT);

        int[][] resultLeft = {{2,1,2},{2,1,0},{3,0,0}};
        assertTrue(Arrays.deepEquals(resultLeft,boardLeft));

        int[][] boardRight = {{2,1,2},{1,1,1},{2,0,2}};
        GameBoard gameBoardRight = new GameBoard(boardRight,0);
        gameBoardRight.getUpdates(GameBoard.DIRECTION_RIGHT);

        int[][] resultRight = {{2,1,2},{0,1,2},{0,0,3}};
        assertTrue(Arrays.deepEquals(resultRight,boardRight));

        int[][] boardUp = {{2,1,2},{1,1,1},{2,0,2}};
        GameBoard gameBoardUp = new GameBoard(boardUp,0);
        gameBoardUp.getUpdates(GameBoard.DIRECTION_UP);

        int[][] resultUp = {{2,2,2},{1,0,1},{2,0,2}};
        assertTrue(Arrays.deepEquals(resultUp,boardUp));

        int[][] boardDown = {{2,1,2},{1,1,1},{2,0,2}};
        GameBoard gameBoardDown = new GameBoard(boardDown,0);
        gameBoardDown.getUpdates(GameBoard.DIRECTION_DOWN);

        int[][] resultDown = {{2,0,2},{1,0,1},{2,2,2}};
        assertTrue(Arrays.deepEquals(resultDown,boardDown));

    }

    @Test
    public void gameOver(){
        int[][] board1 = {{2,1,2},{1,1,1},{2,0,2}};
        GameBoard gameBoard = new GameBoard(board1,0);
        assertFalse(gameBoard.gameOver());
        int[][] board2 = {{2,1,2},{1,1,1},{2,4,2}};
        GameBoard gameBoard2 = new GameBoard(board2,0);
        assertFalse(gameBoard2.gameOver());
        int[][] board4 = {{2,1,2},{1,4,1},{2,4,2}};
        GameBoard gameBoard4 = new GameBoard(board4,0);
        assertFalse(gameBoard4.gameOver());
        int[][] board3 = {{2,1,2},{1,6,1},{2,4,2}};
        GameBoard gameBoard3 = new GameBoard(board3,0);
        assertTrue(gameBoard3.gameOver());
    }

    @Test
    public void undo(){
        int[][] board = {{2,0,2},{1,0,1},{2,0,2}};
        int[][] board2 = {{2,0,2},{1,0,1},{2,0,2}};
        GameBoard gameBoard = new GameBoard(board,0);
        gameBoard.getUpdates(GameBoard.DIRECTION_LEFT);

        assertFalse(Arrays.deepEquals(board,board2));

        gameBoard.undo();

        assertTrue(Arrays.deepEquals(board,board2));
    }

    @Test
    public void scoreUpdate(){
        int[][] boardLeft = {{2,1,2},{1,1,1},{2,0,2}};
        GameBoard gameBoard = new GameBoard(boardLeft,0);
        gameBoard.getUpdates(GameBoard.DIRECTION_LEFT);
        assertTrue(gameBoard.getScore()==3);
    }


}
