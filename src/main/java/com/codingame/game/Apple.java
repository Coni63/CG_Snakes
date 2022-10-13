package com.codingame.game;

public class Apple {
    private Position apple;
    private int id;

    public Apple(int id)
    {
        this.id = id;
    }

    public void setPosition(Position p)
    {
        this.apple = p;
    }
    
    public Position getPosition()
    {
        return this.apple;
    }

    public boolean occupy(Position p)
    {
        // when the board spawns apples, the method checks sometimes apples with null position 
        if (apple == null)
        {
            return false; 
        } 
        return apple.equals(p);
    }

    public int getId()
    {
        return this.id;
    }
}
