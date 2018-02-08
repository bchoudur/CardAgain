package gui;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Type;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.google.gson.reflect.TypeToken;

import client.Client;
import communication.Callback;
import communication.commands.GameListingCommand;
import dataModels.GameServerProperties;


/**
 * ServerBroswer handles the ServerBrowser panel
 * @author Jacob Richards
 *
 */
public class Serverbrowser {
	
	private JPanel panSB = new JPanel();
	private BaseScreen Base = new BaseScreen();
	private LinkedList<GameServerProperties> serverList;
	private JTable tblServers;
	private DefaultTableModel serverTable;
	Font buttonFont = new Font("Serif Sana", Font.PLAIN, 20);

	/**
	 * Adds all the buttons, labels, lines, etc.
	 * to the panel.
	 */
	public void renderPanel()
	{
		
		//Panel
		panSB.setLayout(null);
		panSB.setBounds(0, 0, 1280, 720);
		
		// Buttons
		JButton buttReturn = new JButton("<-- Return");
		JButton buttSend = new JButton("Send");
		
		buttReturn.setForeground(new Color(0xffffff));
		buttReturn.setBackground(new Color(0xF72224));
		buttReturn.setFont(buttonFont);
		
		buttSend.setForeground(new Color(0xffffff));
		buttSend.setBackground(new Color(0xF72224));
		
		// TextFields
		JTextField tfGameType = new JTextField("");
		JTextField tfFriendOnly = new JTextField("");
		JTextField tfGameName = new JTextField("");
		JTextField tfChat = new JTextField("Type here to chat");
		
		// Labels
		JLabel labGameType = new JLabel("Gametype");
		JLabel labFriendOnly = new JLabel("Friends Only");
		JLabel labGameName = new JLabel("Game Name");
		JLabel labServerName = new JLabel("Server Name");
		JLabel labGameMode = new JLabel("Game Mode");
		JLabel labPlayers = new JLabel("Players");
		JLabel labPing = new JLabel("Ping");
		
		//Table
		tblServers = new JTable();
		serverTable = (DefaultTableModel) tblServers.getModel();
		tblServers.setRowSelectionAllowed(true);
		tblServers.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		// Table attributes
		tblServers.getTableHeader().setReorderingAllowed(false);
		tblServers.setTableHeader(null);
		tblServers.setShowGrid(true);
		tblServers.setRowHeight(20);
		tblServers.setFont(new Font("Serif", Font.BOLD, 15));
		tblServers.setEnabled(false);
		
		// Columns that are added
		serverTable.addColumn("Name");
		serverTable.addColumn("Games Mode");
		serverTable.addColumn("Players");
		serverTable.addColumn("ID");
		
		// Add a scroll pane for server browser table
		JScrollPane serverScrollPane = new JScrollPane(tblServers);
		serverScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		//allow the user to click on a server to join it
		tblServers.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				JTable source = (JTable)e.getSource();
				int row = source.rowAtPoint(e.getPoint());
				System.out.println("Joining " + source.getValueAt(row, 3));
				
				if (Client.cardNet.joinGame((String) source.getValueAt(row, 3))){
					//joined successfully					
					new BaseScreen().switchToLobby(false);
				};
			}
		});
				
		// Button Placement and Size
		buttReturn.setBounds(50,550,150,50);
		buttSend.setBounds(900,580,75,20);
		
		// TextFields Placement and Size
		tfGameType.setBounds(50,125,150,20);
		tfFriendOnly.setBounds(50,175,150,20);
		tfGameName.setBounds(50,225,150,20);
		
		tfChat.setBounds(350,580,550,20);
		
		
		// Labels Placement and Size
		labGameType.setBounds(50,100,100,15);
		labFriendOnly.setBounds(50,150,100,15);
		labGameName.setBounds(50,200,100,15);
		labServerName.setBounds(350,100,100,15);
		labGameMode.setBounds(600,100,100,15);
		labPlayers.setBounds(900,100,100,15);
		labPing.setBounds(1050,100,100,15); 
		
		//Adding buttons to the frame
		panSB.add(buttReturn);
		panSB.add(buttSend);
		
		// action listener for buttons
		buttReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Base.switchToMainMenu();
			}
		});
		
		//Adding textfields to the frame
		panSB.add(tfGameType);
		panSB.add(tfFriendOnly);
		panSB.add(tfGameName);
		panSB.add(tfChat);
		
		//Adding labels to the frame
		panSB.add(labGameType);
		panSB.add(labFriendOnly);
		panSB.add(labGameName);
		panSB.add(labServerName);
		panSB.add(labGameMode);
		panSB.add(labPlayers);
		panSB.add(labPing);
		
		//add the server table
		serverScrollPane.setBounds(350, 125, 800, 440);
		refreshData();
		panSB.add(serverScrollPane);
	}
	/**
	 * Returns the serverbrowser panel
	 * @return the serverbrowser panel
	 */
	public JComponent getGui()
	{
		return panSB;
	}
	
	public void refreshData(){
		System.out.println("Fetching Server Listings...");
		Client.cardNet.emitCommand(new GameListingCommand(), new Callback(){
			@Override
			public void call(Object... args) {
				Type listType = new TypeToken<LinkedList<GameServerProperties>>(){}.getType();
				serverList = utility.GsonHelper.gson.fromJson((String)args[0], listType);
				GameServerProperties gsp;
				//render table
				serverTable.setRowCount(0);
				for(int i = 0; i<serverList.size(); i++){
					gsp = serverList.get(i);
					serverTable.addRow(new String[] {gsp.getServerName(),gsp.getGameModeString(),gsp.getCurrentPlayerCount() + "/" + gsp.getMaxPlayerCount(), gsp.getID()});
				}
			}});
	}

}
