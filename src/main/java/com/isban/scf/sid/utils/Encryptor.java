/**
 * Encryptor
 *
 * It is used to encrypt the password before executing the code
 * for first time and to decrypt all the passwords at execution time.
 *
 * @author Elías Alfonso Carrasco Guerrero
 * @author email: eliascarrasco1227@gmail.com
 */


package com.isban.scf.sid.utils;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Encryptor {
	private static final Logger logger = LogManager.getLogger(Encryptor.class);
    
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    
    /*private static byte[] parseBase64StringToBytes(String base64Message) {
        return Base64.getDecoder().decode(base64Message);
    }*/

    /**
	 * This method encrypt a message with specific key and parse it in base 64.
	 *
	 * @param encryptedMessage The encrypted string message to be decrypted.
	 * @return A string of the message derypted.
	 * 
	 * @throws Exception If key or the algorithm are wrong.
	 */
    public static String decryptMessage(String encryptedMessage) 
            throws Exception {
        
    	byte[] key = SharedProperties.getEncryptKey().getBytes();
    	
    	byte[] messageBytes = Base64.getDecoder().decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(messageBytes);
        
        return new String(decryptedBytes);
    }
    
	/**
	 * This method encrypt a message with specific key and parse it in base 64.
	 *
	 * @param message The string message to be encrypted.
	 * @return A string in base 64 of the message encrypted.
	 * 
	 * @throws Exception If key or the algorithm are wrong.
	 */
    public static String encryptMessage(String message) 
            throws Exception {
    	if (message == null) {
            throw new IllegalArgumentException("Message to be encrypted cannot be null");
    	} 
    	byte[] key = SharedProperties.getEncryptKey().getBytes();
        
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedMessage = cipher.doFinal(message.getBytes());
        
        return Base64.getEncoder().encodeToString(encryptedMessage);
    }

    // Example usage
	/**
	 * This method is an example of how to use this class.
	 * First you should create a key for encrypt.key.
	 * Secondly you can change testPassword for yours and encrypt it.
	 * Then you can save the encrypted password in app.properties.
	 * Finally you can access it like in this main method.
	 *
	 * @param args An unused variable.
	 */
    public static void main(String[] args) {
        try {        	
            String originalMessage = "buscafieras11";
            String encryptedMessage = encryptMessage(originalMessage);
            System.out.println("Encrypted Message: " + new String(encryptedMessage)); // Encrypted bytes as string (may not be readable)
            
            String decryptedMessage = decryptMessage(encryptedMessage);
            System.out.println("Decrypted Message: " + decryptedMessage);
            
            String encriptedPassword = SharedProperties.get("dbPassword");
            String decryptedMessage2 = decryptMessage(encriptedPassword);
            System.out.println("Decrypted Message: " + decryptedMessage2);
            
        } catch (Exception e) {
        	logger.error("Error at Main of Encryptor: " + e);
        }
    }
}


