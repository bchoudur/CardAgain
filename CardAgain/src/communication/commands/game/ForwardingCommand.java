package communication.commands.game;

import java.util.UUID;

import communication.commands.CommandType;
import communication.commands.iCommand;

public class ForwardingCommand implements iCommand{
	String cmd;
	String type;
	UUID senderUUID;
	String destinationUUID;
	
	public ForwardingCommand(String command, String type, String destinationUUID){
		this.cmd = command;
		this.type = type;
		this.destinationUUID = destinationUUID;
		System.out.println("New Forwarding command created: " + utility.GsonHelper.toJson(this));
	}
	public ForwardingCommand(String command, String type, UUID destinationUUID){
		this(command,type,destinationUUID.toString());
	}
	public String getCommand(){
		return cmd;
	}
	public String getTypeString(){
		return this.type;
	}
	public void setUUID(UUID newUUID){
		this.senderUUID = newUUID;
	}
	public UUID getUUID(){
		return this.senderUUID;
	}
	public String getDestinationUUID(){
		return (destinationUUID);
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.FORWARDINGCOMMAND;
	}
}
