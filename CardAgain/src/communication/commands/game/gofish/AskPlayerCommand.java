package communication.commands.game.gofish;

import communication.commands.CommandType;
import communication.commands.iCommand;
import gameObjects.Card;

public class AskPlayerCommand implements iCommand {
	int playerNum;
	Card card;
	public AskPlayerCommand(int otherPlayerNum, Card card){
		playerNum = otherPlayerNum;
		this.card = card;
	}
	@Override
	public CommandType getCommandType() {
		// TODO Auto-generated method stub
		return CommandType.ASKPLAYERCOMMAND;
	}
	public int getPlayerNum(){
		return this.playerNum;
	}
	public Card getCard(){
		return this.card;
	}
}
