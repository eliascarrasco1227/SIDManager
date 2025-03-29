package com.isban.scf.sid.enumerated;

public enum ResponseEnum {
	
	// Success messages
	SIGN_UP_SUCCESS("singup.success", 210),
	CONFIRM_SUCCESS("confirm.success", 220),
	LOGIN_SUCCESS("login.success", 230),
	UPDATE_PSW_SUCCESS_1("update.psw.success.1", 240),
	UPDATE_PSW_SUCCESS_2("update.psw.success.2", 241),
	UPDATE_PSW_SUCCESS_3("update.psw.success.3", 242),

	// Warning messages
	SIGN_UP_WARNING_1("signup.warning.1", 310),
	SIGN_UP_WARNING_2("signup.warning.2", 311),
	CONFIRM_WARNING("confirm.warning", 320),
	LOGIN_WARNING_1("login.warning.1", 330),
	LOGIN_WARNING_2("login.warning.2", 331),
	UPDATE_PSW_WARNING_1("update.psw.warning.1", 340),
	UPDATE_PSW_WARNING_2("update.psw.warning.2", 341),
	UPDATE_PSW_WARNING_3("update.psw.warning.3", 342),
	UPDATE_PSW_WARNING_4("update.psw.warning.4", 343),
	LIST_WARNING_1("list.warning.1", 350),
	
	// Error messages
	LOGIN_ERROR_1("login.error.1", 430),
	LOGIN_ERROR_2("login.error.2", 431);
	
	private String messageKey;
	private int status;
	
	private ResponseEnum (String messageKey, int status){
		this.messageKey = messageKey;
		this.status = status;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public int getStatus() {
		return status;
	}
	
}
