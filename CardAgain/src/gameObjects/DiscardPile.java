package gameObjects;

import java.util.ArrayList;
import java.util.Collections;

public class DiscardPile {
	
	private ArrayList<Card> pile = new ArrayList<Card>();
	
	public DiscardPile()
	{
		
	}
	
	public void add(Card card)
	{
		pile.add(card);
	}
	
	public void shuffle()
	{
		Collections.shuffle(pile);
	}
	
	public int size()
	{
		return pile.size();
	}
	
	// Todo later
	public void removeAll()
	{
		pile.removeAll(pile);
	}
	
	// Not sure if we will have a game that needs this
	public Card remove(int index)
	{
		return pile.remove(index);
	}

}
