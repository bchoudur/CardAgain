package utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class GsonHelper {
	//static publically available GSON object with all the adapters added in.
	//created at program startup to avoid constantly recreating it.
	public static Gson gson = construct();
	
	private static Gson construct(){
		GsonBuilder gb = new GsonBuilder();
		//add any custom TypeAdapters here
		return gb.create();
	}
	
	public static String toJson(Object e){
		return gson.toJson(e);
	}
	
	public static <T> T fromJson(Object json, Class<T> classOfT){
		return fromJson((String)json, classOfT);
	}
	public static <T> T fromJson(String json, Class<T> classOfT){
		return gson.fromJson(json, classOfT);
	}
	
	public static String strFromJson(Object o){
		return gson.fromJson((String)o, String.class);
	}
}
