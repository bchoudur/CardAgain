package communication.commands.game.war;

import communication.commands.CommandType;
import communication.commands.iCommand;

public class RoundStatusCommand implements iCommand{
	private boolean didIWin;
	public RoundStatusCommand(boolean didThisPlayerWin){
		this.didIWin = didThisPlayerWin;
	}
	@Override
	public CommandType getCommandType() {
		// TODO Auto-generated method stub
		return CommandType.ROUNDSTATUSCOMMAND;
	}
	
	public boolean didIWinThisRound(){
		return this.didIWin;
	}

}
