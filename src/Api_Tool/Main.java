package Api_Tool;

import org.apache.commons.io.IOUtils;
import org.json.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class Main {

	public static void main(String[] args) throws JSONException, MalformedURLException, IOException {
		/*String userInput = "new york";
		HashMap<String, LinkedList<JSONObject>> hashMap =  storeCityJsonObject("/city.list.json");
		
		LinkedList<JSONObject> cityObjList = hashMap.get(userInput);
		int id = 519188;
		if(cityObjList.size() == 1) {
			id = (int) cityObjList.get(0).get("id");
			System.out.println("abc");
		}else {
			for(int i=0; i<cityObjList.size(); i++) {
				JSONObject json = cityObjList.get(i);
				System.out.println(i+1 + ". " + json.get("name") + ", " + json.get("country") + " id: " + json.get("id"));
			}
			
		}*/
		
		
		System.out.println("abc");
		
		JSONObject json = new JSONObject(IOUtils.toString(new URL("http://api.wunderground.com/api/c697bbfa1025bf5a/conditions/q/CA/San_Francisco.json"), Charset.forName("UTF-8")));
		System.out.println(json.toString());
		System.out.println(json.length());

		//JSONObject json = new JSONObject(IOUtils.toString(new URL("https://api.openweathermap.org/data/2.5/forecast?units=metric&id=2655138&APPID=d8884b5567d4fe84a035a9a454792df5"), Charset.forName("UTF-8")));
		//JSONObject json = new JSONObject(IOUtils.toString(new URL("https://api.openweathermap.org/data/2.5/forecast?units=metric&id=" +id+ "&APPID=d8884b5567d4fe84a035a9a454792df5"), Charset.forName("UTF-8")));
		//System.out.println(json.toString());
		//System.out.println(json.length());
		

	}
	
	public static HashMap<String, LinkedList<JSONObject>> storeCityJsonObject(String path) {
		  JSONArray jsonArray = new JSONArray(JSONUtils.getJSONToknerFromFile(path));
		  HashMap<String, LinkedList<JSONObject>> hashMap = new HashMap<String, LinkedList<JSONObject>>();
		  Iterator it = jsonArray.iterator();
		  while(it.hasNext()) {			  
			  JSONObject obj = (JSONObject) it.next();
			  String cityName = (String) obj.get("name");
			  cityName = cityName.toLowerCase();
			  
			  LinkedList<JSONObject> list;
			  if(hashMap.containsKey(cityName)) {
				  list = hashMap.get(cityName);
			  }else{
				  list = new LinkedList<JSONObject>();
			  }			  
			  list.add(obj);			  
			  
			  hashMap.put(cityName.toLowerCase(), list);
		  }
		  
		  return hashMap;
		  
	}

}
