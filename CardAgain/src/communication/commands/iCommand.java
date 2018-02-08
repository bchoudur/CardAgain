package communication.commands;
/**
 * iCommand interface is the top level interface for all commands.
 * Quite simply, it ensures that all commands will be able to 
 * designate a CommandType.
 * 
 * When adding a new Command, simply implement this interface and
 * store any data or methods you may need in the Command class
 * itself. Implementing this interface will ensure that it's easy
 * to send it over the network.
 * @author Collin Franzen
 *
 */
public interface iCommand {
	public CommandType getCommandType();
}
