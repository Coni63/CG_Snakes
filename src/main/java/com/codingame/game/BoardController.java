package com.codingame.game;

import java.util.ArrayList;
import java.util.Random;

import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;

public class BoardController {
    private GraphicEntityModule graphicEntityModule;
    private TooltipModule tooltips;
    private Board board;
    private Sprite[][][] tiles;
    private int offsetX = 0;
    private int offsetY = 0;
    private String[][] tilesets = new String[4][];
    private String[] grass;
    private String[] sand;

    public BoardController(GraphicEntityModule graphicEntityModule, Board board, TooltipModule tooltips){
        this.graphicEntityModule = graphicEntityModule;
        this.tooltips = tooltips;
        this.board = board;

        this.tiles = new Sprite[4][board.getHeight()][board.getWidth()];  // contains tiles where the snake can move

        // each snake have a different color from a separated tileset
        tilesets[0] = loadTileset("snake_green.png");
        tilesets[1] = loadTileset("snake_blue.png");
        tilesets[2] = loadTileset("snake_red.png");
        tilesets[3] = loadTileset("snake_yellow.png");

        // for the grass, I use CG's asset which are 128x128 but the game is 64x64 so I split in 4 the tileset and randomly pick one on rendering
        this.grass = graphicEntityModule.createSpriteSheetSplitter()
            .setSourceImage("tileGrass.png")
            .setImageCount(4)
            .setWidth(Constants.TILE_WIDTH)
            .setHeight(Constants.TILE_WIDTH)
            .setOrigRow(0)
            .setOrigCol(0)
            .setImagesPerRow(2)
            .setName("grass")
            .split();

        this.sand = graphicEntityModule.createSpriteSheetSplitter()
            .setSourceImage("tileSand.png")
            .setImageCount(4)
            .setWidth(Constants.TILE_WIDTH)
            .setHeight(Constants.TILE_WIDTH)
            .setOrigRow(0)
            .setOrigCol(0)
            .setImagesPerRow(2)
            .setName("sand")
            .split();
    }

    public void init()
    {
        Random rnd = new Random();
        // offset used to center the playground
        offsetX = (graphicEntityModule.getWorld().getWidth() - (board.getWidth() * Constants.TILE_WIDTH)) / 2;
        offsetY = (graphicEntityModule.getWorld().getHeight() - (board.getHeight() * Constants.TILE_WIDTH)) / 2;

        // set background -- sand
        int nx = graphicEntityModule.getWorld().getWidth() / Constants.TILE_WIDTH;
        int ny = graphicEntityModule.getWorld().getHeight() / Constants.TILE_WIDTH;
        for (int i = 0; i < ny+1; i++)
        {
            for (int j = 0; j < nx+1; j++)
            {
                int idx = rnd.nextInt(4);
                graphicEntityModule.createSprite()
                .setImage(sand[idx])                    
                .setX(Constants.TILE_WIDTH * j)
                .setY(Constants.TILE_WIDTH * i);
            }
        }

        // set playground -- grass
        for (int i = 0; i < board.getHeight(); i++)
        {
            for (int j = 0; j < board.getWidth(); j++)
            {
                int idx = rnd.nextInt(4);
                graphicEntityModule.createSprite()
                .setImage(grass[idx])
                .setX(offsetX + Constants.TILE_WIDTH * j)
                .setY(offsetY + Constants.TILE_WIDTH * i);
            }
        }

        // set transparent tileset in the grass with positions in tooltips
        for (int i = 0; i < board.getHeight(); i++)
        {
            for (int j = 0; j < board.getWidth(); j++)
            {
                for (int k = 0; k < 4; k++)
                {
                    this.tiles[k][i][j] = graphicEntityModule.createSprite()
                    .setImage(tilesets[0][6])                    // transparent, just to setup the board
                    .setX(offsetX + Constants.TILE_WIDTH * j)
                    .setY(offsetY + Constants.TILE_WIDTH * i);
                }

                tooltips.setTooltipText(this.tiles[0][i][j], i + ", " + j);
            }
        }

        updateView(0);
    }


    public void updateView(int turn)
    {
        // first clear all the board (apples and snakes)
        for (int i = 0; i < board.getHeight(); i++)
        {
            for (int j = 0; j < board.getWidth(); j++)
            {
                for (int k = 0; k < 4; k++)
                {
                    this.tiles[k][i][j].setImage(tilesets[0][6]);   // transparent, just to clear the board
                }
            }
        }
        
        // render every snake and proper apple
        for (int i = 0; i < board.NUM_SNAKES; i++)
        {
            renderSnake(i, board.getSnake(i).getPosition(), tilesets[i]);
            // set the apple
            setTiles(i, board.getApple(i).getPosition(), 15, tilesets[i]);
        }
    }

    private void renderSnake(int layer, ArrayList<Position> snake, String[] tileset)
    {
        /*
         * Ugly function to place snake body based on the orientation
         */
        Position delta, delta2;

        // position the head in the proper direction
        delta = snake.get(1).sub(snake.get(0));
        if (delta.equals(Position.DOWN))
        {
            setTiles(layer, snake.get(0), 3, tileset);
        }
        if (delta.equals(Position.LEFT))
        {
            setTiles(layer, snake.get(0), 4, tileset);
        }
        if (delta.equals(Position.RIGHT))
        {
            setTiles(layer, snake.get(0), 8, tileset);
        }
        if (delta.equals(Position.UP))
        {
            setTiles(layer, snake.get(0), 9, tileset);
        }

        // place the body with proper direction
        for (int i = 1; i < snake.size() - 1; i++)
        {
            delta = snake.get(i).sub(snake.get(i-1));
            delta2 = snake.get(i+1).sub(snake.get(i));

            if (delta.equals(Position.UP) && delta2.equals(Position.UP))
            {
                setTiles(layer, snake.get(i), 7, tileset);
            }
            else if (delta.equals(Position.UP) && delta2.equals(Position.LEFT))
            {
                setTiles(layer, snake.get(i), 2, tileset);
            }
            else if (delta.equals(Position.UP) && delta2.equals(Position.RIGHT))
            {
                setTiles(layer, snake.get(i), 0, tileset);
            }
            else if (delta.equals(Position.LEFT) && delta2.equals(Position.LEFT))
            {
                setTiles(layer, snake.get(i), 1, tileset);
            }
            else if (delta.equals(Position.LEFT) && delta2.equals(Position.UP))
            {
                setTiles(layer, snake.get(i), 5, tileset);
            }
            else if (delta.equals(Position.LEFT) && delta2.equals(Position.DOWN))
            {
                setTiles(layer, snake.get(i), 0, tileset);
            }
            else if (delta.equals(Position.DOWN) && delta2.equals(Position.DOWN))
            {
                setTiles(layer, snake.get(i), 7, tileset);
            }
            else if (delta.equals(Position.DOWN) && delta2.equals(Position.LEFT))
            {
                setTiles(layer, snake.get(i), 12, tileset);
            }
            else if (delta.equals(Position.DOWN) && delta2.equals(Position.RIGHT))
            {
                setTiles(layer, snake.get(i), 5, tileset);
            }
            else if (delta.equals(Position.RIGHT) && delta2.equals(Position.RIGHT))
            {
                setTiles(layer, snake.get(i), 1, tileset);
            }
            else if (delta.equals(Position.RIGHT) && delta2.equals(Position.DOWN))
            {
                setTiles(layer, snake.get(i), 2, tileset);
            }
            else if (delta.equals(Position.RIGHT) && delta2.equals(Position.UP))
            {
                setTiles(layer, snake.get(i), 12, tileset);
            }

        }

        // place the queue with proper direction
        delta = snake.get(snake.size()-1).sub(snake.get(snake.size()-2));
        if (delta.equals(Position.DOWN))
        {
            setTiles(layer, snake.get(snake.size()-1), 13, tileset);
        }
        if (delta.equals(Position.LEFT))
        {
            setTiles(layer, snake.get(snake.size()-1), 14, tileset);
        }
        if (delta.equals(Position.RIGHT))
        {
            setTiles(layer, snake.get(snake.size()-1), 18, tileset);
        }
        if (delta.equals(Position.UP))
        {
            setTiles(layer, snake.get(snake.size()-1), 19, tileset);
        }
    }

    private void setTiles(int layer, Position pos, int tileIdx, String[] tileset)
    {
        this.tiles[layer][pos.row][pos.col].setImage(tileset[tileIdx]);
    }

    private String[] loadTileset(String img)
    {
        // used to load every snakes color
        return graphicEntityModule.createSpriteSheetSplitter()
            .setSourceImage(img)
            .setImageCount(20)
            .setWidth(Constants.TILE_WIDTH)
            .setHeight(Constants.TILE_WIDTH)
            .setOrigRow(0)
            .setOrigCol(0)
            .setImagesPerRow(5)
            .setName(img)
            .split();
    }

}
