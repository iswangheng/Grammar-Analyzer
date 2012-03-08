package syntax;
import java.io.File;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.filechooser.*;

public class ExampleFileFilter extends FileFilter{
	private Hashtable filters = null;
	private String description = null;
	private String fullDescription = null;
	private Boolean useExtensionsInDescription = true;
	
	public ExampleFileFilter(){
		this.filters = new Hashtable();
	}
	
	public boolean accept(File f){
		if(f != null){
			if(f.isDirectory()){
				return true;
			}
			String extension = getExtension(f);
			if(extension != null && filters.get(getExtension(f)) != null){
				return true;
			};
		}
		return false;
	}
	
	public String getExtension(File f){
		if(f != null){
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if(i>0 && i<filename.length()-1){
				return filename.substring(i+1).toLowerCase();
			};
		}
		return null;
	}
	
	public void addExtension(String extension){
		if(filters == null){
			filters = new Hashtable(5);
		}
		filters.put(extension.toLowerCase(), this);
		fullDescription = null;
	}
	
	public String getDescription(){
		if(fullDescription == null){
			if(description == null||isExtensionsListInDescription()){
				fullDescription=description==null?"(":description+"(";
			Enumeration extensions=filters.keys();
			if(extensions != null){
				fullDescription+="."+(String)extensions.nextElement();
				while(extensions.hasMoreElements()){
					fullDescription += ", ."+ (String) extensions.nextElement();
				}
			}
			fullDescription += ")";
			}
			else {
				fullDescription=description;
			}
		}
		return fullDescription;
	}
	
	public void setDescription(String description){
		this.description=description;
		fullDescription=null;
	}
	
	public void setExtensionListInDescription(Boolean b){
		useExtensionsInDescription = b;
		fullDescription = null;
	}
	
	public Boolean isExtensionsListInDescription(){
		return useExtensionsInDescription;
	}
} 



