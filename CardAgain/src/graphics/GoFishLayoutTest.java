package graphics;

import java.io.IOException;
import java.util.ArrayList;

import client.GameController;
import client.GoFishGameController;
import communication.commands.game.gofish.HandCommand;
import gameObjects.Deck;
import gameObjects.Hand;

public class GoFishLayoutTest {

	static Deck deck = new Deck();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GameController gc = new GoFishGameController();
		GoFishLayout goFish = new GoFishLayout();
		GameGraphics gg = new GameGraphics(gc, goFish, "no");
		goFish.setGraphics(gg);
		//gg.setGameSize(4);
		goFish.init();
		gg.setPlayerInfo("GoFish", 3);
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();
		Hand hand3 = new Hand();
		Hand hand4 = new Hand();
		
		int numOfPlayers = 4;
		
		ArrayList<Hand> hands = new ArrayList<Hand>();
		
		shuffleDeck();
		
		hands.add(hand1);
		hands.add(hand2);
		hands.add(hand3);
		hands.add(hand4);
		
	//	GoFishLayout goFish = new GoFishLayout(gg);
		
		hands.set(0, createPlayerHand());
		hands.set(1, createPlayerHand());
		hands.set(2, createPlayerHand());
		hands.set(3, createPlayerHand());
		
		
		
		for(int i = 0; i < hands.size(); i++){
			System.out.println("The cards for player: " + i + 1 + " are:");
			for(int j = 0; j < 5; j++){
				System.out.println(hands.get(i).getCard(j).getSuitAndValue());
			}
		}
		
		for(int i = 1; i < numOfPlayers + 1; i++){
			goFish.setHand(hands.get(i - 1), i);
		}
		
		
		try {
			gg.setGFLayout(goFish);
			gg.createGameWindow("Fish", 4);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gg.showWindow();
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		goFish.setHand(createPlayerHand2(), 3);
//		gg.showWindow();
	}
	
	static Hand createPlayerHand(){
		Hand hand = new Hand();
		for(int i = 0; i < 5; i++){
			hand.takeCard(deck.draw());
		}
		return hand;
	}
	
	static Hand createPlayerHand2(){
		Hand hand = new Hand();
		for(int i = 0; i < 13; i++){
			hand.takeCard(deck.draw());
		}
		return hand;
	}
	
	static void shuffleDeck(){
		deck.shuffle();
	}

}
