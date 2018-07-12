package Api_Tool;

import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONObject;
import org.json.JSONTokener;

public class JSONUtils {

	public static String getJSONStringFromFile(String path) {
		Scanner scanner;
		InputStream in = FileHandle.inputStreamFromFile(path);
		scanner = new Scanner(in);
		String json = scanner.useDelimiter("\\Z").next();
		scanner.close();
		return json;
		
	}
	
	public static JSONTokener getJSONToknerFromFile(String path) {
		Scanner scanner;
		InputStream in = FileHandle.inputStreamFromFile(path);
		scanner = new Scanner(in);
		String json = scanner.useDelimiter("\\Z").next();
		JSONTokener tokener = new JSONTokener(json);
		scanner.close();
		return tokener;
		
	}
	

	
	public static JSONObject getJSONObjectFromFile(String path) {
		return new JSONObject(getJSONStringFromFile(path));
	}
	
	
	public static boolean ObjectExists(JSONObject jsonObject, String key) {
		Object o;
		try {
			o = jsonObject.get(key);
		}catch(Exception e) {
			return false;
		}
		
		return o != null;
	}
}
