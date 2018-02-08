package communication.commands.game;

import communication.commands.CommandType;
import communication.commands.iCommand;
import gameObjects.Card;

public class DisplayCardCommand implements iCommand{
	private Card card;
	
	public DisplayCardCommand(Card cardToDisplay){
		this.card = cardToDisplay;
	}
	
	public Card getCard(){
		return this.card;
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.DISPLAYCARDCOMMAND;
	}

}
