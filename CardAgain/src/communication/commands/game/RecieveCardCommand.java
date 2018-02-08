package communication.commands.game;

import communication.commands.CommandType;
import communication.commands.iCommand;
import gameObjects.Card;

public class RecieveCardCommand implements iCommand{
	private Card card;
	public RecieveCardCommand(Card c){
		this.card  = c;
	}
	public Card getCard(){
		return this.card;
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.RECIEVECARDCOMMAND;
	}
	
}
