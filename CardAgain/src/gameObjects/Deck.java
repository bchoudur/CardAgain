package gameObjects;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;

public class Deck {
	
	private ArrayList<Card> cardDeck = new ArrayList<Card>();
	
	// Deck Object Constructor
	public Deck()
	{
		// Create the cards, each with a different suit
		// Start at 1 since first card value is 1
		for(int i = 1; i < 14; i++)
		{
 			cardDeck.add(new Card("Clubs", i, false));
		}
		
		for(int i = 1; i < 14; i++)
		{
			cardDeck.add(new Card("Hearts", i, false));
		}
		
		for(int i = 1; i < 14; i++)
		{
			cardDeck.add(new Card("Diamonds", i, false));
		}
		
		for(int i = 1; i < 14; i++)
		{
			cardDeck.add(new Card("Spades", i, false));
		}
		
	}
	
	// Shuffles the deck
	public void shuffle()
	{
		Collections.shuffle(cardDeck);
	}
	
	// Draw's the top card, (last card in the deck)
	public Card draw(){
		Card aCard = null;
		
		aCard = cardDeck.get(cardDeck.size() - 1);
		cardDeck.remove(cardDeck.size() - 1);
		return aCard;
	}
	
	// Returns size of the Deck
	public int size()
	{
		return cardDeck.size();
	}
	
	/**Created this method to help with mapping with the cards .png images to the actual
	 * card's values and suits in the CardDictionary class
	 * Takes the cardDeck ArrayList and makes a String ArrayList version
	 * @return cardNames
	 */
	public ArrayList<String> createStringArrayList(){
		ArrayList<String> cardNames = new ArrayList<String>();
		
		for(int i = 0; i < size(); i++){
			cardNames.add(cardDeck.get(i).getSuitAndValue());
		}
		
		return cardNames;
	}

}
