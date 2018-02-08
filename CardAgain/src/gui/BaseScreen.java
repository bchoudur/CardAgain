package gui;
import java.sql.SQLException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import server.MySQLGameAccess;

/**
 * BaseScreen handles the JFrame of the gui
 * and handles the switching of panels within
 * that JFrame
 * @author Jacob Richards
 *
 */
public class BaseScreen {
	
	public MySQLGameAccess ga = new MySQLGameAccess();
	private static JFrame framCardAgain = new JFrame("Card Again");
	private static Mainmenu m = new Mainmenu();
	private static Profile p = new Profile();
	private static LeaderBoard lb = new LeaderBoard();
	private static Serverbrowser sb = new Serverbrowser();
	private static ServerScreen ss = new ServerScreen();
	private static Lobby l = new Lobby();
	private static JComponent curPanel;
	public static String message = "You must play atleast 5 games to access this feature";
	public static String username = "User Name";
	
	/**
	 * The main method uses the startScreen method
	 * to initiate a default panel to run for the user
	 * and connects to the sql database
	 * @param args
	 */
	public static void main(String args[])
	{
		try {
			MySQLGameAccess.init();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(args != null)
		{
			username = args[0];
		}
		
		startScreen();

	}
	
	/**
	 * This method adds the first panel into the JFrame
	 * along with setting the size and layout of the JFrame
	 */
	public static void startScreen()
	{
		// Program ends when JFrame is closed
		framCardAgain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m.renderPanel();
		curPanel = m.getGui();
		framCardAgain.add(curPanel);
		
		// JFrame size and layout
		framCardAgain.setSize(1280, 720);
		framCardAgain.setLayout(null);
		framCardAgain.setVisible(true);
	}
	
	/**
	 * This method removes the current panel
	 * and adds the LeaderBoard panel
	 */
	public void switchToLeaderBoard()
	{
		framCardAgain.remove(curPanel);
		curPanel = lb.getGui();
		lb.renderPanel();
		framCardAgain.add(curPanel);
		framCardAgain.validate();
		framCardAgain.repaint();
	}
	
	/**
	 * This method removes the current panel
	 * and adds the Profile panel
	 */
	public void switchToProfile()
	{
		framCardAgain.remove(curPanel);
		curPanel = p.getGui();
		p.renderPanel();
		framCardAgain.add(curPanel);
		framCardAgain.validate();
		framCardAgain.repaint();
	}
	
	/**
	 * This method removes the current panel
	 * and adds the ServerBrowser panel
	 */
	public void switchToServerBrowser()
	{
		framCardAgain.remove(curPanel);
		curPanel = sb.getGui();
		sb.renderPanel();
		framCardAgain.add(curPanel);
		framCardAgain.validate();
		framCardAgain.repaint();
	}
	
	/**
	 * This method removes the current panel
	 * and adds the MainMenu panel
	 */
	public void switchToMainMenu()
	{
		framCardAgain.remove(curPanel);
		curPanel = m.getGui();
		m.renderPanel();
		framCardAgain.add(curPanel);
		framCardAgain.validate();
		framCardAgain.repaint();
	}
	
	public void switchToServerScreen()
	{
		if(MySQLGameAccess.getUserRoleFromDB(username) == 0)
		{
			JOptionPane.showMessageDialog(null, message, "InfoBox: " + "BaseScreen", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			framCardAgain.remove(curPanel);
			curPanel = ss.getGui();
			ss.renderPanel();
			framCardAgain.add(curPanel);
			framCardAgain.validate();
			framCardAgain.repaint();
		}
	}
	
	public void switchToLobby(boolean isUserHost)
	{
		l.setGame(ss.getGameName());
		framCardAgain.remove(curPanel);
		curPanel = l.getGui();
		l.renderPanel(isUserHost);
		framCardAgain.add(curPanel);
		framCardAgain.validate();
		framCardAgain.repaint();
	}
	
	public void logOut()
	{
		gui.BaseRegisterScreen.main(null);
		username = "";
		framCardAgain.dispose();
	}

}