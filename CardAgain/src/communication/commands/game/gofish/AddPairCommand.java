package communication.commands.game.gofish;

import communication.commands.CommandType;
import communication.commands.iCommand;

public class AddPairCommand implements iCommand {
	int pairCount;
	
	public AddPairCommand(int numPairs){
		this.pairCount = numPairs;
	}
	
	@Override
	public CommandType getCommandType() {
		return null;
	}

}
