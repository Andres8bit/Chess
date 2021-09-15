package audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioDataLine implements Runnable{
	private static final int BUFFER_SIZE_MS = 50;
	private List<LineListener> listeners = Collections.synchronizedList(new ArrayList<LineListener>());
	private Thread writer;
	private AudioFormat audioFormat;
	private SourceDataLine dataLine;
	private byte[] rawData;
	private byte[] soundData;
	private int bufferSize;
	private int loopCount;
	private volatile boolean restart = false;
	
	public AudioDataLine(byte[] rawData) {
		this.rawData = rawData;
	}
	
	public void initlialize() {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(rawData);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(in);
			audioFormat = audioInputStream.getFormat();
			bufferSize = computeBufferSize(BUFFER_SIZE_MS);
			soundData = readSoundData(audioInputStream);
		}catch(UnsupportedAudioFileException e) {
			throw new SoundException(e.getMessage(),e);
		}catch(IOException e){
			throw new SoundException(e.getMessage(),e);
		}
	}
		
	public void addLineListener(LineListener listener) {
		listeners.add(listener);
	}
	
	public void open() {
		try {
			DataLine.Info info = new DataLine.Info(SourceDataLine.class,audioFormat,AudioSystem.NOT_SPECIFIED);
			dataLine = (SourceDataLine)AudioSystem.getLine(info);
			synchronized(listeners) {
				for(LineListener listener: listeners) {
					dataLine.addLineListener(listener);
				}
			}
			dataLine.open(audioFormat,bufferSize);
		}catch(LineUnavailableException e){
			throw new SoundException(e.getMessage(),e);
		}
	}
	
	private byte[] readSoundData(AudioInputStream audioInputStream) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			long chunk = audioFormat.getFrameSize();
			byte[] buf = new byte[(int) chunk];
			
			while(audioInputStream.read(buf) != -1) {
				out.write(buf);
			}
			audioInputStream.close();
			return out.toByteArray();
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private int computeBufferSize(int milliseconds) {
		double sampleRate = audioFormat.getSampleRate();
		double bitSize = audioFormat.getSampleSizeInBits();
		double channels = audioFormat.getChannels();
		
		
		if(bitSize == AudioSystem.NOT_SPECIFIED    ||
		   sampleRate == AudioSystem.NOT_SPECIFIED ||
		   channels == AudioSystem.NOT_SPECIFIED) {
				return -1;
		}else {
				double temp  = milliseconds;
				double frames = sampleRate * temp/1000.0;
				
				while(frames != Math.floor(frames)) {
					temp++;
					frames = sampleRate * temp/1000.0;
				}
				double bytesPerFrame = bitSize /8.0;
				double size = (int)(frames * bytesPerFrame * channels);
				
				return (int) size;	
		}
	}
	
	public void close() {
		dataLine.close();
	}
	
	public void start() {
		loopCount = 0;
		dataLine.flush();
		dataLine.start();
		writer = new Thread(this);
		writer.start();
	}
	
	public void reset() {
		restart = true;
	}

	public void loop(int count) {
		loopCount = count;
		dataLine.flush();
		dataLine.start();
		writer = new Thread(this);
		writer.start();
	}
	
	public void stop() {
		if(writer != null) {
			Thread temp = writer;
			writer =  null;
			try {
				temp.join(10000);
			}catch(InterruptedException e) {
				
			}
		}
	}
	
	public Line getLine() {
		return dataLine;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				int written = 0;
				int length = bufferSize == -1? dataLine.getBufferSize():bufferSize;
				
				while(written < soundData.length) {
					if(Thread.currentThread() != writer) {
						System.out.println("stream canceled");
						loopCount = 0;
						break;
					}else if(restart) {
						restart = false;
						System.out.println("Stream Canceled");
						if(loopCount != AudioStream.LOOP_CONTINOUSLY) {
							loopCount++;
						}
						break;
					}
					int bytesLeft = soundData.length - written;
					int toWrite = bytesLeft > length * 2 ? length: bytesLeft;
					written += dataLine.write(soundData,written,toWrite);
				}
				if(loopCount == 0) {
					break;
				}else if(loopCount != AudioStream.LOOP_CONTINOUSLY) {
					loopCount--;
				}
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			System.out.println("Stream finished");
			dataLine.drain();
			dataLine.stop();
		}
	}
}
