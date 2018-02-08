package gameServer;

import communication.commands.game.ForwardingCommand;
import gameObjects.Games;

/**
 * 
 * @author ajdague
 * A class that holds the current gamestate of a game. This will keep track of things like player hands and current cards in play.
 *
 */
public interface GameState{
	public void handleForwarding(ForwardingCommand cmd,Object... args);
	public Games getGameType();
	public void startGame();
}
