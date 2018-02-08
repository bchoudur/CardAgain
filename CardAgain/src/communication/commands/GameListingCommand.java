package communication.commands;

public class GameListingCommand implements iCommand{
	@Override
	public CommandType getCommandType() {
		return CommandType.GAMELISTING;
	}
}
