package gameServer.games.goFish;

import java.util.ArrayList;
import java.util.UUID;

import communication.commands.CommandType;
import communication.commands.iCommand;
import communication.commands.game.EndGameCommand;
import communication.commands.game.EndTurnCommand;
import communication.commands.game.ForwardingCommand;
import communication.commands.game.StartGameCommand;
import communication.commands.game.StartTurnCommand;
import communication.commands.game.gofish.AddPairCommand;
import communication.commands.game.gofish.AskPlayerCommand;
import communication.commands.game.gofish.GoFishCommand;
import communication.commands.game.gofish.HandCommand;
import communication.commands.game.gofish.SendHandCommand;
import dataModels.User;
import gameObjects.Card;
import gameObjects.Deck;
import gameObjects.Games;
import gameObjects.Hand;
import gameObjects.Player;
import gameServer.GameServer;
import gameServer.GameState;
import server.MySQLGameAccess;
import utility.GsonHelper;

public class Fish implements GameState{
	private GameServer gs;
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private Deck gameDeck;
	private Player currentPlayer;
	private int playerNum;
	private String message;
	private ArrayList<Hand> playerHands = new ArrayList<Hand>();
	
	public Fish(GameServer gameServer) {
		gs = gameServer;
	}

	/*
	// Rough Draft of how Fish is going to work
	public static void main(String args[])
	{
		// Create players
		Player p1 = new Player("player1");
		Player p2 = new Player("player2");
		Player p3 = new Player("player3");
		Player p4 = new Player("player4");
		
		// Create and Shuffle Deck
		Deck drawPile = new Deck();
		drawPile.shuffle();
		
		// Deal Cards
		dealCards(p1, drawPile);
		dealCards(p2, drawPile);
		dealCards(p3, drawPile);
		dealCards(p4, drawPile);
		
		// Keeps track of which players turn it is
		Player currentPlayer;
		
		ArrayList<Player> playerList = new ArrayList<Player>();
		playerList.add(p1);
		playerList.add(p2);
		playerList.add(p3);
		playerList.add(p4);
		
		int count = 0;
		int t;
		int askingValue;
		Card askingCard;
		
		// Game is over when there are no cards in the draw pile or in people's hands
		while(drawPile.size() != 0 && !p1.playerHand.isEmpty() && !p2.playerHand.isEmpty() && !p3.playerHand.isEmpty() && !p4.playerHand.isEmpty())
		{
			
			// There are 4 players so take the loop count modded by 4 to get which the correct players turn
			t = count % 4;
			currentPlayer = playerList.get(t);
			
			// Player has their turn
		
			// Player chooses a card to ask for
			askingCard = pickCardValue(currentPlayer, 0);
			askingValue = askingCard.getValue();
		
			// Player chooses someone to ask
			if(t < 3)
			{
				checkForCardValue(playerList.get(t + 1), playerList.get(t), askingCard, drawPile);
			}
			else
			{
				checkForCardValue(playerList.get(0), playerList.get(t), askingCard, drawPile);
			}
		
			// Lay down all pairs/books
			// Since it is possible a player gets all 4 cards in one turn therefore having 2 pairs,
			// we run the method twice to check.
			checkForPairsInHand(currentPlayer, askingValue);
			checkForPairsInHand(currentPlayer, askingValue);
			
			// Next player turn
			count++;
		}
		
		checkForWinner(playerList);
		
		
	}
	*/
	
	// Deals 5 cards to each player - WORKS
	public void dealCards(Player player)
	{
		for(int i = 0; i < 5; i++)
		{
			player.playerHand.takeCard(gameDeck.draw());
		}
	}
	
	// Gets the card value that the player wants to ask for in their hand - WORKS
	public Card pickCardValue(Player player, int pos)
	{
		Card pickedCard = player.playerHand.getCard(pos);
		System.out.println(player.getName() + " picks a card value of " + pickedCard.getValue());
		return pickedCard;
	}
	
	// Checks the checkPlayers hand for card value, if they have any cards of that value,
	// recievePlayer takes those cards. otherwise recievePlayer draws from pile - WORKS
	public void checkForCardValue(Player checkPlayer, Player recievePlayer, Card pickedCard)
	{
		int value = pickedCard.getValue();
		
		// If they the card, take all of that kind
		ArrayList<Card> takenCards = checkPlayer.playerHand.containsCardValue(value, 0);
		
		// If not, go fish / draw a card from the main deck
		if(takenCards.size() == 0)
		{
			sendCommandToPlayer(new GoFishCommand(), currentPlayer);
		}
		else
		{
			for(int i = 0; i < takenCards.size(); i++)
			{
				recievePlayer.playerHand.takeCard(takenCards.get(i));
				
				message = recievePlayer.getName() + " has taken the " + takenCards.get(i).getSuitAndValue() + " from " + checkPlayer.getName();
				gs.broadcastChat(message);

			}
		}
	}
	
	// Checks the given players hand for any pairs to lay down - WORKS
	public void checkForPairsInHand(Player player, int value)
	{
		// Check for pairs of 2
		ArrayList<Card> book = player.playerHand.containsCardValue(value, 1);
		
		// Adds the pair to their group of books / pairs
		if(book.size() == 2)
		{
			player.playerBooks.add(book.get(0));
			player.playerBooks.add(book.get(1));
			
			message = player.getName() + " has laid down the pair " + book.get(0).getSuitAndValue()
					+ " and " + book.get(1).getSuitAndValue() + " to create a book";
			gs.broadcastChat(message);
			
			// tells the player how many new books / pairs they have
			sendCommandToPlayer(new AddPairCommand(1), currentPlayer);
			
		}
		if(book.size() == 4)
		{
			player.playerBooks.add(book.get(0));
			player.playerBooks.add(book.get(1));
			player.playerBooks.add(book.get(2));
			player.playerBooks.add(book.get(3));
				
			message = player.getName() + " has laid down the pair " + book.get(0).getSuitAndValue()
					+ " and " + book.get(1).getSuitAndValue() + " to create a book";
			gs.broadcastChat(message);
			message = player.getName() + " has laid down the pair " + book.get(2).getSuitAndValue()
					+ " and " + book.get(3).getSuitAndValue() + " to create a book";
			gs.broadcastChat(message);
			
			// tells the player how many new books / pairs they have
			sendCommandToPlayer(new AddPairCommand(2), currentPlayer);
		}
	}
	
	// Goes through the given playerList to determine who has the most Books - WORKS
	public void checkForWinner()
	{
		int bookCount;
		ArrayList<Player> winners = new ArrayList<Player>();
		Player winner;
		
		// Sets the bookCount and winner to the first player initially
		bookCount = playerList.get(0).playerBooks.size() / 2;
		winners.add(playerList.get(0));
		
		// Compares all other players to the current highest bookCount player
		for(int i = 1; i < playerList.size(); i++)
		{
			if((playerList.get(i).playerBooks.size() / 2) > bookCount)
			{
				winners.removeAll(winners);
				bookCount = playerList.get(i).playerBooks.size() / 2;
				winners.add(playerList.get(i));
			}
			else if(playerList.get(i).playerBooks.size() / 2 == bookCount)
			{
				winners.add(playerList.get(i));
			}
		}
		
		displayWinner(winners, bookCount);
	}
	
	// Prints a message that shows which player won the game with how many books they have - WORKS
	public void displayWinner(ArrayList<Player> winners, int bookCount)
	{
		if(winners.size() == 1)
		{
			message = winners.get(0).getName() + " wins with " + bookCount + " books/pairs!";
			gs.broadcastChat(message);
			updateWinners(winners);
			
			gs.broadcastCommandToAll(new EndGameCommand());
		}
		else if(winners.size() > 1)
		{
			for(int i = 0; i < winners.size(); i++)
			{
				message = winners.get(i).getName() + " tied with " + bookCount + " books/pairs!";
				gs.broadcastChat(message);
			}
			updateWinners(winners);
			gs.broadcastCommandToAll(new EndGameCommand());
		}
	}
	
	// If there are no cards left in play, game is over
	public boolean isGameOver()
	{
		if(gameDeck.size() == 0 && areAllPlayersHandsEmpty())
		{
			return true;
		}
		
		return false;
	}
	
	// If a card is found in a players hand, return false
	// otherwise, return true
	public boolean areAllPlayersHandsEmpty()
	{
		for(int i = 0; i < playerList.size(); i++)
		{
			if(playerList.get(i).playerHand.getSize() != 0)
			{
				return false;
			}
		}
		
		return true;
	}
	
	// Assigns the players in the connectedUsers map to a Player
	public void assignPlayers()
	{
		int playerCount = 1;
		for(UUID u : gs.getConnectedClients().keySet()){
			User user = gs.getConnectedClients().get(u);
			
			Player temp = new Player(user.getUsername(), u, user.getUserID());
			playerList.add(temp);
			playerHands.add(temp.playerHand);
			
			System.out.println("Player " + playerCount + "is" + u);
			playerCount++;
		}
	}
	
	// Changes to the next players turn
	public void nextPlayerTurn()
	{
		sendCommandToPlayer(new EndTurnCommand(), currentPlayer);
		
		if(isGameOver())
		{
			checkForWinner();
		}
		else
		{
			checkNextPlayerTurn();
			
			sendCommandToPlayer(new StartTurnCommand(), currentPlayer);
		}
	}
	
	public void checkNextPlayerTurn()
	{
		// Increments player
		if(playerNum < playerList.size())
		{
			currentPlayer = playerList.get(playerNum);
			playerNum++;
		}
		else if(playerNum == playerList.size())
		{
			currentPlayer = playerList.get(0);
			playerNum = 1;
		}
		
		// After new player is selected, check to see if they have any cards in their
		// hand, if not check to see if they can draw any cards
		if(currentPlayer.playerHand.getSize() == 0)
		{
			if(gameDeck.size() == 0)
			{
				// Since the current players hand and the deck are empty, move on to the next player
				checkNextPlayerTurn();
			}
			else
			{
				// The players hand is empty but there are still cards in the deck
				// The current player draws a card
				Card drawCard = gameDeck.draw();
				currentPlayer.playerHand.takeCard(drawCard);
				
				// The new single card Hand is sent to the player
				sendCommandToPlayer(new SendHandCommand(currentPlayer.playerHand), currentPlayer);
			}
			
		}
	}
	
	public void updateWinners(ArrayList<Player> winners)
	{
		for(int i = 0; i < playerList.size(); i++)
		{
			// If this player is a winner
			if(winners.contains(playerList.get(i)))
			{
				MySQLGameAccess.updateGamesPlayed(playerList.get(i).uID, true);
			}
			else
			{
				MySQLGameAccess.updateGamesPlayed(playerList.get(i).uID, false);
			}
		}
	}

	@Override
	public void handleForwarding(ForwardingCommand cmd, Object... args) {
		String cmds = cmd.getCommand();
		String cmdType = cmd.getTypeString();
		iCommand comd = (iCommand) utility.GsonHelper.gson.fromJson(cmds, CommandType.getEnumByString(cmdType).getType());
		UUID sender = cmd.getUUID();
		switch(comd.getCommandType()){
		case DRAWCARDCOMMAND:
			// Draws a card from the deck and places it into the players hand
			gs.broadcastChat(currentPlayer.getName() + " GO FISH!");
			Card drawCard = gameDeck.draw();
			currentPlayer.playerHand.takeCard(drawCard);
			
			// Checks to see if the new card provides the new player with a pair
			checkForPairsInHand(currentPlayer, drawCard.getValue());
			
			// Sends the player their new hand
			if(currentPlayer.playerHand.getSize() != 0)
			{
				sendCommandToPlayer(new SendHandCommand(currentPlayer.playerHand), currentPlayer);
			}
			
			// next players turn starts
			nextPlayerTurn();
			
			break;
		case ASKPLAYERCOMMAND:
			AskPlayerCommand acc = (AskPlayerCommand)comd;
			//Call method to ask player acc.getPlayer() for the selected card
			checkForCardValue(playerList.get(acc.getPlayerNum() - 1), currentPlayer, acc.getCard());		
			break;
		default:
			System.out.println("No handler registered for " + cmdType + " command.");
			break;
		}
	}

	@Override
	public Games getGameType() {
		return Games.GOFISH;
	}

	@Override
	public void startGame() {
		// TODO Auto-generated method stub
		// Creates the player objects for each user connected
		assignPlayers();
		
		gameDeck = new Deck();
		gameDeck.shuffle();
		
		// Deals cards to all connected players
		for(int i = 0; i < playerList.size(); i++)
		{
			dealCards(playerList.get(i));
		}
		String initialHandJSON = GsonHelper.toJson(new HandCommand(playerHands));
		int playerNum = 1;
		for(UUID u : this.gs.getConnectedClients().keySet()){
			gs.sendCommandToPlayer(new StartGameCommand(playerNum,getGameType(),this.gs.getConnectedClients().size(),initialHandJSON),u);
			playerNum++;
		}
		currentPlayer = playerList.get(0);
		playerNum = 1;
		sendCommandToPlayer(new StartTurnCommand(), currentPlayer);
	}
	
	public void sendCommandToPlayer(iCommand cmd, Player p){
		this.gs.sendCommandToPlayer(cmd, p.playerID);
	}
	
	
	
}