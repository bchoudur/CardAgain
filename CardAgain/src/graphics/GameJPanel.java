package graphics;

import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class GameJPanel
{
	private JPanel userPanel;
	private int panelOwner = -1;
	public GameJPanel()
	{
		userPanel = new JPanel();
	}
	public void setPlayer(int player)
	{
		panelOwner = player;
	}
	public int getPlayer()
	{
		return panelOwner;
	}
	public JPanel getPanel()
	{
		return userPanel;
	}
	public void setMouseListener(MouseListener list)
	{
		userPanel.addMouseListener(list);
	}
}
