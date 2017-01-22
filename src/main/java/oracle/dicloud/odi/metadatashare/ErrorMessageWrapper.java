package oracle.dicloud.odi.metadatashare;

public class ErrorMessageWrapper {

	private String errorMessage;

	public ErrorMessageWrapper(String errorMessage) {
	        this.errorMessage = errorMessage;
	    }

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
