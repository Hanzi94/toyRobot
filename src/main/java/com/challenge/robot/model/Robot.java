package com.challenge.robot.model;

public class Robot {
    public static final int MAX_VALUE = 4;
    public static final int MIN_VALUE = 0;

    private Direction face;
    private int x;
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
        if(x >= MIN_VALUE && x <= MAX_VALUE ){
            this.x = x;
        }
    }
    public void setY(int y){
        if(y >= MIN_VALUE && y <= MAX_VALUE ){
            this.y = y;
        }
    }
    public String toString(){
        return x + "," + y + "," + face.toString();
    }

}