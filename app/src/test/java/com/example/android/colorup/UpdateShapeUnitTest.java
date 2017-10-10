package com.example.android.colorup;

import com.example.android.colorup.model.Coordinates;
import com.example.android.colorup.model.GameBoard;
import com.example.android.colorup.model.UpdateSquare;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class UpdateShapeUnitTest {



    @Test
    public void updateShape(){
        UpdateSquare updateShape1 = new UpdateSquare(new Coordinates(2,3),2, GameBoard.DIRECTION_LEFT,false);
        assertTrue(updateShape1.getSignedDistance() == -2);
        assertTrue(updateShape1.getEndRow()==2);
        assertTrue(updateShape1.getEndColumn()==1);
        assertTrue(updateShape1.getChangingLocation()==3);

        UpdateSquare updateShape2 = new UpdateSquare(new Coordinates(2,3),2,GameBoard.DIRECTION_RIGHT,false);
        assertTrue(updateShape2.getSignedDistance() == 2);
        assertTrue(updateShape2.getEndRow()==2);
        assertTrue(updateShape2.getEndColumn()==5);
        assertTrue(updateShape2.getChangingLocation()==3);

        UpdateSquare updateShape3 = new UpdateSquare(new Coordinates(2,3),2,GameBoard.DIRECTION_UP,false);
        assertTrue(updateShape3.getSignedDistance() == -2);
        assertTrue(updateShape3.getEndRow()==0);
        assertTrue(updateShape3.getEndColumn()==3);
        assertTrue(updateShape3.getChangingLocation()==2);

        UpdateSquare updateShape4 = new UpdateSquare(new Coordinates(2,3),2,GameBoard.DIRECTION_DOWN,false);
        assertTrue(updateShape4.getSignedDistance() == 2);
        assertTrue(updateShape4.getEndRow()==4);
        assertTrue(updateShape4.getEndColumn()==3);
        assertTrue(updateShape4.getChangingLocation()==2);
    }


}
