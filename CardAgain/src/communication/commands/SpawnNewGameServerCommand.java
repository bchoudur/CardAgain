package communication.commands;

import dataModels.GameServerProperties;

public class SpawnNewGameServerCommand implements iCommand {
	//This will eventually have all the game server details, like Game mode, max players, host, etc etc but for now it's just got a name
	private GameServerProperties gs;
	
	public SpawnNewGameServerCommand(GameServerProperties serverDetails){
		this.gs = serverDetails;
	}
	
	public GameServerProperties getGameServer(){
		return this.gs;
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.SPAWNNEWGAMESERVER;
	}

	public GameServerProperties getGameServerProperties() {
		return this.gs;
	}

}
