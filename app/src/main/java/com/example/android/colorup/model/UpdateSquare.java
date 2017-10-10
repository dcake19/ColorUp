package com.example.android.colorup.model;


public class UpdateSquare {


    private Coordinates mStartCoordinates;
    private int distance;
    private int direction;
    private boolean increase = false;

    public UpdateSquare(Coordinates startCoordinates, int distance, int direction, boolean increase) {
        mStartCoordinates = startCoordinates;
        this.distance = distance;
        this.direction = direction;
        this.increase = increase;
    }

    @Override
    public boolean equals(Object obj) {
        UpdateSquare updateShape = (UpdateSquare) obj;

        return this.increase == updateShape.getIncrease() &&
                this.direction == updateShape.getDirection() &&
                this.distance == updateShape.getDistance() &&
                this.mStartCoordinates.i == updateShape.getStartRow() &&
                this.mStartCoordinates.j == updateShape.getStartColumn();
    }

    public UpdateSquare(int i, int j) {
        mStartCoordinates = new Coordinates(i,j);
    }

//    public void setDistance(int xDistance, int yDistance){
//        this.xDistance = xDistance;
//        this.yDistance = yDistance;
//    }

    public boolean getIncrease(){
        return increase;
    }

    public int getDistance() {
        return distance;
    }

    public int getDirection() {
        return direction;
    }

    public int getStartRow(){
        return mStartCoordinates.i;
    }

    public int getStartColumn(){
        return mStartCoordinates.j;
    }

    public int getEndRow(){
        if(direction==GameBoard.DIRECTION_LEFT || direction==GameBoard.DIRECTION_RIGHT)
            return mStartCoordinates.i ;
        else
            return mStartCoordinates.i+ getSignedDistance();
    }

    public int getEndColumn(){
        if(direction==GameBoard.DIRECTION_LEFT || direction==GameBoard.DIRECTION_RIGHT)
            return mStartCoordinates.j + getSignedDistance();
        else
            return mStartCoordinates.j;
    }

    public int getSignedDistance(){
        if(direction==GameBoard.DIRECTION_LEFT || direction==GameBoard.DIRECTION_UP)
            return -1*distance;
        else
            return distance;
    }



    public String getPropertyName(){
        if(direction==GameBoard.DIRECTION_LEFT || direction==GameBoard.DIRECTION_RIGHT)
            return "translationX";
        else
            return "translationY";
    }

    // returns the column if horizontal or the row is vertical
    public int getChangingLocation(){
        if(direction== GameBoard.DIRECTION_LEFT || direction==GameBoard.DIRECTION_RIGHT)
            return mStartCoordinates.j;
        else
            return mStartCoordinates.i;
    }
}
