package org.sigspl.analysis.jira.models;

import org.sigspl.analysis.commons.ifaces.IAsset;

public class Issue implements IAsset{
	
	public String id="";
	public String name="";
	public String text="";
	
	
	public String toString()
	{
		return name + "\n"+ text;
	}


	public String getName() {

		return name;
	}
	
	public String getID() {

		return id;
	}
	
	public String getCompoundIdentificator ()
	{
		return getID() + " " + getName(); 
	}

}
