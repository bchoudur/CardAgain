package gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import client.Client;
import client.GameController;
import client.GoFishGameController;
import client.WarGameController;
import communication.commands.CommandType;
import communication.commands.game.StartGameCommand;
import gameObjects.Games;
import io.socket.emitter.Emitter;

/**
 * Lobby handles the Lobby panel
 * @author Jacob Richards
 *
 */
public class Lobby {
	
	private JPanel panLobby = new JPanel();
	private Games game;
	private GameController gc;
	
	/**
	 * Adds all the buttons, labels, lines, etc.
	 * to the panel.
	 */
	public void renderPanel(boolean userIsHost)
	{
		
		// Panel
		panLobby.setLayout(null);
		panLobby.setBounds(0, 0, 1280, 720);
		
		//Chat Panel
		ChatPanel chat = new ChatPanel();
		
		// Buttons
		JButton buttLaunch = new JButton("Launch");
		
		buttLaunch.setForeground(new Color(0xffffff));
		buttLaunch.setBackground(new Color(0xF72224));
		
		
		// Labels
		JLabel labServerDets = new JLabel("Server Details:");
		JLabel labHostUser = new JLabel("Host User:");
		JLabel labGameMode = new JLabel("Game Mode:");
		JLabel labTotPlayers = new JLabel("Total Players: ");
		JLabel labPlayersCon = new JLabel("Connected Users: ");
		
		// Separating lines
		JSeparator sepHorz1 = new JSeparator(SwingConstants.HORIZONTAL);
		JSeparator sepVert = new JSeparator(SwingConstants.VERTICAL);
		
		//Button Placement and Size
		buttLaunch.setBounds(275,650,100,20);
		
		//Chat Pane Placement and Size
		chat.setBounds(410, 0, 815, 670);
		
		// Label Placement and Size
		labServerDets.setBounds(10,50,150,15);
		labHostUser.setBounds(10,100,150, 15);
		labGameMode.setBounds(10,150,150,15);
		labTotPlayers.setBounds(10,250,150,15);
		labPlayersCon.setBounds(10,400,150,15);
		
		// Separating Lines size
		sepHorz1.setPreferredSize(new Dimension(100, 20));
		sepHorz1.setBounds(0,350,400,15);
		sepVert.setBounds(400,0,15,720);
		
		// Adding buttons to frame
		//panLobby.add(buttSend);
		if(userIsHost){
			panLobby.add(buttLaunch);
		}
		
		// Adding Chat to frame
		panLobby.add(chat);
		
		// Adding labels to frame
		panLobby.add(labServerDets);
		panLobby.add(labHostUser);
		panLobby.add(labGameMode);
		panLobby.add(labTotPlayers);
		panLobby.add(labPlayersCon);

		// Adding Seperation Lines
		panLobby.add(sepHorz1);
		panLobby.add(sepVert);
		
		//If the user is the host, then don't forget to create the game controller and add the Launch button
		if(userIsHost){
			//add launching capability
			buttLaunch.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.out.println("Launching Card Game as Host");
					client.Client.cardNet.forwardCommand(utility.GsonHelper.toJson(new StartGameCommand(0,Games.WAR,0)),CommandType.STARTGAMECOMMAND.getName());
				}
			});
		}

		//Add a listener for the start of the game
		Client.cardNet.addOnCmd(CommandType.STARTGAMECOMMAND, new Emitter.Listener(){
			@Override
			public void call(Object... args) {
				StartGameCommand sgc = utility.GsonHelper.fromJson(args[0],StartGameCommand.class);
				launchGame(sgc);
			}
		});
		
 	}
	/**
	 * Returns the lobby panel
	 * @return the lobby panel
	 */
	public JComponent getGui()
	{
		return panLobby;
	}
	
	public void setGame(Games gameName)
	{
		game = gameName;
	}
	
	/**
	 * Properly instantiates the Game Controller based on received game type, and launches the game.
	 * @param sgc the received StartGameCommand
	 */
	public void launchGame(StartGameCommand sgc){
		System.out.println(utility.GsonHelper.toJson(sgc));
		switch(sgc.getGameType()) 
	      { 
	      case WAR: 
	    	System.out.println("Starting a War Game");
	    	gc = new WarGameController();
	        break;
	      case GOFISH:
	        System.out.println("Starting a Go Fish Game");
	    	gc = new GoFishGameController();
	    	break;
	      default:
    	    System.out.println("Game unrecognized, starting a War Game");
	        gc = new WarGameController(); 
	        break; 
	      }
		gc.start(sgc);
	}

}
