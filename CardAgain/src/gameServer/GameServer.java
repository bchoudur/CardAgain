package gameServer;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import communication.commands.ChatCommand;
import communication.commands.CommandType;
import communication.commands.GameServerRegistrationCommand;
import communication.commands.iCommand;
import communication.commands.game.ForwardingCommand;
import communication.commands.game.StartGameCommand;
import dataModels.GameServerProperties;
import dataModels.User;
import gameObjects.Games;
import gameServer.games.goFish.Fish;
import gameServer.games.war.War;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import server.Server;

public class GameServer extends Thread{
	private GameServerProperties properties;
	private int connectedPlayers;
	private Socket ioSock;
	private String url = "http://localhost:11899";
	private Map<UUID,User> connectedUsers;
	//temp game variable
	private GameState game;
	
	public GameServer(GameServerProperties props){
		this.properties = props;
		this.connectedPlayers = 0;
		this.properties.setCurrentPlayerCount(0);
		
		connectedUsers = Collections.synchronizedMap(new LinkedHashMap<UUID,User>());
		
		//setup game properly
		switch(this.properties.getGameMode()){
		case WAR:
			game = new War(this);
			break;
		case GOFISH:
			game = new Fish(this);
			break;
		default:
			game = new War(this);
			break;
		}
	}
	
	@Override
	public void run() {
		System.out.println("Strating up Game Server " + this.getID());
		try {
			ioSock = IO.socket(url);
			//add a connection listener
			ioSock.on(ioSock.EVENT_CONNECT, new Emitter.Listener() {
				public void call(Object... arg0) {
					System.out.println("Server connected");
					ioSock.emit(CommandType.GAMESERVERREGISTRATION.getName(), utility.GsonHelper.gson.toJson(new GameServerRegistrationCommand(properties)));
				}
			});
			
			ioSock.on(CommandType.FORWARDINGCOMMAND.getName(), new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					ForwardingCommand cmd = utility.GsonHelper.gson.fromJson((String)args[0], ForwardingCommand.class);
					handleForwarding(cmd,args);
				}
			});
			
			ioSock.connect();
		} catch (URISyntaxException e){
			e.printStackTrace();
		}
	}
	
	public String getID(){
		return this.properties.getID();
	}
	
	public GameServerProperties getProperties(){
		return this.properties;
	}
	
	//connect a user if possible
	public boolean addUser(UUID uuid, User user){
		boolean added = false;
		if(this.connectedPlayers < this.properties.getMaxPlayerCount()){
			System.out.println("Adding user " + uuid);
			connectedUsers.put(uuid, user);
			added = true;
		}
		return added;
	}
	
	protected void handleForwarding(ForwardingCommand cmd,Object... args){
		String cmds = cmd.getCommand();
		String cmdType = cmd.getTypeString();
		iCommand comd = (iCommand) utility.GsonHelper.gson.fromJson(cmds, CommandType.getEnumByString(cmdType).getType());
		UUID sender = cmd.getUUID();
		switch(comd.getCommandType()){
		case PLAYERINFOCOMMAND:
			Ack ack = (Ack)args[1];
			int num = 1;
			for(UUID u : connectedUsers.keySet()){
				if(u.equals(sender)){
					break;
				}
				else{
					num++;
				}
			}
			ack.call(utility.GsonHelper.toJson(num));
			break;
		case DISPLAYOPPOSINGCARD:
			broadcastCommandToAllBut(comd, sender);
			break;
		case CHAT:
			broadcastCommandToAll(comd);
			break;
		case STARTGAMECOMMAND:
			game.startGame();
			break;
		default:
			game.handleForwarding(cmd, args);
			break;
		}
	}
	
	public Map<UUID,User> getConnectedClients(){
		return this.connectedUsers;
	}
	
	public void broadcastCommandToAll(iCommand cmd){
		for(UUID u : this.connectedUsers.keySet()){
			Server.sendCommandToClient(u, cmd);
		}
	}
	public void sendCommandToPlayer(iCommand cmd, UUID playerUUID){
		Server.getClientByUUID(playerUUID).sendEvent(cmd.getCommandType().getName(), utility.GsonHelper.toJson(cmd));
	}
	public void broadcastCommandToAllBut(iCommand command, UUID user) {
		for(UUID u : this.connectedUsers.keySet()){
			if(!u.equals(user)){
				Server.sendCommandToClient(u, command);
			}
		}
	}
	public void broadcastChat(String message){
		broadcastCommandToAll(new ChatCommand("server",message));
	}
}