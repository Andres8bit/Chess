package audio;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class AudioPlayer implements LineListener{
	boolean finished;
	
	public AudioPlayer(){
		this.finished = false;
	}
	
	public void  play(String filePath) {
		File file = new File(filePath);
		
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
			
			AudioFormat format = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip)AudioSystem.getLine(info);
			long clipLength = clip.getMicrosecondLength();
			
			clip.addLineListener(this);
			clip.open(audioStream);
			clip.start();
			
			while(!finished) {
                   Thread.sleep(clipLength/10);
			}
		}catch(Exception e){
			e.printStackTrace();
			
		}
	}
	@Override
	public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
        
        if (type == LineEvent.Type.START) {
        	finished = false;
        } else if (type == LineEvent.Type.STOP) {
            finished = true;
        }
		
	}

}
