package communication.commands.game.gofish;

import communication.commands.CommandType;
import communication.commands.iCommand;
import gameObjects.Hand;

public class SendHandCommand implements iCommand {
	Hand hand;
	public SendHandCommand(Hand h){
		hand = h;
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.SENDHANDCOMMAND;
	}
	public Hand getHand(){
		return hand;
	}

}
