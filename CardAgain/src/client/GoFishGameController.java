package client;

import communication.commands.CommandType;
import communication.commands.game.StartGameCommand;
import communication.commands.game.gofish.AskPlayerCommand;
import communication.commands.game.gofish.HandCommand;
import gameObjects.Card;
import graphics.GameGraphics;
import graphics.GoFishLayout;
import io.socket.emitter.Emitter.Listener;
import utility.GsonHelper;

public class GoFishGameController extends GameController
{
	private static boolean started = false;
	private GoFishLayout gl;
	public GoFishGameController()
	{
		super();
		GoFishLayout gl = new GoFishLayout();
		
	//	super.gg.setGameString("Fish");
	}

	public void registerNetworkRoutes() {
		Client.cardNet.addOnCmd(CommandType.STARTTURNCOMMAND, new Listener(){
			@Override
			public void call(Object... args) {
								
			}
		});
		
		Client.cardNet.addOnCmd(CommandType.ADDPAIRCOMMAND, new Listener(){
			@Override
			public void call(Object... args) {
				
			}
		});
		
		Client.cardNet.addOnCmd(CommandType.GOFISHCOMMAND,new Listener(){
			@Override
			public void call(Object... args){
				
			}
		});
		
		Client.cardNet.addOnCmd(CommandType.ENDTURNCOMMAND, new Listener(){
			@Override
			public void call(Object... args) {
								
			}
		});
		
		Client.cardNet.addOnCmd(CommandType.SENDHANDCOMMAND, new Listener(){
			@Override
			public void call(Object... args) {
				
			}
		});
		
		Client.cardNet.addOnCmd(CommandType.ENDGAMECOMMAND, new Listener(){
			@Override
			public void call(Object... args) {
								
			}
		});
		
		Client.cardNet.addOnCmd(CommandType.HANDCOMMAND, new Listener(){
			@Override
			public void call(Object... args) 
			{
				HandCommand hc = GsonHelper.fromJson((String)args[0],HandCommand.class);
				for(int i = 0; i < hc.getHands().size(); i++)
				{
					gl.setHand(hc.getHands().get(i), i+1);
				}
				started = true;
			}
		});
	}

	public void start(StartGameCommand sgc) 
	{
		registerNetworkRoutes();
		super.gg = new GameGraphics(this, gl, "Fish");
		gl.setGraphics(super.gg);
		HandCommand hc= GsonHelper.fromJson((String)sgc.getData(), HandCommand.class);
		gg.setGameSize(sgc.getNumberOfPlayers());
		gl.init();
		int playernum = sgc.getPlayerNum();
		gg.setPlayerInfo("Fish", playernum);
		for(int i = 0; i < hc.getHands().size(); i++)
		{
			gl.setHand(hc.getHands().get(i), i);
		}
	//	gg.showWindow();
		
	}
	
	public void askPlayer(int playerNum, Card card)
	{
		Client.cardNet.forwardCommand(GsonHelper.toJson(new AskPlayerCommand(playerNum, card)), CommandType.ASKPLAYERCOMMAND.getName());
	}
}
