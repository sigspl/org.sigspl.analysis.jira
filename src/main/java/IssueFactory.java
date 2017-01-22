import org.sigspl.analysis.commons.AssetStorage;
import org.sigspl.analysis.commons.ifaces.IAsset;
import org.sigspl.analysis.commons.ifaces.IAssetFactory;
import org.sigspl.analysis.commons.util.StringUtil;
import org.sigspl.analysis.jira.models.Issue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class IssueFactory implements IAssetFactory {

	public  AssetStorage loadFile (String fileID) {
	
		AssetStorage assets = new AssetStorage();
	
		JsonElement jelement = new JsonParser().parse(fileID);
	    JsonObject  jobject = jelement.getAsJsonObject();

	    JsonArray jarray = jobject.getAsJsonArray("issues");
	    int z = 0;
	    for (int i = 0; i < jarray.size(); i++) {
			JsonElement itemEl = jarray.get(i);
			JsonObject   object = itemEl.getAsJsonObject();

			String key = object.get("key").toString();
			
			if (Configuration.JIRA_ID_PREFIX!=null && !StringUtil.matchRegex(key, Configuration.JIRA_ID_PREFIX))
			{
				
				continue;
			}

			JsonElement fields = object.get("fields");
			JsonObject f1 = fields.getAsJsonObject();
			JsonObject o = f1.getAsJsonObject("issuetype");
			String name = o.get("name").toString();

			String d1 = f1.get("description").toString();
			String s = f1.get("summary").toString();
			
			//if (d1==null || d1.equalsIgnoreCase("null")) continue;
			
			//System.out.println(key + " | " + name + " | " +s);
			//System.out.println(d1);
			//System.out.println("\n\n");
			
			Issue issue = new Issue ();
			issue.id = StringUtil.clean(key);
			issue.name = StringUtil.clean(s);
			issue.text = StringUtil.clean(d1);
			
			assets.store(issue.getCompoundIdentificator(), issue);
			
			z++;
			
			
		}
		
		return assets;
	}
	
	

	
	
}
