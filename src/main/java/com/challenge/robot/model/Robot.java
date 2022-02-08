package com.challenge.robot.model;

/**
 * Entity of a toy robot
 */
public class Robot {


    /**
     * The face direction of this robot
     */
    private Direction face;

    /**
     * The x coordinate on the table
     */
    private int x;

    /**
     * The y coordinate on the table
     */
    private int y;

    public Robot(int x, int y, Direction face) {
        this.face = face;
        this.x = x;
        this.y = y;
    }
    public Direction getFace(){
        return face;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setFace(Direction face){
        this.face = face;
    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public String toString(){
        return x + "," + y + "," + face.toString();
    }

}
