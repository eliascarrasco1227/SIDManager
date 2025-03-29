package com.isban.scf.sid.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathManager {	
	/**
	* This method get the path to resources. It takes the initial path
	* (which usually is \SIDMonitor\target\classes\), and then
	* the method search for the final path 
	* (which usually is \SIDMonitor\src\main\resources\app.properties)
	*
	* @param  fileName A file name in string format 
	* (normally app.properties, but it can change
	* if it is needed in shared properties).
	* 
	* @return A string with the path to the file 
	* that contains the properties
	*/
	public static String getFilePath(String fileName) {
		// We get the path two directories down
		String initialPath = getActualPath();
		String parentPathString = getParentDirectory(initialPath);
		String grantfatherPathString = getParentDirectory(parentPathString);
		//String grantfatherPathString = "C:\\Users\\n799834\\Workspaces\\SIDMonitor\\";
		
		// We concatenate to the path, the relative path that leads to the desired file
		String relativePathToResources = "\\src\\main\\resources\\" + fileName;
		String filePath = grantfatherPathString + relativePathToResources;
		
	    //System.out.println("Initial path : " + initialPath);
		//System.out.println("Final path:    " + filePath);
		
		return filePath;
	}
	
	/**
	* This method get the actual path and change
	* the bars from / to \.
	* 
	* @return A string with the actual path 
	* (probably  C:\...\SIDMonitor\target\classes\).
	* 
	*/
	public static String getActualPath() {
		String initialPath;
        java.net.URL resourceUrl = Thread.currentThread().getContextClassLoader().getResource(".");
        
        if (resourceUrl != null) {
        	initialPath = new String(resourceUrl.getPath());
        } else {
        	// If the resource is not found, use the current directory
        	initialPath = new String(new File(".").getAbsolutePath());           
        }
        
        if (initialPath.startsWith(File.separator) || initialPath.startsWith("/")) {
	        initialPath = initialPath.substring(1);
	    }
        
       //Change the bars from / to \
       initialPath = initialPath.replace("/", File.separator).replace("\\", File.separator);
        
        return initialPath;
    }
	
	/*private static String getActualPath() {
		//Return current path
		String initialPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		
		//Adjust the path to the correct format
		//If the path starts with a bar, it is removed
		if (initialPath.startsWith(File.separator) || initialPath.startsWith("/")) {
	        initialPath = initialPath.substring(1);
	    }
		
		//Change the bars from / to \
		initialPath = initialPath.replace("/", File.separator).replace("\\", File.separator);
		
		return initialPath;
	}
	*/

	/**
	* This method get the parent directory path
	* of the path parameter and return it as string.
	*
	* @param  initialPath A specific path in string format.
	* 
	* @return A string with the parent directory path.
	*/
	private static String getParentDirectory(String initialPath) {
		// Convert the String path to a Path object
	    Path path = Paths.get(initialPath);
	
	    // Go up one level in the directory (equivalent to 'cd ..')
	    Path parentPath = path.getParent();
	    
	    // Convert back to String
	    String parentPathString = parentPath.toString();
	    
		return parentPathString;
	}

}
