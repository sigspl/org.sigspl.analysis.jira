

import java.io.IOException;
import org.sigspl.analysis.jira.models.Issue;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Runner {

	public static void main(String[] args) throws IOException{
		
		Charset charset=Charset.forName("UTF-8");
		String jsonstring = readFile("/home/p/lab/jira-qa/input/dataset1.json", charset);
		
		LinkedList<Issue> issues = new LinkedList<Issue>();
		
		JsonElement jelement = new JsonParser().parse(jsonstring);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    //jobject = jobject.getAsJsonObject("issues");
	    JsonArray jarray = jobject.getAsJsonArray("issues");
	    int z = 0;
	    for (int i = 0; i < jarray.size(); i++) {
			JsonElement itemEl = jarray.get(i);
			JsonObject   object = itemEl.getAsJsonObject();
			//System.out.println(object.get("key"));
			String key = object.get("key").toString();
			//System.out.println(object.get("fields"));
			JsonElement fields = object.get("fields");
			JsonObject f1 = fields.getAsJsonObject();
			JsonObject o = f1.getAsJsonObject("issuetype");
			String name = o.get("name").toString();
			//System.out.println(name);
			//String d = o.get("description").toString();
			String d1 = f1.get("description").toString();
			String s = f1.get("summary").toString();
			//if (d1==null || d1.equalsIgnoreCase("null")) continue;
			System.out.println(key + " | " + name + " | " +s);
			
			
			System.out.println(d1);
			System.out.println("\n\n");
			z++;
			
			// description, summary
			
			
			
		}
	    System.out.println("printed: " +z);
	    
		
		

	}
	
	

	
	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}

}

