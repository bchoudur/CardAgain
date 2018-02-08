package communication.commands.game;

import communication.commands.CommandType;
import communication.commands.iCommand;

public class EndTurnCommand implements iCommand {

	@Override
	public CommandType getCommandType() {
		return CommandType.ENDTURNCOMMAND;
	}

}
