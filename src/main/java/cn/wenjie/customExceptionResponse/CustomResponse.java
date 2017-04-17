package cn.wenjie.customExceptionResponse;

public class CustomResponse {
	
	/** customized response message to be sent to the clients */
	private String message;
	
	public CustomResponse(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
