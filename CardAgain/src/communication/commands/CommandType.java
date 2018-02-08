package communication.commands;

import communication.commands.game.DisplayCardCommand;
import communication.commands.game.DisplayOpposingCardCommand;
import communication.commands.game.DrawCardCommand;
import communication.commands.game.EndGameCommand;
import communication.commands.game.EndTurnCommand;
import communication.commands.game.ForwardingCommand;
import communication.commands.game.JoinGameCommand;
import communication.commands.game.PlayerInfoCommand;
import communication.commands.game.RecieveCardCommand;
import communication.commands.game.StartGameCommand;
import communication.commands.game.StartTurnCommand;
import communication.commands.game.gofish.AddPairCommand;
import communication.commands.game.gofish.AskPlayerCommand;
import communication.commands.game.gofish.GoFishCommand;
import communication.commands.game.gofish.HandCommand;
import communication.commands.game.gofish.SendHandCommand;
import communication.commands.game.war.EndWarCommand;
import communication.commands.game.war.RoundStatusCommand;
import communication.commands.game.war.StartWarCommand;

/*
 * This Enum is a listing of all possible commands.
 * 
 * When adding a new Command, an entry must be added as follows:
 * 
 * EXAMPLE(ExampleCommand.class,"example")
 * 
 * ExampleCommand.class should be the actual class of the command
 * that you'll need to deserialize to later.
 * 
 * "example" is the string that will be used to listen and send
 * the command over socket.io. No spaces.
 */
public enum CommandType {
	//add command listings here, comma seperated, semicolon after the last entry.
	//Client commands
	DEFAULT(iCommand.class,"default"),
	CHAT(ChatCommand.class,"chat"),
	LOGIN(LoginCommand.class,"login"),
	USERREGISTRATION(UserRegistrationCommand.class,"userregistration"),
	GAMELISTING(GameListingCommand.class,"gamelisting"),
	JOINGAME(JoinGameCommand.class,"joingame"),
	
	//Game Server commands
	GAMESERVERREGISTRATION(GameServerRegistrationCommand.class,"gameserverregistration"),
	SPAWNNEWGAMESERVER(SpawnNewGameServerCommand.class,"spawnnewgameserver"),
	FORWARDINGCOMMAND(ForwardingCommand.class,"forwardingcommand"),
	GAMESERVERDISCONNECT(GameServerDisconnectCommand.class,"gameserverdisconnect"),
	
	//General game commands
	DRAWCARDCOMMAND(DrawCardCommand.class,"drawcard"),
	DISPLAYCARDCOMMAND(DisplayCardCommand.class,"displaycard"),
	STARTGAMECOMMAND(StartGameCommand.class,"startgame"),
	ENDGAMECOMMAND(EndGameCommand.class,"endgame"),
	STARTTURNCOMMAND(StartTurnCommand.class,"startturn"),
	ENDTURNCOMMAND(EndTurnCommand.class,"endturn"),
	PLAYERINFOCOMMAND(PlayerInfoCommand.class,"playerinfo"),
	RECIEVECARDCOMMAND(RecieveCardCommand.class,"recievecard"),
	DISPLAYOPPOSINGCARD(DisplayOpposingCardCommand.class,"displayopposingcard"),
	
	//War commands
	STARTWARCOMMAND(StartWarCommand.class,"startwar"),
	ENDWARCOMMAND(EndWarCommand.class,"endwar"),
	ROUNDSTATUSCOMMAND(RoundStatusCommand.class,"roundstatus"),
	
	//Go Fish Commands
	SENDHANDCOMMAND(SendHandCommand.class,"sendhand"),
	ASKPLAYERCOMMAND(AskPlayerCommand.class,"askplayer"),
	GOFISHCOMMAND(GoFishCommand.class,"gofish"),
	ADDPAIRCOMMAND(AddPairCommand.class,"addpair"),
	HANDCOMMAND(HandCommand.class,"hand")
	;
	
	//internal enum properties/methods past here.
	//Enum Class Type field
	private Class<?> type;
	//Enum Class Type name
	private String name;
	//Enum Constructor
	private CommandType(Class<?> c, String s){
		this.type = c;
		this.name = s;
	}
	//accessor method for Class Type
	public Class<?> getType(){
		return this.type;
	}
	
	public String getName(){
		return this.name;
	}
	
	@Override
	public String toString(){
		return this.getName();
	}
	
	public static CommandType getEnumByCommand(iCommand command){
		Class<?> c = command.getClass();
		for(CommandType ct : CommandType.values()){
			if(c == ct.getType()){return ct;}
		}
		return CommandType.DEFAULT;
	}
	
	public static CommandType getEnumByString(String type) {
		for(CommandType ct : CommandType.values()){
			if(type.equals(ct.getName())){return ct;}
		}
		return CommandType.DEFAULT;
	}
}
