package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import communication.commands.ChatCommand;
import communication.commands.CommandType;
import io.socket.emitter.Emitter.Listener;
import server.MySQLGameAccess;

public class ChatPanel extends JPanel{
	private JTextArea txt;
	private JScrollPane sp;
	private JTextField typeHere;
	
	public ChatPanel(){
		//setup chat pane
		txt = new JTextArea();
		txt.setEditable(false);
		sp = new JScrollPane(txt);
		sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		//setup typing field
		typeHere = new JTextField();
		typeHere.setForeground(Color.GRAY);
		
		//restrict sending chat to role 1 and higher
		if(MySQLGameAccess.getUserRoleFromDB(client.Client.authenticatedUser.getUsername()) == 0)
		{
			typeHere.setText("Chat is disabled until you've played a few games, sorry...");
			typeHere.setEditable(false);
		}else{
			typeHere.setText("Type here to chat, press enter to send...");
			//add handlers to send chat messages.		
			typeHere.addKeyListener(new KeyListener(){
	
				@Override
				public void keyTyped(KeyEvent e) {}
				@Override
				public void keyPressed(KeyEvent e) {}
				@Override
				public void keyReleased(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER){
						sendChat();
					}
				}
			});
			
			//Add listeners for managing placeholder text
			typeHere.addFocusListener(new FocusListener() {
			    @Override
			    public void focusGained(FocusEvent e) {
			        if (typeHere.getText().equals("Type here to chat, press enter to send...")) {
			        	typeHere.setText("");
			        	typeHere.setForeground(Color.BLACK);
			        }
			    }
			    @Override
			    public void focusLost(FocusEvent e) {
			        if (typeHere.getText().isEmpty()) {
			        	typeHere.setForeground(Color.GRAY);
			        	typeHere.setText("Type here to chat, press enter to send...");
			        }
			    }
		    });
		}
		
		
		//clear any other chat listeners
		client.Client.cardNet.offCmd(CommandType.CHAT);
		//adds chat to the box when it's received
		client.Client.cardNet.addOnCmd(CommandType.CHAT, new Listener(){
			@Override
			public void call(Object... args) {
				ChatCommand cmd = utility.GsonHelper.fromJson(args[0],ChatCommand.class);
				txt.append(cmd.getUser() + ": " + cmd.getMessage() + "\n");
			}	
		});
		
		//add components to this panel
		this.add(sp,BorderLayout.CENTER);
		this.add(typeHere, BorderLayout.SOUTH);
	}
	
	//sends a chat message and clears the text box
	private void sendChat(){
		String message = typeHere.getText();
		if(message.length() > 0){
			ChatCommand cc = new ChatCommand(client.Client.authenticatedUser.getUsername(),message);
			client.Client.cardNet.forwardCommand(utility.GsonHelper.toJson(cc), CommandType.CHAT.getName());
		}
		typeHere.setText("");
	}
	
	//override to properly size the components when setBounds is called on the panel.
	@Override
	public void setBounds(int x, int y, int width, int height){
		super.setBounds(x,y,width,height);
		this.setPreferredSize(new Dimension(width,height));
		typeHere.setPreferredSize(new Dimension(width,30));
		sp.setPreferredSize(new Dimension(width,height-40));
		
	}
}
