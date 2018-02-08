package graphics;

//import java.awt.Image;
//import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.Dictionary;
//import java.util.Enumeration;
//import java.util.Hashtable;
//import java.util.Map;

//import javax.swing.ImageIcon;
import javax.swing.JLabel;
//import javax.swing.JPanel;
import javax.swing.JPanel;

import client.GameController;
import gameObjects.Card;
import gameServer.games.war.WarHand;

public class WarLayout extends ParentLayout{
	
	
	private int warTracker;
	
	private String warGameString = "War";
	
	boolean inWar = false;
	
	//public GameController gc;
	private GameGraphics gg;
	private CardDictionary cd;
	Card dCard;
	
	private ArrayList<GameJPanel> dragAreas;
	private ArrayList<GameJPanel> myDragAreas;
	private ArrayList<GameJPanel> opponentDragAreas;
	
	public WarLayout(){
		//this.gc = gc;
		myDragAreas = new ArrayList<GameJPanel>();
		opponentDragAreas = new ArrayList<GameJPanel>();
		cd = new CardDictionary();
	}
	/**
	 * Creates the war middle panel layout by by how many players there are
	 * First it creates the middle panels by creating a max of 4 drag
	 * areas and then using a second loops to to only add the drag areas we need based off of
	 * the number of players
	 * */
	public void createMiddlePanel(int numofPlayers, GameJPanel panel){
		//This will store the drag areas what will be drawn in the middle panel
		//Very similar format to createGameWindow
		dragAreas = new ArrayList<GameJPanel>();
		GameJPanel dragArea;
		//This loop initizes all possible play areas
		for(int i = 0; i < 4; i++){
			switch (i){
			case 0: 
				//Drag area 1
				dragArea = gg.createPanels(5, 15, 155, 205);
				dragAreas.add(dragArea);
				break;
			case 1:
				//Drag area 2
				dragArea = gg.createPanels(255, 15, 155, 205);
				dragAreas.add(dragArea);
				break;
			case 2:
				//Drag area 3
				dragArea = gg.createPanels(505, 15, 155, 205);
				dragAreas.add(dragArea);
				break;
			case 3: 
				//Drag area 4
				dragArea = gg.createPanels(760, 15, 155, 205);
				dragAreas.add(dragArea);
				break;
			}
		}
		
		for(int i = 0; i < 4; i++){
			panel.getPanel().add(dragAreas.get(i).getPanel());
		}
	//	System.out.println(dragAreas.get(0).toString());
	}
	
	/**
	 * This will create the panels for the players
	 * @throws IOException 
	 * */
	public GameJPanel populatePlayerWarPanel(GameJPanel panel, int playerNum) throws IOException{
		int x = 0;
		int y = 0;
		ArrayList<JLabel> pics = new ArrayList<JLabel>();
		JLabel pic;
		//for(int i = 0; i < hand.getSize(); i++){
			if(playerNum == 1 || playerNum == 2){
				x = x + 30;
				pic = gg.drawCard("back.png", panel, x, 30);
				pics.add(pic);
				System.out.println("TEST " + panel.toString());
				gg.addCard(panel, pic);
				x++;
			}
			
			if(playerNum == 3 || playerNum == 4){
				y = y + 30;
				pic = gg.drawCard("back.png", panel, 30, y);
				pics.add(pic);
				gg.addCard(panel, pic);
				y++;
			}
	//	}
		String imageFileName = "back.png";
		gg.drawCard(imageFileName, panel, x, 15);
		return panel;
	}
	
	public void addWarCard(final GameJPanel panel, JLabel label, Card rCard, boolean myTurn, boolean pSpaceOpen, GameController gc){
		System.out.println("inWar value: " + inWar);
		if(!inWar)
		{
			pSpaceOpen = false;
			JLabel picLabel = null;
			try {
				picLabel = gg.drawCard(cd.mapCardtoImageString(rCard), myDragAreas.get(0), 0, 0);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			initMyDragArea(0, picLabel);
			myTurn = false;
			//tell the opponent to display our card
			gc.requestOpponentDisplay(rCard);
			gc.turnComplete();
		}
		else if(inWar)
		{
			if(warTracker == 0)
			{
				System.out.println("GOT IN THE inWar");
				JLabel picLabel = null;
				try {
					picLabel = gg.drawCard("back.png", myDragAreas.get(1), 0, 0);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				initMyDragArea(1, picLabel);
				warTracker++;
				//
			}
			else if(warTracker == 1)
			{
				pSpaceOpen = false;
				//System.out.println("debug");
				JLabel picLabel = null;
				try {
					picLabel = gg.drawCard(cd.mapCardtoImageString(rCard), myDragAreas.get(0), 0, 0);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				initMyDragArea(0, picLabel);
				myTurn = false;
				//tell the opponent to display our card
				gc.requestOpponentDisplay(rCard);
				gc.turnComplete();
				warTracker++;
				/*boolean empty = false;
				if(empty)
				{
					panel.removeAll();
					panel.repaint();
					panel.validate();
				}*/
				
			}
		}
		/*else if(player == 2 && !inWar)
		{
			alreadyGotCard = true;
			System.out.println("debug");
			JLabel picLabel = null;
			try {
				picLabel = drawCard(cd.mapCardtoImageString(testCard), dragAreas.get(2), 5, 15);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			dragAreas.get(2).repaint();
			dragAreas.get(2).add(picLabel);
			dragAreas.get(2).validate();
			boolean empty = false;
			if(empty)
			{
				panel.removeAll();
				panel.repaint();
				panel.validate();
			}
		}
		else if (player == 2 && inWar)
		{
			alreadyGotCard = true;
			System.out.println("debug");
			JLabel picLabel = null;
			try {
				picLabel = drawCard("back.png", dragAreas.get(3), 5, 15);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			dragAreas.get(3).repaint();
			dragAreas.get(3).add(picLabel);
			dragAreas.get(3).validate();
			boolean empty = false;
			if(empty)
			{
				panel.removeAll();
				panel.repaint();
				panel.validate();
			}
		}*/
		System.out.println("BC: AddWarCard EXECUTED");
	}
	
	
	//Initizes the values for the drag areas
	public void initMyDragArea(int i, JLabel picLabel){
		myDragAreas.get(i).getPanel().repaint();
		myDragAreas.get(i).getPanel().add(picLabel);
		myDragAreas.get(i).getPanel().validate();
	}
		
	public void initOpponentDragArea(int i, JLabel picLabel)
	{
		opponentDragAreas.get(i).getPanel().repaint();
		opponentDragAreas.get(i).getPanel().add(picLabel);
		opponentDragAreas.get(i).getPanel().validate();
	}
	
	public void setInWar(boolean war)
	{
		inWar = war;
		System.out.println("THE inWar VALUE AFTER setInWar");
		if(war)
		{
			gg.clearPlaySpace("War");
		}
	}
	
	public boolean getInWar()
	{
		return inWar;
	}
	
	public void clearWarPlaySpace(){
		for(int i = 0; i < myDragAreas.size(); i++)
		{
			myDragAreas.get(i).getPanel().removeAll();
			myDragAreas.get(i).getPanel().repaint();
			myDragAreas.get(i).getPanel().validate();
			opponentDragAreas.get(i).getPanel().removeAll();
			opponentDragAreas.get(i).getPanel().repaint();
			opponentDragAreas.get(i).getPanel().validate();
		}
	}
	
	public void displayWarCard (Card c)
	{
		dCard = c;
		//System.out.println("got to displayCard method");
		JLabel picLabel = null;
		try {
			picLabel = gg.drawCard(cd.mapCardtoImageString(dCard), opponentDragAreas.get(0), 0, 0);
		//	System.out.println("finished creating picLabel");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		initOpponentDragArea(0, picLabel);
		if(inWar)
		{
			try {
				picLabel = gg.drawCard("back.png", opponentDragAreas.get(1), 0, 0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			initOpponentDragArea(1, picLabel);
		}
		
	}
	
	public void setWarPlayerInfo(int player){
//		dragAreas.add(new GameJPanel());
//		dragAreas.add(new GameJPanel());
//		dragAreas.add(new GameJPanel());
//		dragAreas.add(new GameJPanel());
		if(player == 1)
		{
			myDragAreas.add(dragAreas.get(0));
			myDragAreas.add(dragAreas.get(1));
			opponentDragAreas.add(dragAreas.get(2));
			opponentDragAreas.add(dragAreas.get(3));
		}
		if(player == 2)
		{
			myDragAreas.add(dragAreas.get(2));
			myDragAreas.add(dragAreas.get(3));
			opponentDragAreas.add(dragAreas.get(0));
			opponentDragAreas.add(dragAreas.get(1));
		}
	}
		
	public WarHand generateHand(){
		WarHand hand = new WarHand();
		return hand;
	}
	
	public void createDragAreas(int numofPlayers, GameJPanel panel){
		createMiddlePanel(numofPlayers, panel);
	}
//	public void createDragAreas(int numofPlayers, GameJPanel panel){
//		dragAreas = createMiddlePanel(numofPlayers, panel);
//	}
	
	public void resetWarTracker(){
		warTracker = 0;
	}
	
	//Testing purpose
	public int returnWarTracker(){
		return warTracker;
	}
	public void setGraphics(GameGraphics graph)
	{
		this.gg = graph;
	}
}
