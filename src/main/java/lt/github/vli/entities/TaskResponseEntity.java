package lt.github.vli.entities;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class TaskResponseEntity extends ResponseEntity<Task> {
	
	private String message;

	public TaskResponseEntity(Task body, HttpStatusCode status, String message) {
		super(body, status);
		this.setMessage(message);
	}
	
	public TaskResponseEntity(HttpStatusCode status, String message) {
		super(status);
		this.setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
