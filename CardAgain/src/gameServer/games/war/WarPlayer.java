package gameServer.games.war;

import java.util.UUID;

import gameObjects.DiscardPile;

public class WarPlayer {
	
	private String name = "";
	public WarHand playerHand;
	public DiscardPile playerDiscPile;
	public boolean hasPlayed;
	public UUID playerID;
	public int uID;

	public WarPlayer(String s)
	{
		name = s;
		playerHand = new WarHand();
		playerDiscPile = new DiscardPile();
		hasPlayed = false;
	}
	
	public WarPlayer(String s, UUID id, int uID)
	{
		name = s;
		playerHand = new WarHand();
		playerDiscPile = new DiscardPile();
		hasPlayed = false;
		playerID = id;
		this.uID = uID;
	}
	
	public String getName()
	{
		return name;
	}
}