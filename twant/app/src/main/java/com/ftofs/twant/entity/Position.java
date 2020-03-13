package com.ftofs.twant.entity;

public class Position {
    public int positionId;
    public String positionName;
    public boolean isSelected;
    public int index;
    public Position(){
    }
    public Position(int positionId,String positionName,boolean isSelected,int index){
        this.positionId = positionId;
        this.positionName = positionName;
        this.isSelected = isSelected;
        this.index =index;
    }
}

