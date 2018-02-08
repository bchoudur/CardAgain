package communication.commands;

//The LoginCommand will be sent when a user tries to login.
public class LoginCommand implements iCommand {
	
	protected String username;
	protected String password;
	
	public LoginCommand(String userName, String pass){
		super();
		this.username = userName;
		this.password = pass;
	}
	
	public CommandType getCommandType() {
		return CommandType.LOGIN;
	}
	
	//accessors and modifiers
	public String getUsername(){
		return this.username;
	}
	public String getPassword(){
		return this.password;
	}
}
