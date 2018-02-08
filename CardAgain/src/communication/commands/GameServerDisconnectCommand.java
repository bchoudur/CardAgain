package communication.commands;

public class GameServerDisconnectCommand implements iCommand {

	@Override
	public CommandType getCommandType() {
		return CommandType.GAMESERVERDISCONNECT;
	}

}
