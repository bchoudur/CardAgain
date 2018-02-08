package graphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import client.Client;
import client.GoFishGameController;
import communication.commands.game.DrawCardCommand;

public class FishDeckListener implements MouseListener
{
	private GoFishLayout gl;
	private GoFishGameController gc;
	private GameGraphics gg;
	
	FishDeckListener(GoFishLayout fish, GameGraphics graphic)
	{
		gg = graphic;
		gl = fish;
		gc = (GoFishGameController)gg.getGameController();
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(gl.getGoFish())
		{
			gl.setGoFish(false);
			gc.requestDrawFromServer();
		}
	}

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
