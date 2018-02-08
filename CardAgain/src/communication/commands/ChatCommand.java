package communication.commands;

public class ChatCommand implements iCommand {
	private String user;
	private String message;
	
	public ChatCommand(String sender, String msg){
		this.user = sender;
		this.message = msg;
	}
	public CommandType getCommandType() {
		return CommandType.CHAT;
	}
	public String getUser(){
		return this.user;
	}
	public String getMessage(){
		return this.message;
	}
}
