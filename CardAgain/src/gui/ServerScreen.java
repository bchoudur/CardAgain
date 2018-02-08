package gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.Client;
import communication.Callback;
import dataModels.GameServerProperties;
import gameObjects.Games;



//The Screen that allows a server host to select options for their server and game just before hosting
public class ServerScreen
{
	private int maximumPlayers;
	private int minimumPlayers;
	//server password will be null if there is none
	private String serverPassword;
	private int selectedGame;
	private JPanel panServerScreen;
	private Games selectedGameName;
	private BaseScreen Base = new BaseScreen();
	
	 //HUD elements, defeined here since Java doesn't like closures 
	JComboBox gameList;
	JLabel descriptions;
	JComboBox cbMaxPlayers;
	JComboBox cbMinPlayers;
	JTextField tfPassword;
	
	Font buttonFont = new Font("Serif Sana", Font.PLAIN, 20);
	 
	public ServerScreen()
	{
		//by default this screen has not finished it's purpose. Once this is true the loop in the main function exits and deconstructs this screen
		//by default minimum palyers is 2 and max is 8, this will change per game
		minimumPlayers = 2;
		maximumPlayers = 8;
		//the server password is null by default, if it remains null there will be no password for this server
		serverPassword = null;
		//0 is the index of GoFish the game that is  selected by default
		selectedGame = 0;
		panServerScreen = new JPanel();
	}

	public void renderPanel() 
	{
		panServerScreen.removeAll();
		panServerScreen.setLayout(null);
		panServerScreen.setBounds(0,0,1280,720);

		//The highest panel on the hierarchy, this panel holds the other panels
		//it's set in FlowLayout. This just puts it's objects added next to each other in a line
		panServerScreen.setLayout(new FlowLayout());
		//sets the main panel to the content pane so that it is the panel the users sees
		panServerScreen.setVisible(true);
		//The first sub panel, this will house the combo boxes and the host button
		JPanel subPanel1 = new JPanel();
		//This one is in Grid layout with a 1 column of 9 rows for the current 3 combo boxes and 1 textbox, each requiring their own label and a button 
		subPanel1.setLayout(new BoxLayout(subPanel1, BoxLayout.Y_AXIS));
		//creating the second sub panel. This one will house the description of the game  chosen in the combo box.
		JPanel subPanel2 = new JPanel();
		//this is another grid layout. This one of one column with 2 rows for the description and the label of the description
		subPanel2.setLayout(new GridLayout(2,1));
		//adding this sub panel to the main panel so it will appear first (leftmost)
		panServerScreen.add(subPanel1, BorderLayout.WEST);
		//adding sub panel 2 so it will be on the right
		panServerScreen.add(subPanel2, BorderLayout.EAST);
		//Descriptions Default String. It's Go Fish's explanation because that will be the first combo box choice (up for change as project moves forward)
		//This string uses HTML because JLabel doesn't support \n for a new line, but does support HTML
		//I'd be willing to take any suggestions on putting this string in any better looking fashion/tossing it somewhere obscure
		String defaultDescription = "<html>Go Fish is a simple card game. On your turn you are able to ask any player"
				+ "<br/>if they have a single card of your choosing."
				+ "<br/>If the player has that card they are required"
				+ "<br/>to give it to you. If, however, the player does"
				+ "<br/>not have the card they tell you to \"Go Fish\"<br/>and you pull the top card from the deck"
				+ "<br/>and add it to your hand."
				+ "<br/><br/>The goal of the game is to have the most"
				+ "<br/>matches at the end of the game. A match"
				+ "<br/>is made when you have two cards of the"
				+ "<br/>same number or roalty they are a match."
				+ "<br/>You remove them from your hand and"
				+ "<br/>place them next to you. The game ends"
				+ "<br/>when the deck is gone and all cards have"
				+ "<br/>been matched</html>";
		//creating the labels for displaying the game descriptions
		descriptions = new JLabel();
		descriptions.setText(defaultDescription);
		JLabel descLabel = new JLabel();
		descLabel.setText("Game Description:\n\n");
		descriptions.setSize(512, 256);
		subPanel2.add(descLabel);
		JPanel subPanelDescriptions = new JPanel();
		//setting another subpanel for the Descriptions Label so I can set a consistent size
		subPanelDescriptions.setLayout(new BorderLayout());
		subPanelDescriptions.setPreferredSize(new Dimension(512, 256));
		subPanelDescriptions.setBorder(BorderFactory.createLineBorder(Color.black));
		subPanelDescriptions.add(descriptions, BorderLayout.NORTH);
		subPanel2.add(subPanelDescriptions);
		//Array of strings for first combo box. Currently only has "Go Fish" and "War", but will expand
		String[] gameSelection = {"Go Fish", "War"};
		gameList = new JComboBox(gameSelection);
		gameList.setSize(new Dimension(200, 30));
		//adds the action listener to gameList
		gameList.addActionListener(new ActionListener() 
		{

            public void actionPerformed(ActionEvent e)
            {
            	selectedGame = gameList.getSelectedIndex();
            	switch (gameList.getSelectedIndex())
            	{
            	case 0: 
            		//the text here is in HTML because JLabels do not support \n
        			descriptions.setText("<html>Go Fish is a simple card game. On your turn you are able to ask any player"
        				+ "<br/>if they have a single card of your choosing."
        				+ "<br/>If the player has that card they are required"
        				+ "<br/>to give it to you. If, however, the player does"
        				+ "<br/>not have the card they tell you to \"Go Fish\"<br/>and you pull the top card from the deck"
        				+ "<br/>and add it to your hand."
        				+ "<br/><br/>The goal of the game is to have the most"
        				+ "<br/>matches at the end of the game. A match"
        				+ "<br/>is made when you have two cards of the"
        				+ "<br/>same number or roalty they are a match."
        				+ "<br/>You remove them from your hand and"
        				+ "<br/>place them next to you. The game ends"
        				+ "<br/>when the deck is gone and all cards have"
        				+ "<br/>been matched</html>");
        			break;
        		case 1:
        			descriptions.setText("<html>War is a simple card game made to be played with two players" +
        					"<br/>each player gets half of the deck. They do not look at their cards"
        					+ "<br/>On a turn of the game both players place the top card from their deck"
        					+ "<br/>in the play area. Whoever has the higher cards adds the cards to the bottom"
        					+ "<br/>of their hand. In the event of two cards with the same value, the game goes"
        					+ "<br/>into war. Each player must place the topp three cards face down into the"
        					+ "<br/>play area then must place the top card face up into the play area. The player"
        					+ "<br/>with the higher face up card gets all of the cards added to the bottom of their"
        					+ "<br/>of their hand. If the face up cards are the same value in a war, you add 3 more"
        					+ "<br/>face down cards to the pile and place another card face up to compare. War continues"
        					+ "<br/>until someone has a higher card and then gets all of the play cards in the play area"
        					+ "<br/>added to the bottom of their hand. The game is won when one player posses all of the"
        					+ "<br/>cards in the deck, leaving the other player empty handed</html>");
        			break;
            	}
            }
		});
		JLabel listLabel = new JLabel();
		//adds the label and the combo box to the panel
		listLabel.setText("Select Game:");
		//Eventually each game will get it's own set of max and min players to select from
		String[] maxSelection = {"2", "3", "4", "5", "6", "7", "8"};
		cbMaxPlayers = new JComboBox(maxSelection);
		JLabel maxLabel = new JLabel();
		maxLabel.setText("Max Players:");
		String[] minSelection = {"2", "3", "4", "5", "6", "7", "8"};
		cbMinPlayers = new JComboBox(minSelection);
		JLabel minLabel = new JLabel();
		minLabel.setText("Min Players:");
		//the password will not exist if nothing is entered, if something is entered, that will be the password for this server
		tfPassword = new JTextField();
		JLabel passwordLabel = new JLabel();
		passwordLabel.setText("Server Password (optional):");
		JButton createServer = new JButton("Create Server");
		
		createServer.setForeground(new Color(0xffffff));
		createServer.setBackground(new Color(0xF72224));
		createServer.setFont(buttonFont);
		//adding an action listener to createServer 
		createServer.addActionListener(new ActionListener() 
		{
			//this action listener sets the objects parameters for use on the next step. Currently just exits the screen, will do more once
			//we decide how screens will communicate
            public void actionPerformed(ActionEvent e)
            {
            	maximumPlayers = Integer.parseInt(cbMaxPlayers.getSelectedItem().toString());
            	minimumPlayers = Integer.parseInt(cbMinPlayers.getSelectedItem().toString());
            	selectedGame = gameList.getSelectedIndex();
            	//Games is an enum that we use instead of keeping track of the format of a specific string
                switch(selectedGame){
                	case 0:
                		selectedGameName = Games.GOFISH;
                		break;
                	case 1:
                		selectedGameName = Games.WAR;
                		break;
                	default:
                		selectedGameName = Games.GOFISH;
                }
                //if the password textbox has text, it will set the password to that text. Otherwise it will remain null.
            	if(!(tfPassword.getText().equals("")))
            	{
            		serverPassword = tfPassword.getText();
            	}
            	
            	//Construct an object with these options for server creation
            	GameServerProperties gameServerOptions = new GameServerProperties("New Game", selectedGameName, minimumPlayers, maximumPlayers, serverPassword);
            	Client.cardNet.createGameServer(gameServerOptions, new Callback(){
					@Override
					public void call(Object... args) {
						String response = utility.GsonHelper.strFromJson(args[0]);
						if(!response.equals("fail")){
							if(Client.cardNet.joinGame(response)){
								System.out.println("Successfully created and connected to " + response);
								new BaseScreen().switchToLobby(true);
							};
						}
					}
            	});
            	
            }
		});
		JButton goBack = new JButton("<-- Return");
		goBack.setForeground(new Color(0xffffff));
		goBack.setBackground(new Color(0xF72224));
		goBack.setFont(buttonFont);
		
		goBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Base.switchToMainMenu();
			}
		});
		subPanel1.add(listLabel);
		subPanel1.add(gameList);
		subPanel1.add(maxLabel);
		subPanel1.add(cbMaxPlayers);
		subPanel1.add(minLabel);
		subPanel1.add(cbMinPlayers);
		subPanel1.add(passwordLabel);
		subPanel1.add(tfPassword);
		subPanel1.add(createServer);
		subPanel1.add(goBack);
	}
	public JComponent getGui()
	{
		return panServerScreen;
	}
	
	public Games getGameName()
	{
		return selectedGameName;
	}

}