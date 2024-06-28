package com.multiplayergame.model;


import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Cell extends Rectangle {
    private boolean hasShip;
    private boolean isMine;
    private int row;
    private int col;
    private boolean isHit=false;
    private boolean isMarked=false;

    public Cell(double v, double v1) {
        super(v, v1);
        hasShip=false;
        isMine=false;


    }

    public boolean isHasShip() {
        return hasShip;
    }

    public void setHasShip(boolean hasShip) {
        this.hasShip = hasShip;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public void setRowCol(int row,int col)
    {
        this.row=row;
        this.col=col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;


    }





}
