package com.isban.scf.sid.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.isban.scf.sid.enumerated.ResponseEnum;
import com.isban.scf.sid.model.ClientUser;
import com.isban.scf.sid.service.ClientUserService;
import com.isban.scf.sid.utils.SharedProperties;

@Controller
public class ClientUserController {
	
	private static final Logger logger = LogManager.getLogger(ClientUserController.class);
	
	@Autowired
	private ClientUserService clientUserService;

	/**
	* This method return a view to create an user like a common sign up.
	* 
	* @return a view (HTML template) in String format with the sign up.
	*/
    @GetMapping("/user/create")
    public String getCreateClientUserView() {
    	logger.debug("Entering GET: /user/create");
    	
        return "createClientUserView"; 
    }
    
	/**
	* This method save the user data in the database with createClientUser.
	*
	* @param  model A Model from spring boot used to storage the responseMessage
	* @param  clientUser A Model from spring boot which has the user data.
	* @param  passwordRepeat A second password in string format that is
	*  used to check if both passwords are the same.
	* 
	* @return  a thymeleaf view for the sign up.
	* 
	* @throws java.lang.Exception if there is an error creating the user
	*/    
    @PostMapping("/user/create")
    public String createClientUser(Model model, 
    								@ModelAttribute ClientUser clientUser,
    								@RequestParam("psw-repeat") String passwordRepeat
    								) throws Exception {
    	
        logger.debug(" - Entering clientUserService.saveClientUser(), from POST: /user/create");
        
        ResponseEnum responseMessage = clientUserService.createClientUser(clientUser, passwordRepeat);

        // Add the response message to the model
        model.addAttribute("responseMessage", responseMessage.getMessageKey());
        addColorToResponse(model, responseMessage);
        
        return "createClientUserView";  // The name of your HTML view
    }

	/**
	* This method confirm the user by calling confirmUser
	* and add to the model the responseMessage that is going to
	* show in the web application.
	*
	* @param  model A Model from spring boot used to storage the responseMessage
	* @param  email The clientUser email email in string format that is
	*  used to check the validation .
	* @param confirmationCode The validation code in string format that is
	*  used to check the validation .
	* 
	* @return a thymeleaf view for the confirmation.
	*/    
    @GetMapping("/user/confirm")
    public String getConfirmation(Model model, 
    							  @RequestParam("email") String email,
						    	  @RequestParam("confirmationCode") String confirmationCode) {
    	
    	logger.debug(" - Entering clientUserService.confirmUser(), from GET: /user/confirm");
    	ResponseEnum responseMessage = clientUserService.confirmUser(email,confirmationCode);
    	logger.debug(" - Exiting clientUserService.confirmUser()");
    	
    	model.addAttribute("responseMessage", responseMessage.getMessageKey());
    	addColorToResponse(model, responseMessage);
    	
        return "confirmationUser"; 
    }    
    
	/**
	* This method print in the backend console a list of
	* all the ClientUsers and their data
	* 
	* @return a view (HTML template) in String format which is empty.
	* @throws Exception if clientUserService.getAllUsers() throw an exception
	*/
    @GetMapping("/user/getAll")
    public String getAllUsers() throws Exception {
    	logger.debug(" - Entering clientUserService.getAllUsers(), from GET: /user/getAll");
    	clientUserService.getAllUsers();
    	logger.debug(" - Exiting clientUserService.getAllUsers()");
    	
        return "prueba"; 
    }
    
	@GetMapping("/user/deleteAll")
    public String deleteAll() {
    	logger.debug(" - Entering clientUserService.deleteAll(), from GET: /user/deleteAll");
    	clientUserService.deleteAll();
    	logger.debug(" - Exiting clientUserService.deleteAll()");
    	
    	return "prueba";
    }
	
	// _______________________________________________________________ //
	// ---------------------------- LOGIN ---------------------------- //
	// _______________________________________________________________ //
	
	/**
	* This method return a view to validate the user and password
	* like a common log in.
	* 
	* @return a view (HTML template) in String format with the log in.
	* 
	* @param  model A Model from spring boot used to storage the responseMessage
	* and other attributes.
	* @param request An HttpServletRequest used to check
	* if there was an error on post function.
	* 
	* @throws Exception if there is an exception while
	* using shared properties or using session.
	*/
	@GetMapping("/user/login")
	public String getLoginView(Model model,
							   HttpServletRequest request
							   ) throws Exception {
		String serverUrl = SharedProperties.get("serverUrl");
	    String requestUpdatePasswordUrl =  serverUrl + "user/requestUpdatePassword";
	    String createClientUserUrl = serverUrl + "user/create";
	    String errorMessage = (String) request.getSession().getAttribute("error");
	    
	    model.addAttribute("requestUpdatePasswordUrl", requestUpdatePasswordUrl);
	    model.addAttribute("createClientUserUrl", createClientUserUrl);
	    
	    if (errorMessage != null) {
	    	ResponseEnum responseMessage = ResponseEnum.LOGIN_WARNING_1;
	        model.addAttribute("responseMessage", responseMessage.getMessageKey());
	        addColorToResponse(model, responseMessage);
	        
	        // Elimina el mensaje de error de la sesión
	        request.getSession().removeAttribute("error");
	    }
	    
	    return "loginView";
	}
    
	//@PostMapping("/user/login")
	// Spring security take care about login.
    /*
    @PostMapping("/user/login")
    public String loginClientUser(Model model, 
    							  @ModelAttribute ClientUser clientUser
    							  //HttpServletResponse response
    							  ) throws IOException {
    	logger.debug("Entering POST: /user/login"); 	
    	String responseMessage = clientUserService.checkLogin(clientUser);

    	model.addAttribute("responseMessage", responseMessage);
    	
        return (responseMessage.equals("correct")) ? "redirect:/" : "loginView"; 
    }
    */
    
	// _________________________________________________________________________________ //
	// ---------------------------- request Update Password ---------------------------- //
	// _________________________________________________________________________________ //
    
	/**
	* This method return a view to make a request for change the password.
	* 
	* @return a view (HTML template) in String format with the
	* form to request for change the password.
	*/
    @GetMapping("/user/requestUpdatePassword")
    public String getRequestUpdatePasswordView() {
    	return "requestUpdatePasswordView";
    }
    
    /**
	* Function that calls sendUpdatePasswordUrl method.
	* 
	* @param  model A Model from spring boot used to storage the responseMessage
	* @param  email The clientUser email email in string format.
	* 
	* @throws Exception if clientUserService.sendUpdatePasswordUrl()
	*  function throw an exception.
	* 
	* @return a thymeleaf view for the confirmation.
	*/    
    @PostMapping("user/requestUpdatePassword")
    public String requestUpdatePassword(Model model,
    									@RequestParam("email") String email
    									) throws Exception  {
    	
    	ResponseEnum responseMessage = clientUserService.sendUpdatePasswordUrl(email);
    	
    	model.addAttribute("responseMessage", responseMessage.getMessageKey());
    	addColorToResponse(model, responseMessage);
    	
    	return "requestUpdatePasswordView";
    }
    
	/**
	* This method return a view to change the password.
	* 
	* @param  model A Model from spring boot used to storage the responseMessage
	* @param session An HttpSession used to storage the email
	*  and use it on the post function.
	* @param  email The clientUser email email in string format.
	* @param confirmationCode The validation code in string format that is
	*  used to check the validation .
	* 
	* @throws Exception if clientUserService.getUpdatePasswordView 
	* function throws an exception.
	* 
	* @return a view (HTML template) in String format with the
	* form (if status is success) to request for change the password.
	*/
    @GetMapping("/user/updatePassword")
    public String getUpdatePasswordView(Model model, 
    									HttpSession session,
							    		@RequestParam("email") String email,
								    	@RequestParam("confirmationCode") String confirmationCode
								    	) throws Exception {
    	boolean shouldFormBeShown = false;
    	ResponseEnum responseMessage = clientUserService.getUpdatePasswordView(email, confirmationCode);
    	
    	if (responseMessage.getStatus() == ResponseEnum.UPDATE_PSW_SUCCESS_2.getStatus()) {
    		shouldFormBeShown = true;
    	} else {
    		model.addAttribute("responseMessage", responseMessage.getMessageKey());
    		addColorToResponse(model, responseMessage);
    	}
    	
    	model.addAttribute("shouldFormBeShown", shouldFormBeShown);
    	
    	//Save the email to send it later in the post
    	session.setAttribute("email", email);
    	
    	return "updatePasswordView";
    }
    
    /**
	* Function that calls updatePassword method.
	* 
	* @param  model A Model from spring boot used to storage the responseMessage
	* @param session A session in HttpSession format where we get the email.
	* @param clientUser A user in ClientUser format that storages the password submitted.
	* @param passwordRepeat A second password repeated to check if both passwords are equals.
	* 
	* @throws Exception if clientUserService.sendUpdatePasswordUrl()
	*  function throw an exception.
	* 
	* @return a thymeleaf view for the confirmation.
	*/    
    @PostMapping("/user/updatePassword")
    public String updatePassword( Model model,
    						      HttpSession session,
								  @ModelAttribute ClientUser clientUser,
								  @RequestParam("psw-repeat") String passwordRepeat
						    	  ) throws Exception {
    	String loginUrl = SharedProperties.get("serverUrl") + "user/login";
    	String email = (String) session.getAttribute("email");
    	boolean shouldFormBeShown = true;
    	ResponseEnum responseMessage = clientUserService.updatePassword(email, clientUser, passwordRepeat);
    	
    	if (responseMessage.getStatus() == ResponseEnum.UPDATE_PSW_SUCCESS_3.getStatus()) {
    		shouldFormBeShown = false;
    		model.addAttribute("loginUrl", loginUrl);
    	} 
    	
    	model.addAttribute("responseMessage", responseMessage.getMessageKey());
    	model.addAttribute("shouldFormBeShown", shouldFormBeShown);
    	addColorToResponse(model, responseMessage);
    	
    	return "updatePasswordView";
    }
    
    @GetMapping("/home")
    public String home(Model model) throws Exception {
        logger.debug(" ----- Entering home page ----- ");
        String serverUrl = SharedProperties.get("serverUrl");
        String loginUrl = serverUrl + "user/login";
        
        model.addAttribute("dashboardUrl", serverUrl);
        model.addAttribute("loginUrl", loginUrl);
        
        return "home"; // Nombre del archivo HTML sin extensión
    }
    
	private void addColorToResponse(Model model, ResponseEnum responseMessage) {
		model.addAttribute("isAWarningMessage", 
				(responseMessage.getStatus() > 299) ? true : false);
	}
    
}
