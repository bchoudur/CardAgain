package communication;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class SocketAction extends Thread {
	private BufferedReader inStream = null;
	protected PrintStream outStream = null;
	private Socket socket = null;
	
	//The SocketAction will serve as a class to interface with sockets at a low level.
	//It will abstract away sending and recieving directly from a socket etc.
	public SocketAction(Socket sock){
		super("SocketAction");
		try{
			this.socket = sock;
			inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outStream = new PrintStream(new BufferedOutputStream(socket.getOutputStream()),true);
		}
		catch(IOException e){
			System.out.println("Error in Socket Initialization!");
			e.printStackTrace();
		}
	}
	
	//empty, will not contribute processing overhead while thread is running.
	public void run(){}
	
	//sends a string
	public void send(String s){
		outStream.println(s);
	}
	
	//receives a String
	public String receive() throws IOException{
		return inStream.readLine();
	}
	
	//closes the socket connection.
	public void disconnect(){
		try{
			socket.close();
			socket=null;
		}
		catch(IOException e){
			System.out.println("Could not close socket!");
			e.printStackTrace();
		}
	}
	
	//closes the socket connection if it hasn't already.
	protected void finalize(){
		if(socket != null){
			this.disconnect();
		}
	}
}
