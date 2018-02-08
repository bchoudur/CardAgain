package dataModels;

import gameObjects.Games;

public class GameServerProperties {
	private String id;
	private String name;
	private Games gameMode;
	private String password;
	private int minNumPlayers;
	private int maxNumPlayers;
	private int currentNumPlayers;
	
	
	/**
	 * Creates a new GameServer object. This object is not an actual game server, it acts more as a configuration and tracking tool.
	 * @param id the Game Server's id
	 * @param name the name of the game server
	 * @param gamemode the gamemode the server is playing
	 * @param maxPlayers the maximum number of players allowed
	 * @param currentPlayers the current number of players connected
	 */
	public GameServerProperties(String id, String name, Games gamemode, int maxPlayers, int minimumPlayers, int currentPlayers, String password){
		this.id = id;
		this.name = name;
		this.gameMode = gamemode;
		this.password = password;
		
		this.minNumPlayers = minimumPlayers;
		this.maxNumPlayers = maxPlayers;
		this.currentNumPlayers = currentPlayers;
	}
	public GameServerProperties(String name, Games gamemode, int minimumPlayers, int maxPlayers, String password){
		this("", name, gamemode, maxPlayers, minimumPlayers, 0, password);
	}

	//accessor methods
	public String getID(){
		return this.id;
	}
	public void setID(String newID){
		this.id = newID;
	}
	public String getServerName(){
		return this.name;
	}
	public Games getGameMode(){
		return this.gameMode;
	}
	
	public String getGameModeString()
	{
		switch(this.gameMode)
		{
		case WAR:
			return "War";
		case GOFISH:
			return "GoFish";
		default:
			return "GoGish";
		}
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public int getMinPlayerCount(){
		return this.minNumPlayers;
	}
	public int getMaxPlayerCount(){
		return this.maxNumPlayers;
	}
	public int getCurrentPlayerCount(){
		return this.currentNumPlayers;
	}
	
	//Determine if the server is password protected
	public boolean isPasswordProtected(){
		return this.getPassword().length() > 0;
	}
	
	//methods for managing player count
	public void setCurrentPlayerCount(int newCount){
		if(newCount < this.maxNumPlayers && newCount > 0){
			this.currentNumPlayers = newCount;
		}
	}
	//called when a player joins, updates player count
	public void onPlayerJoin(){
		this.setCurrentPlayerCount(this.getCurrentPlayerCount()+1);
	}
	//called when a player leaves, updates player count
	public void onPlayerLeave(){
		this.setCurrentPlayerCount(this.getCurrentPlayerCount()-1);
	}
	
}
