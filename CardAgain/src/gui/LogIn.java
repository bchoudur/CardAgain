package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import communication.Callback;
import communication.commands.LoginCommand;
import dataModels.User;
import utility.GsonHelper;

public class LogIn extends JFrame {
	
	JPanel logInPanel = new JPanel();
	BaseRegisterScreen baseRegister = new BaseRegisterScreen();
	BaseScreen Base = new BaseScreen();
	
	
	JTextField usernameText, passwordText;
	Font mainFont = new Font("Serif San", Font.PLAIN, 14);
	Font buttonFont = new Font("Serif Sana", Font.PLAIN, 20);
	
	public void renderPanel(){
		logInPanel.setLayout(null);
		logInPanel.setBounds(0, 0, 600, 600);
		logInPanel.setBackground(new Color(0x2E2E2E));
		
		
		// All of the SignUp buttons
		JButton LOGIN = new JButton("Login");
		JButton SIGNUPROUTE = new JButton("Need to Sign Up? Sign Up");
		
		// The labels
		JLabel usernameLabel = new JLabel("Username:");
		JLabel passwordLabel = new JLabel("Password:");
		JLabel pageLabel = new JLabel("Card Again Login");
		
		// The text fields
		usernameText = new JTextField(15);
		passwordText = new JTextField(15);
		
		// Where everything is placed
		SIGNUPROUTE.setBounds(50, 150, 245, 50);
		usernameLabel.setBounds(50, 215, 100, 30);
		passwordLabel.setBounds(50, 270, 100, 30);
		usernameText.setBounds(120, 215, 175, 30);
		passwordText.setBounds(120, 270, 175, 30);
		LOGIN.setBounds(120, 315, 175, 50);
		
		
		// Adding everything
		logInPanel.add(usernameLabel);
		logInPanel.add(passwordLabel);
		logInPanel.add(usernameText);
		logInPanel.add(passwordText);
		logInPanel.add(LOGIN);
		logInPanel.add(SIGNUPROUTE);
		
		//Styles
		SIGNUPROUTE.setForeground(new Color(0xffffff));
		SIGNUPROUTE.setBackground(new Color(0xF72224));
		SIGNUPROUTE.setFont(buttonFont);
				
		LOGIN.setForeground(new Color(0xffffff));
		LOGIN.setBackground(new Color(0xF72224));
		LOGIN.setFont(buttonFont);
		
		usernameLabel.setForeground(new Color(0xffffff));
		passwordLabel.setForeground(new Color(0xffffff));
		pageLabel.setForeground(new Color(0xffffff));
		
		// end of information for the sign up panel
		
		//information for the log in panel
		
		logInPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Card Agains Log In"));
		((TitledBorder) logInPanel.getBorder()).setTitleFont(mainFont);
		logInPanel.setPreferredSize(new Dimension(450, 550));

		//add(logInPanel);
		pack();
		//setResizable(true);
		//setLocationRelativeTo(null);
		//this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		SIGNUPROUTE.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				baseRegister.switchToSignup();
			}
		});
		
		LOGIN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				loginRequirement();
			}
		});
		
		//DEBUG PURPOSES - Fast Login
		//loginRequirement();
		
	}
	
	private void loginRequirement(){
		
		
		String user = getUsername();
		String pass = getPassword();
		
		final String uArg[] = {user, ""};
		
		JLabel userWarning = new JLabel("(Must enter Username)");
		JLabel passWarning = new JLabel("(Must enter Password)");
		
		int count = 0;
		
		if(user.equals("")){
			userWarning.setFont(mainFont);
			userWarning.setForeground(new Color(0xff0000));
			userWarning.setBounds(300, 215, 175, 30);
			//logInPanel.add(userWarning);
			count++;
		}
		
		if(pass.equals("")){
			passWarning.setFont(mainFont);
			passWarning.setForeground(new Color(0xff0000));
			passWarning.setBounds(300, 270, 175, 30);
			//logInPanel.add(passWarning);
			count++;
		}
		
		if(count == 0){
			try {
				User authenticatedUser = null;
				//create a network registration request and send it.
				LoginCommand lc = new LoginCommand(getUsername(), getPassword());
				
				client.Client.cardNet.emitCommand(lc, new Callback(){
					public void call(Object... args){
						String status = GsonHelper.gson.fromJson((String)args[0],String.class);
						System.out.println(status);
						if(status.equals("success")){
							//user was successfuly logged in
							System.out.println((String)args[1]);
							//deserialize user
							User objUser = GsonHelper.gson.fromJson((String)args[1], User.class);
							//track user
							client.Client.authenticatedUser = objUser;
							//switch to main screen
							Base.main(uArg);
							//kill this window
							baseRegister.close();
						}
					}
				});
				//sqlGameAccess.readUsersPassDataBase(user);
				//System.out.println("You have stored user information");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// action listener for buttons
		
	}
	
	private void printUserInfo(){
		String user = getUsername();
		String pass = getPassword();
		
		System.out.println(user);
		System.out.println(pass);
	}
	
	private String getUsername(){
		return usernameText.getText();
	}
	
	private String getPassword(){
		return passwordText.getText();
	}
	
	/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		LogIn frame = new LogIn();
		frame.setSize(500, 600);
		frame.setVisible(true);
		
	}
	*/
	public JPanel getGui() {
		// TODO Auto-generated method stub
		return logInPanel;
	}
	
}
