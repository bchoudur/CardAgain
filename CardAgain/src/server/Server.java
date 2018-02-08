package server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import communication.CardNetServer;
import communication.commands.CommandType;
import communication.commands.GameServerDisconnectCommand;
import communication.commands.GameServerRegistrationCommand;
import communication.commands.SpawnNewGameServerCommand;
import communication.commands.UserRegistrationCommand;
import communication.commands.iCommand;
import communication.commands.game.ForwardingCommand;
import communication.commands.game.JoinGameCommand;
import dataModels.GameServerProperties;
import dataModels.User;
import gameServer.GameServer;
import utility.GsonHelper;

public class Server {
	private static boolean enableDebug = true;
	public static CardNetServer cardNetServer = new CardNetServer(enableDebug);
	//currently just a map to hold game servers as they register, implementation will evolve. concurrent and thread safe
	public static ConcurrentHashMap<String, GameServer> gameServerListing = new ConcurrentHashMap<String, GameServer>();
	public static ConcurrentHashMap<String, UUID> gameServerUUIDs = new ConcurrentHashMap<String, UUID>();
	public static ConcurrentHashMap<SocketIOClient, User> userSocketListing = new ConcurrentHashMap<SocketIOClient, User>();
	
	public static void main(String[] args) {
		System.out.println("Initializing CardAgain Server Application.");
		//Start database connection
		try {
			MySQLGameAccess.init();
		} catch (SQLException e) {
			System.out.println("Error starting database connection");
			e.printStackTrace();
			System.exit(1);
		} catch (InstantiationException e) {
			System.out.println("Error starting MYSQL driver");
			e.printStackTrace();
			System.exit(1);
		} catch (IllegalAccessException e) {
			System.out.println("Error accessing MYSQL driver");
			e.printStackTrace();
			System.exit(1);
		} catch (ClassNotFoundException e) {
			System.out.println("Error finding MYSQL driver");
			e.printStackTrace();
			System.exit(1);
		}
		
		cardNetServer.getServer().addDisconnectListener(new DisconnectListener(){
			public void onDisconnect(SocketIOClient client) {
				System.out.println("Client " + client.getSessionId() + " has disconnected.");

				//check if this is a game server disconnect
				if(gameServerUUIDs.containsValue(client)){
					//This was a game server, determine their ID then remove them from the listings
					for(Entry<String, UUID> e : gameServerUUIDs.entrySet()){
						if(e.getValue().equals(client.getSessionId())){
							//Alert all connected clients that their server is being disconnected, and remove them from the room.
							System.out.println("Server " + e.getKey() + " has been disconnected. Alerting clients.");
							Collection<SocketIOClient> connectedUsers = cardNetServer.getServer().getRoomOperations(e.getKey()).getClients();
							for(SocketIOClient user : connectedUsers){
								//broadcast a server disconnect and kick them from the roomName
								cardNetServer.sendToClient(new GameServerDisconnectCommand(), user.getSessionId());
								user.leaveRoom(e.getKey());
							}
							//remove the server from the server maps.
							gameServerListing.remove(e.getKey());
							gameServerUUIDs.remove(e.getKey());
						}
					}
				}
				
				//in all cases, unmap the client from the userSocketListing
				userSocketListing.remove(client);
			}
		});
		
		/**
		 * Our Server client communications are built on a library known
		 * as socket.io. A Command based protocol is in place to ensure
		 * data shows up in the right places, and to make our lives much
		 * easier. Commands themselves are also encoded into JSON before
		 * being sent over the network.
		 * 
		 * Below is an example of how to tie all these things together.
		 * 
		 * This is a socket.io event listener, specifically the one that 
		 * handles user registration. It will sit on the server and listen 
		 * for events with some kind of a name attatched.
		 * 
		 * These listeners should always be managed via commands. The first
		 * argument will be the type of command to listen for. You can find
		 * this in the CommandType enum in communication.commands
		 * 
		 * The second parameter should be String.class, because we'll be
		 * recieving a JSON string. 
		 * 
		 * The final argument is the event listener to execute. This will
		 * usually be a new DataListener<String>(){} with an onData method.
		 */
		cardNetServer.getServer().addEventListener(CommandType.USERREGISTRATION.getName(), String.class, new DataListener<String>(){
			//this will be the function executed.
			//your arguments will be a SocketIOClient, a String, and an AckRequest. 
			public void onData(SocketIOClient client, String json, AckRequest ack) throws Exception {
				//To use GSON properly, we have to deserialize it.
				//feed GsonHelper.gson.fromJson() a JSON string, and the object class.
				UserRegistrationCommand reg = GsonHelper.gson.fromJson(json, UserRegistrationCommand.class);
				
				//From here onewards, do whatever processing you need to with the command you recieved.
				
				//Get the requested properties
				String username = reg.getUsername();
				String password = reg.getPassword();
				String firstName = reg.getFirstName();
				String lastName = reg.getLastName();
				System.out.println("Registration request recieved: " + username + ", " + password + ", " + firstName + ", " + lastName);
				
				//Parameter validation will go here
				
				//Try adding the user to the database.
				boolean userSuccessfullyRegistered = false;
				String message = "Invalid map setup";
				User objUser = null;
				HashMap<String,String> registrationResults = MySQLGameAccess.registerUser(username, password, firstName, lastName);
				if(registrationResults.containsKey("status")){
					if(registrationResults.get("status").equals("success")){
						System.out.println("User Registered");
						int userid = Integer.parseInt(registrationResults.get("userid"));
						int userRole = Integer.parseInt(registrationResults.get("userRole"));
						userSuccessfullyRegistered = true;
						message = "User added to database successfully.";
						objUser = new User(userid,username,firstName,lastName, userRole);
					}
					else{
						message = registrationResults.get("reason");
						System.out.println(message);
					}
				}
				/*
				 * ACK is shorthand for acknowledgement.
				 * Basically, it's a way of the server replying directly to the client
				 * in regards to a specific event they emitted.
				 * The client is responsible for implementing a handler for the ack on 
				 * their end. You can send back any data you want from here, just make 
				 * sure to serialize it before calling ack.sendAckData();
				 */
				//respond to user with success or failure
				if(ack.isAckRequested()){
					String jsonForAckResponse;
					if(userSuccessfullyRegistered){
						//respond with a confirmation
						jsonForAckResponse = GsonHelper.gson.toJson("success");
						String userJSON = GsonHelper.gson.toJson(objUser);
						ack.sendAckData(jsonForAckResponse,userJSON);
					}
					else{
						//respond letting the user know there was an issue.
						jsonForAckResponse = GsonHelper.gson.toJson("failure: " + message);
						ack.sendAckData(jsonForAckResponse);
					}
					
				}
			}
		});
		//default disconnect handler
		cardNetServer.getServer().addDisconnectListener(new DisconnectListener(){
			public void onDisconnect(SocketIOClient disconnectedClient) {
				Server.userSocketListing.remove(disconnectedClient);
			}
		});
		
		//Handle Login requests
		cardNetServer.getServer().addEventListener(CommandType.LOGIN.getName(), String.class, new DataListener<String>(){
			//this will be the function executed.
			//your arguments will be a SocketIOClient, a String, and an AckRequest. 
			public void onData(SocketIOClient client, String json, AckRequest ack) throws Exception {
				//To use GSON properly, we have to deserialize it.
				//feed GsonHelper.gson.fromJson() a JSON string, and the object class.
				UserRegistrationCommand reg = GsonHelper.gson.fromJson(json, UserRegistrationCommand.class);
				
				//From here onewards, do whatever processing you need to with the command you recieved.
				
				//Get the requested properties
				String username = reg.getUsername();
				String password = reg.getPassword();
				System.out.println("Login request recieved: " + username + ", " + password);
				
				//Parameter validation will go here
				
				boolean loginSuccessful = false;
				
				//Try logging in
				User objUser = MySQLGameAccess.userLogin(username, password);
				if(objUser != null)
					loginSuccessful = true;
				
				//respond to user with success or failure
				if(ack.isAckRequested()){
					String jsonForAckResponse;
					if(loginSuccessful){
						//map the user as logged in
						Server.userSocketListing.put(client, objUser);
						
						//respond with a confirmation
						jsonForAckResponse = GsonHelper.gson.toJson("success");
						String userJSON = GsonHelper.gson.toJson(objUser);
						ack.sendAckData(jsonForAckResponse,userJSON);
					}
					else{
						//respond letting the user know there was an issue.
						jsonForAckResponse = GsonHelper.gson.toJson("failure");
						ack.sendAckData(jsonForAckResponse);
					}
					
				}
			}
		});
		
		//called when a client wants to start up a new game
		cardNetServer.getServer().addEventListener(CommandType.SPAWNNEWGAMESERVER.getName(), String.class, new DataListener<String>(){
			@Override
			public void onData(SocketIOClient client, String json, AckRequest ack) throws Exception {
				SpawnNewGameServerCommand gsrc = GsonHelper.gson.fromJson(json,SpawnNewGameServerCommand.class);
				GameServerProperties gs = gsrc.getGameServerProperties();
				System.out.println("Recieved request to start a new " + gs.getGameModeString() + " server called '" + gs.getServerName() + "'");
				//generate a unique id for the game server
				String id;
				do{
					id = "gs" + ((int)(Math.random()*5000));
				}while(Server.gameServerListing.containsKey(id));
				System.out.println("Spawning " + id);
				gs.setID(id);
				//Create and launch a new GameServer
				GameServer gameServer = new GameServer(gs);
				//put the game server in a tracking map on this server
				Server.gameServerListing.put(id, gameServer);
				gameServer.run();
				
				//respond with the game server's assigned ID.
				ack.sendAckData(GsonHelper.gson.toJson(id));
			}
		});
		
		//handle game server connection requests
		cardNetServer.getServer().addEventListener(CommandType.GAMESERVERREGISTRATION.getName(), String.class, new DataListener<String>(){
			@Override
			public void onData(SocketIOClient client, String json, AckRequest ack) throws Exception {
				GameServerRegistrationCommand gsc = GsonHelper.gson.fromJson(json, GameServerRegistrationCommand.class);
				GameServerProperties gsp = gsc.getGameServerProperties();
				Server.gameServerUUIDs.put(gsp.getID(), client.getSessionId());
				System.out.println("Registered Game server " + gsp.getID() + " to client " + client.getSessionId());
				client.joinRoom(gsp.getID());
			}
		});
		
		//handle clients requesting to join a game.
		cardNetServer.getServer().addEventListener(CommandType.JOINGAME.getName(), String.class, new DataListener<String>(){
			@Override
			public void onData(SocketIOClient client, String json, AckRequest ack) throws Exception {
				JoinGameCommand jgc = utility.GsonHelper.gson.fromJson(json, JoinGameCommand.class);
				GameServer gs = Server.gameServerListing.get(jgc.getServerID());
				
				String status = "fail";
				if(!(gs==null) && gs.addUser(client.getSessionId(), jgc.getUser())){
					client.joinRoom(gs.getID());
					status = "success";
				}
				ack.sendAckData(utility.GsonHelper.toJson(status));
			}
		});
		
		//handle game listing requests from the server browser
		cardNetServer.getServer().addEventListener(CommandType.GAMELISTING.getName(), String.class, new DataListener<String>(){
			@Override
			public void onData(SocketIOClient client, String json, AckRequest ack) throws Exception {
				//create a listing of GameServer objects from GameServerProperties objects
				LinkedList<GameServerProperties> gsl = new LinkedList<GameServerProperties>();
				for(GameServer gs : Server.gameServerListing.values()){
					gsl.add(gs.getProperties());
				}
				//return the listing of GameServer's we have tracked
				ack.sendAckData(GsonHelper.toJson(gsl));
			}
		});
		
		cardNetServer.getServer().addEventListener(CommandType.FORWARDINGCOMMAND.getName(), String.class, new DataListener<String>(){
			@Override
			public void onData(SocketIOClient client, String json, AckRequest ack) throws Exception {

				//update forwarding with UUID of sender
				ForwardingCommand f = utility.GsonHelper.fromJson(json, ForwardingCommand.class);
				System.out.println("Forwarding decoded");
				f.setUUID(client.getSessionId());
				//send to the appropriate server.
				SocketIOClient s = getClientByUUID(gameServerUUIDs.get(f.getDestinationUUID()));
				if(ack.isAckRequested()){
					s.sendEvent(CommandType.FORWARDINGCOMMAND.getName(), new ForwardedAck(ack) , utility.GsonHelper.gson.toJson(f));
				}
				else{
					s.sendEvent(CommandType.FORWARDINGCOMMAND.getName(), utility.GsonHelper.gson.toJson(f));
				}
			}
		});
	}
	
	public static SocketIOClient getClientByUUID(UUID uuid){
		return cardNetServer.getClient(uuid);
	}
	
	public static void sendCommandToClient(UUID uuid, iCommand cmd){
		cardNetServer.getClient(uuid).sendEvent(cmd.getCommandType().getName(), GsonHelper.toJson(cmd));
	}
	
	
	//Private class used to forward ACK responses automatically
	private static class ForwardedAck extends AckCallback<String> {
		AckRequest a;
		public ForwardedAck(AckRequest ack) {
			super(String.class);
			a=ack;
		}
		@Override
		public void onSuccess(String result) {
			a.sendAckData(result);
		}
	}
}
