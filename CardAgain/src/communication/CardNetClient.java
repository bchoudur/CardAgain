package communication;

import java.net.URISyntaxException;

import client.Client;
import communication.commands.*;
import communication.commands.game.*;
import dataModels.GameServerProperties;
import gui.BaseScreen;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.emitter.Emitter.Listener;
import utility.GsonHelper;

public class CardNetClient {
	
	protected Socket ioSock;
	private IO.Options opts;
	protected boolean debugModeEnabled;
	protected String forwardingID;
	String url = "http://localhost:11899";
	
	/**
	 * CardNet, a central point of networking for CardAgain.
	 */
	public CardNetClient(){
		this(false);
	}
	/**
	 * CardNetCient maintains all traffic between the host and 
	 * server It is made statically available to be called from
	 *               client.Client.cardNet
	 *               
	 * @param enableDebug boolean which, when true will allow
	 * console output.
	 */
	public CardNetClient(boolean enableDebug){
		System.out.println("Initializing CardNet Client-Side");
		this.debugModeEnabled = enableDebug;
		//set socket options
		opts = new IO.Options();
		opts.forceNew = true;
		
		try {
			System.out.println("Creating server");
			//CardNet is powered by a libraray known as Socket.IO
			//This makes for a very event based netowrking system
			//setup the server
			ioSock = IO.socket(url, opts);
			
			//add a connection listener
			ioSock.on(ioSock.EVENT_CONNECT, new Emitter.Listener() {
				public void call(Object... arg0) {System.out.println("Server connected");}
			});
			
			//connect
			System.out.println("Connecting.");			
			this.connect();
			
		} catch (URISyntaxException e) {
			System.out.println("Failed to resolve invalid URI.");
			e.printStackTrace();
		}
	}
	
	//Listeners
	
	/**
	 * Add a listener for a specific event.
	 * @param event the name of the even to listen for
	 * @param listener the listerner function to call when this event is observed
	 */
	public void addOn(String event, Listener listener){
		ioSock.on(event,listener);
	}
	public void addOnCmd(CommandType cmd, Listener listener){
		addOn(cmd.getName(),listener);
	}
	
	//Listener Clearing
	public void off(String event){
		ioSock.off(event);
	}
	public void offCmd(CommandType cmd){
		off(cmd.getName());
	}
	
	//NON-ACK emitters
	
	/**
	 * Emit a particular event to the server
	 * @param event 
	 * @param args
	 */
	public void emitCommand(iCommand cmd){
		debugLog("emitting " + cmd.getCommandType().getName() + " command.");
		String json = utility.GsonHelper.gson.toJson(cmd);
		ioSock.emit(cmd.getCommandType().getName(),json);
	}
	
	//ACK Based emitters
	
	/**
	 * Emit a particular command to the server, with an ACK callback function
	 * @param cmd
	 * @param ack
	 */
	public void emitCommand(iCommand cmd, Ack ack){
		Object arr[] = {utility.GsonHelper.gson.toJson(cmd)};
		System.out.println("Emitting " + (String)arr[0]);
		ioSock.emit(cmd.getCommandType().getName(), arr, ack);
	}
	
	//Game Server Communication
	
	/**
	 * Request the creation of a new Game Server
	 * @param properties a GameServerProperties object that will be used to configure the game
	 * @param c A callback function. This callback will recieve either the string "fail" if the server fails to create a GameServer, or a String representing the ID of the game server created. This ID should be passed to the joinGame function.
	 */
	public void createGameServer(GameServerProperties properties, Ack c){
		emitCommand(new SpawnNewGameServerCommand(properties) , c);
	}
	
	/**
	 * Method for properly connecting a player to a game.
	 * 
	 * @param gameServerID the ID of the game server to join.
	 * @return a boolean representing whether the user successfully joined the requested game
	 */
	String joinStatus;
	public boolean joinGame(String gameServerID){
		this.forwardingID = gameServerID;
		joinStatus = "";
		this.emitCommand(new JoinGameCommand(this.forwardingID, Client.authenticatedUser), new Callback(){
			@Override
			public void call(Object... args) {
				debugLog("recieved response from server");
				joinStatus = utility.GsonHelper.strFromJson(args[0]);
			}
		});
		//while(joinStatus.length()==0);
		//return joinStatus.equals("success");
		return true;
	}
	
	/**
	 * This method is used to forward commands to the Game server.
	 * It does so by wrapping the command in a ForwardingRequest
	 * command.This requries the command to be passed in as a 
	 * pre-serialized JSON string, due to GSON serialization 
	 * issues with interfaces
	 * 
	 * @param cmd a JSON string representing the command
	 * @param type the Type of command being sent, available from CommandType.COMMANDENUM.getName();
	 */
	public void forwardCommand(String cmd, String type){
		System.out.println("Forwarding '" + type + "' command " + cmd + " to " + forwardingID);
		emitCommand(new ForwardingCommand(cmd, type, forwardingID));
	}
	//sending commands to game server with an ACK callback
	public void forwardCommand(String cmd, String type, Ack ack){
		System.out.println("Forwarding '" + type + "' command " + cmd + " with callback to " + forwardingID);
		ForwardingCommand f = new ForwardingCommand(cmd,type,forwardingID);
		emitCommand(f,ack);
	}
		
	//connection management
	public void connect(){
		if(!ioSock.connected())
			ioSock.connect();
	}
	public void disconnect(){
		if(ioSock.connected())
			ioSock.disconnect();
	}
	
	//assorted utility
	private void debugLog(String s){
		if(this.debugModeEnabled)
			System.out.println(s);
	}
}
