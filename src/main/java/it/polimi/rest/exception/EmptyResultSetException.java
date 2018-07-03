package it.polimi.rest.exception;


public class EmptyResultSetException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String resourceName;
	private String customMessage;
	
	public EmptyResultSetException(String resourceName) {
		this.setResourceName(resourceName);
		this.setCustomMessage("Method returned an empty " + resourceName + " result set");
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
}
