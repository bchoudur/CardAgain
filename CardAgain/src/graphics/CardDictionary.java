package graphics;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Dictionary;
//import java.util.Hashtable;import java.util.Map;

import gameObjects.Card;
//import gameObjects.Deck;

public class CardDictionary {
	
	/**
	 * This methods takes the card and searches for the proper image .png name
	 * in the folder and then returns image .png name in the folder that will 
	 * be use in the drawCard() method;
	 * @return cardValueAndSuite - string that will be use in the drawCard()
	 * */
	public String mapCardtoImageString(Card card){
		String cardValueAndSuite;
		if(card.getFacedown())
		{
			cardValueAndSuite = "back.png";
		}
		else
		{
			cardValueAndSuite = card.getSuitAndValue();
		}
		ArrayList<String> imageFileName = getStringFromFolder();
		if(imageFileName.contains(cardValueAndSuite + ".png")){
			cardValueAndSuite = cardValueAndSuite + ".png";
			return cardValueAndSuite;
		}else{
			System.out.println(cardValueAndSuite + " Card doesn't exist");
		}
		return cardValueAndSuite;
		
	}
	
	/**
	 * This methods loops thought the image folder and grabs the name of all the images
	 * Once it gets the name it then stores it as a string in an ArrayList
	 * The ArrayList will be use in the another method to map the suit and value of the card to the actual image
	 * name
	 * */
	public static ArrayList<String> getStringFromFolder(){
		File dir = new File("src/graphics/Playing_Cards/PNG-cards/");
		ArrayList<String> imgNames = new ArrayList<String>(Arrays.asList(dir.list()));
		
		return imgNames;
	}
	
	
	//Testing purpose
	public static void main(String[] args){
		
	}
}
