package gameObjects;

import java.util.UUID;

public class Player {
	
	private String name = "";
	public Hand playerHand;
	public DiscardPile playerBooks;
	public UUID playerID;
	public int uID;

	public Player(String s)
	{
		name = s;
		playerHand = new Hand();
		playerBooks = new DiscardPile();
	}
	
	public Player(String s, UUID id, int uID)
	{
		name = s;
		playerHand = new Hand();
		playerBooks = new DiscardPile();
		playerID = id;
		this.uID = uID;
	}
	
	public String getName()
	{
		return name;
	}
	
}