package com.codingame.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.codingame.game.Exceptions.InvalidPosition;


public class Board {
    private Long seed;

    private Snake[] snakes;
    private Apple[] apples;

    private int width;
    private int height;
    public int NUM_SNAKES;

    private Map<Character, Position> direction;
    private int score;
    
    public Board(Long seed, int width, int height, int numSnakes, ArrayList<String> heads) {
        direction = new HashMap<Character, Position>();
        direction.put('U', Position.UP);
        direction.put('D', Position.DOWN);
        direction.put('L', Position.LEFT);
        direction.put('R', Position.RIGHT);

        this.seed = seed;
        this.score = 0;
        
        this.width = width;
        this.height = height;
        this.NUM_SNAKES = numSnakes;
        
        initSnakes(heads);
        initApples();
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public Snake getSnake(int id)
    {
        return this.snakes[id];
    }

    public Apple getApple(int id)
    {
        return this.apples[id];
    }

    public Apple getApplePos(int id)
    {
        return this.apples[id];
    }
    
    public ArrayList<String> getInitialInput()
    {
        /*
         * Sequence of string to return in the first turn only
         */
        ArrayList<String> ans = new ArrayList<String>();
        ans.add(this.height + " " + this.width);   // size of the board in format "row col"
        return ans;
    }

    public ArrayList<String> getInput()
    {
        /*
         * Sequence of string to return every turn (game state)
         */
        ArrayList<String> ans = new ArrayList<String>();
        ans.add(String.valueOf(this.seed));         // current seed - change after every apple consumed
        ans.add(String.valueOf(this.NUM_SNAKES));   // fixed value but required to iterate over every snakes
        for (int i = 0; i < this.NUM_SNAKES; i++)
        {
            Snake s = this.snakes[i];
            Apple a = this.apples[i];

            ans.add(String.valueOf(s.getSize())); // length of the snake
            for (Position p: s.getPosition())     // add all position of the snake from head to tail
            {
                ans.add(p.toString());            // position of the body part in format "row col"
            }
            // include apple position
            ans.add(a.getPosition().toString());  // position of the related apple in format "row col"
        }
        return ans;
    }

    public int getScore() {
        return score;
    }

    public void playTurn(Character action, int id) throws Exception
    {
        Position head = snakes[id].getHead();
        Position newHead = head.add(direction.get(action));
        if (!isInBoard(newHead))
        {
            throw new InvalidPosition("Snake " + id + " is out of the board");
        }

        snakes[id].setHead(newHead);
        // check if the snake eats an apple
        for (Apple apple: apples)
        {
            if (newHead.equals(apple.getPosition()))
            {
                if (apple.getId() == id) // if the apple is the one for this snake
                {
                    addScore(id); // add score and also resetTimeout
                    this.apples[id].setPosition(spawnApple(id));  // set a new apple position
                    return; // we don't need to check other apples, remove the last tail (as the length grow)
                }
                else
                {
                    throw new Exception("Snake " + id + " ate the wrong apple");
                }
            }
        }
        // the snake did not eat an apple so we remove 1 to the timeout and shorten the snake
        snakes[id].removeLastTail();
        snakes[id].decreaseTimeout();
    }

    private Position spawnApple(int id) {
        /*
         * Position a new apple in a free position using the seed. The seed change afterward (same as 2048's optimization game)
         */
        ArrayList<Position> freeCells = new ArrayList<>();
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.height; j++) {
                boolean isFree = true;
                Position p = new Position(i, j);
                for (Snake s: snakes)
                {
                    if (s.occupy(p))
                    {
                        isFree = false;
                        break;
                    } 
                }
                for (Apple a: apples)
                {
                    // there is no need to check if the apple is the same as the now one ate 
                    // when we spawn a new apple, the snake's head occupy the position
                    if (a.occupy(p)) 
                    {
                        isFree = false;
                        break;
                    } 
                }

                if (isFree) freeCells.add(p);
            }
        }

        Position newPos = freeCells.get((int)(seed % freeCells.size()));
        seed = seed * seed % 50515093L;
        return newPos;
    }

    private boolean isInBoard(Position pos)
    {
        return (pos.row >= 0) && (pos.row < height) && (pos.col >= 0) && (pos.col < width);
    }

    private void initSnakes(ArrayList<String> heads)
    {
        this.snakes = new Snake[this.NUM_SNAKES];
        for (int i =0; i < this.NUM_SNAKES; i++)
        {
            String[] snakeInfo = heads.get(i).split(" "); // input string from the test -- format "row col direction" like "5 10 D"
            int row = Integer.parseInt(snakeInfo[0]);
            int col = Integer.parseInt(snakeInfo[1]);
            String dir = snakeInfo[2];
            this.snakes[i] = new Snake(i, new Position(row, col), dir);
            this.snakes[i].resetTimeout(this.width * this.height);
        }
    }

    private void initApples()
    {
        this.apples = new Apple[this.NUM_SNAKES];
        for (int i =0; i < this.NUM_SNAKES; i++)
        {
            // At this stage the apple does not have position
            // I prefer the game to be responsible to spawn position and not the object itself
            this.apples[i] = new Apple(i);
        }
        // new both object are intantiated, we can set a position( we need to have intanciated object in the spawnApple function)
        for (int i =0; i < this.NUM_SNAKES; i++)
        {
            this.apples[i].setPosition(spawnApple(i));
        }
    }

    private void addScore(int id)
    {
        this.score += this.snakes[id].getTimeout();
        this.snakes[id].resetTimeout(this.width * this.height);
    }
}
