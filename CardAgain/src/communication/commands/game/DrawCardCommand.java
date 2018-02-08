package communication.commands.game;

import communication.commands.CommandType;
import communication.commands.iCommand;

public class DrawCardCommand implements iCommand {

	@Override
	public CommandType getCommandType() {
		return CommandType.DRAWCARDCOMMAND;
	}

}
