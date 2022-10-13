package com.codingame.game;

public class Position
{
    public static Position UP = new Position(-1, 0);
    public static Position DOWN = new Position(1, 0);
    public static Position LEFT = new Position(0, -1);
    public static Position RIGHT = new Position(0, 1);

    int row;
    int col;

    public Position(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public Position add(Position other)
    {
        return new Position(row + other.row, col + other.col);
    }

    public Position sub(Position other)
    {
        return new Position(row - other.row, col - other.col);
    }

    public String toString() {
        return row + " " + col;
    }

    public boolean equals(Position other) {
        return (this.row == other.row) && (this.col == other.col);
    }
}