package it.polimi.rest.exception;

public class InvalidDateException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String customMessage;
	
	public InvalidDateException() {
		this.setCustomMessage("Start date must be after end date");
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}


}
