package audio;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;



public class AudioPlayer{
	private final static String PLACE_PATH = "C:\\Users\\andres\\eclipse-workspace\\NQueens\\soundEffects\\place.wav";
	private byte[] place_piece;
	private byte[] invalid_move;
	private byte[] check;
	private byte[] check_mate;
	private OneShotEvent clip;
	private String loaded;

	public void initialize() {
		InputStream in = ResourceLoader.load(PLACE_PATH);
		place_piece = readBytes(in);
	}
	
	private void shutDown() {
		if(clip != null) {
			clip.shutDown();
		}
	}

	private byte[] readBytes(InputStream in) {	
		try {
			BufferedInputStream buf = new BufferedInputStream(in);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int read;
			while((read = buf.read()) != -1){
				out.write(read);
			}
			in.close();
			return out.toByteArray();
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	private void loadWaveFile(byte[] rawData) {
		shutDown();
		clip = new OneShotEvent(new BlockingDataLine(rawData));
		clip.initialize();
	}
	
	public void processInput(String event) {
		if(event.toLowerCase().equals("place")) {
			loadWaveFile(place_piece);
			loaded = "place";
			clip.fire();
		}else if(event.toLowerCase().equals("check")) {
			
			loaded = "check";
		}else if(event.toLowerCase().equals("checkmate")) {
			
			loaded = "checkmate";
		}else if(event.toLowerCase().equals("invalid")) {
			
			loaded = "invalid";
		}else if(event.toLowerCase().equals("done") && clip != null){
			System.out.println("call to clip done");
			      clip.done();
		}
		
	}
	
	public void terminate() {
		shutDown();
	}
	
	
}
