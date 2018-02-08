package gameObjects;

public class Card implements Comparable<Card>, Cloneable{
	
	private String suit;
	private int value;
	private boolean facedown;
	
	// Constructor
	public Card (String s, int val, boolean isFacedown)
	{
		// Must be a valid Suit
		if(s.equals("Clubs") || s .equals("Diamonds") || s.equals("Hearts") || s .equals("Spades"))
		{
			suit = s;
		}
		else
		{
			System.out.println("Invalid suit, try again");
		}
		
		// 11 - Jack, 12 - Queen, 13 - King, 1 - Ace, 0 - Joker
		// Initially will not allow Joker Cards, can change later if need to
		
		if(val >= 1 && val <= 13)
		{
			value = val;
		}
		facedown = isFacedown;
	}
	public Card(String s, int val) {
		this(s,val,false);
	}
	
	// Returns card value
	public int getValue()
	{
		return value;
	}
	
	// Returns card suit
	public String getSuit()
	{
		return suit;
	}
	
	// Combines Suit and Value into a string, mostly used for testing purposes
	public String getSuitAndValue()
	{
		//String sAndVal = Integer.toString(value) + "_of_" + suit.toLowerCase();
		String sAndVal = (value < 10 ? "0" : "") + value + "_of_" + suit.toLowerCase();
		
		return sAndVal;
	}
	

	// Compares card values
	public int compareTo(Card compareCard) {
		if(value > compareCard.getValue())
		{
			return 1;
		}
		else if (value == compareCard.getValue())
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}
	public boolean getFacedown()
	{
		return facedown;
	}
	public void setFacedown(boolean isFacedown)
	{
		facedown = isFacedown;
	}
	
	public Card clone(){
		Card c;
		try{
			c = (Card) super.clone();
			c.suit = this.suit;
			c.value = this.value;
			c.facedown = this.facedown;
			return c;
		} catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
	}
}
