package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import dataModels.User;

public class MySQLGameAccess {
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	private static final String hostName = "mysql.cs.iastate.edu";
	private static final String dbUsername = "dbu309ppc5";
	private static final String dbUserPassword = "@4v!AzCC";
	private static final String schema = "db309ppc5";
	private static Connection dbConnection;
	
	public static void init() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		dbConnection = DriverManager.getConnection("jdbc:mysql://" + hostName + ":3306/"+schema,dbUsername,dbUserPassword);
	}
	public static void close(){
		try{
			if(dbConnection != null){
				dbConnection.close();
			}
		}catch(Exception e){
			System.out.println("Failed to close Database connection.");
			e.printStackTrace();
		}
	}
	public static Connection getConnection(){
		return dbConnection;
	}
	/*
	//Stores the infomation from the table that I created with my own database
	public void readUserDataBase(String firstName, String lastName, String username, String password) throws Exception{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			connect = DriverManager.getConnection("jdbc:mysql://localhost/cardAgainDB?" + "user=user1&password=user1Password");
			
			statement = connect.createStatement();
			
			resultSet = statement.executeQuery("Select * from cardAgainDB.gameUsers");
			writeResultSet(resultSet);
			
			preparedStatement = connect.prepareStatement("insert into cardAgainDB.gameUsers values (default, ?, ?, ?, ?)");
			
			preparedStatement.setString(1, firstName);
	
			preparedStatement.setString(2, lastName);

			preparedStatement.setString(3, username);
			
			preparedStatement.setString(4, password);
			
			preparedStatement.executeUpdate();
			
			preparedStatement = connect.prepareStatement("SELECT FirstName, LastName, Username, Password from cardAgainDB.gameUsers");
			
			resultSet = preparedStatement.executeQuery();
			writeResultSet(resultSet);
			
			//preparedStatement = connect.prepareStatement("delete from cardAgainDB.gameUsers where FirstName= ? ; ");
			//preparedStatement.setString(1, "Test");
			//preparedStatement.executeUpdate();
			
			resultSet = statement.executeQuery("select * from cardAgainDB.gameUsers");
			writeMetaData(resultSet);
		} catch (Exception e){
			throw e;
		}finally{
			close();
		}
	}
	*/
	
	public static HashMap<String,String> registerUser(String username, String password, String firstName, String lastName){
		/*this map will have two keys. Status, UserID, and Reason.
		* Status will be 'success' or 'failure' representing whether the user was created successfully
		* if successful, UserID will contain the UID of the created user. Reason will be blank.
		* if failure, Reason will contain the reason why creation failed. UserID will be -1
		*/
		HashMap<String,String> userCreationStatus = new HashMap<String, String>();
		userCreationStatus.put("status", "failure");
		userCreationStatus.put("userid", "-1");
		userCreationStatus.put("reason", "initialization failed");
		
		try {
			//prepare a statement to check if the username is taken
			PreparedStatement usernameCheck = dbConnection.prepareStatement("SELECT * FROM db309ppc5.Users WHERE LOWER(username) = ?");
			usernameCheck.setString(1, username.toLowerCase());
			usernameCheck.executeQuery();
			if(usernameCheck.getResultSet().next()){
				//Username is taken
				userCreationStatus.replace("reason", "username taken");
			}
			else{
				//username isn't taken, add the user.
				PreparedStatement userInsert = dbConnection.prepareStatement("INSERT INTO db309ppc5.Users (username, firstName, lastName) VALUES (?,?,?)",Statement.RETURN_GENERATED_KEYS);
				userInsert.setString(1, username);
				userInsert.setString(2, firstName);
				userInsert.setString(3, lastName);
				if(userInsert.executeUpdate() != 0){
					ResultSet generatedKey = userInsert.getGeneratedKeys();
					//update the map properly
					generatedKey.next();
					int userID = generatedKey.getInt(1);
					//insert the password into the other table
					PreparedStatement passInsert = dbConnection.prepareStatement("INSERT INTO db309ppc5.UPass (userid, pass) VALUES (?,?)");
					passInsert.setInt(1, userID);
					passInsert.setString(2, password);
					passInsert.executeUpdate();
					passInsert.closeOnCompletion();
					generatedKey.close();
					//update status map
					userCreationStatus.replace("status", "success");
					userCreationStatus.replace("userid", Integer.toString(userID));
					userCreationStatus.replace("reason", "");
				}
				else{
					userCreationStatus.replace("reason", "Insert update failed");
				}
				userInsert.closeOnCompletion();
			}
			usernameCheck.closeOnCompletion();
		} catch (SQLException e) {
			System.out.println("There was an error while creating this user");
			e.printStackTrace();
			userCreationStatus.replace("reason", "Database Error, please consult the devs.");
		}
		
		return userCreationStatus;
	}
	
	/**
	 * Attempts to log a user in
	 * @param username the username to log in with
	 * @param password the password to log in with
	 * @return either a User object representing the successfully authenticated user, or null, representing an unsuccessful login attempt.
	 */
	public static User userLogin(String username, String password){
		
		//prepare a statement to check if the credentials are correct
		
		try {
			PreparedStatement loginStatement = dbConnection.prepareStatement(
					"SELECT U.UserID FROM db309ppc5.Users U LEFT JOIN db309ppc5.UPass UP ON UP.userid = U.userid WHERE U.username = ? AND UP.pass = ? LIMIT 1");
			loginStatement.setString(1, username);
			loginStatement.setString(2, password);
			loginStatement.executeQuery();
			ResultSet rs = loginStatement.getResultSet();
			if(rs.next()){
				return loadUserByID(rs.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//no user found or error, return null
		return null;
	}
	
	public static User loadUserByID(int userID){
		try {
			PreparedStatement loadUser = dbConnection.prepareStatement("SELECT U.username, U.firstName, U.lastName, U.GamesWon, U.GamesPlayed, U.userRole FROM Users U LEFT JOIN db309ppc5.UPass UP ON UP.userid = U.userid  WHERE U.userid = ? LIMIT 1");
			loadUser.setInt(1, userID);
			loadUser.executeQuery();
			ResultSet rs = loadUser.getResultSet();
			if(rs.next()){
				System.out.println("User " + userID + " loaded.");
				String username = rs.getString(2), firstName = rs.getString(3), lastName = rs.getString(4);
				int userRole = rs.getInt(5);
				return new User(userID, username, firstName, lastName, userRole);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// This method takes the information from the Users table and puts it into the leaderboard
	public void addUsersToLeaderBoard(DefaultTableModel t)
	{
		try {
		
			Statement statement = dbConnection.createStatement();
			ResultSet rs;
			
			rs = statement.executeQuery("select * from Users u");
			
			String uName = "";
			String uType = "";
			int gamesPlayed;
			int wins;
			int losses;
			String addToLB[] = {"", "", "", "", ""};
			
			while(rs.next())
			{
				uName = rs.getString("username");
				gamesPlayed = rs.getInt("GamesPlayed");
				wins = rs.getInt("GamesWon");
				
				if(gamesPlayed < 10)
				{
					uType = "Beginner";
				} else {
					uType = "Normal";
				}
				
				losses = gamesPlayed - wins;
				addToLB[0] = uName;
				addToLB[1] = Integer.toString(gamesPlayed);
				addToLB[2] = uType;
				addToLB[3] = Integer.toString(wins);
				addToLB[4] = Integer.toString(losses);
				
				t.addRow(addToLB);
			}
			
			statement.close();
			rs.close();
		
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
	}
	
	public void addUserProfileData(String username, JPanel pan)
	{
		try{
			Statement statement = dbConnection.createStatement();
			ResultSet rs;
			
			rs = statement.executeQuery("select * from Users u where u.username =" + "'" +username +"'");
			
			String lName = "";
			String fName = "";
			String uType = "";
			int gamesPlayed;
			int wins;
			int losses;
			
			rs.next();
			gamesPlayed = rs.getInt("GamesPlayed");
			wins = rs.getInt("GamesWon");
			fName = rs.getString("firstName");
			lName = rs.getString("lastName");
			
			System.out.println(gamesPlayed + wins + fName + lName);
				
			if(gamesPlayed < 10)
			{
				uType = "Beginner";
			} else {
				uType = "Normal";
			}
				
			losses = gamesPlayed - wins;
			
			JLabel uNameVal = new JLabel(username);
			JLabel nameVal = new JLabel(fName + " " + lName);
			JLabel uTypeVal = new JLabel(uType);
			JLabel gPlayedVal = new JLabel(Integer.toString(gamesPlayed));
			JLabel winsVal = new JLabel(Integer.toString(wins));
			JLabel lossesVal = new JLabel(Integer.toString(losses));
			
			uNameVal.setBounds(315, 200, 100, 15);
			nameVal.setBounds(315, 250, 100, 15);
			uTypeVal.setBounds(315,350,100,15);
			gPlayedVal.setBounds(975, 200, 100, 15);
			winsVal.setBounds(975, 250, 100, 15);
			lossesVal.setBounds(975, 300, 100, 15);
			
			pan.add(uNameVal);
			pan.add(nameVal);
			pan.add(uTypeVal);
			pan.add(gPlayedVal);
			pan.add(winsVal);
			pan.add(lossesVal);
			
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
	}
	
	//Stores the users table data
		public void readUsersPassDataBase(String username) throws Exception{
			try{
				Class.forName("com.mysql.jdbc.Driver");
				
				statement = dbConnection.createStatement();
				
				System.out.println("running");
				resultSet = statement.executeQuery("Select * from db309ppc5.Users");
				writeResultSet(resultSet);
				
				preparedStatement = dbConnection.prepareStatement("insert into db309ppc5.Users (username) values (?)");
				
				preparedStatement.setString(1, username);
				
				preparedStatement.executeUpdate();
				
				preparedStatement = dbConnection.prepareStatement("SELECT *  from db309ppc5.Users");
				
				resultSet = preparedStatement.executeQuery();
				writeResultSet(resultSet);
				
				resultSet = statement.executeQuery("select * from db309ppc5.Users");
				writeMetaData(resultSet);
			} catch (Exception e){
				throw e;
			}
		}
	
	//stores the information from the UPass table
	public void readUPassDataBase(int userid, String pass) throws Exception{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			statement = dbConnection.createStatement();
			
			resultSet = statement.executeQuery("Select * from db309ppc5.UPass");
			writeResultSet(resultSet);
			
			preparedStatement = dbConnection.prepareStatement("insert into db309ppc5.UPass values (default, ?, ?)");
			
			preparedStatement.setInt(1, userid);
			preparedStatement.setString(2, pass);
			
			preparedStatement.executeUpdate();
			
			preparedStatement = dbConnection.prepareStatement("SELECT userid, pass from db309ppc5.UPass");
			
			resultSet = preparedStatement.executeQuery();
			writeResultSet(resultSet);
			
			resultSet = statement.executeQuery("select * from db309ppc5.UPass");
			writeMetaData(resultSet);
		} catch (Exception e){
			throw e;
		}
	}
	
	//Stores data to the Friends table
	public void readFriendsPassDataBase(int userid1, int userid2) throws Exception{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			statement = dbConnection.createStatement();
			
			resultSet = statement.executeQuery("Select * from db309ppc5.Friends");
			writeResultSet(resultSet);
			
			preparedStatement = dbConnection.prepareStatement("insert into db309ppc5.Friends values (default, ?, ?)");
			
			preparedStatement.setInt(1, userid1);
			preparedStatement.setInt(2, userid2);
			
			preparedStatement.executeUpdate();
			
			preparedStatement = dbConnection.prepareStatement("SELECT userid, pass from db309ppc5.Friends");
			
			resultSet = preparedStatement.executeQuery();
			writeResultSet(resultSet);
			
			resultSet = statement.executeQuery("select * from db309ppc5.Friends");
			writeMetaData(resultSet);
		} catch (Exception e){
			throw e;
		}
	}
	
	private void writeMetaData(ResultSet resultSet) throws SQLException{
		System.out.println("The columns in the table are: ");
		
		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for(int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++){
			System.out.println("Column " +i + " " + resultSet.getMetaData().getColumnName(i));
		}
	}
	
	//Use for testing purpose for my own table in CardAgainDB will be delete eventually
	private void writeResultSet(ResultSet resultSet) throws SQLException {
		
		while(resultSet.next()){
			String first = resultSet.getString("userid");
			String last = resultSet.getString("username");
			String username = resultSet.getString("GamesWon");
			String password = resultSet.getString("gamesPlayed");
			
			System.out.println("First name: " + first);
			System.out.println("Last name: " + last);
			System.out.println("Username: " + username);
			System.out.println("Password: " + password);
		}
	}
	
	// Updates the given players userids' GamesPlayed and GamesWon
	public static void updateGamesPlayed(int userid, boolean hasWon)
	{
		try {
			// Retrieve the current number of GamesPlayed and Game Wins
			PreparedStatement getUserWins = dbConnection.prepareStatement("Select * from db309ppc5.Users where userid = ?");
			getUserWins.setInt(1, userid);
			ResultSet rs = getUserWins.executeQuery();
			rs.next();
			
			int gamesPlayed = rs.getInt("GamesPlayed");
			int wins = rs.getInt("GamesWon");
			
			// Update the current number of GamesPlayed and Game Wins
			PreparedStatement updateStats = dbConnection.prepareStatement("update db309ppc5.Users set GamesPlayed=?, GamesWon=? where userid=?");
			if(hasWon)
			{
				wins++;
			}
			gamesPlayed++;
			
			//set values and update
			updateStats.setInt(1, gamesPlayed);
			updateStats.setInt(2, wins);
			updateStats.setInt(3, userid);
			updateStats.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Gets a users role given their username
	public static int getUserRoleFromDB(String username)
	{
		int userRole = 0;
		
		try {
			PreparedStatement getUserRole = dbConnection.prepareStatement("Select * from db309ppc5.Users where username = ?");
			getUserRole.setString(1, username);
			ResultSet rs = getUserRole.executeQuery();
			rs.next();
		
			userRole = rs.getInt("userRole"); 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userRole;
	}
	
	
	private boolean doesUserExist(String username){
		return true;
	}
	
	

}