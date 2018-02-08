package graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;

import gameObjects.Card;
import gameObjects.Hand;


public class GoFishLayout extends ParentLayout{
	
	private GameGraphics gg;
	//private Deck deck = new Deck();
	private ArrayList<Hand> hands = new ArrayList<Hand>();
	//private Hand hand;
	private CardDictionary cd;
	private boolean goFish;
	//This boolean is set by FishPlayerListener, it tells FishOpponentListener if the user has selected a card.
	//If they have, the FishOpponenetListener emits the command to tell the server what card the player selected
	//and what player they are attempting to get it from.
	private boolean cardSelected;
	//This ArrayList goes with hands. When initially creating the game the setDeck method needs to not update the player hand until
	//everything is created. After that, however, the game should just update the hand that is changing.
	private ArrayList<Boolean> initiated = new ArrayList<Boolean>();
	
	private GameJPanel matchingArea;
	
	private GameJPanel deck;
	
	private JButton prev;
	private JButton next;
	
	private int selectedCardIndex;
	//This tells what the starting index is for the cards currently being displayed from the hand
	private int handStartingIndex;
	
	public GoFishLayout(){
		goFish = false;
		cardSelected = false;
		matchingArea = new GameJPanel();
		//deck.shuffle();
		
		//Makes the hands arraylist the same size as the number of players
		
		
		
		cd = new CardDictionary();
	}
	
	public void init()
	{
		for(int i = 0; i < gg.getGameSize(); i++){
			hands.add(null);
			initiated.add(false);
		}
	}
	
	public GameJPanel createGoFishMiddle( GameJPanel panel) throws IOException{
		matchingArea = gg.createPanels(5, 15, 155, 205);
		deck = gg.createPanels(400, 15, 155, 205);
		panel.getPanel().add(matchingArea.getPanel());
		panel.getPanel().add(deck.getPanel());
		prev = new JButton();
		prev.setText("prev");
		prev.setEnabled(false);
		prev.addActionListener(prevList());
		next = new JButton();
		next.setText("next");
		next.setEnabled(false);
		next.addActionListener(nextList());
		gg.drawButton(0, 300, prev);
		gg.drawButton(800, 300, next);
		panel.getPanel().add(next);
		panel.getPanel().add(prev);
		initDeck(panel);
		return matchingArea;
	}
	
	public GameJPanel populateGoFishPlayerPanel(GameJPanel panel, int playerNum) throws IOException{
		int x = 0;
		int y = 0;
		//int buttonY = 0;
		//int yIncrement = 60;
		//ArrayList<JLabel> pics = new ArrayList<JLabel>();
		JLabel pic;
	//	JButton button;
		if(playerNum == gg.getCurrPlayer())
		{
			for(int i = handStartingIndex; i < handStartingIndex+5 && i < hands.get(playerNum-1).getSize(); i++){
				String cardString = cd.mapCardtoImageString(hands.get(playerNum-1).getCard(i));
				if(playerNum == 1 || playerNum == 2){
					x = x + 155;
					pic = gg.drawCard(cardString, panel, x, 15);
					if(playerNum == gg.getCurrPlayer())
					{
						pic.addMouseListener(new FishPlayerListener(cardString, gg, this, matchingArea, i));
					}
					else
					{
						panel.setMouseListener(new FishOpponentListener(this, panel, gg));
					}
				//	button = gg.drawButton(cardString.substring(0, cardString.length() - 4), x, 220);
					gg.addCard(panel, pic);
				//	addButton(panel, button);
				//	x++;
				}
				if(playerNum == 3 ||playerNum == 4){
				//	int startingY = 500;
		//			buttonY = startingY + (yIncrement*i);
					
					pic = gg.drawCard(cardString, panel, 30, y);
					if(playerNum == gg.getCurrPlayer())
					{
						pic.addMouseListener(new FishPlayerListener(cardString, gg, this, matchingArea, i));
					}
					else
					{
						panel.setMouseListener(new FishOpponentListener(this, panel, gg));
					}
				//	button = gg.drawButton(cardString.substring(0, cardString.length() - 4), 30, buttonY);
					gg.addCard(panel, pic);
					y = y + 150;
				//	addButton(panel, button);
				}
			}
		}
		else
		{
			for(int i = 0; i < 5 && i < hands.get(playerNum-1).getSize(); i++){
				String cardString = cd.mapCardtoImageString(hands.get(playerNum-1).getCard(i));
				if(playerNum == 1 || playerNum == 2){
					x = x + 155;
					pic = gg.drawCard(cardString, panel, x, 15);
					if(playerNum == gg.getCurrPlayer())
					{
						pic.addMouseListener(new FishPlayerListener(cardString, gg, this, matchingArea, i));
					}
					else
					{
						panel.setMouseListener(new FishOpponentListener(this, panel, gg));
					}
				//	button = gg.drawButton(cardString.substring(0, cardString.length() - 4), x, 220);
					gg.addCard(panel, pic);
				//	addButton(panel, button);
				//	x++;
				}
				if(playerNum == 3 ||playerNum == 4){
				//	int startingY = 500;
		//			buttonY = startingY + (yIncrement*i);
					
					pic = gg.drawCard(cardString, panel, 30, y);
					if(playerNum == gg.getCurrPlayer())
					{
						pic.addMouseListener(new FishPlayerListener(cardString, gg, this, matchingArea, i));
					}
					else
					{
						panel.setMouseListener(new FishOpponentListener(this, panel, gg));
					}
				//	button = gg.drawButton(cardString.substring(0, cardString.length() - 4), 30, buttonY);
					gg.addCard(panel, pic);
					y = y + 150;
				//	addButton(panel, button);
				}
			}
		}
		return panel;
	}
	
	
	public void createMatchingArea(GameJPanel panel) throws IOException{
		matchingArea = createGoFishMiddle(panel);
	}
	
	public void clearMatchingArea()
	{
		matchingArea.getPanel().removeAll();
		matchingArea.getPanel().repaint();
		matchingArea.getPanel().validate();
	}
	
	//Temporally
	public void setHand(Hand hand, int playerNum)
	{
		if(playerNum == gg.getCurrPlayer() && initiated.get(playerNum-1))
		{
			handStartingIndex = 0;
			prev.setEnabled(false);
			if(hand.getSize() > 5)
			{
				next.setEnabled(true);
			}
			else
			{
				next.setEnabled(false);
			}
		}
		for(int i = 0; i < hand.getSize(); i++)
		{
		//	int num = gg.getCurrPlayer();
			if(playerNum == gg.getCurrPlayer())
			{
				hand.getCard(i).setFacedown(false);
			}
			else
			{
				hand.getCard(i).setFacedown(true);
			}
		}
	//	hands.add(playerNum - 1, hand);
		
		hands.set(playerNum-1, hand);
		if(initiated.get(playerNum-1))
		{
			updatePlayerHand(playerNum);
		}
		initiated.set(playerNum-1, true);
		//hands.add(playerNum - 1, hand);
	}
	
	public void addCardToMatchingArea(GameJPanel panel, String card) throws IOException{
		matchingArea.getPanel().removeAll();
		System.out.println("got here");
		JLabel label = gg.drawCard(card, panel, 0, 0);
		initMatchingArea(label);
	}
	
	public void addButton(GameJPanel panel, JButton button){
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//System.out.println("Clicked: " + button.getText());
			}
		});
		panel.getPanel().add(button);
	}
	
	public void initDeck(GameJPanel panel)
	{
		JLabel pic = new JLabel();
		try {
			pic = gg.drawCard("back.png", deck, 0, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pic.addMouseListener(new FishDeckListener(this, gg));
		deck.getPanel().repaint();
		deck.getPanel().add(pic);
		deck.getPanel().validate();
	}
	public void initMatchingArea(JLabel picLabel){
		matchingArea.getPanel().repaint();
		matchingArea.getPanel().add(picLabel);
		matchingArea.getPanel().validate();
	}
	public void setCardSelected(boolean selCard)
	{
		cardSelected = selCard;
	}
	public boolean getCardSelected()
	{
		return cardSelected;
	}
	public Card getMiddleCard()
	{
		return hands.get(gg.getCurrPlayer()).getCard(selectedCardIndex);
	}
	public void setCardIndex(int index) 
	{
		selectedCardIndex = index;
	}
	public void setGoFish(boolean fishing)
	{
		goFish = fishing;
	}
	public boolean getGoFish()
	{
		return goFish;
	}
	public void updatePlayerHand(int playerNum)
	{
		ArrayList<GameJPanel> panels = gg.getPlayerPanels();
		panels.get(playerNum-1).getPanel().removeAll();
		try {
			populateGoFishPlayerPanel(panels.get(playerNum-1), playerNum);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		panels.get(playerNum-1).getPanel().repaint();
		panels.get(playerNum-1).getPanel().validate();
	}
	public void setGraphics(GameGraphics graph)
	{
		this.gg = graph;
	}
	public ActionListener prevList()
	{
		ActionListener list = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				handStartingIndex = handStartingIndex-5;
				if(handStartingIndex+5 >= hands.get(gg.getCurrPlayer()-1).getSize())
				{
					next.setEnabled(false);
				}
				if(handStartingIndex-5 < 0)
				{
					prev.setEnabled(false);
				}
				if(handStartingIndex+5 < hands.get(gg.getCurrPlayer()-1).getSize())
				{
					next.setEnabled(true);
				}
				updatePlayerHand(gg.getCurrPlayer());
			}
		};
		return list;
	}
	public ActionListener nextList()
	{
		ActionListener list = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				handStartingIndex = handStartingIndex + 5;
				if(handStartingIndex+5 >= hands.get(gg.getCurrPlayer()-1).getSize())
				{
					next.setEnabled(false);
				}
				if(handStartingIndex-5 < 0)
				{
					prev.setEnabled(false);
				}
				else
				{
					prev.setEnabled(true);
				}
				updatePlayerHand(gg.getCurrPlayer());
			}
		};
		return list;
	}
}
