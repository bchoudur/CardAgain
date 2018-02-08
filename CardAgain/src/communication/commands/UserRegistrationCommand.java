package communication.commands;

/* The UserRegistrationCommand will be sent when a new user registers.
 * It is important to note that when you extend a command, you must
 *  override the getCommandType() method with the proper result, lest
 * you should have severe problems sending it over the network.
 */
public class UserRegistrationCommand extends LoginCommand implements iCommand{
	
	private String firstName;
	private String lastName;
	
	public UserRegistrationCommand(String firstName, String lastName, String username, String password){
		super(username, password);
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public CommandType getCommandType() {
		return CommandType.USERREGISTRATION;
	}
	
	public String getFirstName(){
		return this.firstName;
	}
	
	public String getLastName(){
		return this.lastName;
	}
}
