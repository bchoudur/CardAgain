package gameObjects;

import java.util.*;

public class Hand implements Cloneable{
	
	private ArrayList<Card> hand = new ArrayList<Card>();
	
	public Hand()
	{
		
	}
	
	public void takeCard(Card card){
		hand.add(card);
	}
	
	public Card getCard(int index){
		return (Card) hand.get(index);
	}
	
	//Removes the specified card from the current hand.
	public Card removeCard(Card card){
		int index = hand.indexOf(card);
		if(index < 0){
			return null;
		}else{
			return hand.remove(index);
		}
	}
	
	//Shows the card base off of were the card is in the hand.
	public Card show(int position){
		Card aCard = hand.get(position);
		return aCard;
	}
	
	public Card draw(int position){
		Card aCard = show(position);
		hand.remove(position);
		return aCard;
	}
	
	//Gets the number of cards in the deck
	public int getSize(){
		return hand.size();
	}
	
	public boolean isEmpty(){
		return hand.isEmpty();
	}
	
	public String toString(){
		return hand.toString();
	}
	
	public void clearHand(){
		hand.clear();
	}
	
	// Returns an array list of cards that all have the given value
	// pairs = 1, if you want to only take out cards in pairs
	// pairs = 0, if you don't care how many cards are taken
	public ArrayList<Card> containsCardValue(int val, int pairs)
	{
		ArrayList<Card> temp = new ArrayList<Card>();
		int curCount = 0;
		int firstCard = 0;
		
		// Goes through the hand
		for(int i = hand.size() - 1; i >= 0; i--)
		{
			// If the card has equal value, add it to the array list and remove it from the hand
			if(hand.get(i).getValue() == val)
			{
				if(pairs == 1)
				{
					// Must find 2 cars of the same value before we make them a pair and remove them
					if(curCount == 0)
					{
						firstCard = i;
						curCount++;
					}
					// Since this is the second card we have found that is the same, we will just add both
					// cards to the array list and remove them from the hand
					else if(curCount == 1)
					{
						// Add cards to array list
						temp.add(hand.get(firstCard));
						temp.add(hand.get(i));
						
						// Remove the cards from the hand
						hand.remove(hand.get(firstCard));
						hand.remove(hand.get(i));
					
						// Reset card counter to 0
						curCount = 0;
					}
				}
				else if(pairs == 0)
				{
					temp.add(hand.get(i));
					hand.remove(hand.get(i));
				}
			}
		}
		
		return temp;
	}
	
	public Hand clone(){
		Hand h;
		try{
			h = (Hand) super.clone();
			for(Card c : hand){
				h.takeCard(c.clone());
			}
			return h;
		}catch(CloneNotSupportedException e){
			throw new RuntimeException();
		}
	}
}


