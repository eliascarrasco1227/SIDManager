package com.isban.scf.sid.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.isban.scf.sid.enumerated.ResponseEnum;
import com.isban.scf.sid.model.ClientUser;
import com.isban.scf.sid.repository.ClientUserRepository;
import com.isban.scf.sid.utils.Mail;
import com.isban.scf.sid.utils.SharedProperties;

@Service
public class ClientUserService {
	
	private static final Logger logger = LogManager.getLogger(ClientUserService.class);
	
	@Autowired
    private ClientUserRepository clientUserRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	* This method save the user data in the database and 
	* send an email to confirm the registration.
	*
	* @param clientUser A Model from spring boot which has the user data.
	* @param passwordRepeat A second password in string format that is
	*  used to check if both passwords are the same.
	*  
	* @throws Exception if an exception occurs.
	* 
	* @return  a responseMessage in String format with the status information.
	*/   
	public ResponseEnum createClientUser(ClientUser clientUser,
										 String passwordRepeat
										 ) throws Exception {
        ResponseEnum responseMessage;
        String password = clientUser.getPassword();
        
        if (password != null && password.equals(passwordRepeat)) {
        	// Check if the user exists in the database
	        ClientUser existingUser = this.getClientUser(clientUser.getEmail());
	        
	        if (existingUser == null) {
	        	// Save the user in the database
	        	logger.debug(" - Entering clientUserService.saveClientUser()");
	            clientUser = this.saveClientUser(clientUser);
	            logger.debug(" - Exiting clientUserService.saveClientUser()");
	            
	            // Send a confirmation email
	            String path = "user/confirm?email=";
	            String urlConfirmation = this.getConfirmationUrl(clientUser, path);
	            
	            logger.debug(" - Entering sendMail");
	            String subject = "Confirm your sign up in SIDManager";
	            String body = "Click on the next URL to activate your SIDManager user: ";
	            Mail.sendMail(clientUser.getEmail(), urlConfirmation, subject, body);
	            logger.debug(" - Exiting sendMail");
	            
	            responseMessage = ResponseEnum.SIGN_UP_SUCCESS;
	        } else {
	        	responseMessage =  ResponseEnum.SIGN_UP_WARNING_2;
	        }
        } else {
        	//Check if both passwords are the same
        	responseMessage = ResponseEnum.SIGN_UP_WARNING_1;
        }      
        
        return responseMessage;
	}	
	
	/**
	* This method get the ClientUser find by email.
	*
	* @param  email The clientUser email in string format that is
	*  used to search that user.
	* 
	* @return a user in ClientUser format with the email searched.
	*/   
	public ClientUser getClientUser(String email) {
		return clientUserRepository.findByEmail(email);
	}
	
	/**
	* This method save the user data in the database
	* with the encrypted password and random confirmation code.
	*
	* @param  clientUser A Model from spring boot which has the user data.
	* 
	* @throws Exception if an exception occurs.
	* 
	* @return  a responseMessage in String format with the status information.
	*/   
	@Transactional
	public ClientUser saveClientUser(ClientUser clientUser) throws Exception {
		try {
			if (clientUser.getPassword() != null) {
				logger.debug(" -- Entering encryptMessage()");
				clientUser.setPassword(encryptFacade(clientUser.getPassword()));
				
				logger.debug(" -- Entering randomAlphanumeric()");
				clientUser.setConfirmationCode(RandomStringUtils.randomAlphanumeric(30));
				
				clientUser.setStatus(0);
		        clientUser.setCreationDate(new Date()); 
				clientUserRepository.save(clientUser);
			} else {
				 throw new IllegalArgumentException("Password cannot be null");
			}
		} catch (Exception e) {
			logger.error(" ERROR: Possible error in encryption, confirmation code generation "
					+ " or saveClientUser:" + e);
			throw e;
		}
		
		return clientUser;
	}
	
	/**
	* This method get the confirmation url to validate the clientUser
	* appending the url, email and confirmation code.
	*
	* @param clientUser A Model from spring boot which has the user data. 
	* @param path A url path in String format for the different propose 
	* (change email, confim user...)
	* 
	* @throws Exception if an exception occurs.
	*   
	* @return  a confirmationUrl in String format with url that has server url, email and confirmation code.
	*/   
    private String getConfirmationUrl(ClientUser clientUser,
    								  String path
    								  ) throws Exception {
    	StringBuilder confirmationUrl = new StringBuilder();
    	String serverUrl = SharedProperties.get("serverUrl");
    	
    	confirmationUrl.append(serverUrl);
    	confirmationUrl.append(path);
    	confirmationUrl.append(clientUser.getEmail());
    	confirmationUrl.append("&confirmationCode=");
    	confirmationUrl.append(clientUser.getConfirmationCode());
    	
		return confirmationUrl.toString();
	}
	
	/**
	* This method confirm the user and return the responseMessage
	*  that is going to show in the web application.
	*
	* @param  email The clientUser email email in string format that is
	*  used to check the validation .
	* @param confirmationCode The validation code in string format that is
	*  used to check the validation .
	* 
	* @return a responseMessage in String format with the status information.
	*/    
	@Transactional
	public ResponseEnum confirmUser(String email,
									String confirmationCode
									) {
		ResponseEnum responseMessage;
		ClientUser clientUser = clientUserRepository.findByEmailAndConfirmationCodeAndStatus(email, confirmationCode, 0);	
		
		if (clientUser != null) {
			responseMessage = ResponseEnum.CONFIRM_SUCCESS;
			clientUser.setStatus(1);
	        clientUser.setConfirmationDate(new Date()); 
	        clientUserRepository.save(clientUser);
		} else {
			logger.debug(" User not found, not correct or already confirmed. ");
			responseMessage = ResponseEnum.CONFIRM_WARNING;
		}
		
		return responseMessage;
	}
	
	/**
	* This method print in the backend console a list of
	* all the ClientUsers and their data
	* 
	* @throws Exception if an exception occurs.
	*/
	public void getAllUsers() throws Exception {
		logger.debug(" -- Entering clientUserRepository.findAll()");
		List<ClientUser> users =  clientUserRepository.findAll();
		logger.debug(" -- Exiting clientUserRepository.findAll()");
		
		System.out.println();
		logger.info(	   "_______________________________________________________________________________________________________________________________________________");
		logger.info(	   "|                Email                |         Password         |              Code              | Status | Creation Date | Confirmation date|");
		//					| elias.guerrero@santanderconsumer.pt | F3EvDw+yhwBXuqUTVCHHiQ== | UAu3tIuSRsHvrBcJJWfWxe7bgFwD9c |   1    |  2024-08-13 11:32:11.31   |    2024-08-13 11:32:58.18    |
		logger.info(	   "|_____________________________________________________________________________________________________________________________________________|");
		for (ClientUser user : users) {
			logger.info(
				  "| " + user.getEmail()
				+ " | " + user.getPassword()
				+ " | " + user.getConfirmationCode()
				+ " | " +  "  " + user.getStatus() + "   "
				+ " | " +   user.getCreationDate() 
				+ " | " + user.getConfirmationDate() 
				+ " | "
			);
		}
		logger.info("|_____________________________________________________________________________________________________________________________________________|");
		System.out.println();  
	}
	
	@Transactional
	public void deleteAll() {
		logger.debug(" -- Entering clientUserRepository.deleteAll();");  
		clientUserRepository.deleteAll();
		logger.debug(" -- Exiting clientUserRepository.deleteAll();"); 
		
		System.out.println();
		logger.info(	   "______________________________________________________");
		logger.info(	   " --------- ALL DATA WAS SUCCESSFULLY DELETED --------- ");
		logger.info(	   "______________________________________________________");
		System.out.println();
	}
	
	// _______________________________________________________________ //
	// ---------------------------- LOGIN ---------------------------- //
	// _______________________________________________________________ //
	/*
	public String checkLogin(ClientUser clientUser) {
		logger.debug(" -- Entering clientUserRepository.checkLogin();");  
		String errorMessage = "Error in log in: ";
        String responseMessage;
        
        logger.debug(" -- Entering clientUserRepository.isClientUserSet();");  
        if (clientUser != null && clientUser.getPassword() != null) {
	        if (this.isConfirmedClientUser(clientUser)) {
				try {
					String inputPassword = encryptFacade(clientUser.getPassword());
					ClientUser existingUser = this.getClientUser(clientUser.getEmail());
					
					responseMessage = (inputPassword.equals(existingUser.getPassword()))
							? "correct" 
							: " Wrong password. Try again or click Forgot password to reset it. ";
							
				} catch (Exception e) {
					responseMessage = 
		        			errorMessage 
		        			+ " For some reason we can not encrypt the password. "
		        			+ "Please contact the support team. ";
					logger.error(" ERROR: error trying to encrypting password in isClientUserSet(). "); 
				}
	        } else {
	        	responseMessage = 
	        			  " We couldn't find an account with that username. "
	        			+ "Try another, or get a new account. ";
	        	logger.error(" ERROR: There is no account with that username. "); 
	        }
        } else {
        	responseMessage = 
        			errorMessage 
        			+ " For some reason we can not get the user or password. "
        			+ "Please contact the support team. ";
        	logger.error(" ERROR: The clientUser, password or both are null. "); 
        }
		
        logger.debug(" -- Exiting clientUserRepository.checkLogin();");  
        
		return responseMessage;
	}
	

	private boolean isConfirmedClientUser(ClientUser clientUser) {
        // Check if the user exists in the database
		ClientUser dbUser = this.getClientUser(clientUser.getEmail());
		
		return (dbUser != null && dbUser.getStatus() == 1);
	}
	*/

	// _________________________________________________________________________________ //
	// ---------------------------- request Update Password ---------------------------- //
	// _________________________________________________________________________________ //
	
	/**
	* This method send an email with the url to the website to change the password.
	*
	* @param  email The clientUser email email in string format that is
	*  used to check the validation .
	*  
	* @throws Exception if an exception occurs.
	* 
	* @return a responseMessage in String format with the status information.
	*/    
	public ResponseEnum sendUpdatePasswordUrl(String email) throws Exception {
		ResponseEnum responseMessage;
		ClientUser clientUser = clientUserRepository.findByEmail(email);
		
		if (clientUser != null && clientUser.getStatus() == 1) {
			String path = "user/updatePassword?email=";
            String urlUpdatePassword = this.getConfirmationUrl(clientUser, path);
            
            logger.debug(" - Entering sendMail");
            String subject = " Change the password of your SIDManager user. ";
            String body = " Click on the next URL if you want to change your password: ";
            Mail.sendMail(clientUser.getEmail(), urlUpdatePassword, subject, body);
            logger.debug(" - Exiting sendMail");
            
            responseMessage = ResponseEnum.UPDATE_PSW_SUCCESS_1;//" Check the URL that we have sent to your email: " + email;
		} else {
			responseMessage = ResponseEnum.UPDATE_PSW_WARNING_1;
			logger.info(" The user does not exist or is not confirmed. ");
		}

		return responseMessage;
	}

	/**
	* This method return good status if the email, confirmation code and status
	* of the user are corrects.
	*
	* @param  email The clientUser email email in string format that is
	*  used to check the validation .
	* @param confirmationCode The validation code in string format that is
	*  used to check the validation .
	* 
	* @return a responseMessage in String format with the status information.
	*/   
	public ResponseEnum getUpdatePasswordView(String email,
											  String confirmationCode
											  ) {
		ResponseEnum responseMessage;
		ClientUser clientUser 
			= clientUserRepository.findByEmailAndConfirmationCodeAndStatus(
					email, confirmationCode, 1);	

		if (clientUser != null) {
			responseMessage = ResponseEnum.UPDATE_PSW_SUCCESS_2;
		} else {
			responseMessage = ResponseEnum.UPDATE_PSW_WARNING_2;
			logger.info(responseMessage.getMessageKey());
		}
		
		return responseMessage;
	}

	/**
	* This method return good status if the email, confirmation code and status
	* of the user are corrects.
	*
	* @param  email The clientUser email email in string format that is
	*  used to check the validation .
	* @param clientUser A user in ClientUser format that storages the password submitted.
	* @param passwordRepeat A second password repeated to check if both passwords are equals.
	* 
	* @throws Exception if an exception occurs.
	* 
	* @return a responseMessage in String format with the status information.
	*/   
	public ResponseEnum updatePassword(String email, 
									   ClientUser clientUser, 
									   String passwordRepeat
									   ) throws Exception {
		ResponseEnum responseMessage;
		String password = clientUser.getPassword();
		
		 if (password != null && password.equals(passwordRepeat)) {
	        	// Check if the user exists in the database
		        ClientUser existingUser = this.getClientUser(email);
		        
		        if (existingUser != null) {
		        	existingUser.setPassword(encryptFacade(password));
		        	clientUserRepository.save(existingUser);
		            
		            responseMessage = ResponseEnum.UPDATE_PSW_SUCCESS_3;
		        } else {
		        	responseMessage = ResponseEnum.UPDATE_PSW_WARNING_4;
		        }
	        } else {
	        	responseMessage = ResponseEnum.UPDATE_PSW_WARNING_3;
	        }      
		
		return responseMessage;
	}
	
	public String encryptFacade(String password) {
		//return Encryptor.encryptMessage(password);
		return passwordEncoder.encode(password);
	}
}
