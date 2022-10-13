package com.codingame.game;
import com.codingame.gameengine.core.AbstractSoloPlayer;

public class Player extends AbstractSoloPlayer {
    @Override
    public int getExpectedOutputLines() {
        // Returns the number of expected lines of outputs for a player

        // this value cannot be driven once the game is started so we need to have 1 return for the max amount of snakes
        // only the first 'numSnakes' will be processed
        return Constants.MAX_SNAKES;
    }
}
