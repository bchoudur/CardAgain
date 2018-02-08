package communication;

import java.io.IOException;
import java.net.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
public class JSONSocket {
	private SocketAction socketActor = null;
	private Gson gson = null;
	public JSONSocket(Socket s){
		socketActor = new SocketAction(s);
		gson = new Gson();
	}
	
	//serializes an object and sends the JSON string along the network.
	public void sendObject(Object o){
		socketActor.send(gson.toJson(o));
	}
	
	//deserializes incoming JSON into an object of type T
	public <T> Object recieveObject(Class<T> classOfT) throws JsonSyntaxException, IOException{
		return gson.fromJson(socketActor.receive(), classOfT);
	}
	
	//at the moment this is the same as recieveRaw, will be updated to validate JSON later.
	public String recieveJSON() throws IOException{
		return recieveRaw();
	}
	
	//returns the String directly from the socket.
	public String recieveRaw() throws IOException{
		return socketActor.receive();
	}
}
