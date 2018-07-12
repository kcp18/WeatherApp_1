package Api_Tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class FileHandle {
	
	public static InputStream inputStreamFromFile(String path) {
		try {
			InputStream inputStream = FileHandle.class.getResourceAsStream(path); 
			return inputStream;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	

}
