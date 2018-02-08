package communication.commands.game;

import communication.commands.CommandType;
import communication.commands.iCommand;

public class EndGameCommand implements iCommand {
	
	public EndGameCommand()
	{
		
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.ENDGAMECOMMAND;
	}
	
	public String getName()
	{
		return "";
	}

}