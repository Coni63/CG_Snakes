package com.codingame.game;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class Snake {
    private Deque<Position> snake;   // queue with snakes's head first followed by tail. 
    private int timeout;             // each snake need to eat withing a given number of turns
    private int id;

    public Snake(int id, Position head, String dir)
    {
        this.id = id;
        // intanciate a 3-tile long snake in direction "dir"
        this.snake = new LinkedList<Position>();
        this.snake.add(head);
        if (dir.equals("U"))
        {
            this.snake.add(head.add(new Position(1, 0)));
            this.snake.add(head.add(new Position(2, 0)));
        }
        else if (dir.equals("D"))
        {
            this.snake.add(head.add(new Position(-1, 0)));
            this.snake.add(head.add(new Position(-2, 0)));
        }
        else if (dir.equals("L"))
        {
            this.snake.add(head.add(new Position(0, 1)));
            this.snake.add(head.add(new Position(0, 2)));
        }
        else
        {
            this.snake.add(head.add(new Position(0, -1)));
            this.snake.add(head.add(new Position(0, -2)));
        }
    }

    public int getId()
    {
        return this.id;
    }

    public Position getHead()
    {
        return this.snake.getFirst(); // don't remove the Position
    }

    public void setHead(Position p)
    {
        this.snake.addFirst(p);
    }

    public void removeLastTail()
    {
        this.snake.removeLast();
    }

    public int getSize()
    {
        return this.snake.size();
    }

    public ArrayList<Position> getPosition()
    {
        return new ArrayList<Position>(this.snake);
    }

    public boolean occupy(Position p)
    {
        return occupy(p, false);
    }

    public boolean occupy(Position p, boolean skipHead)
    {
        // check if the snake occupy a given position
        // skipLast is used to forget last position
        // this is required when we check the snake's head with his body when we move.
        // the head can pass when the queue ends at the next turn
        ArrayList<Position> pos = getPosition();
        int offset = skipHead ? 1 : 0;
        for (int i = offset; i < pos.size(); i++)
        {
            if (pos.get(i).equals(p)) return true;
        }
        return false;
    }

    public void resetTimeout(int value)
    {
        this.timeout = value;
    }

    public int getTimeout()
    {
        return this.timeout;
    }

    public void decreaseTimeout()
    {
        this.timeout--;
    }

    public boolean biteSnake(Snake other)
    {
        Position head = getHead();
        // if we check the position of the head with the same snake, we need to skip the head otherwise it is always true
        return other.occupy(head, other.equals(this));
    }
}
