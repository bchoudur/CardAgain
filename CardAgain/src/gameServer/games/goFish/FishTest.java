package gameServer.games.goFish;

import java.sql.SQLException;
import java.util.ArrayList;

import gameObjects.Card;
import gameObjects.Deck;
import gameObjects.Player;
import server.MySQLGameAccess;

public class FishTest {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		
		Fish test = new Fish();
		
		Deck testDeck = new Deck();
		
		Player p1 = new Player("p1");
		Player p2 = new Player("p2");
		Player p3 = new Player("p3");
		
		ArrayList<Player> playerList = new ArrayList<Player>();
		
		playerList.add(p1);
		playerList.add(p2);
		playerList.add(p3);
		
		// Give p1 card
		p1.playerHand.takeCard(new Card("Hearts", 4, true));
		
		// Check to see if it detects any pairs
		test.checkForPairsInHand(p1, 4);
		
		// Give p2 all other values of 4
		p2.playerHand.takeCard(new Card("Diamonds", 4, true));
		p2.playerHand.takeCard(new Card("Spades", 4, true));
		p2.playerHand.takeCard(new Card("Clubs", 4, true));
		
		// p1 picks a card to guess
		int result = test.pickCardValue(p1, 0);
		
		// check if p2 has any cards of the same value
		test.checkForCardValue(p2, p1, result, testDeck);
		
		// p1 draws a new card
		p1.playerHand.takeCard(new Card("Hearts", 5, true));
		
		// p1 picks a new card to guess
		result = test.pickCardValue(p1, 4);
		
		// check if p2 has any cards of the new value
		test.checkForCardValue(p2, p1, result, testDeck);
		
		// check for any pairs in p1's hand
		test.checkForPairsInHand(p1, 4);
		
		// who is the winner
		test.checkForWinner(playerList);
		
		// give p3 new cards
		p3.playerHand.takeCard(new Card("Hearts", 6, true));
		p3.playerHand.takeCard(new Card("Diamonds", 6, true));
		p3.playerHand.takeCard(new Card("Spades", 6, true));
		p3.playerHand.takeCard(new Card("Clubs", 6, true));
		
		// check is p3 has any pairs
		test.checkForPairsInHand(p3, 6);
		
		// who is the winner now
		test.checkForWinner(playerList);
		
		// give p3 more new cards
		p3.playerHand.takeCard(new Card("Hearts", 7, true));
		p3.playerHand.takeCard(new Card("Diamonds", 7, true));
		p3.playerHand.takeCard(new Card("Spades", 7, true));
		p3.playerHand.takeCard(new Card("Clubs", 7, true));
		
		// check for pairs again
		test.checkForPairsInHand(p3, 7);
		
		// who is the winner now
		test.checkForWinner(playerList);
		
		// give p3 3 new cards
		p3.playerHand.takeCard(new Card("Hearts", 8, true));
		p3.playerHand.takeCard(new Card("Diamonds", 8, true));
		p3.playerHand.takeCard(new Card("Spades", 8, true));
		
		// checks to make sure only 2 cards are taken out in a pair, and the 3rd card stays
		test.checkForPairsInHand(p3, 8);
		
		/*
		MySQLGameAccess sql = new MySQLGameAccess();
		sql.init();
		
		sql.updateGamesPlayed(4, true);
		*/
	}
}