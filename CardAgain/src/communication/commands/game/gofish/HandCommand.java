package communication.commands.game.gofish;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;

import communication.commands.CommandType;
import communication.commands.iCommand;
import gameObjects.Hand;

public class HandCommand implements iCommand {
	String handsJSON;
	public HandCommand(ArrayList<Hand> playerHands){
		handsJSON = utility.GsonHelper.toJson(playerHands);
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.HANDCOMMAND;
	}
	public ArrayList<Hand> getHands(){
		Type collectionType = new TypeToken<ArrayList<Hand>>(){}.getType();
		return utility.GsonHelper.gson.fromJson(handsJSON, collectionType);
	}
	public String getJson(){
		return this.handsJSON;
	}
}
