package communication.commands.game;

import communication.commands.CommandType;
import communication.commands.iCommand;
import gameObjects.Card;

public class DisplayOpposingCardCommand implements iCommand{
	Card c;
	public DisplayOpposingCardCommand(Card card){
		this.c = card;
	}
	public Card getCard(){
		return this.c;
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.DISPLAYOPPOSINGCARD;
	}

}
