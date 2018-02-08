package client;

import communication.commands.CommandType;
import communication.commands.game.EndTurnCommand;
import communication.commands.game.StartGameCommand;
import communication.commands.game.StartTurnCommand;
import communication.commands.game.war.EndWarCommand;
import communication.commands.game.war.StartWarCommand;
import graphics.GameGraphics;
import graphics.WarLayout;
import io.socket.emitter.Emitter.Listener;

public class WarGameController extends GameController
{
	private boolean warRoutesRegistered;
	private WarLayout wl;
	public WarGameController()
	{
		super();
		wl = new WarLayout();
		super.gg = new GameGraphics(this, wl, "War");
	//	super.gg.setGameString("War");
		wl.setGraphics(gg);
		//super.gg.setWarLayout(wl);
		warRoutesRegistered = false;
	}
	@Override
	public void start(StartGameCommand startGameCmd) {
		registerNetworkRoutes();
		gg.setPlayerInfo("War", startGameCmd.getNumberOfPlayers());
		gg.showWindow();
	}
	
	protected void registerNetworkRoutes()
	{
		super.registerNetworkRoutes();
		if(!warRoutesRegistered){
			Client.cardNet.addOnCmd(CommandType.ENDWARCOMMAND, new Listener(){
	
				@Override
				public void call(Object... args) {
					//End war
					EndWarCommand ewc = utility.GsonHelper.fromJson((String)args[0], EndWarCommand.class);	
					//gg.setInWar(false);
					wl.setInWar(false);
				}
			});
			
			Client.cardNet.addOnCmd(CommandType.STARTWARCOMMAND, new Listener(){
	
				@Override
				public void call(Object... args) {
					StartWarCommand swc = utility.GsonHelper.fromJson((String)args[0], StartWarCommand.class);	
					//gg.setInWar(true);
					wl.setInWar(true);
				}
			});
			Client.cardNet.addOnCmd(CommandType.STARTTURNCOMMAND, new Listener(){

				@Override
				public void call(Object... args) {
					StartTurnCommand stc = utility.GsonHelper.fromJson((String)args[0], StartTurnCommand.class);
					requestDrawFromServer();
				}
			});
			
			Client.cardNet.addOnCmd(CommandType.ENDTURNCOMMAND, new Listener(){

				@Override
				public void call(Object... args) {
					EndTurnCommand etc = utility.GsonHelper.fromJson((String)args[0], EndTurnCommand.class);	
					System.out.println("Your turn is over.");
				}
			});
		}
		warRoutesRegistered = true;
	}
	
}








/*
*/