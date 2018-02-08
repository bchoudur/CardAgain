package communication.commands.game;

import communication.commands.CommandType;
import communication.commands.iCommand;
import dataModels.User;

public class JoinGameCommand implements iCommand {
	private String serverID;
	private User userInfo;
	public JoinGameCommand(String serverIDtoJoin, User user){
		this.serverID = serverIDtoJoin;
		this.userInfo = user;
	}
	public String getServerID(){
		return this.serverID;
	}
	public User getUser(){
		return this.userInfo;
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.JOINGAME;
	}

}
