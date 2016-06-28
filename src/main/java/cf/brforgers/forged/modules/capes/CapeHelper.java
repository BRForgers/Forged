package cf.brforgers.forged.modules.capes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import cf.brforgers.core.lib.IOHelper;

public class CapeHelper {
	public static URL newURL(String url)
	{
		if (url == null) return null;
		try {
			return new URL(url);
		} catch (MalformedURLException ignored) {}
		return null;
	}
	
	public static String getStr(URL url)
	{
		try {
			return IOHelper.toString(url);
		} catch (Exception e) {}
		return null;
	}
	
	private static final Gson gson = new Gson();

	public static boolean isJSONValid(String JSON_STRING) {
	    try {
	        gson.fromJson(JSON_STRING, Map.class);
	        return true;
	    } catch(com.google.gson.JsonSyntaxException ex) {}
	    return false;
	}
	
	/**
	 * Follow pointer links until find a end or max recursivity
	 * @param url
	 * @return
	 */
	public static String[] follow(URL url, int max) { return follow(url, 0, max); }
	
	public static String[] follow(URL url, int i, int max) {
		String dwld = getStr(url);
		if (dwld == null) return new String[]{};
		
		String[] lines = dwld.split("\\r?\\n");
		if (lines.length > 0) {
			String cDwld = dwld.replaceAll("(?m)^#.*", "");
			if (lines[0] == "#!json" || isJSONValid(dwld) || isJSONValid(cDwld)) {
				return (isJSONValid(dwld) ? new String[]{dwld} : new String[]{cDwld});
			} else {
				boolean valid = (lines[0] == "#!pointer" && i<max);
				
				if (!valid) {
					boolean isNowValid = true;
					for (String line : lines) {
						isNowValid = valid || (isNowValid && newURL(line) != null);
					}
					valid = valid || isNowValid;
				}
				
				if (valid) {
					List<String> results = new ArrayList<String>();
					for (String line : lines) {
						String[] returned = follow(newURL(line), i+1, max);
						
						for (String string : returned) if (string != null && !string.replaceAll("[\\s\\r\\n]","").equalsIgnoreCase("")) results.add(string);
					}
					
					return results.toArray(new String[results.size()]);
				}
			}
		}
		return new String[]{};
	}
}
