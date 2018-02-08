package communication.commands.game.war;

import communication.commands.CommandType;
import communication.commands.iCommand;

public class EndWarCommand implements iCommand {

	@Override
	public CommandType getCommandType() {
		return CommandType.ENDWARCOMMAND;
	}

}
