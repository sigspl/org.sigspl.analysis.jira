


import java.io.IOException;




import org.sigspl.analysis.commons.AssetStorage;
import org.sigspl.analysis.commons.util.StringUtil;

import java.nio.charset.Charset;






public class Runner {

	public static void main(String[] args) throws IOException{
		
		
		
		
		
		Charset charset=Charset.forName(Constants.DEFAULT_CHARSET);
		
		String fileID= StringUtil.readFile(Configuration.DATA_SOURCE, charset);
		
		AssetStorage issues = new IssueFactory().loadFile(fileID);
		
	    //issues.list();
		
		//System.out.println(getResourceFiles());
		
	    MeasurementExperiment m1 = new MeasurementExperiment();
	    
	    System.out.println("id;chars;words;sentences;vocabulary;match_voc1");
		m1.setStorage(issues);
		
		m1.executeAll();
		

	}
		
	
	
	
	

}

