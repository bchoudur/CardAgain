package communication.commands.game;

import communication.commands.CommandType;
import communication.commands.iCommand;
import gameObjects.Games;

public class StartGameCommand implements iCommand {
	Games gameType;
	int playerNum;
	int numberOfPlayers;
	String data;
	
	public StartGameCommand(int player, Games gameType, int numOfPlayers, String data){
		this.playerNum = player;
		this.gameType = gameType;
		this.numberOfPlayers = numOfPlayers;
		this.data = data;
	}
	
	public StartGameCommand(int player, Games gameType, int numOfPlayers){
		this(player,gameType,numOfPlayers,"");
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.STARTGAMECOMMAND;
	}
	
	public int getPlayerNum(){
		return this.playerNum;
	}
	public Games getGameType(){
		return this.gameType;
	}
	public int getNumberOfPlayers(){
		return this.numberOfPlayers;
	}
	public String getData(){
		return data;
	}

}
