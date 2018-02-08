package gameObjects;

import java.util.ArrayList;
import java.util.Collections;

public class DeckTest {
private ArrayList<Card> cardDeck = new ArrayList<Card>();
	
	// Deck Object Constructor
	public DeckTest()
	{
		cardDeck.add(new Card("Clubs", 2, false));
		cardDeck.add(new Card("Clubs", 3, false));
		cardDeck.add(new Card("Clubs", 4, false));
		cardDeck.add(new Card("Clubs", 5, false));
		cardDeck.add(new Card("Clubs", 6, false));
		cardDeck.add(new Card("Clubs", 7, false));
		cardDeck.add(new Card("Clubs", 8, false));
		cardDeck.add(new Card("Clubs", 9, false));
		cardDeck.add(new Card("Clubs", 10, false));
		cardDeck.add(new Card("Clubs", 11, false));
		cardDeck.add(new Card("Clubs", 12, false));
		cardDeck.add(new Card("Clubs", 13, false));
		
		
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

