package communication;

import java.net.URISyntaxException;

import communication.commands.GameServerRegistrationCommand;
import dataModels.GameServerProperties;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import utility.GsonHelper;

public class CardNetGameServer{
	protected boolean debugModeEnabled;
	protected Socket ioSock;
	protected GameServerProperties gameServerDetails;
	
	public CardNetGameServer(GameServerProperties serverDetails){
		this(serverDetails, false);
	}
	public CardNetGameServer(GameServerProperties serverDetails, boolean enableDebug){
		System.out.println("Initializing CardNet GameServerProperties");
		this.debugModeEnabled = enableDebug;
		this.gameServerDetails = serverDetails;
		//set socket options
		IO.Options opts = new IO.Options();
		opts.forceNew = true;
		try {			
			//setup the server
			ioSock = IO.socket("http://localhost:11899", opts);
			
			//add a connection listener to broadcast a registration event and set this up as a GameServerProperties
			ioSock.on(ioSock.EVENT_CONNECT, new Emitter.Listener() {
				public void call(Object... arg0) {
					System.out.println("Connected to primary server, attempting registration...");
					//register with main server and get our game server id
					GameServerRegistrationCommand gsrc = new GameServerRegistrationCommand(gameServerDetails);
					ioSock.emit(gsrc.getCommandType().getName(), GsonHelper.gson.toJson(gsrc), new Callback(){
						@Override
						public void call(Object... args) {
							String id = GsonHelper.gson.fromJson((String)args[0], String.class);
							if(id.length() > 0){
								System.out.println("Sucessfully registered with primary server, recieved id: " + id);
								gameServerDetails.setID(id);
							}
							else{
								System.out.println("Oh noes, could not initialize Game Server, check main server logs for more details.");
								System.exit(1);
							}
						}
					});	
				}
			});
			
			ioSock.on("forward", new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					handleForward(args);
				}
			});
			
			//connect
			System.out.println("Connecting to primary server...");			
			this.connect();
			
		} catch (URISyntaxException e) {
			System.out.println("Failed to resolve invalid URI.");
			e.printStackTrace();
		}
	}
	
	public void handleForward(Object... args){
		//handle a forwarding request
	}
	public void connect(){
		if(!ioSock.connected())
			ioSock.connect();
	}
}
