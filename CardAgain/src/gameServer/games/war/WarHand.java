package gameServer.games.war;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import gameObjects.*;

// Since in War you only draw the top card, I think a special Hand that is a Queue
// would work best for this game
public class WarHand {

	private Queue<Card> warHand = new LinkedList<Card>();
	
	public WarHand()
	{
		
	}
	
	// Adds cards to hand
	public void addCardToHand(Card card)
	{
		warHand.add(card);
	}
	
	// Adds all cards from discard pile to the end of the hand
	public void addDiscardToHand(DiscardPile cards)
	{
		cards.shuffle();
		
		for(int i = cards.size() - 1; i >= 0 ; i--)
		{
			warHand.add(cards.remove(i));
		}	
	}
	
	// draws the top card from the hand (queue)
	public Card drawFromHand()
	{
		return warHand.poll();
	}
	
	public int size()
	{
		return warHand.size();
	}
	
	public void removeAll()
	{
		warHand.clear();
	}
	
	public boolean isEmpty()
	{
		return warHand.isEmpty();
	}
	
	public int getSize()
	{
		return warHand.size();
	}
}
