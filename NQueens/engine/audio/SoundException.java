package audio;

public class SoundException extends RuntimeException{

	public SoundException(String msg) {
		super(msg);
	}
	
	public SoundException(String msg, Throwable cause) {
		super(msg,cause);
	}
}
