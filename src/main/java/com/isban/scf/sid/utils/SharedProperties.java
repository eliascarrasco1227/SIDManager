/**
 * SharedProperties
 *
 * This class implements singleton to share some information
 * from a external file in properties that is going to use
 * the rest of the classes.
 *
 * @author Elías Alfonso Carrasco Guerrero
 * @author eliascarrasco1227@gmail.com
 */

package com.isban.scf.sid.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SharedProperties {
	
	private static final Logger logger = LogManager.getLogger(SharedProperties.class);

	private static Properties properties;
	
	private static String fileName = "application.properties";
	
	
	/**
	 * This method gets the property value from the property key called "key"
	 * from the file "encrypt.key"
	 *
	 * @return A string with the property value of the key "key".
	 * 
	 * @throws Exception If the property is not found, because fileName or propertyKey are wrong.
	 */
	
	public static String getEncryptKey() throws Exception{
		// Get the path of the file where the properties are saved
		String filePath = PathManager.getFilePath("encrypt.key"); 
		
		try {
			Properties propertiesKey = new Properties();
			propertiesKey.load(new FileInputStream(filePath));
			
			// Get the property in string format
			String propertyValue = propertiesKey.getProperty("key");
			
			if (propertyValue == null) {
				logger.error("The property key is not found in file encrypt.key.");
				throw new Exception();
			}
			
			// Remove whitespace from the beginning and end of the propertyValue
			return propertyValue.trim();
			
		} catch (FileNotFoundException e) {
			logger.error("Error in getKey (FileNotFoundException): " + e);
			throw new Exception();
		} catch (IOException e) {
			logger.error("Error in getKey (IOException): " + e);
			throw new Exception();
		}
	}
	
	/**
	 * This method gets the property value from the property key defined by the user
	 * from a external file.
	 *
	 * @param propertyKey A property key to get his value.
	 * @return A string with the property value of the key given by the user.
	 * 
	 * @throws Exception If the property is not found, because fileName or propertyKey are wrong.
	 */	
	public static String get(String propertyKey) throws Exception {
		if(properties == null) {
			loadProperties();
		}
		
		// Get the property in string format
		String propertyValue = properties.getProperty(propertyKey);
		
		if (propertyValue == null) {
			logger.error("The property is not found in file " + fileName + ". The property searched was is: " + propertyKey);
			throw new Exception("Property not found: " + propertyKey);
		}
		
		// Remove whitespace from the beginning and end of the propertyValue
		return propertyValue.trim();
	}
	
	/**
	 * This method gets the property value from the property key defined by the user
	 * from a external file. If it's not possible, the method takes default value.
	 *
	 * @param propertyKey A property key to get his value.
	 * @param defaultValue A default value if key value can not be used.
	 * @return A string with the property value of the key given by the user.
	 * 
	 * @throws Exception If the property is not found, because fileName is wrong
	 * or property value and default value are wrong.
	 */	
	public static String get(String propertyKey, String defaultValue) throws Exception {
		if(properties == null) {
			loadProperties();
		}
		
		// Get the property in string format
		String propertyValue = properties.getProperty(propertyKey);
		
		if (propertyValue == null) {
			propertyValue = defaultValue;
			logger.warn("Para la propiedad " + propertyKey + " se ha utilizado el valor por defecto");
		}
		
		// Remove whitespace from the beginning and end of the propertyValue
		return propertyValue.trim();
	}
	
	/**
	 * This method gets the property value from the property key defined by the user
	 * from a external file and return it in integer format.
	 *
	 * @param propertyKey A property key to get his value.
	 * @return A integer with the property value of the key given by the user.
	 * 
	 * @throws Exception If the property is not found, because fileName or propertyKey are wrong. 
	 * It could happen too if number is negative or it is not in integer format.
	 */	
	public static int getInteger(String propertyKey) throws Exception {
		int propertyValue = -1;
		
		try {			
            propertyValue = Integer.parseInt(get(propertyKey));
                        
            if (propertyValue < 1) {
            	throw new Exception("Property can not be negative");
            }           
        } catch (Exception e) {
        	logger.error("The file string is not a valid number.");
        	logger.error("The number must be between 1 and 2147483647 without decimals and the defined one has a value: " + propertyValue);
        	logger.error("The property " + propertyKey + " is not in the correct format: " + propertyValue);
            throw new Exception();
        }
		
		return propertyValue;
	}
	
	/**
	 * This method gets the property value from the property key defined by the user
	 * from a external file and return it in integer format. If it's not possible,
	 * the method takes default value.
	 *
	 * @param propertyKey A property key to get his value.
	 * @param defaultValue A default value if key value can not be used.
	 * @return A integer with the property value of the key given by the user.
	 * 
	 * @throws Exception If the property is not found, because fileName is wrong
	 * or property value and default value are wrong. 
	 * It could happen too if number is negative or it is not in integer format.
	 */	
	public static int getInteger(String propertyKey, String defaultValue) throws Exception {
		int propertyValue = -1;
		
		try {
            propertyValue = Integer.parseInt(get(propertyKey, defaultValue));
            
            if (propertyValue < 1) {
            	logger.error("Property can not be negative, " + propertyKey + ": " + defaultValue);
            	throw new Exception();
            }           
        } catch (Exception e) {
        	logger.error("The file string is not a valid number.");
        	logger.error("The number must be between 1 and 2147483647 without decimals and the defined one has a value: " + propertyValue);
        	logger.error("The property is not in the correct format: " + propertyKey);
        	throw new Exception();
        }
		
		return propertyValue;
	}
	
	/**
	 * This method gets an string array that contains the string given
	 * by the user divided where there are comma and white space.
	 *
	 * @param receivers A string with all the names
	 *  of the email receivers separated with comma and whitespace.
	 * 
	 * @return A string array with all email.
	 */	
    public static String[] splitWithComma(String receivers) {
    	// It splits by comma and whitespace ", "
        String[] array = receivers.split(", ");
           
        logger.info(" Receivers :");

        // Print each element of the array to check 
        for (String receiver : array) {
        	logger.info("Mail will be sent to: " + receiver);
        }
        
        return array;
    }
    
    /**
	 * This method gets the properties of external file and save
	 * them into a global variable called properties.
	 * 
	 * @throws FileNotFoundException If the path is not correct 
	 * or the file is not found in the path specified by getFilePath.
	 * @throws IOException If an error occurs with input or output stream.
	 */	
	private static void loadProperties() throws Exception {
		// Get the path of the file where the properties are saved
		String filePath = PathManager.getFilePath(fileName); 
		
		try {
			properties = new Properties();
			properties.load(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			logger.error("Error loading the properties: " + e);
			throw new Exception();
		} catch (IOException e) {
			logger.error("Error loading the properties: " + e);
			throw new Exception();
		}
		
	}
}
