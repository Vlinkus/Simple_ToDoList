package lt.github.vli.exceptions;

public class TaskDoesNotExistException extends IllegalArgumentException{

	private static final long serialVersionUID = 6997971750753612334L;

	public TaskDoesNotExistException(String msg) {
		super(msg);
	}
	
	

}
