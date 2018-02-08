package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import communication.Callback;
import communication.commands.UserRegistrationCommand;
import server.MySQLGameAccess;

public class SignUpTwo extends JFrame {
	
	JPanel signUpPanel = new JPanel();
	BaseRegisterScreen baseRegister = new BaseRegisterScreen();
	
	
	JTextField usernameText, passwordText, firstNameText, lastNameText;
	GridBagConstraints constraints;
	Font mainFont = new Font("Serif San", Font.PLAIN, 20);
	
	public void renderPanel(){
		
		// All of the SignUp buttons
		JButton SIGNUP = new JButton("Sign Up");
		JButton LOGINROUTE = new JButton("Already a user? Log In");
		
		// The labels
		JLabel usernameLabel = new JLabel("Username:");
		JLabel passwordLabel = new JLabel("Password:");
		JLabel firstNameLabel = new JLabel("First Name:");
		JLabel lastNameLabel = new JLabel("Last Name:");
		JLabel pageLabel = new JLabel("Card Again Sign Up");
		
		// The text fields
		usernameText = new JTextField(15);
		passwordText = new JTextField(15);
		firstNameText = new JTextField(15);
		lastNameText = new JTextField(15);
		
		// Where everything is placed
		
		
		// end of information for the sign up panel
		
		//information for the log in panel
		
		signUpPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Card Agains Sign Up"));
		((TitledBorder) signUpPanel.getBorder()).setTitleFont(mainFont);
		signUpPanel.setPreferredSize(new Dimension(450, 550));
		//add(signUpPanel);
		pack();
		//setResizable(true);
		//setLocationRelativeTo(null);
		//this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//action listener for buttons
		LOGINROUTE.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				baseRegister.switchToLogin();
			}
		});
		
		SIGNUP.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				requirement();
			}
		});
		
	}
	
	public JComponent getGui(){
		return signUpPanel;
	}
	
	private void requirement(){
		String first = getFirstName();
		String last = getLastName();
		String user = getUsername();
		String pass = getPassword();
		
		//For testing purpose
		int gameWins = 8;
		int gamePlayed = 19;
		
		JLabel firstWarning = new JLabel("(Must enter first name)");
		JLabel lastWarning = new JLabel("(Must enter Last name)");
		JLabel userWarning = new JLabel("(Must enter Username)");
		JLabel passWarning = new JLabel("(Must enter Password)");
		
		int count = 0;
		
		if(first.equals("")){
			firstWarning.setFont(mainFont);
			firstWarning.setForeground(new Color(0xff0000));
			constraints.gridx = 3;
			constraints.gridy = 1;
			signUpPanel.add(firstWarning, constraints);
			count++;
		}
		if(last.equals("")){
			lastWarning.setFont(mainFont);
			lastWarning.setForeground(new Color(0xff0000));
			constraints.gridx = 3;
			constraints.gridy = 2;
			signUpPanel.add(lastWarning, constraints);
			count++;
		}
		if(user.equals("")){
			userWarning.setFont(mainFont);
			userWarning.setForeground(new Color(0xff0000));
			constraints.gridx = 3;
			constraints.gridy = 3;
			signUpPanel.add(userWarning, constraints);
			count++;
		}
		if(pass.equals("")){
			passWarning.setFont(mainFont);
			passWarning.setForeground(new Color(0xff0000));
			constraints.gridx = 3;
			constraints.gridy = 4;
			signUpPanel.add(passWarning, constraints);
			count++;
		}
		if(count == 0){
			MySQLGameAccess sqlGameAccess = new MySQLGameAccess();
			try {
				/*
				 * Below is a sample of how to communicate effectively with the server.
				 * Communications should be done through commands, as they hold the
				 * data properly and ease the process of serializing/standardizing
				 * things.
				 * 
				 * The first step is to create your command, in this case we want to
				 * tie the functionality to the "register User" command, so we create
				 * a UserRegistrationCommand object, and pass it all the required
				 * parameters. If you need a command that doesn't exist, the Command
				 * interface and CommandType Enums have instructions on how to add
				 * your own properly.
				 */
				//create a network registration request and send it.
				UserRegistrationCommand rc = new UserRegistrationCommand(getFirstName(), getLastName(), getUsername(), getPassword());
				/* The second step, is to "emit" the command, which sends it to the
				 * server. There is a statically available object called cardNet 
				 * which will handle the sending from client to server properly.
				 * You'll need to call:
				 * 
				 * Client.cardNet.emitCommand(commmand)
				 * 
				 * to use it. If you want your command to recieve a reply from
				 * the server for some purpose (e.g. a success/failure message)
				 * then you may also call emitCommand with a Callback function.
				 * In that case, you'll need to call:
				 * 
				 * Client.cardNet.emitCommand(command, new Callback(){
				 * 		public void call(object... args){
				 * 			***Your callback code here***
				 * 		}
				 * });
				 * 
				 * The below demonstrates how to send a registration command out,
				 * and recieve the server's response as to whether the user was
				 * registered or not. Note, that Object... args will always be
				 * sent back as a JSON string, unless the handler on the server
				 * side is implemented wrong. It also doesn't have to be a
				 * command, and you can pretty much set up whatever you want here.
				 */
				client.Client.cardNet.emitCommand(rc, new Callback(){
					public void call(Object... args){
						String json = (String)args[0];
						System.out.println(json);
					}
				});

				//sqlGameAccess.readUsersPassDataBase(user);
				//System.out.println("You have stored user information");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void printUserInfo(){
		String first = getFirstName();
		String last = getLastName();
		String user = getUsername();
		String pass = getPassword();
		
		System.out.println(first);
		System.out.println(last);
		System.out.println(user);
		System.out.println(pass);
	}
	
	private String getFirstName(){
		return firstNameText.getText();
	}
	
	private String getLastName(){
		return lastNameText.getText();
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
		
		SignUp frame = new SignUp();
		frame.setSize(500, 600);
		frame.setVisible(true);
		
	}
	*/

}
