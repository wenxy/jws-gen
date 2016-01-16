package context;

import java.util.HashMap;
import java.util.Map;

public class Context{
	private static Map<String,String> map = new HashMap<String,String>();
	private Context(){}
	
	public static String get(String key){
		return map.get(key);
	}
	public static void set(String key,String value){
		map.put(key, value);
	}
}
