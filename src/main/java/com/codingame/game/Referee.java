package com.codingame.game;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.game.Exceptions.InvalidSequenceException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.SoloGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;


public class Referee extends AbstractReferee {
    @Inject private SoloGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
    @Inject private TooltipModule tooltips;

    private BoardController boardController;
    private Board board;
    Player player;

    @Override
    public void init() {
        gameManager.setMaxTurns(Constants.MAX_TURNS);
        gameManager.setFirstTurnMaxTime(Constants.TIMELIMIT_INIT);
        gameManager.setTurnMaxTime(Constants.TIMELIMIT_TURN);
        gameManager.setFrameDuration(Constants.FRAMEDURATION);

        player = gameManager.getPlayer();
        try {
            List<String> inputs = gameManager.getTestCaseInput();
            Long seed = Long.parseLong(inputs.get(0));
            int width = Integer.parseInt(inputs.get(1));
            int height = Integer.parseInt(inputs.get(2));
            int numSnakes = Integer.parseInt(inputs.get(3));

            ArrayList<String> heads = new ArrayList<String>();
            for (int i = 0; i < numSnakes; i++)
            {
                // string (row, col, direction) like 6 5 D
                heads.add(inputs.get(4+i)); 
            }
            board = new Board(seed, width, height, numSnakes, heads);            
        } catch (Exception ex) {
            Random random = new Random();
            Long seed = random.nextLong();
            int width = Constants.MIN_COLS + random.nextInt(Constants.MAX_COLS - Constants.MIN_COLS); // random.nextInt(int, int) is not working on CG...
            int height = Constants.MIN_ROWS + random.nextInt(Constants.MAX_ROWS - Constants.MIN_ROWS);
            int numSnakes = Constants.MIN_SNAKES + random.nextInt(Constants.MAX_SNAKES - Constants.MIN_SNAKES);

            // when there is no test case (should not occurs)
            // the starting position is fixed in corner like
            /*
             *    v....<<<
             *    v.......
             *    v.......
             *    .......^
             *    .......^
             *    >>>....^
             */
            ArrayList<String> heads = new ArrayList<String>();
            ArrayList<String> predefined = new ArrayList<String>();
            predefined.add(2 + " " + 0 + " D");
            predefined.add((height-2) + " " + (width-1) + " U");
            predefined.add((height-1) + " " + 2 + " R");
            predefined.add(0 + " " + (width-2) + " L");
            for (int i = 0; i < numSnakes; i++)
            {
                heads.add(predefined.get(i));
            }
            board = new Board(seed, width, height, numSnakes, heads);
        }

        // the board is responsible to provides input even if some of them are present in inputs
        for (String line : board.getInitialInput())
        {
            player.sendInputLine(line);
        } 
        
        boardController = new BoardController(graphicEntityModule, board, tooltips);
        boardController.init();
    }

    @Override
    public void gameTurn(int turn) {
        if (turn == Constants.MAX_TURNS) {
            gameManager.putMetadata("Points", String.valueOf(board.getScore()));
            gameManager.winGame("score: " + board.getScore());
            gameManager.addToGameSummary("Maximum number of turns reached");
            return;
        }

        // send game state
        for (String line : board.getInput())
        {
            player.sendInputLine(line);
        }
        player.execute();
        

        // process output with output control
        int seqLength = 0;
        ArrayList<String> processedOutput = new ArrayList<>();
        try {
            seqLength = player.getOutputs().get(0).length(); // every output should have the same lenght for active snakes so we take the 1st output as baseline
            for (int i = 0; i < board.NUM_SNAKES; i++)
            {
                String action = player.getOutputs().get(i).toUpperCase();
                
                if (action.length() != seqLength)
                {
                    throw new InvalidSequenceException("The sequence of actions for every snake should have the same length");
                }

                if (seqLength == 0 || seqLength > Constants.MAX_OUTPUT_LENGTH)
                {
                    throw new InvalidSequenceException("The output's length per snake should be between 1 and " + Constants.MAX_OUTPUT_LENGTH + " (Current is " + seqLength + " chars long)");
                }

                if (!Pattern.matches("^[ULDR]+$", action))
                {
                    throw new InvalidSequenceException("The action '" + action + "' is not valid");
                }

                processedOutput.add(action);
            }
        } 
        catch (TimeoutException e)
        {
            gameManager.putMetadata("Points", String.valueOf(board.getScore()));
            gameManager.winGame("timeout");
            return;
        } 
        catch (InvalidSequenceException e)
        {
            gameManager.putMetadata("Points", String.valueOf(board.getScore()));
            gameManager.winGame(e.getMessage());
            return;
        }

        for (int k = 0; k < seqLength; k++)
        {
            for (int i = 0; i < board.NUM_SNAKES; i++)
            {
                Character action = processedOutput.get(i).charAt(k);
                try
                {
                    board.playTurn(action, i);
                    boardController.updateView(turn);
                }
                catch(Exception ex) // 1 of the move is invalid (out of boundary, a snake bite another snake or itseft, a snake eat a wrong apple)
                {
                    gameManager.putMetadata("Points", String.valueOf(board.getScore()));
                    gameManager.winGame(ex.getMessage());
                    return;
                }
            }

            // cehck collisions
            for (int i = 0; i < board.NUM_SNAKES; i++)
            {
                // check if the snake bites other snakes or itself
                for (int j = 0; j < board.NUM_SNAKES; j++)
                {
                    if (board.getSnake(i).biteSnake(board.getSnake(j)))
                    {
                        gameManager.putMetadata("Points", String.valueOf(board.getScore()));
                        String message = (i == j) ? "Snake " + i + " bites itself" : "Snake " + i + " bites snake " + j;
                        gameManager.winGame(message);
                        return;
                    }
                }
            }

            // check that every snakes are still valid
            for (int i = 0; i < board.NUM_SNAKES; i++)
            {
                if (board.getSnake(i).getTimeout() == 0)
                {
                    gameManager.putMetadata("Points", String.valueOf(board.getScore()));
                    gameManager.winGame("Snake " + i + " starved to death");
                    return;
                }
            }
        }

        // add extra info in the turn summary
        for (int i = 0; i < board.NUM_SNAKES; i++)
        {
            gameManager.addToGameSummary("Timeout for snake " + i + " in " + board.getSnake(i).getTimeout() + " turn(s)");
        }

    }

    @Override
    public void onEnd() {
        gameManager.addToGameSummary("Final score: " + board.getScore());
        gameManager.putMetadata("Points", String.valueOf(board.getScore()));
    }
}
