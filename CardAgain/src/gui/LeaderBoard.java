package gui;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;


/**
 * LeaderBoard handles the LeaderBoard panel
 * @author Jacob Richards
 *
 */
public class LeaderBoard {
	
	private JPanel panLeaderBoard = new JPanel();
	private BaseScreen Base = new BaseScreen();
	
	Font buttonFont = new Font("Serif Sana", Font.PLAIN, 20);
	
	/**
	 * Adds all the buttons, labels, lines, etc.
	 * to the panel.
	 */
	public void renderPanel()
	{
		
		// Panel
		panLeaderBoard.setLayout(null);
		panLeaderBoard.setBounds(0,0,1280,720);
		
		// Buttons
		JButton buttMainMenu = new JButton("Main Menu");
		JButton buttServerBrowser = new JButton("Server Browser");
		JButton buttHostServers = new JButton("Host Server");
		JButton buttProfile = new JButton("Profile");
		JButton buttLogout = new JButton("Logout");
		
		// Labels
		JLabel labName = new JLabel("Name");
		JLabel labGamesPlayed = new JLabel("Games Played");
		JLabel labUserType = new JLabel("User Type");
		JLabel labWins = new JLabel("Wins");
		JLabel labLosses = new JLabel("Losses");
		
		// Separator Line
		JSeparator sepHorz1 = new JSeparator(SwingConstants.HORIZONTAL);
		
		// Table for leaderboard listings
		JTable lbListings = new JTable();
		DefaultTableModel model = (DefaultTableModel) lbListings.getModel();
		
		// Table attributes
		lbListings.getTableHeader().setReorderingAllowed(false);
		lbListings.setTableHeader(null);
		lbListings.setShowGrid(false);
		lbListings.setRowHeight(20);
		lbListings.setFont(new Font("Serif", Font.BOLD, 15));
		lbListings.setEnabled(false);
		
		// Columns that are added
		model.addColumn("Name");
		model.addColumn("Games Played");
		model.addColumn("User Type");
		model.addColumn("Wins");
		model.addColumn("Losses");
		
		
			// Rows that are added
		Base.ga.addUsersToLeaderBoard(model);
		
		
		// Add a scroll pane for leaderboard table
		JScrollPane lbsp = new JScrollPane(lbListings);
		lbsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		
		// Button Placement and Size
		buttMainMenu.setBounds(0,0,256,50);
		buttServerBrowser.setBounds(256,0,256,50);
		buttHostServers.setBounds(512,0,256,50);
		buttProfile.setBounds(768,0,256,50);
		buttLogout.setBounds(1020,0,256,50);
		
		// Label Placement and Size
		labName.setBounds(265,150,150,15);
		labGamesPlayed.setBounds(422,150,150,15);
		labUserType.setBounds(579,150,150,15);
		labWins.setBounds(736,150,150,15);
		labLosses.setBounds(893,150,150,15);
		
		// Separator Line Placement
		sepHorz1.setBounds(265,170,785,15);
		
		// Scrollpane placement (scrollpane contains the table within it) 
		lbsp.setBounds(265, 200, 785, 400);
		lbsp.getViewport().setBackground(Color.WHITE);
		
		// Adding Buttons
		panLeaderBoard.add(buttMainMenu);
		panLeaderBoard.add(buttServerBrowser);
		panLeaderBoard.add(buttHostServers);
		panLeaderBoard.add(buttProfile);
		panLeaderBoard.add(buttLogout);
		
		buttMainMenu.setForeground(new Color(0xffffff));
		buttMainMenu.setBackground(new Color(0xF72224));
		buttMainMenu.setFont(buttonFont);
		
		buttServerBrowser.setForeground(new Color(0xffffff));
		buttServerBrowser.setBackground(new Color(0xF72224));
		buttServerBrowser.setFont(buttonFont);
		
		buttHostServers.setForeground(new Color(0xffffff));
		buttHostServers.setBackground(new Color(0xF72224));
		buttHostServers.setFont(buttonFont);
		
		buttProfile.setForeground(new Color(0xffffff));
		buttProfile.setBackground(new Color(0xF72224));
		buttProfile.setFont(buttonFont);
		
		buttLogout.setForeground(new Color(0xffffff));
		buttLogout.setBackground(new Color(0xF72224));
		buttLogout.setFont(buttonFont);
		
		
		// action listener for buttons
		buttMainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Base.switchToMainMenu();
			}
		});
				
		buttProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Base.switchToProfile();
			}
		});
				
		buttServerBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Base.switchToServerBrowser();
			}
		});
		
		buttLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Base.logOut();
			}
		});
		buttHostServers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Base.switchToServerScreen();
			}
		});
		
		// Adding Labels
		panLeaderBoard.add(labName);
		panLeaderBoard.add(labGamesPlayed);
		panLeaderBoard.add(labUserType);
		panLeaderBoard.add(labWins);
		panLeaderBoard.add(labLosses);
		
		// Adding Separation Line
		panLeaderBoard.add(sepHorz1);
		
		// Adding Scrollpane with table
		panLeaderBoard.add(lbsp);
		//panLeaderBoard.add(lbListings);
	}
	
	/**
	 * Returns the leaderboard panel
	 * @return the leaderboard panel
	 */
	public JComponent getGui()
	{
		return panLeaderBoard;
	}

}
