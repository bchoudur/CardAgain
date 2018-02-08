package communication.commands.game.gofish;

import communication.commands.CommandType;
import communication.commands.iCommand;

public class GoFishCommand implements iCommand {
	@Override
	public CommandType getCommandType() {
		return CommandType.GOFISHCOMMAND;
	}

}
