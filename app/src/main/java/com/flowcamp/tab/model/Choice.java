package com.flowcamp.tab.model;

public class Choice {
    private int row;
    private int column;
    private int score;

    public Choice(int row, int column, int score) {
        this.row = row;
        this.column = column;
        this.score = score;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getScore() {
        return score;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
