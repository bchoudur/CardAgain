package graphics;

//import java.awt.Canvas;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import client.GameController;
import gameObjects.Card;
import gameObjects.Hand;
import gui.BaseScreen;

public class GameGraphics //extends Canvas{
{
	static JFrame gameWindow;
	private static int windowWidth = 1600;
	private static int windowHeight = 1024;
	static ArrayList<GameJPanel> panelList = new ArrayList<GameJPanel>();
	static GameJPanel middlePanel;
	static GameJPanel panels;
	private GameJPanel myPanel;
	private WarLayout wl;
	private GoFishLayout gf;
	private int currPlayer;
	private CardDictionary cd;
	private int gameSize = 4;
	protected String game;
	
	
	//Testing
	private ArrayList<String> urlPaths = new ArrayList<String>();
	//0 for no cards played, 1 for war card played and 2 for both cards played
	//private int warTracker;
	private ArrayList<Hand> hands;
	
	boolean pSpaceOpen = true;
	Card rCard;
	private boolean myTurn = false;
	public GameController gc;
	
	private String warGameString = "War";
	private String goFishString = "Fish";
	
	
	/**
	 * Creates a GameController object, sets the layout depending the on the
	 * String (type) at is passed in and String type is the kind of game being
	 * processed
	 * @param gc
	 * @param lay
	 * @param type
	 */
	public GameGraphics(GameController gc, ParentLayout lay, String type){
		game = type;
		this.gc = gc;
		cd = new CardDictionary();
		if(game.equals(warGameString))
		{
			wl = (WarLayout)lay;
			gf = null;
		}
		if(game.equals(goFishString))
		{
			gf = (GoFishLayout)lay;
			wl = null;
		}
	}
	
	
	/**
	 * 
	 * @return A CardDictionary object use to get the image url string for the card images
	 */
	public CardDictionary getCardDictionary(){
		return this.cd;
	}
	
	/**
	 * sets the main gamewindow (JFrame) visible to true so it can see
	 */
	public void showWindow(){
		gameWindow.setVisible(true);
	}
	
	/**
	 * This method is used to sortend the createGameWindow by taking care of creating the player panels,
	 * populating the player's  panel and then adding it by calling three different methods
	 * @param game
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param playerPanelNumber
	 * @throws IOException
	 */
	private void setUpPlayerPanel(String game, int x, int y, int width, int height, int playerPanelNumber) throws IOException{
		if(currPlayer == playerPanelNumber)
		{
		//	if(playerPanelNumber == 1)
			//{
				System.out.println("BC: if(playerPanelNumber == 1) EXECUTED");
				myPanel = createPanels(x, y, width, height);
				myPanel.setPlayer(currPlayer);
			//}
		//	else if(playerPanelNumber == 2)
//			{
//				System.out.println("BC: if(playerPanelNumber == 2) EXECUTED");
//				myPanel = createPanels(x, y, width, height);
//				myPanel.setPlayer(2);
//			}
			populatePlayerPanel(game, myPanel, playerPanelNumber);
			panelList.add(myPanel);
		}
		else
		{
			//Player 1 in the bottom center
			panels = createPanels(x, y, width, height);
			if(playerPanelNumber > 1)
				panels.setPlayer(playerPanelNumber);
			populatePlayerPanel(game, panels, playerPanelNumber);
			panelList.add(panels);
		}
		System.out.println("BC: SetUpPlayerPanel EXECUTED");
	}
	
	/**
	 * 
	 * @param game: String that tells what game your using to populate the player's panel
	 * @param targetNum: Use to keep track of what player panel is drawing 
	 * @throws IOException
	 * Setups up the player panel by seeing base off of the type of game 
	 */
	private void populatePlayerPanel(String game, GameJPanel panel, int playerPanelNum) throws IOException{
		if(game.equals(warGameString)){
			panel = wl.populatePlayerWarPanel(panel, playerPanelNum);
		}
		if(game.equals(goFishString)){
			panel = gf.populateGoFishPlayerPanel(panel, playerPanelNum);
		}
	}
	
		
	/**
	  Places down the jpanels based off of the number of players is in the game
	  The panels are stored in an arraylist with index 0 being the first player and up
	 * @param game
	 * @param numofPlayers
	 * @throws IOException
	 */
	public void createGameWindow(String game, int numofPlayers) throws IOException{
		initGameWindow();
		
		for(int i = 0; i < numofPlayers; i++){
			middlePanel = createPanels((windowWidth/2) - 475, 310, 950, 350);
			if(game.equals(warGameString)){
				wl.createMiddlePanel(2, middlePanel);
			}
			if(game.equals(goFishString)){
				gf.createMatchingArea(middlePanel);
			}
			switch (i){
				case 0: 
					setUpPlayerPanel(game, (windowWidth/2) - 475, windowHeight - 350, 950, 300, 1);
					break;
				case 1:
					setUpPlayerPanel(game, (windowWidth/2) - 475,0, 950, 300, 2);
					break;
				case 2:
					//Player 3 on the left side
					setUpPlayerPanel(game, 5,15, 300, 950, 3);
					break;
				case 3: 
					//Player 4 on the right side
					setUpPlayerPanel(game, windowWidth - 320, 15, 300, 950, 4);
					break;
			}
			System.out.println("BC: CreateGameWindow EXECUTED");
		}
		
		gameWindow.add(middlePanel.getPanel());
		
		for(int i = 0; i < numofPlayers; i++){
			gameWindow.add(panelList.get(i).getPanel());
		} 
	 }
	
	/**
	 * Creates the panels for the game
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return panel
	 */
	public GameJPanel createPanels(int x, int y, int width, int height){
		GameJPanel panel = new GameJPanel();
		panel.getPanel().setLayout(null);
		panel.getPanel().setLocation(x, y);
		panel.getPanel().setSize(width, height);
		panel.getPanel().setVisible(true);
		return panel;
	}
	
	
	/**
	 * Returns a JLabel what is what the images will set in
	 * @param file
	 * @param panel
	 * @param x
	 * @param y
	 * @return picLabel
	 * @throws IOException
	 */
	public JLabel drawCard(String file, GameJPanel panel, int x, int y) throws IOException{
		String filePath = "src/graphics/Playing_Cards/PNG-cards/";
		ImageIcon image = new ImageIcon(new ImageIcon(filePath + file).getImage().getScaledInstance(150, 200, Image.SCALE_DEFAULT));
		JLabel picLabel = new JLabel();
			picLabel.setIcon(image);
			picLabel.setSize(150, 200);
			picLabel.setLocation(x, y);
		return picLabel;
	}
	
	
	/**
	 * Creates a button
	 * @param x
	 * @param y
	 * @param b
	 * @return b
	 */
	public JButton drawButton(int x, int y, JButton b){
		b.setBounds(0, 0, 150, 50);
		b.setLocation(x, y);
		return b;
	}
	
	
	/**
	 * Sets the game string used to keep track of what game is being process
	 * @param newGameString
	 */
	public void setGameString(String newGameString){
		this.game = newGameString;
	}
	
	/**
	 * This methods adds the cards to the game and checks for players turn.
	 * Depending on what game is be process, if the game is war then it calls
	 * the addWarCard method that takes care of the rest
	 * @param panel
	 * @param label
	 */
	public void addCard(final GameJPanel panel, JLabel label){
		label.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				int test = panel.getPlayer();
				//System.out.println("Panel.getPlayer() is " + test + " while currPlayer is " +currPlayer);
				
				//String fileName = iconfilename.substring(iconfilename.lastIndexOf("/"  ) + 1);
				
				if(currPlayer == panel.getPlayer() && myTurn){
			//		hasCard = false;
					//System.out.println("currPlayer" + currPlayer + "has passed through the if");
					
					if(game.equals(warGameString)){
						wl.addWarCard(panel, (JLabel)e.getSource(), rCard, myTurn, pSpaceOpen, gc);
					}
					
				/*	if(game.equals(warGameString)){
						try {
				//			gf.addCardToMatchingArea(panel, (JLabel)e.getSource());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}*/
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
		});
		panel.getPanel().repaint();
		panel.getPanel().add(label);
		panel.getPanel().validate();
	}
	
	/**
	 * Sets the gameWindow size and layout
	 */
	private void initGameWindow(){
		gameWindow = new JFrame("Player " + currPlayer);
		gameWindow.setSize(windowWidth, windowHeight);
		gameWindow.setLayout(null);
	}
	
	//Not sure if this will have to be in go fish so I created the parts the would I believe could
	//Universal and the parts that are related to only gofish in another method clearWarPlaySpace
	/**
	 * Clears the players drag areas (were the cards are compared in war) and allows
	 * for them not to be clear instantanly but rather in 1.5 seconds
	 * @param game
	 */
	public void clearPlaySpace(String game)
	{
		//1.5s delay before clearing
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(game.equals(warGameString)){
			wl.clearWarPlaySpace();
		}
		
		pSpaceOpen = true;
	}
	
	
	/**
	 * Sets up gameWindow based of what player is passed in.
	 * @param game
	 * @param player
	 */
	public void setPlayerInfo(String game, int player)
	{
		System.out.println("The player is " + player);
		currPlayer = player;
		if(game.equals(warGameString)){
			try {
				createGameWindow(warGameString, 2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			wl.setWarPlayerInfo(player);
		}
		else if(game.equals(goFishString))
		{
			try {
				createGameWindow(goFishString, 4);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//showWindow();
	}
	
	
	/**
	 * Calles the method resetWarTracker if the game is war
	 * @param game
	 * @param c
	 */
	public void recieveCard(String game, Card c)
	{
		System.out.println("Game Graphics recieved a " + c.getSuitAndValue() + " card.");
		rCard = c;
		if(game.equals(warGameString)){
			wl.resetWarTracker();
		}
		
	}
	
	/**
	 * calls the desplayWarCard if the game is war
	 * @param game
	 * @param c
	 */
	public void displayCard(String game, Card c){
		if(game.equals(warGameString)){
			wl.displayWarCard(c);
		}
	}
	
	/**
	 * Starts the turn for next player
	 * @param game
	 */
	public void startTurn(String game)
	{
		if(game.equals(warGameString)){
			wl.resetWarTracker();
		}
		
		myTurn = true;
	}
	
	/**
	 * returns the size of the game (number of players)
	 * @return
	 */
	public int getGameSize()
	{
		return gameSize;
	}
	
	/**
	 * returns the current player
	 * @return currPlayer
	 */
	public int getCurrPlayer()
	{
		return currPlayer;
	}
	//TEMPORARY
	public void setGFLayout(GoFishLayout gboi)
	{
		this.gf = gboi;
	}
	
	/**
	 * Sets the WarLayout variable
	 * @param w
	 */
	public void setWarLayout(WarLayout w)
	{
		this.wl = w;
	}
	
	/**
	 * 
	 * @return myTurn
	 */
	public boolean getTurn()
	{
		return myTurn;
	}
	
	/**
	 * returns the game controller object
	 * @return gc
	 */
	public GameController getGameController()
	{
		return gc;
	}
	
	//This method is for the GoFishLayout. It will need access to the player panels to empty out and restock a players hand
	/**
	 * returns a JPanel
	 * @return panelList
	 */
	public ArrayList<GameJPanel> getPlayerPanels()
	{
		return panelList;
	}
}
