package graphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import client.Client;
import client.GoFishGameController;
import communication.commands.game.gofish.AskPlayerCommand;

public class FishOpponentListener implements MouseListener 
{
	private GameJPanel panel;
	private GoFishLayout gl;
	private GoFishGameController gc;
	private GameGraphics gg;
	
	FishOpponentListener(GoFishLayout fish, GameJPanel p, GameGraphics graphic)
	{
		gg = graphic;
		gl = fish;
		panel = p;
		gc = (GoFishGameController)gg.getGameController();
	}
	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		if(gl.getCardSelected())
		{
			gl.clearMatchingArea();
			gl.setCardSelected(false);
			//resets the card index to -1 so that any attempts to call this method at the wrong time will result in an error
			gl.setCardIndex(-1);
			gc.askPlayer(panel.getPlayer(), gl.getMiddleCard());
		}
		
	}
	//everything past here is unused, but required by the MouseListener interface
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
