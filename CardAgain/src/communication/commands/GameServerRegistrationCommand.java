package communication.commands;

import dataModels.GameServerProperties;

public class GameServerRegistrationCommand implements iCommand {
	//This will eventually have all the game server details, like Game mode, max players, host, etc etc but for now it's just got a name
	private GameServerProperties gs;
	
	public GameServerRegistrationCommand(GameServerProperties serverDetails){
		this.gs = serverDetails;
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.GAMESERVERREGISTRATION;
	}

	public GameServerProperties getGameServerProperties() {
		return this.gs;
	}

}
