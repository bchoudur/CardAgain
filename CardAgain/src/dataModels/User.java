package dataModels;

public class User {
	private int userID;
	private String username;
	private String firstName;
	private String lastName;
	private int userRole;
	
	public User(int uid, String userName, String firstName, String lastName, int userRole){
		this.userID = uid;
		this.username = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userRole = userRole;
	}
	public int getUserID(){
		return this.userID;
	}
	public String getUsername(){
		return this.username;
	}
	public String getLastName(){
		return this.lastName;
	}
	public String getFirstName(){
		return this.firstName;
	}
	public int getUserRole()
	{
		return this.userRole;
	}
}
