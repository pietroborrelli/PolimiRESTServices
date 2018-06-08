package it.polimi.rest.exception;


public class ResourceNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String resourceName;
	private String customMessage;
	
	public ResourceNotFoundException(String resourceName) {
		this.setResourceName(resourceName);
		this.setCustomMessage("Resource " + resourceName + " is null or does not exist in db");
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
