package client;

import java.sql.SQLException;

import communication.CardNetClient;
import dataModels.User;
import server.MySQLGameAccess;

public class Client {
	//This object handles all the client-server networking.
	//created once on client startup.
	//Note: move this deceleration to wherever we place the other pseudo-globals eventually
	private static boolean enableDebug = true;
	public static CardNetClient cardNet;
	public static User authenticatedUser;
	
	public static void main(String[] args) {
		cardNet = new CardNetClient(enableDebug);
		gui.BaseRegisterScreen.main(args);
		
	}

}
