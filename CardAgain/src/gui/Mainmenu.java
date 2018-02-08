package gui;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * MainMenu handles the MainMenu panel
 * @author Jacob Richards
 *
 */
public class Mainmenu {
	
	private JPanel panMainMenu = new JPanel();
	private BaseScreen Base = new BaseScreen();
	//private BaseRegisterScreen baseRegister = new BaseRegisterScreen();
	
	Font buttonFont = new Font("Serif Sana", Font.PLAIN, 20);
	
	/**
	 * Adds all the buttons, labels, lines, etc.
	 * to the panel.
	 */
	public void renderPanel()
	{
		// The window that is used
		//JFrame framGui= new JFrame("CardAgain");
		
		//Main Menu Panel
		panMainMenu.setLayout(null);
		panMainMenu.setBounds(0,0,1280,720);
		panMainMenu.setBackground(new Color(0x2E2E2E));
		
		// All of the Main Menu buttons
		JButton buttPlayNow = new JButton("Play Now!");
		JButton buttHostServer = new JButton("Host Server");
		JButton buttServerBrowser = new JButton("Server Browser");
		JButton buttLeaderBoards = new JButton("Leaderboards");
		JButton buttProfileSettings = new JButton("Profile Settings");
		JButton buttLogout = new JButton("Logout");
		
		// The labels
		JLabel labMain = new JLabel("Main Menu");
		
		//TODO Change username to variable
		JLabel labUserName = new JLabel(Base.username);
		
		labMain.setForeground(new Color(0xffffff));
		labUserName.setForeground(new Color(0xffffff));
		
		// Where each button is placed on the window

		buttPlayNow.setBounds(320,250,640,50); // x axis, y axis, width, height
		buttHostServer.setBounds(320,310,640,50);
		buttServerBrowser.setBounds(320,370,640,50);
		buttLeaderBoards.setBounds(320,430,640,50);
		buttProfileSettings.setBounds(320,490,640,50);
		buttLogout.setBounds(320,550,640,50);
		
		// Where the label is placed and the size
		labMain.setBounds(50, 50, 375, 100);
		labMain.setFont(new Font("Serif", Font.PLAIN, 75));
		labUserName.setBounds(1025, 50, 150, 100);
		labUserName.setFont(new Font("Arial", Font.PLAIN, 25));
		
		// Panel Placement
		panMainMenu.setBounds(0,0,1280,720);
		
		// adding each button to the frame
		panMainMenu.add(buttPlayNow);
		panMainMenu.add(buttHostServer);
		panMainMenu.add(buttServerBrowser);
		panMainMenu.add(buttLeaderBoards);
		panMainMenu.add(buttProfileSettings);
		panMainMenu.add(buttLogout);
		
		//Styles
		buttPlayNow.setForeground(new Color(0xffffff));
		buttPlayNow.setBackground(new Color(0xF72224));
		buttPlayNow.setFont(buttonFont);
		
		buttHostServer.setForeground(new Color(0xffffff));
		buttHostServer.setBackground(new Color(0xF72224));
		buttHostServer.setFont(buttonFont);
		
		buttServerBrowser.setForeground(new Color(0xffffff));
		buttServerBrowser.setBackground(new Color(0xF72224));
		buttServerBrowser.setFont(buttonFont);
		
		buttLeaderBoards.setForeground(new Color(0xffffff));
		buttLeaderBoards.setBackground(new Color(0xF72224));
		buttLeaderBoards.setFont(buttonFont);
		
		buttProfileSettings.setForeground(new Color(0xffffff));
		buttProfileSettings.setBackground(new Color(0xF72224));
		buttProfileSettings.setFont(buttonFont);
		
		buttLogout.setForeground(new Color(0xffffff));
		buttLogout.setBackground(new Color(0xF72224));
		buttLogout.setFont(buttonFont);
		
		// action listener for buttons
		buttLeaderBoards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Base.switchToLeaderBoard();
			}
		});
		
		buttProfileSettings.addActionListener(new ActionListener() {
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
		buttHostServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Base.switchToServerScreen();
			}
		});
		
		
		// adding the label to the frame
		panMainMenu.add(labMain);
		panMainMenu.add(labUserName);
	}
	/**
	 * Returns the mainmenu panel
	 * @return the mainmenu panel
	 */
	public JComponent getGui()
	{
		return panMainMenu;
	}

}
