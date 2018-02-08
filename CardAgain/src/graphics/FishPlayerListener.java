/**
 * @author Alex Dague
 * This is a class that implements MouseListener. It was needed because a normal mouse listener
 * could not access the card String of the label it was appended to. We needed access to the card
 * string to display it in the area that shows which card the player has selected.
 */

package graphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import client.Client;

public class FishPlayerListener implements MouseListener
{
	private String cardString;
	private GameGraphics gg;
	private GoFishLayout gl;
	private GameJPanel panel;
	private boolean cardSelected;
	private int cardIndex;
	FishPlayerListener(String card, GameGraphics graphics, GoFishLayout fish, GameJPanel p, int index)
	{
		gl = fish;
		cardString = card;
		gg = graphics;
		panel = p;
		cardSelected = false;
		cardIndex = index;
	}
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if(gg.getTurn())
		{
			try {
				gl.addCardToMatchingArea(panel, cardString);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			gl.setCardIndex(cardIndex);
			gl.setCardSelected(true);
		}
	}
	//everything past here is unused, but required by the MouseListener interface
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}}
